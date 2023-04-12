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
import ru.clevertec.knyazev.dao.ProductDAOJPA;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.service.ProductService;
import ru.clevertec.knyazev.service.ProductServiceImpl;
import ru.clevertec.knyazev.service.exception.ServiceException;

@WebServlet(name = "ProductController", urlPatterns = { "/products" })
public class ProductController extends HttpServlet {
	private static final long serialVersionUID = 554514546166415L;
	
	private ProductService productServiceImpl;
	
	private Gson gson;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		productServiceImpl = new ProductServiceImpl(new ProductDAOJPA(AppConnectionConfig.getInstance()));
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
				Optional<Product> product = productServiceImpl.showProduct(id);
				String productResult = (product.isEmpty()) ? "{}" : gson.toJson(product.get());
				sendResponse(resp, 200, "UTF-8", "application/json", productResult);
			} catch (NumberFormatException e) {
				sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400. Id param should be valid.");
			}
		} else {
			List<Product> products = null;

			if (pagePar != null) {
				try {
					Integer page = Integer.valueOf(pagePar);

					if (pageSizePar != null) {
						Integer pageSize = Integer.valueOf(pageSizePar);
						products = productServiceImpl.showAllProducts(page, pageSize);
					} else {
						products = productServiceImpl.showAllProducts(page);
					}

				} catch (NumberFormatException e) {
					sendResponse(resp, 400, "UTF-8", "text/plain",
							"Bad Request - 400. Page param or pagesize param should be valid.");
					return;
				}
			} else {
				products = productServiceImpl.showAllProducts();
			}

			String productsResult = (products.size() == 0) ? "[]"
					: gson.toJson(products);

			sendResponse(resp, 200, "UTF-8", "application/json", productsResult);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Product product = parseJSONRequestBody(req);
			Product savedProduct = productServiceImpl.addProduct(product);			
			sendResponse(resp, 200, "UTF-8", "application/json", gson.toJson(savedProduct));
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Product product = parseJSONRequestBody(req);
			Product updatedProduct = productServiceImpl.changeProduct(product);			
			sendResponse(resp, 200, "UTF-8", "application/json", gson.toJson(updatedProduct));
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}	
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Product product = parseJSONRequestBody(req);
			productServiceImpl.removeProduct(product);			
			sendResponse(resp, 200, "UTF-8", "application/json", "Successfully removed");
		} catch (JsonSyntaxException | ServiceException e) {
			sendResponse(resp, 400, "UTF-8", "text/plain", "Bad Request - 400." + e.getMessage());
		}	
	}
	
	private Product parseJSONRequestBody(HttpServletRequest req) throws IOException, JsonSyntaxException {
		String body = new String(req.getInputStream().readAllBytes(), Charset.forName("UTF-8"));
		Product product = gson.fromJson(body, Product.class);
		return product;		
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
