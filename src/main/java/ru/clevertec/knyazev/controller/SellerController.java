package ru.clevertec.knyazev.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.knyazev.dao.SellerDAOJPA;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.data.validator.SellerValidator;
import ru.clevertec.knyazev.entity.Seller;
import ru.clevertec.knyazev.service.SellerService;
import ru.clevertec.knyazev.service.SellerServiceImpl;

@WebServlet(name = "SellerController", urlPatterns =  { "/sellers" })
public class SellerController extends HttpServlet {
	private static final long serialVersionUID = 1654584545L;

	private SellerService sellerServiceImpl;
	private SellerValidator sellerValidator;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		sellerServiceImpl = new SellerServiceImpl(new SellerDAOJPA(AppConnectionConfig.getEntityManager()));
		sellerValidator = new SellerValidator();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String idPar = req.getParameter("id");

		if (idPar != null) {
			try {
				Long id = Long.valueOf(idPar);
				Optional<Seller> seller = sellerServiceImpl.showSeller(id);
				String sellerResult = (seller.isEmpty()) ? "{}" : seller.get().toString();
				resp.setStatus(200);
				resp.setCharacterEncoding("UTF-8");
				resp.setContentType("application/json");
				PrintWriter writer = resp.getWriter();
				writer.append(sellerResult);
			} catch (NumberFormatException e) {
				resp.setStatus(400);
				resp.setCharacterEncoding("UTF-8");
				resp.setContentType("text/plain");
				PrintWriter writer = resp.getWriter();
				writer.append("Bad Request - 400. Id param should be valid.");
			}
		} else {
			List<Seller> sellers = sellerServiceImpl.showAllSellers();

			String sellerResult = (sellers.size() == 0) ? "[]"
					: sellers.stream().map(seller -> seller.toString()).collect(Collectors.joining(",", "[", "]"));
			sellerResult = sellerResult.replaceFirst(",\\]$", "]");

			resp.setStatus(200);
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json");
			PrintWriter writer = resp.getWriter();
			writer.append(sellerResult);
		}

	}

}
