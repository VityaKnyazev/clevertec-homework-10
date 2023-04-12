package ru.clevertec.knyazev.dao;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.entity.Shop;

/**
 * 
 * Represents interface for Shop data access object.
 *
 */

public interface ShopDAO {
	
	/**
	 * 
	 * Get Optional<Shop> from database on giving id.
	 * 
	 * @param Long id shop id for searching
	 * @return Optional<Shop> if shop not exists in database - Optional empty.
	 */
	Optional<Shop> getShopById(Long id);
	
	/**
	 * 
	 * Get all shops from database
	 * 
	 * @return List<Shop> all shops from database or empty list.
	 */
	List<Shop> getAllShops();
	
	/**
	 * 
	 * Get all shops from database on given Integer page of given quantity of elements.
	 * 
	 * @param Integer page number.
	 * @param Integer elementsOnPage quantity of elements on page.
	 * @return List<Shop> shops on given Integer page of given Integer quantity of elements
	 *         from database or empty list.
	 */
	List<Shop> getAllShops(Integer page, Integer elementsOnPage);
	
	/**
	 * 
	 * Get all shops from database on given Integer page
	 * 
	 * @param Integer page number
	 * @return List<Shop> shops on given Integer page from database or empty list.
	 */
	List<Shop> getAllShops(Integer page);
	
	/**
	 * 
	 * Save shop to database
	 * 
	 * @param Shop shop for saving.
	 * @return Optional<Shop> if was success on saving - optional shop, otherwise - optional empty.
	 */
	Optional<Shop> saveShop(Shop shop);
	
	
	/**
	 * 
	 * Update shop in database
	 * 
	 * @param Shop shop for updating.
	 * @return Optional<Shop> if was success on updating - optional shop, otherwise - optional empty.
	 */
	Optional<Shop> updateShop(Shop shop);
	
	/**
	 * 
	 * Delete shop in database
	 * 
	 * @param Shop shop for deleting
	 * @return Boolean true on success deleting, otherwise - false.
	 */
	Boolean deleteShop(Shop shop);
}
