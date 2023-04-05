package ru.clevertec.knyazev.service.discount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import ru.clevertec.knyazev.entity.Storage;

/**
 * 
 * This interface serve for discount applying
 *
 */

public interface DiscountService {

	/**
	 * 
	 * @param boughtProductsInStorages All products that user bought. Long - product
	 *                                 id, List<Storage> - product in storages with
	 *                                 quantities and prices
	 * @return discount BigDecimal value on product group
	 * 
	 */
	BigDecimal applyDiscount(Map<Long, List<Storage>> boughtProductsInStorages);

	public static enum Group {
		DISCOUNT_CARD_GROUP, DISCOUNT_PRODUCT_GROUP
	}
}