package ru.clevertec.knyazev.dao;

import ru.clevertec.knyazev.entity.DiscountCard;

/**
 * 
 * Represents a data access object operations with user discount cards
 *
 */
public interface DiscountCardDAO {

	DiscountCard getDiscountCardByNumber(String number);

	DiscountCard saveDiscountCard(DiscountCard discountCard);

	boolean isDiscountCardExists(String discountCardNumber);
}