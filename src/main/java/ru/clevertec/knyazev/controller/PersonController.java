package ru.clevertec.knyazev.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MimeTypeUtils;
import ru.clevertec.knyazev.config.AppContextListener;
import ru.clevertec.knyazev.config.PagingProperties;
import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.data.PersonDTO;
import ru.clevertec.knyazev.pagination.Paging;
import ru.clevertec.knyazev.pagination.impl.PagingImpl;
import ru.clevertec.knyazev.service.PersonService;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.util.HttpServletUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Slf4j
@WebServlet(value = "/persons")
public class PersonController extends HttpServlet {
    private static final String PAGE_REQUEST_PARAM = "page";
    private static final String PAGE_SIZE_REQUEST_PARAM = "page_size";

    private static final String EMPTY_JSON = "{}";

    private Gson gson;

    private PagingProperties pagingProperties;

    private PersonService personServiceImpl;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        gson = (Gson) config.getServletContext()
                .getAttribute(AppContextListener.GSON);
        pagingProperties = (PagingProperties) config.getServletContext()
                .getAttribute(AppContextListener.PAGING_PROPERTIES);
        personServiceImpl = (PersonService) config.getServletContext()
                .getAttribute(AppContextListener.PERSON_SERVICE_IMPL);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = HttpServletUtil.getPathParameter(req);

        String pageParam = req.getParameter(PAGE_REQUEST_PARAM);
        String pageSizeParam = req.getParameter(PAGE_SIZE_REQUEST_PARAM);

        String jsonResult = EMPTY_JSON;

        try {

            if (idParam != null) {
                UUID id = UUID.fromString(idParam);

                PersonDTO personDTO = personServiceImpl.get(id);
                jsonResult = gson.toJson(personDTO);

            } else {
                Integer page = (pageParam != null)
                        ? Integer.valueOf(pageParam)
                        : null;
                Integer pageSize = (pageSizeParam != null)
                        ? Integer.valueOf(pageSizeParam)
                        : null;

                Paging paging = new PagingImpl(page,
                        pageSize,
                        pagingProperties);

                List<PersonDTO> personDTOS = personServiceImpl.getAll(paging);

                jsonResult = gson.toJson(personDTOS);
            }

            HttpServletUtil.sendResponse(resp,
                    HttpServletResponse.SC_OK,
                    StandardCharsets.UTF_8.name(),
                    MimeTypeUtils.APPLICATION_JSON_VALUE,
                    jsonResult);
        } catch (IllegalArgumentException | ServiceException | DAOException e) {
            log.error(e.getMessage(), e);

            HttpServletUtil.sendResponse(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    StandardCharsets.UTF_8.name(),
                    MimeTypeUtils.APPLICATION_JSON_VALUE,
                    jsonResult);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            PersonDTO personDTO = HttpServletUtil.parseJSONRequestBody(req, gson, PersonDTO.class);
            PersonDTO savedPerssonDTO = personServiceImpl.add(personDTO);

            HttpServletUtil.sendResponse(resp,
                    HttpServletResponse.SC_CREATED,
                    StandardCharsets.UTF_8.name(),
                    MimeTypeUtils.APPLICATION_JSON_VALUE,
                    gson.toJson(savedPerssonDTO));
        } catch (IOException | JsonSyntaxException | ServiceException | DAOException e) {
            log.error(e.getMessage(), e);

            HttpServletUtil.sendResponse(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    StandardCharsets.UTF_8.name(),
                    MimeTypeUtils.APPLICATION_JSON_VALUE,
                    EMPTY_JSON);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            PersonDTO personDTO = HttpServletUtil.parseJSONRequestBody(req, gson, PersonDTO.class);
            personServiceImpl.update(personDTO);

            HttpServletUtil.sendResponse(resp,
                    HttpServletResponse.SC_OK,
                    StandardCharsets.UTF_8.name(),
                    MimeTypeUtils.APPLICATION_JSON_VALUE,
                    gson.toJson(personDTO));
        } catch (IOException | JsonSyntaxException | ServiceException | DAOException e) {
            log.error(e.getMessage(), e);

            HttpServletUtil.sendResponse(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    StandardCharsets.UTF_8.name(),
                    MimeTypeUtils.APPLICATION_JSON_VALUE,
                    EMPTY_JSON);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
       String personIdParam = HttpServletUtil.getPathParameter(req);

        try {
            UUID personId = UUID.fromString(personIdParam);
            personServiceImpl.remove(personId);

            HttpServletUtil.sendResponse(resp,
                    HttpServletResponse.SC_OK,
                    StandardCharsets.UTF_8.name(),
                    MimeTypeUtils.APPLICATION_JSON_VALUE,
                    gson.toJson(EMPTY_JSON));
        } catch (IllegalArgumentException  | ServiceException | DAOException e) {
            log.error(e.getMessage(), e);

            HttpServletUtil.sendResponse(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    StandardCharsets.UTF_8.name(),
                    MimeTypeUtils.APPLICATION_JSON_VALUE,
                    EMPTY_JSON);
        }
    }
}
