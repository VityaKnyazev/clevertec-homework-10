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
import ru.clevertec.knyazev.dao.ShopDAOJPA;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.Shop;
import ru.clevertec.knyazev.service.ShopService;
import ru.clevertec.knyazev.service.ShopServiceImpl;
import ru.clevertec.knyazev.service.exception.ServiceException;

@WebServlet(name = "ShopController", urlPatterns = { "/shops" })
public class ShopController extends HttpServlet {
	private static final long serialVersionUID = 7176615493838642L;
	
	private ShopService shopServiceImpl;
	
	private Gson gson;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		shopServiceImpl = new ShopServiceImpl(new ShopDAOJPA(AppConnectionConfig.getInstance()));
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
				Optional<Shop> shop = shopServiceImpl.showShop(id);
				String shopResult = (shop.isEmpty()) ? "{}" : gson.toJson(shop.get());
				sendResponse(resp, 200, "UTF-8", "application/json", shopResult);
			} catch (NumberFormatException e) {
				sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400. Id param should be valid.");
			}
		} else {
			List<Shop> shops = null;

			if (pagePar != null) {
				try {
					Integer page = Integer.valueOf(pagePar);

					if (pageSizePar != null) {
						Integer pageSize = Integer.valueOf(pageSizePar);
						shops = shopServiceImpl.showAllShops(page, pageSize);
					} else {
						shops = shopServiceImpl.showAllShops(page);
					}

				} catch (NumberFormatException e) {
					sendResponse(resp, 400, "UTF-8", "text/plain",
							"Bad Request - 400. Page param or pagesize param should be valid.");
					return;
				}
			} else {
				shops = shopServiceImpl.showAllShops();
			}

			String shopsResult = (shops.size() == 0) ? "[]"
					: gson.toJson(shops);

			sendResponse(resp, 200, "UTF-8", "application/json", shopsResult);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Shop shop = parseJSONRequestBody(req);
			Shop savedShop = shopServiceImpl.addShop(shop);			
			sendResponse(resp, 200, "UTF-8", "application/json", gson.toJson(savedShop));
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Shop shop = parseJSONRequestBody(req);
			Shop updatedShop = shopServiceImpl.changeShop(shop);			
			sendResponse(resp, 200, "UTF-8", "application/json", gson.toJson(updatedShop));
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}	
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Shop shop = parseJSONRequestBody(req);
			shopServiceImpl.removeShop(shop);			
			sendResponse(resp, 200, "UTF-8", "application/json", "Successfully removed");
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}	
	}
	
	private Shop parseJSONRequestBody(HttpServletRequest req) throws IOException, JsonSyntaxException {
		String body = new String(req.getInputStream().readAllBytes(), Charset.forName("UTF-8"));
		Shop shop = gson.fromJson(body, Shop.class);
		return shop;		
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
