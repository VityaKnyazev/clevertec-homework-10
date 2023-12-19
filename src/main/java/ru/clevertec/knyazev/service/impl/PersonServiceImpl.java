package ru.clevertec.knyazev.service.impl;

import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.dao.proxy.PersonDaoProxy;
import ru.clevertec.knyazev.data.PersonDTO;
import ru.clevertec.knyazev.entity.Person;
import ru.clevertec.knyazev.mapper.PersonMapper;
import ru.clevertec.knyazev.service.PersonService;
import ru.clevertec.knyazev.service.exception.ServiceException;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private static final String ADDING_UPDATING_ERROR = "Error when adding or updating person: %s";
    private static final String  REMOVING_ERROR = "Error when removing on null id";

    private PersonDAO personDAOImpl;
    private PersonMapper personMapperImpl;
    private ValidatorFactory validatorFactory;

    @Autowired
    public PersonServiceImpl(PersonDaoProxy personDaoProxy,
                             PersonMapper personMapperImpl,
                             ValidatorFactory validatorFactory) {
        this.personDAOImpl = (PersonDAO) Proxy.newProxyInstance(PersonDAO.class.getClassLoader(),
                new Class[]{PersonDAO.class}, personDaoProxy);
        this.personMapperImpl = personMapperImpl;
        this.validatorFactory = validatorFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonDTO get(UUID personId) {
        if (personId == null) {
            throw new ServiceException();
        }

        Person person = personDAOImpl.findById(personId)
                .orElseThrow(ServiceException::new);

        return personMapperImpl.toPersonDTO(person);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PersonDTO> getAll() {
        return personMapperImpl.toPersonDTOs(personDAOImpl.findAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonDTO add(PersonDTO personDTO) {

        if (personDTO == null) {
            throw new ServiceException(String.format(ADDING_UPDATING_ERROR, "person is null"));
        }

        validatePersonDTO(personDTO);

        return personMapperImpl.toPersonDTO(
                personDAOImpl.save(personMapperImpl.toPerson(personDTO)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(PersonDTO personDTO) {

        if (personDTO == null) {
            throw new ServiceException(String.format(ADDING_UPDATING_ERROR, "person is null"));
        }

        validatePersonDTO(personDTO);

        personMapperImpl.toPersonDTO(
                personDAOImpl.update(personMapperImpl.toPerson(personDTO)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(UUID personId) {
        if (personId == null) {
            throw new ServiceException(REMOVING_ERROR);
        }

        personDAOImpl.delete(personId);
    }

    /**
     * Validate PersonDTO
     * If validation failed - throws Service exception
     *
     * @param personDTO person DTO
     * @throws ServiceException - if validation failed
     */
    private void validatePersonDTO(PersonDTO personDTO) throws ServiceException {
        List<String> validationErrorMessages = new ArrayList<>();

        if (personDTO == null ||
                !validatorFactory.getValidator()
                        .validate(personDTO)
                        .stream()
                        .peek(c -> validationErrorMessages.add(c.getMessage()))
                        .collect(Collectors.toSet())
                        .isEmpty()) {
            throw new ServiceException(String.format(ADDING_UPDATING_ERROR, validationErrorMessages.get(0)));
        }

    }
}
