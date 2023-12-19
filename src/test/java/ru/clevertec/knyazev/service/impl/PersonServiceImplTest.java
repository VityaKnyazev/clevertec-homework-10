package ru.clevertec.knyazev.service.impl;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Condition;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.data.PersonDTO;
import ru.clevertec.knyazev.entity.Person;
import ru.clevertec.knyazev.mapper.PersonMapper;
import ru.clevertec.knyazev.mapper.PersonMapperImpl;
import ru.clevertec.knyazev.service.exception.ServiceException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {
    @Mock
    private PersonDAO personDAOImplMock;

    @Spy
    private PersonMapper personMapperImplSpy = new PersonMapperImpl();

    @Spy
    private ValidatorFactory validatorFactorySpy = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();

    @Captor
    ArgumentCaptor<Person> personArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @InjectMocks
    private PersonServiceImpl personServiceImpl;

    @Test
    public void checkGetShouldReturnPersonDTO() {
        Person expectedPerson = Person.builder()
                .id(new UUID(251L, 16L))
                .name("Misha")
                .surname("Nikolaev")
                .email("misha@mail.ru")
                .citizenship("Belarus")
                .age(48)
                .build();


        Mockito.when(personDAOImplMock.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(expectedPerson));

        UUID inputUUID = new UUID(251L, 16L);
        PersonDTO actualPersonDTO = personServiceImpl.get(inputUUID);

        assertThat(actualPersonDTO).isNotNull()
                .has(new Condition<>(p -> p.id().equals(inputUUID), "check id"))
                .has(new Condition<>(p -> p.name().equals(expectedPerson.getName()), "check name"))
                .has(new Condition<>(p ->
                        p.surname().equals(expectedPerson.getSurname()), "check surname"))
                .has(new Condition<>(p -> p.email().equals(expectedPerson.getEmail()), "check email"))
                .has(new Condition<>(p ->
                        p.citizenship().equals(expectedPerson.getCitizenship()), "check citizenship"))
                .has(new Condition<>(p -> p.age().equals(expectedPerson.getAge()), "check age"));
    }

    @Test
    public void checkGetShouldThrowServiceException() {
        Optional<Person> expectedPerson = Optional.empty();

        Mockito.when(personDAOImplMock.findById(Mockito.any(UUID.class)))
                .thenReturn(expectedPerson);

        UUID inputUUID = new UUID(24L, 18L);
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> personServiceImpl.get(inputUUID));

    }

    @Test
    public void checkGetShouldThrowServiceExceptionWhenIdIsNull() {
        UUID inputUUID = null;
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> personServiceImpl.get(inputUUID));
    }

    @Test
    public void checkGetAllShouldReturnPersonDTOs() {
        List<Person> expectedPersons = List.of(
                Person.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .surname("Alexandrov")
                        .email("vasya@gmail.com")
                        .citizenship("Russia")
                        .age(28)
                        .build(),
                Person.builder()
                        .name("Kolya")
                        .surname("Petrov")
                        .email("kolya@gmail.com")
                        .citizenship("Belarus")
                        .age(22)
                        .build()
        );

        Mockito.when(personDAOImplMock.findAll())
                .thenReturn(expectedPersons);

        List<PersonDTO> actualPersonDTOs = personServiceImpl.getAll();

        assertThat(actualPersonDTOs)
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    public void checkGetAllShouldReturnEmptyList() {
        List<Person> expectedPersons = Collections.emptyList();

        Mockito.when(personDAOImplMock.findAll())
                .thenReturn(expectedPersons);

        List<PersonDTO> actualPersonDTOs = personServiceImpl.getAll();

        assertThat(actualPersonDTOs)
                .isEmpty();
    }

    @Test
    public void checkAddShouldReturnSavedPersonDTO() {

        Mockito.when(personDAOImplMock.save(Mockito.any(Person.class)))
                .thenAnswer(invocation -> {
                    Person savedPerson = invocation.getArgument(0);
                    savedPerson.setId(new UUID(125L, 228L));
                    return savedPerson;
                });

        PersonDTO inputSavingPersonDTO = PersonDTO.builder()
                .name("Vasya")
                .surname("Alexandrov")
                .email("vasya@gmail.com")
                .citizenship("Russia")
                .age(28)
                .build();

        PersonDTO actualSavedPersonDTO = personServiceImpl.add(inputSavingPersonDTO);

        assertThat(actualSavedPersonDTO)
                .isNotNull()
                .has(new Condition<>(p -> p.id().equals(new UUID(125L, 228L)), "check id"))
                .has(new Condition<>(p -> p.name().equals(inputSavingPersonDTO.name()), "check name"))
                .has(new Condition<>(p ->
                        p.surname().equals(inputSavingPersonDTO.surname()), "check surname"))
                .has(new Condition<>(p -> p.email().equals(inputSavingPersonDTO.email()), "check email"))
                .has(new Condition<>(p ->
                        p.citizenship().equals(inputSavingPersonDTO.citizenship()), "check citizenship"))
                .has(new Condition<>(p -> p.age().equals(inputSavingPersonDTO.age()), "check age"));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("getPersonDTOForAddAndUpdateMethodsNegative")
    public void checkAddShouldThrowServiceExceptionWhenPersonDTOValidationFailed(PersonDTO invalidPersonDTO) {
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> personServiceImpl.add(invalidPersonDTO));
    }

    @Test
    public void checkUpdateShouldReturnUpdatedPersonDTO() {

        Mockito.when(personDAOImplMock.update(Mockito.any(Person.class)))
                .thenAnswer(invocation -> (Person) invocation.getArgument(0));

        PersonDTO inputUpdatingPersonDTO = PersonDTO.builder()
                .id(UUID.randomUUID())
                .name("Vasya")
                .surname("Alexandrov")
                .email("vasya@gmail.com")
                .citizenship("Russia")
                .age(28)
                .build();

        personServiceImpl.update(inputUpdatingPersonDTO);

        Mockito.verify(personDAOImplMock).update(personArgumentCaptor.capture());

        assertThat(personArgumentCaptor.getValue()).isNotNull()
                .has(new Condition<>(p -> p.getId().equals(inputUpdatingPersonDTO.id()), "check id"))
                .has(new Condition<>(p -> p.getName().equals(inputUpdatingPersonDTO.name()), "check name"))
                .has(new Condition<>(p ->
                        p.getSurname().equals(inputUpdatingPersonDTO.surname()), "check surname"))
                .has(new Condition<>(p ->
                        p.getEmail().equals(inputUpdatingPersonDTO.email()), "check email"))
                .has(new Condition<>(p ->
                        p.getCitizenship().equals(inputUpdatingPersonDTO.citizenship()), "check citizenship"))
                .has(new Condition<>(p ->
                        p.getAge().equals(inputUpdatingPersonDTO.age()), "check age"));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("getPersonDTOForAddAndUpdateMethodsNegative")
    public void checkUpdateShouldThrowServiceExceptionWhenPersonDTOValidationFailed(PersonDTO invalidPersonDTO) {
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> personServiceImpl.update(invalidPersonDTO));
    }

    @Test
    public void checkRemoveShouldRemoveFromDatasource() {
        UUID inputPersonId = UUID.randomUUID();

        personServiceImpl.remove(inputPersonId);

        Mockito.verify(personDAOImplMock).delete(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(inputPersonId);
    }

    @Test
    public void checkRemoveShouldThrowServiceException() {
        UUID inputPersonId = null;

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> personServiceImpl.remove(inputPersonId));
    }

    private static Stream<PersonDTO> getPersonDTOForAddAndUpdateMethodsNegative() {
        return Stream.of(
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .surname("Alexandrov")
                        .email("vasya@gmail.com")
                        .citizenship("Belarus")
                        .age(28)
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasiliu Kyk")
                        .surname("Alexandrov")
                        .email("vasya@gmail.com")
                        .citizenship("Russia")
                        .age(28)
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .email("vasya@gmail.com")
                        .citizenship("Russia")
                        .age(28)
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .surname("VeliKii Ruskiu knyazj")
                        .email("vasya@gmail.com")
                        .citizenship("Poland")
                        .age(28)
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .surname("Alexandrov")
                        .citizenship("Poland")
                        .age(28)
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .surname("VeliKii Ruskiu knyazj")
                        .email("A@yc")
                        .citizenship("Poland")
                        .age(28)
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .surname("VeliKii Ruskiu knyazj")
                        .email("vasya9999999999999999999999999999999@gmail.com")
                        .citizenship("Poland")
                        .age(28)
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .surname("Alexandrov")
                        .email("vasya@gmail.com")
                        .age(28)
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .surname("Alexandrov")
                        .email("vasya@gmail.com")
                        .citizenship("Vilika Respublika Keltskaj")
                        .age(28)
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .surname("Alexandrov")
                        .email("vasya@gmail.com")
                        .citizenship("Russia")
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .surname("Alexandrov")
                        .email("vasya@gmail.com")
                        .citizenship("Russia")
                        .age(-1)
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .surname("Alexandrov")
                        .email("vasya@gmail.com")
                        .citizenship("Russia")
                        .age(0)
                        .build(),
                PersonDTO.builder()
                        .id(UUID.randomUUID())
                        .name("Vasya")
                        .surname("Alexandrov")
                        .email("vasya@gmail.com")
                        .citizenship("Russia")
                        .age(151)
                        .build()
        );
    }

}
