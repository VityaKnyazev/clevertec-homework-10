package ru.clevertec.knyazev.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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

@WebServlet(name = "FController", urlPatterns = "/sellers")
public class FController extends HttpServlet {
	private static final long serialVersionUID = 1654584545L;

	private SellerService sellerServiceImpl;
	private SellerValidator sellerValidator;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Seller> sellers = sellerServiceImpl.showAllSellers();

		String sellerResult = (sellers.size() == 0) ? "[]"
				: sellers.stream().map(seller -> seller.toString()).collect(Collectors.joining(",", "[", "]"));
		sellerResult = sellerResult.replaceFirst(",\\]$", "]");
		
		resp.setStatus(200);
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		PrintWriter writer=resp.getWriter();
		writer.append(sellerResult);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		sellerServiceImpl = new SellerServiceImpl(new SellerDAOJPA(AppConnectionConfig.getEntityManager()));
		sellerValidator = new SellerValidator();
	}

	

}
