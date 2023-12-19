package ru.clevertec.knyazev.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.knyazev.dao.ServiceDAO;
import ru.clevertec.knyazev.data.ServiceDTO;
import ru.clevertec.knyazev.mapper.ServiceMapper;
import ru.clevertec.knyazev.pdf.PDFManager;
import ru.clevertec.knyazev.service.GovernmentService;
import ru.clevertec.knyazev.service.exception.ServiceException;

import java.util.List;
import java.util.UUID;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = { @Autowired })
public class GovernmentServiceImpl implements GovernmentService {

    private static final String PERSON_SERVICE_ERROR = "No services for person id=%s where found";

    private ServiceDAO serviceDAOImpl;

    private ServiceMapper serviceMapperImpl;

    private PDFManager<List<ServiceDTO>> serviceCheckPDFManagerImpl;

    /**
     * @implSpec
     */
    @Override
    public String getAbsolutePathByPersonId(UUID personId) {

        if (personId == null) {
            throw new ServiceException(String.format(PERSON_SERVICE_ERROR, "null"));
        }

        List<ServiceDTO> serviceDTOs = serviceMapperImpl.toServiceDTOs(
                serviceDAOImpl.findByPersonId(personId));

        if (serviceDTOs.isEmpty()) {
            throw new ServiceException(String.format(PERSON_SERVICE_ERROR, personId));
        }

        return serviceCheckPDFManagerImpl.manage(serviceDTOs);
    }
}
