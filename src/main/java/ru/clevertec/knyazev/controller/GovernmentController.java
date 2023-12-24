package ru.clevertec.knyazev.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MimeTypeUtils;
import ru.clevertec.knyazev.config.AppContextListener;
import ru.clevertec.knyazev.pdf.exception.PDFDocumentException;
import ru.clevertec.knyazev.service.GovernmentService;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.util.HttpServletUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@WebServlet(value = "/services")
public class GovernmentController extends HttpServlet {
    private static final String APPLICATION_PDF_MIME_TYPE = "application/pdf";
    private static final String EMPTY_JSON = "{}";

    private GovernmentService governmentServiceImpl;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        governmentServiceImpl = (GovernmentService) config.getServletContext()
                .getAttribute(AppContextListener.GOVERNMENT_SERVICE_IMPL);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String personIdParam = HttpServletUtil.getPathParameter(req);

        try {
            UUID personId = UUID.fromString(personIdParam);

            String pdfLink = governmentServiceImpl.getAbsolutePathByPersonId(personId);

            try (InputStream is = new FileInputStream(pdfLink)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
                resp.setContentType(APPLICATION_PDF_MIME_TYPE);
                resp.getOutputStream().write(is.readAllBytes());
            }

        } catch (IllegalArgumentException | FileNotFoundException |
                 SecurityException | PDFDocumentException | ServiceException e) {
            log.error(e.getMessage(), e);

            HttpServletUtil.sendResponse(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    StandardCharsets.UTF_8.name(),
                    MimeTypeUtils.APPLICATION_JSON_VALUE,
                    EMPTY_JSON);
        }
    }
}
