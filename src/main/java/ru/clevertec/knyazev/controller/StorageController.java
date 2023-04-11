package ru.clevertec.knyazev.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.knyazev.dao.StorageDAOJPA;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.service.StorageService;
import ru.clevertec.knyazev.service.StorageServiceImpl;
import ru.clevertec.knyazev.service.exception.ServiceException;

@WebServlet(name = "StorageController", urlPatterns = { "/storages" })
public class StorageController extends HttpServlet {
	private static final long serialVersionUID = -12454545421L;
	
	private StorageService storageServiceImpl;
	
	private Gson gson;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		storageServiceImpl = new StorageServiceImpl(new StorageDAOJPA(AppConnectionConfig.getInstance()));
		gson = new Gson();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idPar = req.getParameter("id");
		String pagePar = req.getParameter("page");
		String pageSizePar = req.getParameter("pagesize");

		if (idPar != null) {
			try {
				Long id = Long.valueOf(idPar);
				Optional<Storage> storage = storageServiceImpl.showStorage(id);
				String storageResult = (storage.isEmpty()) ? "{}" : gson.toJson(storage.get());
				sendResponse(resp, 200, "UTF-8", "application/json", storageResult);
			} catch (NumberFormatException e) {
				sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400. Id param should be valid.");
			}
		} else {
			List<Storage> storages = null;

			if (pagePar != null) {
				try {
					Integer page = Integer.valueOf(pagePar);

					if (pageSizePar != null) {
						Integer pageSize = Integer.valueOf(pageSizePar);
						storages = storageServiceImpl.showAllStorages(page, pageSize);
					} else {
						storages = storageServiceImpl.showAllStorages(page);
					}

				} catch (NumberFormatException e) {
					sendResponse(resp, 400, "UTF-8", "text/plain",
							"Bad Request - 400. Page param or pagesize param should be valid.");
					return;
				}
			} else {
				storages = storageServiceImpl.showAllStorages();
			}

			String storagesResult = (storages.size() == 0) ? "[]"
					: gson.toJson(storages);

			sendResponse(resp, 200, "UTF-8", "application/json", storagesResult);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Storage storage = parseJSONRequestBody(req);
			Storage savedStorage = storageServiceImpl.addStorage(storage);			
			sendResponse(resp, 200, "UTF-8", "application/json", gson.toJson(savedStorage));
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Storage storage = parseJSONRequestBody(req);
			Storage updatedStorage = storageServiceImpl.changeStorage(storage);			
			sendResponse(resp, 200, "UTF-8", "application/json", gson.toJson(updatedStorage));
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}	
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Storage storage = parseJSONRequestBody(req);
			storageServiceImpl.removeStorage(storage);			
			sendResponse(resp, 200, "UTF-8", "application/json", "Successfully removed");
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}	
	}
	
	private Storage parseJSONRequestBody(HttpServletRequest req) throws IOException, JsonSyntaxException {
		String body = new String(req.getInputStream().readAllBytes(), Charset.forName("UTF-8"));
		Storage storage = gson.fromJson(body, Storage.class);
		return storage;		
	}

	private final void sendResponse(HttpServletResponse resp, int codeStatus, String encoding, String contentType,
			String bodyMessage) throws IOException {
		resp.setStatus(codeStatus);
		resp.setCharacterEncoding(encoding);
		resp.setContentType(contentType);
		PrintWriter writer = resp.getWriter();
		writer.append(bodyMessage);
	}
	
}
