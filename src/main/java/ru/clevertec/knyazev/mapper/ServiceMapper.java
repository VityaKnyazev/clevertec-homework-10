package ru.clevertec.knyazev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.knyazev.data.ServiceDTO;
import ru.clevertec.knyazev.entity.Service;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mapping(target = "personId", source = "person.id")
    @Mapping(target = "personName", source = "person.name")
    @Mapping(target = "personSurname", source = "person.surname")
    @Mapping(target = "serviceName", source = "name")
    @Mapping(target = "localDateTime", expression = "java(java.time.LocalDateTime.now())")
    ServiceDTO toServiceDTO(Service service);

    List<ServiceDTO> toServiceDTOs(List<Service> services);

}
