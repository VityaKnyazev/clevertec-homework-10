package ru.clevertec.knyazev.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.entity.Storage;

public interface StorageDAO {
	/**
	 * 
	 * @param productId product identifier
	 * @return true if product exists in storages or at least one storage contains
	 *         product
	 */
	Boolean isStorageExistsByProductId(Long productId);

	/**
	 * 
	 * @param productId product identifier
	 * @return BigDecimal product quantity that exists in storage on product group
	 */
	BigDecimal getProductQuantityInStorages(Long productId);
	
	/**
	 * 
	 * @param productId product identifier
	 * @return storages with product group (product with different prices)
	 */
	List<Storage> getProductGroupInStoragesById(Long productId);

	/**
	 * 
	 * Get Optional<Storage> from database on giving id.
	 * 
	 * @param Long id for searching storage in database.
	 * @return Optional<Storage> storage if storage not exists in database - Optional empty.
	 */
	Optional<Storage> getStorageById(Long id);
	
	/**
	 * 
	 * Get all storages from database
	 * 
	 * @return List<Storage> all storages from database or empty list.
	 */
	List<Storage> getAllStorages();
	
	/**
	 * 
	 * Get all storages from database on given Integer page of given quantity of elements.
	 * 
	 * @param Integer page number.
	 * @param Integer elementsOnPage quantity of elements on page.
	 * @return List<Storage> storages on given Integer page of given Integer quantity of elements
	 *         from database or empty list.
	 */
	List<Storage> getAllStorages(Integer page, Integer elementsOnPage);
	
	/**
	 * 
	 * Get all storages from database on given Integer page.
	 * 
	 * @param Integer page number.
	 * @return List<Storage> storages on given Integer page from database or empty list.
	 */
	List<Storage> getAllStorages(Integer page);
	
	/**
	 * 
	 * Save storage to database
	 * 
	 * @param Storage storage for saving
	 * @return Optional<Storage> if was success on saving - optional storage, otherwise - optional empty.
	 */
	Optional<Storage> saveStorage(Storage storage);
		
	/**
	 * 
	 * Update storage in database
	 * 
	 * @param Storage storage for updating
	 * @return Optional<Storage> if was success on updating - optional storage, otherwise - optional empty
	 */
	Optional<Storage> updateStorage(Storage storage);
		
	/**
	 * 
	 * Delete storage in database
	 * 
	 * @param Long storage id for deleting
	 * @return Boolean true on success deleting, otherwise - false.
	 */
	Boolean deleteStorage(Long storageId);
}