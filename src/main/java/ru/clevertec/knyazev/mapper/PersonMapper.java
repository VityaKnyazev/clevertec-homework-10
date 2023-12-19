package ru.clevertec.knyazev.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.knyazev.data.PersonDTO;
import ru.clevertec.knyazev.entity.Person;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDTO toPersonDTO(Person person);

    Person toPerson(PersonDTO personDTO);

    List<PersonDTO> toPersonDTOs(List<Person> persons);

    List<Person> toPersons(List<PersonDTO> personDTOs);

}
