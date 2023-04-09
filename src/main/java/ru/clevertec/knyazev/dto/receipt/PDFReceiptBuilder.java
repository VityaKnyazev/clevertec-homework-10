package ru.clevertec.knyazev.dto.receipt;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import ru.clevertec.knyazev.dto.PurchaseDTO;
import ru.clevertec.knyazev.entity.Shop;
import ru.clevertec.knyazev.entity.util.Unit;

public class PDFReceiptBuilder extends AbstractReceiptBuilder {
	private static final String PDF_TEMPLATE_FILE = "receipts/template/template.pdf";
	private String PDF_RECEIPT_FILE;
	
	private static final DecimalFormat PRICE_DECIMAL_FORMAT = new DecimalFormat("##########.##");
	private static final DecimalFormat QUANTITY_DECIMAL_FORMAT = new DecimalFormat("##########.###");

	private PDF pdf;

	private Document pdfDocument;
	
	private Paragraph receiptTitle;
	private Table shopTable;
	private Table casherTable;
	private Table purchasesTable;
	private Table totalPriceTable;
	private Table discountCardsValueTable;
	private Table productGroupsDiscountValueTable;
	private Table totalDiscountPriceTable;

	public PDFReceiptBuilder() {		
		pdf = new PDF();

		receiptTitle = new Paragraph("CASH RECEIPT").setTextAlignment(TextAlignment.CENTER).setMarginTop(220f);
		initTables();
	}

	@Override
	public PDFReceiptBuilder setCasherIdWithDateTime(Long casherId) {
		dateTime = LocalDateTime.now();
		
		Cell casherIdWithDateCell = pdf.createTableCell(
				"casher: № " + casherId + "   date: " + dateTime.format(DateTimeFormatter.ofPattern("dd-MM-YYYY")),
				TextAlignment.LEFT);
		Cell casherTimeCell = pdf.createTableCell("time: " + dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
				TextAlignment.RIGHT);

		casherTable.addCell(casherIdWithDateCell)
				   .addCell(casherTimeCell);

		return this;
	}

	@Override
	public PDFReceiptBuilder setShop(Shop shop) {
		Cell shopName = pdf.createTableCell(shop.getName(), TextAlignment.RIGHT);
		Cell shopAddress = pdf.createTableCell(shop.getAddress().toString(), TextAlignment.RIGHT);
		Cell shopPhone = pdf.createTableCell(shop.getPhone(), TextAlignment.RIGHT);

		shopTable.addCell(shopName)
				 .addCell(shopAddress)
				 .addCell(shopPhone);
		
		return this;
	}

	@Override
	public PDFReceiptBuilder setPurchases(List<PurchaseDTO> purchasesDTO) {
		Cell purchaseQuantity = pdf.createTableCell("QUANTITY", TextAlignment.CENTER);
		Cell purchaseUnit = pdf.createTableCell("UNIT", TextAlignment.CENTER);
		Cell purchaseDescription = pdf.createTableCell("DESCRIPTION", TextAlignment.CENTER);
		Cell purchasePrice = pdf.createTableCell("PRICE", TextAlignment.CENTER);
		Cell purchaseTotal = pdf.createTableCell("TOTAL", TextAlignment.CENTER);

		purchasesTable.addCell(purchaseQuantity)
					  .addCell(purchaseUnit)
					  .addCell(purchaseDescription)
					  .addCell(purchasePrice)
					  .addCell(purchaseTotal);

		for (PurchaseDTO purchaseDTO : purchasesDTO) {
			BigDecimal quantity = purchaseDTO.getQuantity();
			Unit unit = purchaseDTO.getUnit();
			String description = purchaseDTO.getDescription();
			BigDecimal price = purchaseDTO.getPrice();

			BigDecimal productGroupPrice = new BigDecimal("0");
			productGroupPrice = quantity.multiply(price).setScale(2, RoundingMode.HALF_UP);

			Cell purchaseQuantityCell = pdf.createTableCell(QUANTITY_DECIMAL_FORMAT.format(quantity),
					TextAlignment.CENTER);
			Cell purchaseUnitCell = pdf.createTableCell(unit.name(), TextAlignment.CENTER);
			Cell purchaseDescriptionCell = pdf.createTableCell(description, TextAlignment.CENTER);
			Cell purchasePriceCell = pdf.createTableCell(PRICE_DECIMAL_FORMAT.format(price), TextAlignment.CENTER);
			Cell purchaseProductGroupPrice = pdf.createTableCell(PRICE_DECIMAL_FORMAT.format(productGroupPrice),
					TextAlignment.CENTER);

			purchasesTable.addCell(purchaseQuantityCell)
			   			  .addCell(purchaseUnitCell)
			   			  .addCell(purchaseDescriptionCell)
			   			  .addCell(purchasePriceCell)
			   			  .addCell(purchaseProductGroupPrice);
		}

		return this;
	}

	@Override
	public PDFReceiptBuilder setTotalPrice(BigDecimal totalPrice) {		
		Cell totalPriceTitle = pdf.createTableCell("TOTAL:", TextAlignment.LEFT);
		Cell totalPriceValue = pdf.createTableCell(PRICE_DECIMAL_FORMAT.format(totalPrice), TextAlignment.RIGHT);
		
		totalPriceTable.addCell(totalPriceTitle)
					   .addCell(totalPriceValue);
		
		return this;
	}

	@Override
	public PDFReceiptBuilder setDiscountCardsValue(BigDecimal discountCardsValue) {
		Cell discountCardsValueTitle = pdf.createTableCell("CARDS DISCOUNT VALUE:", TextAlignment.LEFT);
		Cell discountCardsValueCell = pdf.createTableCell(PRICE_DECIMAL_FORMAT.format(discountCardsValue), TextAlignment.RIGHT);
		
		discountCardsValueTable.addCell(discountCardsValueTitle).addCell(discountCardsValueCell);
		
		return this;
	}

	@Override
	public PDFReceiptBuilder setProductGroupsDiscountValue(BigDecimal productGroupsDiscountValue) {
		Cell productGroupsDiscountValueTitle = pdf.createTableCell("DISCOUNT ON PRODUCT GROUPS:", TextAlignment.LEFT);
		Cell productGroupsDiscountValueCell = pdf.createTableCell(PRICE_DECIMAL_FORMAT.format(productGroupsDiscountValue), TextAlignment.RIGHT);
		
		productGroupsDiscountValueTable.addCell(productGroupsDiscountValueTitle)
									   .addCell(productGroupsDiscountValueCell);
		
		return this;
	}

	@Override
	public PDFReceiptBuilder setTotalDiscountPrice(BigDecimal totalDiscountPrice) {
		Cell totalDiscountPriceValueTitle = pdf.createTableCell("TOTAL WITH DISCOUNT:", TextAlignment.LEFT);
		Cell totalDiscountPriceValueCell = pdf.createTableCell(PRICE_DECIMAL_FORMAT.format(totalDiscountPrice), TextAlignment.RIGHT);
		
		totalDiscountPriceTable.addCell(totalDiscountPriceValueTitle)
							   .addCell(totalDiscountPriceValueCell);
		
		return this;
	}

	@Override
	public String build() {
		String RES_PATH = System.getProperty("java.io.tmpdir");
		PDF_RECEIPT_FILE = RES_PATH + "receipt-"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH-mm-ss-SSS", Locale.ROOT)) + ".pdf";
		
		pdfDocument = pdf.createDocument(PDF_TEMPLATE_FILE, PDF_RECEIPT_FILE);
		
		pdfDocument.add(receiptTitle)
		           .add(shopTable)
		           .add(casherTable)
		           .add(purchasesTable)
		           .add(totalPriceTable)
		           .add(productGroupsDiscountValueTable)
		           .add(discountCardsValueTable)
		           .add(totalDiscountPriceTable);
		
		pdf.saveDocument(pdfDocument);
		
		initTables();

		return PDF_RECEIPT_FILE;
	}
	
	private final void initTables() {
		shopTable = pdf.createTable(new float[] { 500f }, 5f);
		casherTable = pdf.createTable(new float[] { 500f }, 5f);
		purchasesTable = pdf.createTable(new float[] { 37.5f, 37.5f, 330f, 37.5f, 37.5f }, 10f);
		totalPriceTable = pdf.createTable(new float[] { 50f, 450f });
		discountCardsValueTable = pdf.createTable(new float[] { 400f, 100f });
		productGroupsDiscountValueTable = pdf.createTable(new float[] { 400f, 100f });
		totalDiscountPriceTable = pdf.createTable(new float[] { 400f, 100f });
	}

	private class PDF {
		private static final String TIMES_NEW_ROMAN_FONT = "fonts/times.ttf";
		
		private Document createDocument(String pdfTemplateName, String pdfReceiptName) {
			Document pdfDocument = null;

			try {
				PdfDocument pdfReceipt = new PdfDocument(new PdfReader(pdfTemplateName), new PdfWriter(pdfReceiptName));
				pdfDocument = new Document(pdfReceipt);
				
				FontProgram fontProgram = FontProgramFactory.createFont(TIMES_NEW_ROMAN_FONT);
				PdfFont font = PdfFontFactory.createFont(fontProgram, "CP1251");
				pdfDocument.setFont(font);

			} catch (IOException e) {
				e.printStackTrace();
			}

			return pdfDocument;
		}
		
		private Table createTable(float[] size) {
			Table table = new Table(size);
			table.setHorizontalAlignment(HorizontalAlignment.CENTER);
			table.setVerticalAlignment(VerticalAlignment.BOTTOM);
			table.setMarginTop(3f);
			table.setBorder(Border.NO_BORDER);

			return table;
		}

		private Table createTable(float[] size, float matginTop) {
			Table table = new Table(size);
			table.setHorizontalAlignment(HorizontalAlignment.CENTER);
			table.setMarginTop(matginTop);
			table.setBorder(Border.NO_BORDER);

			return table;
		}

		private Cell createTableCell(String text, TextAlignment textAlignment) {
			Cell cell = new Cell();
			cell.add(new Paragraph(text).setTextAlignment(textAlignment).setFontSize(10));
			cell.setBorder(Border.NO_BORDER);

			return cell;
		}

		private void saveDocument(Document document) {
			if (document != null) {
				PdfDocument pdfDocument = document.getPdfDocument();

				document.close();
				pdfDocument.close();
			}
		}
	}
}
