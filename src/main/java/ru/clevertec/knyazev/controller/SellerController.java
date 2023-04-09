package ru.clevertec.knyazev.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.knyazev.dao.SellerDAOJPA;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.data.validator.SellerValidator;
import ru.clevertec.knyazev.entity.Seller;
import ru.clevertec.knyazev.service.SellerService;
import ru.clevertec.knyazev.service.SellerServiceImpl;
import ru.clevertec.knyazev.service.exception.ServiceException;

@WebServlet(name = "SellerController", urlPatterns = { "/sellers" }, loadOnStartup = 2)
public class SellerController extends HttpServlet {
	private static final long serialVersionUID = 1654584545L;

	private SellerService sellerServiceImpl;
	private SellerValidator sellerValidator;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		sellerServiceImpl = new SellerServiceImpl(new SellerDAOJPA(AppConnectionConfig.getInstance()));
		sellerValidator = new SellerValidator();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String idPar = req.getParameter("id");
		String pagePar = req.getParameter("page");
		String pageSizePar = req.getParameter("pagesize");

		if (idPar != null) {
			try {
				Long id = Long.valueOf(idPar);
				Optional<Seller> seller = sellerServiceImpl.showSeller(id);
				String sellerResult = (seller.isEmpty()) ? "{}" : seller.get().toString();
				sendResponse(resp, 200, "UTF-8", "application/json", sellerResult);
			} catch (NumberFormatException e) {
				sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400. Id param should be valid.");
			}
		} else {
			List<Seller> sellers = null;

			if (pagePar != null) {
				try {
					Integer page = Integer.valueOf(pagePar);

					if (pageSizePar != null) {
						Integer pageSize = Integer.valueOf(pageSizePar);
						sellers = sellerServiceImpl.showAllSellers(page, pageSize);
					} else {
						sellers = sellerServiceImpl.showAllSellers(page);
					}

				} catch (NumberFormatException e) {
					sendResponse(resp, 400, "UTF-8", "text/plain",
							"Bad Request - 400. Page param or pagesize param should be valid.");
					return;
				}
			} else {
				sellers = sellerServiceImpl.showAllSellers();
			}

			String sellerResult = (sellers.size() == 0) ? "[]"
					: sellers.stream().map(seller -> seller.toString()).collect(Collectors.joining(",", "[", "]"));
			sellerResult = sellerResult.replaceFirst(",\\]$", "]");

			sendResponse(resp, 200, "UTF-8", "application/json", sellerResult);
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Gson gson = new Gson();

		String body = new String(req.getInputStream().readAllBytes(), Charset.forName("UTF-8"));
		try {
			Seller seller = gson.fromJson(body, Seller.class);
			sellerValidator.validate(seller);
			sellerServiceImpl.addSeller(seller);
		} catch (JsonSyntaxException | ValidatorException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400. Bad Seller JSON.");
		}	
		
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
