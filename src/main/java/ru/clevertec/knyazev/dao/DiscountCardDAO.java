package ru.clevertec.knyazev.dao;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.entity.DiscountCard;

/**
 * 
 * Represents a data access object operations with user discount cards
 *
 */
public interface DiscountCardDAO {

	/**
	 * 
	 * Get Optional<DiscountCard> discountCard. Will be empty if
	 * discount card was not found.
	 * 
	 * @param String number String discount card number
	 * @return Optional<DiscountCard> discount card Optional object.
	 */
	Optional<DiscountCard> getDiscountCardByNumber(String number);
	
	/**
	 * 
	 * Get all discount cards objects
	 * 
	 * @return List<DiscountCard> list of all saved in database discountCards.
	 */
	List<DiscountCard> getAllDiscountCards();
	
	/**
	 * 
	 * Get all discount cards from database on given Integer page of given quantity of elements.
	 * 
	 * @param Integer page number.
	 * @param Integer elementsOnPage quantity of elements on page.
	 * @return List<DiscountCard> discount cards on given Integer page of given Integer quantity 
	 *         of elements from database or empty list.
	 */
	List<DiscountCard> getAllDiscountCards(Integer page, Integer elementsOnPage);
	
	/**
	 * 
	 * Get all discount cards objects on giving page number
	 * 
	 * @param Integer page number
	 * @return List<DiscountCard> list for current page.
	 */
	List<DiscountCard> getAllDiscountCards(Integer page);

	/**
	 * 
	 * Save giving discount card and returns optional wrap of it.
	 * If didn't save discount card should return empty Optional.
	 * 
	 * @param DiscountCard discountCard for saving
	 * @return Optional<DiscountCard> optional discount card if saves successfully or else
	 * return empty Optional
	 */
	Optional<DiscountCard> saveDiscountCard(DiscountCard discountCard);
	
	/**
	 * 
	 * Update on giving discount card and returns optional wrap of it.
	 * If didn't update discount card should return empty Optional.
	 * 
	 * @param discountCard for updating.
	 * @return Optional<DiscountCard> optional updated discount card or empty optional
	 */
	Optional<DiscountCard> updateDiscountCard(DiscountCard discountCard);
	
	/**
	 * 
	 * Delete discount card in database
	 * 
	 * @param DiscountCard discount card for deleting
	 * @return Boolean true on success deleting, otherwise - false.
	 */
	Boolean deleteDiscountCard(DiscountCard discountCard);

	/**
	 * 
	 * Check is discount card exists in database
	 * 
	 * @param String discountCardNumber the number of discount card
	 * @return boolean true - if discount card is in database, otherwise - false.
	 */
	boolean isDiscountCardExists(String discountCardNumber);
}