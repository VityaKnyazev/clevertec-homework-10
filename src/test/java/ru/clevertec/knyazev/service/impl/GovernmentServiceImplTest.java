package ru.clevertec.knyazev.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.knyazev.dao.ServiceDAO;
import ru.clevertec.knyazev.data.ServiceDTO;
import ru.clevertec.knyazev.entity.Person;
import ru.clevertec.knyazev.entity.Service;
import ru.clevertec.knyazev.mapper.ServiceMapper;
import ru.clevertec.knyazev.mapper.ServiceMapperImpl;
import ru.clevertec.knyazev.pdf.PDFManager;
import ru.clevertec.knyazev.service.exception.ServiceException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class GovernmentServiceImplTest {

    @Captor
    ArgumentCaptor<List<ServiceDTO>> serviceDTOsArgumentCaptor;

    @Mock
    ServiceDAO serviceDAOImplMock;

    @Spy
    private ServiceMapper serviceMapperImpl = new ServiceMapperImpl();

    @Mock
    private PDFManager<List<ServiceDTO>> serviceCheckPDFManagerImplMock;

    @InjectMocks
    private GovernmentServiceImpl governmentServiceImpl;

    @Test
    public void checkGetAbsolutePathByPersonIdShouldReturnAbsolutePathToSavedPDF() {

        Person expectedPerson = Person.builder()
                .id(new UUID(25L, 36L))
                .name("Macho")
                .surname("Manager")
                .email("macho@mail.ru")
                .citizenship("Russia")
                .age(29)
                .build();

        List<Service> expectedServices = List.of(
                Service.builder()
                        .id(UUID.randomUUID())
                        .person(expectedPerson)
                        .name("Справка о заводах")
                        .description("Выдается директору")
                        .price(new BigDecimal("37.00"))
                        .build(),
                Service.builder()
                        .id(UUID.randomUUID())
                        .person(expectedPerson)
                        .name("Справка о пароходах")
                        .description("Выдается лоцману")
                        .price(new BigDecimal("17.00"))
                        .build(),
                Service.builder()
                        .id(UUID.randomUUID())
                        .person(expectedPerson)
                        .name("Справка о зарплате")
                        .description("Выдается работнику")
                        .price(new BigDecimal("7.00"))
                        .build()
        );

        String expectedPath = "/src/main/resources/pdf/check-service-2023-03-18 10-25-36-2569.pdf";

        Mockito.when(serviceDAOImplMock.findByPersonId(Mockito.any(UUID.class)))
                .thenReturn(expectedServices);
        Mockito.when(serviceCheckPDFManagerImplMock.manage(Mockito.anyList()))
                .thenReturn(expectedPath);


        UUID inputPersonId = new UUID(25L, 36L);
        String actualPath = governmentServiceImpl.getAbsolutePathByPersonId(inputPersonId);

        Mockito.verify(serviceCheckPDFManagerImplMock).manage(serviceDTOsArgumentCaptor.capture());

        assertAll(
                () -> assertThat(actualPath).isEqualTo(expectedPath),
                () -> assertThat(serviceDTOsArgumentCaptor.getValue()).isNotEmpty()
                        .hasSize(3)
        );
    }

    @Test
    public void checkGetAbsolutePathByPersonIdShouldThrowServiceExceptionWhenNullPersonId() {
        UUID inputPersonId = null;

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> governmentServiceImpl.getAbsolutePathByPersonId(inputPersonId));
    }

    @Test
    public void checkGetAbsolutePathByPersonIdShouldThrowServiceExceptionWhenEmptyServices() {

        Mockito.when(serviceDAOImplMock.findByPersonId(Mockito.any(UUID.class)))
                .thenReturn(Collections.emptyList());

        UUID inputPersonId = UUID.randomUUID();

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> governmentServiceImpl.getAbsolutePathByPersonId(inputPersonId));
    }

}
