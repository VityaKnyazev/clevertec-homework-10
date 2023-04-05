package ru.clevertec.knyazev.service.discount;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.clevertec.knyazev.entity.Storage;

/**
 * 
 * Abstract class using for calculating discount
 * 
 */
abstract class AbstractDiscountService<T, V> implements DiscountService {
	private static final int MAX_TOTAL_CARDS_DISCOUNT_PERCENT = 45;
	
	/**
	 * For DiscountCardService.class
	 */
	static final int MAX_DISCOUNT_CARD_VAUE = 30;
	
	/**
	 * For DiscountProductGroupService
	 */	
	static final BigDecimal PRODUCT_QUANTITY_FOR_DISCOUNT = new BigDecimal(5);
	static final int DISCOUNT_VALUE_PERCENT_FOR_PRODUCT_GROUP = 10;
	
	/**
	 * 
	 * @param boughtProductsInStorages for divide on group
	 * @return Map<Long, List<Storage>> a group of products in storages for next
	 *         applying discount
	 */
	 abstract Map<Long, List<Storage>> divideOnGroup(Map<Long, List<Storage>> boughtProductsInStorages);

	/**
	 * 
	 * @param discountObjects Object(project group or discount cards) on which
	 *                        discount will be calculating
	 * @return representation <V> of discount calculating
	 */
	abstract V calculateDiscount(Collection<T> discountObjects);
	
	/**
	 * 
	 * @param totalCardsDiscount total cards discount value in percent
	 * @return card discount value that should be less than or equals
	 *         MAX_TOTAL_CARDS_DISCOUNT_PERCENT
	 */
	Integer calculateFinalCardsDiscount(Integer totalCardsDiscount) {
		return totalCardsDiscount >= MAX_TOTAL_CARDS_DISCOUNT_PERCENT ? MAX_TOTAL_CARDS_DISCOUNT_PERCENT
				: totalCardsDiscount;
	}	
}
