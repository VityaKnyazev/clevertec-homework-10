package ru.clevertec.knyazev.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.knyazev.dao.CasherDaoImpl;
import ru.clevertec.knyazev.dao.DiscountCardDAOJPA;
import ru.clevertec.knyazev.dao.ShopDAOJPA;
import ru.clevertec.knyazev.dao.StorageDAOJPA;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.data.DataFactory;
import ru.clevertec.knyazev.service.CasherServiceImpl;
import ru.clevertec.knyazev.service.PurchaseService;
import ru.clevertec.knyazev.service.PurchaseServiceImpl;
import ru.clevertec.knyazev.service.ShopServiceImpl;
import ru.clevertec.knyazev.service.StorageServiceImpl;
import ru.clevertec.knyazev.service.discount.DiscountCardService;
import ru.clevertec.knyazev.service.discount.DiscountProductGroupService;
import ru.clevertec.knyazev.service.discount.DiscountService;

@WebServlet(name = "ReceiptController", urlPatterns = { "/buy" })
public class ReceiptController extends HttpServlet {
	private static final long serialVersionUID = -5153841317395813873L;

	private PurchaseService purchaseServiceImpl;
	private DiscountService discountCardService;
	private DiscountService discountProductGroupService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		purchaseServiceImpl = new PurchaseServiceImpl(
				new StorageServiceImpl(new StorageDAOJPA(AppConnectionConfig.getEntityManager())),
				new CasherServiceImpl(new CasherDaoImpl()),
				new ShopServiceImpl(new ShopDAOJPA(AppConnectionConfig.getEntityManager())));
		discountCardService = new DiscountCardService(new DiscountCardDAOJPA(AppConnectionConfig.getEntityManager()));
		discountProductGroupService = new DiscountProductGroupService();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] purchases = req.getParameterValues("purchase");
		String[] cards = req.getParameterValues("card");
		String type = req.getParameter("type");
		
		final DataFactory.InputSource INPUT_VAL = DataFactory.InputSource.STANDARD;
		final DataFactory.OutputSource OUTPUT_VAL = DataFactory.OutputSource.CONSOLE;

		if (purchases == null || purchases.length == 0) {
			resp.setStatus(400);
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/plain");
			PrintWriter writer = resp.getWriter();
			writer.append("Bad Request - 400. Request param purchase should be present.");
		}
		
		List<String> purchasesCards = new ArrayList<>() {
			private static final long serialVersionUID = 15215815L;

			{
				add(INPUT_VAL.name().toLowerCase(Locale.ROOT));
				add(OUTPUT_VAL.name().toLowerCase(Locale.ROOT));
			}
		};
		
		
		Stream.of(purchases).forEach(purchase -> purchasesCards.add(purchase));
		
		if (cards != null && !(cards.length == 0)) {
			Stream.of(cards).forEach(card -> purchasesCards.add(card));
		}

		String[] inputData = purchasesCards.toArray(new String[0]);
		
		//TODO finish receipt method
		
	}

}
