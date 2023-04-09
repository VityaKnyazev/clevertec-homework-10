package ru.clevertec.knyazev.dao;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.entity.Seller;


/**
 * 
 * Interface for Seller data access object.
 *
 */
public interface SellerDAO {
	
	/**
	 * 
	 * Get Optional<Seller> from database on giving id.
	 * 
	 * @param Long id for searching seller in database.
	 * @return Optional<Seller> seller if seller not exists in database - Optional empty.
	 */
	Optional<Seller> getSellerById(Long id);
	
	/**
	 * 
	 * Get all sellers from database
	 * 
	 * @return List<Seller> all sellers from database or empty list.
	 */
	List<Seller> getAllSellers();
	
	/**
	 * 
	 * Get all sellers from database on given Integer page of given quantity of elements.
	 * 
	 * @param Integer page number.
	 * @param Integer elementsOnPage quantity of elements on page.
	 * @return List<Seller> sellers on given Integer page of given Integer quantity of elements
	 *         from database or empty list.
	 */
	List<Seller> getAllSellers(Integer page, Integer elementsOnPage);
	
	/**
	 * 
	 * Get all sellers from database on given Integer page.
	 * 
	 * @param Integer page number.
	 * @return List<Seller> sellers on given Integer page from database or empty list.
	 */
	List<Seller> getAllSellers(Integer page);
	
	/**
	 * 
	 * Save seller to database
	 * 
	 * @param Seller seller for saving
	 * @return Optional<Seller> if was success on saving - optional seller, otherwise - optional empty.
	 */
	Optional<Seller> saveSeller(Seller seller);
	
	/**
	 * 
	 * Update seller in database
	 * 
	 * @param Seller seller for updating
	 * @return Optional<Seller> if was success on updating - optional seller, otherwise - optional empty
	 */
	Optional<Seller> updateSeller(Seller seller);
	
	/**
	 * 
	 * Delete seller in database
	 * 
	 * @param Seller seller for deleting
	 * @return Boolean true on success deleting, otherwise - false.
	 */
	Boolean deleteSeller(Seller seller);
}
