package ru.clevertec.knyazev.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
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
import ru.clevertec.knyazev.data.DataReaderWriterDecorator;
import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.dto.receipt.AbstractReceiptBuilder;
import ru.clevertec.knyazev.dto.receipt.AbstractReceiptBuilder.ReceiptType;
import ru.clevertec.knyazev.service.CasherServiceImpl;
import ru.clevertec.knyazev.service.PurchaseService;
import ru.clevertec.knyazev.service.PurchaseServiceImpl;
import ru.clevertec.knyazev.service.ShopServiceImpl;
import ru.clevertec.knyazev.service.StorageServiceImpl;
import ru.clevertec.knyazev.service.discount.DiscountCardService;
import ru.clevertec.knyazev.service.discount.DiscountProductGroupService;
import ru.clevertec.knyazev.service.discount.DiscountService;
import ru.clevertec.knyazev.service.discount.DiscountServiceComposite;
import ru.clevertec.knyazev.service.exception.ServiceException;

@WebServlet(name = "PurchaseController", urlPatterns = { "/buy" }, loadOnStartup = 1)
public class PurchaseController extends HttpServlet {
	private static final long serialVersionUID = -5153841317395813873L;
	
	private PurchaseService purchaseServiceImpl;
	private DiscountService discountCardService;
	private DiscountService discountProductGroupService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		AppConnectionConfig appConnectionConfig = AppConnectionConfig.getInstance();
		
		purchaseServiceImpl = new PurchaseServiceImpl(
				new StorageServiceImpl(new StorageDAOJPA(appConnectionConfig)),
				new CasherServiceImpl(new CasherDaoImpl()),
				new ShopServiceImpl(new ShopDAOJPA(appConnectionConfig)));
		discountCardService = new DiscountCardService(new DiscountCardDAOJPA(appConnectionConfig));
		discountProductGroupService = new DiscountProductGroupService();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] purchases = req.getParameterValues("purchase");
		String[] cards = req.getParameterValues("card");
		String receiptType = req.getParameter("type");

		final DataFactory.InputSource INPUT_VAL = DataFactory.InputSource.STANDARD;
		final DataFactory.OutputSource OUTPUT_VAL = DataFactory.OutputSource.CONSOLE;

		if (purchases == null || purchases.length == 0) {
			resp.setStatus(400);
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/plain");
			resp.getWriter()
					.append("Bad Request - 400. Request param purchase should be present.");
			return;
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

		try {
			DataReaderWriterDecorator dataReaderWriter = new DataFactory(inputData).getDataReaderWriter();

			Map<ProductDTO, BigDecimal> products = dataReaderWriter.readPurchases();
			Set<DiscountCardDTO> discountCardsDTO = dataReaderWriter.readDiscountCards();

			((DiscountCardService) discountCardService).setDiscountCardsDTO(discountCardsDTO);

			DiscountServiceComposite discountServiceComposite = DiscountServiceComposite.getInstance();
			discountServiceComposite.addDiscountService(discountCardService);
			discountServiceComposite.addDiscountService(discountProductGroupService);

			ReceiptType cashReceptBuilderType = AbstractReceiptBuilder.defineReceiptType(receiptType);
			purchaseServiceImpl
					.implementReceiptBuilder(AbstractReceiptBuilder.createReceiptBuilder(cashReceptBuilderType));
			String receipt = purchaseServiceImpl.buyPurchases(products);
			
			if (cashReceptBuilderType.equals(ReceiptType.PDF)) {
				InputStream is = new FileInputStream(receipt);
				byte[] receiptData = is.readAllBytes();
				is.close();

				resp.setStatus(200);
				resp.setCharacterEncoding("UTF-8");
				resp.setContentType("application/pdf");
				resp.getOutputStream().write(receiptData);
			} else {				
				resp.setStatus(200);
				resp.setCharacterEncoding("UTF-8");
				resp.setContentType("text/plain");
				resp.getWriter().append(receipt);
			}

		} catch (IOException | ConverterException | ValidatorException | ServiceException e) {
			resp.setStatus(400);
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/plain");
			resp.getWriter()
					.append("Error on buing products." + e.getMessage());
		}

	}

	@Override
	public void destroy() {
		AppConnectionConfig.getInstance().closeEntityManagerFactory();
	}
}
