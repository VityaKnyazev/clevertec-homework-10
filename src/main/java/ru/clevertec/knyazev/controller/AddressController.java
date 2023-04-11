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
import ru.clevertec.knyazev.dao.AddressDAOJPA;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.service.AddressService;
import ru.clevertec.knyazev.service.AddressServiceImpl;
import ru.clevertec.knyazev.service.exception.ServiceException;

@WebServlet(name = "AddressController", urlPatterns = { "/addresses" })
public class AddressController extends HttpServlet {
	private static final long serialVersionUID = 7175394385493838642L;
	
	private AddressService addressServiceImpl;
	
	private Gson gson;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		addressServiceImpl = new AddressServiceImpl(new AddressDAOJPA(AppConnectionConfig.getInstance()));
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
				Optional<Address> address = addressServiceImpl.showAddress(id);
				String shopResult = (address.isEmpty()) ? "{}" : gson.toJson(address.get());
				sendResponse(resp, 200, "UTF-8", "application/json", shopResult);
			} catch (NumberFormatException e) {
				sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400. Id param should be valid.");
			}
		} else {
			List<Address> addresses = null;

			if (pagePar != null) {
				try {
					Integer page = Integer.valueOf(pagePar);

					if (pageSizePar != null) {
						Integer pageSize = Integer.valueOf(pageSizePar);
						addresses = addressServiceImpl.showAllAddresses(page, pageSize);
					} else {
						addresses = addressServiceImpl.showAllAddresses(page);
					}

				} catch (NumberFormatException e) {
					sendResponse(resp, 400, "UTF-8", "text/plain",
							"Bad Request - 400. Page param or pagesize param should be valid.");
					return;
				}
			} else {
				addresses = addressServiceImpl.showAllAddresses();
			}

			String addressesResult = (addresses.size() == 0) ? "[]"
					: gson.toJson(addresses);

			sendResponse(resp, 200, "UTF-8", "application/json", addressesResult);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Address address = parseJSONRequestBody(req);
			Address savedAddress = addressServiceImpl.addAddress(address);			
			sendResponse(resp, 200, "UTF-8", "application/json", gson.toJson(savedAddress));
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Address address = parseJSONRequestBody(req);
			Address updatedAddress = addressServiceImpl.changeAddress(address);			
			sendResponse(resp, 200, "UTF-8", "application/json", gson.toJson(updatedAddress));
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}	
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Address address = parseJSONRequestBody(req);
			addressServiceImpl.removeAddress(address);			
			sendResponse(resp, 200, "UTF-8", "application/json", "Successfully removed");
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}	
	}
	
	private Address parseJSONRequestBody(HttpServletRequest req) throws IOException, JsonSyntaxException {
		String body = new String(req.getInputStream().readAllBytes(), Charset.forName("UTF-8"));
		Address address = gson.fromJson(body, Address.class);
		return address;		
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
