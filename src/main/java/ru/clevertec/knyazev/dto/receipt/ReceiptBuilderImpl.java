package ru.clevertec.knyazev.dto.receipt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import ru.clevertec.knyazev.dto.PurchaseDTO;
import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.entity.Shop;
import ru.clevertec.knyazev.entity.util.Unit;

public class ReceiptBuilderImpl extends AbstractReceiptBuilder {
	
	public ReceiptBuilderImpl() {
		super();
	}

	@Override
	public ReceiptBuilderImpl setCasherIdWithDateTime(Long casherId) {		
		this.casherId = String.format("%-100S", "casher: № " + casherId + "   date: " + dateTime.format(DateTimeFormatter.ofPattern("dd-MM-YYYY")))
				+ System.lineSeparator() + String.format("%100S", "time: " + dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
				+ System.lineSeparator();
		return this;
	}

	@Override
	public ReceiptBuilderImpl setShop(Shop shop) {
		final String SHOP_FORMAT = "%100s";
		
		String name = shop.getName();
		Address address = shop.getAddress();
		String phone = shop.getPhone();

		this.shop = String.format(SHOP_FORMAT, name) + System.lineSeparator()
				+ String.format(SHOP_FORMAT, address.toString())
				+ System.lineSeparator() + String.format(SHOP_FORMAT, "Tel: " + phone) + System.lineSeparator();
		return this;
	}

	@Override
	public ReceiptBuilderImpl setPurchases(List<PurchaseDTO> purchasesDTO) {
		final String HEADER_FORMAT = "%-10S %-8S %-63S %-10S %-10S";
		final String BODY_FORMAT = "%-10.3f %-8s %-63s %-10.2f %-10.2f";
		
		final String header = String.format(HEADER_FORMAT, "quantity", "unit", "description", "price", "total")
				+ System.lineSeparator();

		String bodyData = "";

		for (PurchaseDTO purchaseDTO : purchasesDTO) {
			BigDecimal quantity = purchaseDTO.getQuantity();
			Unit unit = purchaseDTO.getUnit();
			String description = purchaseDTO.getDescription();
			BigDecimal price = purchaseDTO.getPrice();

			BigDecimal productGroupPrice = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
			productGroupPrice = quantity.multiply(price);

			bodyData += String.format(Locale.ROOT, BODY_FORMAT, quantity, unit, description, price, productGroupPrice)
					+ System.lineSeparator();
		}

		String purchases = header + bodyData;

		this.purchases = purchases;
		return this;
	}
	
	@Override
	public ReceiptBuilderImpl setTotalPrice(BigDecimal totalPrice) {
		final String TOTAL_PRICE_FORMAT = "%-8S %92.2f";
		this.totalPrice = String.format(Locale.ROOT, TOTAL_PRICE_FORMAT, "total: ", totalPrice) + System.lineSeparator();
		return this;
	}

	@Override
	public ReceiptBuilderImpl setDiscountCardsValue(BigDecimal discountCardsValue) {
		final String DISCOUNT_CARDS_VALUE_FORMAT = "%-8S %78.2f";
		
		this.discountCardsValue = String.format(Locale.ROOT, DISCOUNT_CARDS_VALUE_FORMAT, "cards discount value:", discountCardsValue)
				+ System.lineSeparator();
		return this;
	}

	@Override
	public ReceiptBuilderImpl setProductGroupsDiscountValue(BigDecimal productGroupsDiscountValue) {
		final String PRODUCT_GROUPS_DISCOUNT_FORMAT = "%-8S %72.2f";
		
		this.productGroupsDiscountValue = String.format(Locale.ROOT, PRODUCT_GROUPS_DISCOUNT_FORMAT, "discount on product groups:",
				productGroupsDiscountValue) + System.lineSeparator();
		return this;
	}

	@Override
	public ReceiptBuilderImpl setTotalDiscountPrice(BigDecimal totalDiscountPrice) {
		final String DISCOUNT_TOTAL_PRICE_FORMAT = "%-8S %80.2f";
		
		this.totalDiscountPrice = String.format(Locale.ROOT, DISCOUNT_TOTAL_PRICE_FORMAT, "total with discount:", totalDiscountPrice);
		return this;
	}
	
	@Override
	public String build() {
		return String.format("%50S", "CASH RECEIPT") + System.lineSeparator() +
				shop +
				casherId +
				purchases +
				totalPrice +
				productGroupsDiscountValue +
				discountCardsValue + 
				totalDiscountPrice;
	}
}