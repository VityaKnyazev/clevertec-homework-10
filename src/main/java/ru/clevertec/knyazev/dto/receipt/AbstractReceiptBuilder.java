package ru.clevertec.knyazev.dto.receipt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import ru.clevertec.knyazev.dto.PurchaseDTO;
import ru.clevertec.knyazev.entity.Shop;

/**
 * 
 * Current class using for creating CASH RECEIPT.
 * 
 * @author Vitya Knyazev
 *
 */
public abstract class AbstractReceiptBuilder {
	LocalDateTime dateTime;

	String casherId;
	String shop;
	String purchases;
	String totalPrice;
	String discountCardsValue;
	String productGroupsDiscountValue;
	String totalDiscountPrice;

	AbstractReceiptBuilder() {
		dateTime = LocalDateTime.now();
	}

	public abstract AbstractReceiptBuilder setCasherIdWithDateTime(Long casherId);

	public abstract AbstractReceiptBuilder setShop(Shop shop);

	public abstract AbstractReceiptBuilder setPurchases(List<PurchaseDTO> purchasesDTO);

	public abstract AbstractReceiptBuilder setTotalPrice(BigDecimal totalPrice);

	public abstract AbstractReceiptBuilder setDiscountCardsValue(BigDecimal discountCardsValue);

	public abstract AbstractReceiptBuilder setProductGroupsDiscountValue(BigDecimal productGroupsDiscountValue);

	public abstract AbstractReceiptBuilder setTotalDiscountPrice(BigDecimal totalDiscountPrice);

	/**
	 * 
	 * @return String representation of CASH RESEIPT. It can be simply String or
	 *         String file name.
	 * 
	 */
	public abstract String build();

	/**
	 * Factory method for creating receipt builder that depends on string builder
	 * type.
	 * 
	 * @param receiptBuilderType
	 * @return AbstractReceiptBuilder implementation that depends on @param
	 *         receiptBuilderType.
	 *         
	 */
	public static AbstractReceiptBuilder createReceiptBuilder(ReceiptType receiptType) {
		return switch (receiptType) {
		case PDF -> new PDFReceiptBuilder();
		case DEFAULT -> new ReceiptBuilderImpl();
		default -> new ReceiptBuilderImpl();
		};
	}
	
	/**
	 * 
	 * Factory method for creating default receipt builder.
	 * 
	 * @return AbstractReceiptBuilder default implementation
	 */
	public static AbstractReceiptBuilder createDefaultReceiptBuilder() {
		return new ReceiptBuilderImpl();
	}
	
	/**
	 * 
	 * Generate correct ReceiptType from input string.
	 * 
	 * @param receiptBuilderType String type for generating correct ReceiptType
	 * @return ReceiptType of PDF, DEFAULT and others
	 */
	public static ReceiptType defineReceiptType(String receiptBuilderType) {
		receiptBuilderType = (receiptBuilderType == null || receiptBuilderType.isBlank()) ? ReceiptType.DEFAULT.name()
				: receiptBuilderType.toUpperCase(Locale.ROOT);

		ReceiptType receiptType = ReceiptType.valueOf(receiptBuilderType);
		
		return (receiptType  == null) ? ReceiptType.DEFAULT : receiptType;
	}

	public static enum ReceiptType {
		PDF, DEFAULT
	}
}