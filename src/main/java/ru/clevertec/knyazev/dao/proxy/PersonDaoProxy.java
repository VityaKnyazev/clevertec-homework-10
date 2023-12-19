package ru.clevertec.knyazev.dao.proxy;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clevertec.knyazev.cache.Cache;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.entity.Person;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents proxy for PerssonDAO class for organization caching Person
 */
@Component
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class PersonDaoProxy implements InvocationHandler {
    private PersonDAO personDAOImpl;
    private Cache<UUID, Person> personCache;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        PersonDAOMethod personDAOMethod = PersonDAOMethod.findByName(method.getName());

        return switch (personDAOMethod) {
            case findById -> whenFindByIdPerson(args);
            case findAll -> method.invoke(personDAOImpl, args);
            case save, update -> whenSaveOrUpdatePerson(args, personDAOMethod);
            case delete -> {
                whenDeletePerson(args);
                yield null;
            }
            case undefined -> throw new IllegalArgumentException("Unexpected method name: " + method.getName());
        };
    }

    /**
     *
     * Capture findById method of PersonDAO and Add to cache Person
     *
     * @param args array where 0 el - optional person to add to cache
     * @return optional person from cache or dao
     */
    private Optional<Person> whenFindByIdPerson(Object[] args) {
        Optional<Person> optionalPersonRes;
        UUID id = (UUID) args[0];

        Person cachedPerson = personCache.get(id);

        if (cachedPerson != null) {
            optionalPersonRes = Optional.of(cachedPerson);
        } else {
            optionalPersonRes = personDAOImpl.findById(id);
            if (optionalPersonRes.isPresent()) {
                personCache.put(id, optionalPersonRes.get());
            }
        }

        return optionalPersonRes;
    }

    /**
     *
     * Capture save or update methods of PersonDAO and Add to cache saved or updated Person
     *
     * @param args array where 0 el - person to add to cache
     * @param personDAOMethod save or update method name
     * @return saved or updated person, adding to cache
     * @throws DAOException when saving or updating failed
     */
    private Person whenSaveOrUpdatePerson(Object[] args, PersonDAOMethod personDAOMethod) throws DAOException {
        Person savingOrUpdatingPerson = (Person) args[0];
        Person savedOrUpdatedPerson = null;

        switch (personDAOMethod) {
            case save ->  savedOrUpdatedPerson = personDAOImpl.save(savingOrUpdatingPerson);
            case update -> savedOrUpdatedPerson = personDAOImpl.update(savingOrUpdatingPerson);
        }

        personCache.put(savedOrUpdatedPerson.getId(), savedOrUpdatedPerson);
        return savedOrUpdatedPerson;
    }

    /**
     *
     * Remove person with given id from data source and cache
     *
     * @param args array where 0 el - person id
     * @throws DAOException when removing person failed
     */
    private void whenDeletePerson(Object[] args) throws DAOException {
        UUID deletingPersonUUID = (UUID) args[0];

        personDAOImpl.delete(deletingPersonUUID);
        personCache.remove(deletingPersonUUID);
    }

    /**
     * Represents method names of PersonDAO class
     */
    private enum PersonDAOMethod {
        findById, findAll, save, update, delete, undefined;

        public static PersonDAOMethod findByName(String name) {

            PersonDAOMethod result = null;
            for (PersonDAOMethod personDAOMethodType : values()) {

                if (personDAOMethodType.name().equalsIgnoreCase(name)) {
                    result = personDAOMethodType;
                    break;
                }

            }

            return result != null ? result : undefined;
        }
    }
}
