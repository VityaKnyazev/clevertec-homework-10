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
import ru.clevertec.knyazev.dao.DiscountCardDAOJPA;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.DiscountCard;
import ru.clevertec.knyazev.service.discount.DiscountCardService;
import ru.clevertec.knyazev.service.exception.ServiceException;

@WebServlet(name = "DiscountCardController", urlPatterns = { "/discards" })
public class DiscountCardController extends HttpServlet {
	private static final long serialVersionUID = 7175394385493838642L;
	
	private DiscountCardService discountCardService;
	
	private Gson gson;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		discountCardService = new DiscountCardService(new DiscountCardDAOJPA(AppConnectionConfig.getInstance()));
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
				Optional<DiscountCard> discountCard = discountCardService.showDiscountCard(id);
				String discounrCardResult = (discountCard.isEmpty()) ? "{}" : gson.toJson(discountCard.get());
				sendResponse(resp, 200, "UTF-8", "application/json", discounrCardResult);
			} catch (NumberFormatException e) {
				sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400. Id param should be valid.");
			}
		} else {
			List<DiscountCard> discountCards = null;

			if (pagePar != null) {
				try {
					Integer page = Integer.valueOf(pagePar);

					if (pageSizePar != null) {
						Integer pageSize = Integer.valueOf(pageSizePar);
						discountCards = discountCardService.showAllDiscountCards(page, pageSize);
					} else {
						discountCards = discountCardService.showAllDiscountCards(page);
					}

				} catch (NumberFormatException e) {
					sendResponse(resp, 400, "UTF-8", "text/plain",
							"Bad Request - 400. Page param or pagesize param should be valid.");
					return;
				}
			} else {
				discountCards = discountCardService.showAllDiscountCards();
			}

			String addressesResult = (discountCards.size() == 0) ? "[]"
					: gson.toJson(discountCards);

			sendResponse(resp, 200, "UTF-8", "application/json", addressesResult);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			DiscountCard discountCard = parseJSONRequestBody(req);
			DiscountCard savedDiscountCard = discountCardService.addDiscountCard(discountCard);			
			sendResponse(resp, 200, "UTF-8", "application/json", gson.toJson(savedDiscountCard));
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			DiscountCard discountCard = parseJSONRequestBody(req);
			DiscountCard updatedDiscountCard = discountCardService.changeDiscountCard(discountCard);			
			sendResponse(resp, 200, "UTF-8", "application/json", gson.toJson(updatedDiscountCard));
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}	
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			DiscountCard discountCard = parseJSONRequestBody(req);
			discountCardService.removeDiscountCard(discountCard);			
			sendResponse(resp, 200, "UTF-8", "application/json", "Successfully removed");
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}	
	}
	
	private DiscountCard parseJSONRequestBody(HttpServletRequest req) throws IOException, JsonSyntaxException {
		String body = new String(req.getInputStream().readAllBytes(), Charset.forName("UTF-8"));
		DiscountCard discountCard = gson.fromJson(body, DiscountCard.class);
		return discountCard;		
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
