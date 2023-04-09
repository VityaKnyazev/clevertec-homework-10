package ru.clevertec.knyazev.dao;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.entity.Product;

/**
 * 
 * Interface for Product data access object.
 * 
 * @author Vitya Knyazev
 *
 */
public interface ProductDAO {

	/**
	 * 
	 * Get Optional<Product> from database on giving id.
	 * 
	 * @param Long id for searching product in database.
	 * @return Optional<Product> product if product not exists in database - Optional empty.
	 */
	Optional<Product> getProductById(Long id);
	
	/**
	 * 
	 * Get all products from database.
	 * 
	 * @return List<Product> list of all saved in database products or empty list.
	 */
	List<Product> getAllProducts();
	
	/**
	 * 
	 * Get all products from database on given Integer page of given quantity of elements.
	 * 
	 * @param Integer page number.
	 * @param Integer elementsOnPage quantity of elements on page.
	 * @return List<Product> products on given Integer page of given Integer quantity 
	 *         of elements from database or empty list.
	 */
	List<Product> getAllProducts(Integer page, Integer elementsOnPage);
	
	/**
	 * 
	 * Get all products on giving page number
	 * 
	 * @param Integer page number
	 * @return List<Product> list for current page.
	 */
	List<Product> getAllProducts(Integer page);
		
	/**
	 * 
	 * Save product to database
	 * 
	 * @param Product product for saving
	 * @return Optional<Product> if was success on saving - optional product, otherwise - optional empty.
	 */
	Optional<Product> saveProduct(Product product);	
	
	/**
	 * 
	 * Update on giving product and returns optional wrap of it.
	 * If didn't update product should return empty Optional.
	 * 
	 * @param Product Product for updating.
	 * @return Optional<Product> optional updated product or empty optional
	 */
	Optional<Product> updateProduct(Product product);
	
	/**
	 *
	 * Delete product from database.
	 * 
	 * @param Product product for deleting.
	 * @return Boolean true on success deleting, otherwise - false.
	 */
	Boolean deleteProduct(Product product);
}