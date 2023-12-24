package ru.clevertec.knyazev.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Represents util methods for HTTPServlet object
 */
@Slf4j
public class HttpServletUtil {

    private final static String URL_SEPARATOR = "/";

    /**
     *
     * Get path parameter from HTTP request
     *
     * @param req HTTP request
     * @return path parameter from HTTP request or null
     */
    public static String getPathParameter(HttpServletRequest req) {
        String pathParameter = null;

        try {
            String path = req.getPathInfo();

            if (path != null) {
                String[] splitPaths = path.split(URL_SEPARATOR);
                pathParameter = splitPaths[splitPaths.length - 1];
            }
        } catch (IllegalArgumentException e) {
            log.debug(e.getMessage(), e);
        }

        return pathParameter;
    }

    /**
     *
     * Send response to client
     *
     * @param resp HTTP servlet response
     * @param codeStatus HTTP code status
     * @param encoding encoding
     * @param contentType content MIME type
     * @param bodyMessage body
     * @throws IOException if an input or output exception occurred
     */
    public static void sendResponse(HttpServletResponse resp,
                              int codeStatus,
                              String encoding,
                              String contentType,
                              String bodyMessage) throws IOException {
        resp.setStatus(codeStatus);
        resp.setCharacterEncoding(encoding);
        resp.setContentType(contentType);
        PrintWriter writer = resp.getWriter();
        writer.append(bodyMessage);
    }

    /**
     *
     * Parse JSON to DTO object
     *
     * @param req HTTP servlet request
     * @param gson gson parser
     * @param t object type in which JSON should be parsed
     * @return T object DTO that represents given JSON
     * @param <T> object type that represents JSON
     * @throws IOException if an I/O error occurs
     * @throws JsonSyntaxException if json is not a valid representation for an object of type T
     * @throws OutOfMemoryError if an array of the required size cannot be allocated.
     */
    public static <T> T parseJSONRequestBody(HttpServletRequest req, Gson gson, Class<T> t) throws IOException, JsonSyntaxException, OutOfMemoryError {
        String body = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return (T) gson.fromJson(body, t);
    }

}
