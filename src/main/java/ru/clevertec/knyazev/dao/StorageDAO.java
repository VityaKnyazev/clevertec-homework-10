package ru.clevertec.knyazev.dao;

import java.math.BigDecimal;
import java.util.List;

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
	 * @param storage storage for update
	 * @return Storage with bought product, bought product quantity and price
	 */
	Storage updateStorage(Storage storage);
	
	/**
	 * 
	 * @param storageId storage id for deleting
	 */
	void deleteStorage(Long storageId);
}