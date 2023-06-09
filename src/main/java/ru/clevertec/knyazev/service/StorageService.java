package ru.clevertec.knyazev.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.service.exception.ServiceException;

public interface StorageService {

	/**
	 * 
	 * @param productId product identifier
	 * @param quantity  product quantity
	 * @return List of storages that contains single product or product group with
	 *         different prices
	 * @throws ServiceException when incorrect product id or quantity
	 */
	List<Storage> buyProductFromStorages(Long productId, BigDecimal quantity) throws ServiceException;

	/**
	 * 
	 * @param boughtProductsGroups contains id of product that links on
	 *                             boughtProduct group
	 * @return total price of bought product groups
	 */
	BigDecimal getBoughtProductsTotalPrice(Map<Long, List<Storage>> boughtProductsGroups);

	Optional<Storage> showStorage(Long id);

	List<Storage> showAllStorages();

	List<Storage> showAllStorages(Integer page, Integer pageSize);

	List<Storage> showAllStorages(Integer page);

	Storage addStorage(Storage storage) throws ServiceException;

	Storage changeStorage(Storage storage) throws ServiceException;

	void removeStorage(Storage storage) throws ServiceException;
}