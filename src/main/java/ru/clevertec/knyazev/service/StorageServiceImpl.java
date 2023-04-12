package ru.clevertec.knyazev.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import ru.clevertec.knyazev.dao.StorageDAO;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.service.exception.ServiceException;

public class StorageServiceImpl implements StorageService {
	private StorageDAO storageDAO;

	public StorageServiceImpl(StorageDAO storageDAO) {
		this.storageDAO = storageDAO;
	}

	@Override
	public List<Storage> buyProductFromStorages(Long productId, BigDecimal quantity) throws ServiceException {
		// Проверяем существование продукта в хранилищах
		if (!storageDAO.isStorageExistsByProductId(productId)) {
			throw new ServiceException(
					"Error. All or some products that passed are not exists or not added in storage!");
		}

		// Проверяем хватает ли товара по количеству для покупки (товары могут быть с
		// разной ценой)
		BigDecimal productQuantityInStoreages = storageDAO.getProductQuantityInStorages(productId);
		if (quantity.compareTo(productQuantityInStoreages) == 1) {
			throw new ServiceException(
					"Error. Given product quantity is exceeding that storages contain. Error for product with id="
							+ productId);
		}

		List<Storage> productGroupInStorages = storageDAO.getProductGroupInStoragesById(productId);
		Collections.sort(productGroupInStorages);

		// Покупаем продукты, удаляя продукты из хранилища с нулевым остатком
		List<Long> storageIdsForDeleting = new ArrayList<>();

		List<Storage> boughtStorages = new ArrayList<>();

		for (int i = 0; i < productGroupInStorages.size(); i++) {
			Storage storage = productGroupInStorages.get(i);

			BigDecimal storageQuantity = storage.getQuantity();

			if (quantity.compareTo(storageQuantity) == 1) {
				quantity = quantity.subtract(storageQuantity);
				boughtStorages.add(storage);
				storageIdsForDeleting.add(storage.getId());
			} else if (quantity.compareTo(storageQuantity) == -1) {
				Storage storageForBought = new Storage(storage.getId(), storage.getProduct(), storage.getUnit(),
						storage.getPrice(), quantity);
				boughtStorages.add(storageForBought);
				storage.setQuantity(storageQuantity.subtract(quantity));

				Optional<Storage> updatedStorage = storageDAO.updateStorage(storage);
				if (updatedStorage.isEmpty()) {
					throw new ServiceException("Error on updating storage when buy product");
				}
				break;
			} else {
				boughtStorages.add(storage);
				storageIdsForDeleting.add(storage.getId());
				break;
			}

			if ((quantity.compareTo(BigDecimal.ZERO) == -1) || (quantity.compareTo(BigDecimal.ZERO) == 0))
				break;
		}

		for (Long storageIdForDeleting : storageIdsForDeleting) {
			Boolean result = storageDAO.deleteStorage(storageIdForDeleting);

			if (!result) {
				throw new ServiceException("Error on deleting storage when buy product");
			}
		}

		return boughtStorages;
	}

	@Override
	public BigDecimal getBoughtProductsTotalPrice(Map<Long, List<Storage>> boughtProductsGroups) {
		BigDecimal totalPrice = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

		for (Map.Entry<Long, List<Storage>> bougtStorages : boughtProductsGroups.entrySet()) {
			for (Storage storage : bougtStorages.getValue()) {
				BigDecimal quantity = storage.getQuantity();
				BigDecimal price = storage.getPrice();

				BigDecimal productPrice = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
				productPrice = quantity.multiply(price);

				totalPrice = totalPrice.add(productPrice);
			}
		}

		return totalPrice;
	}

	@Override
	public Optional<Storage> showStorage(Long id) {
		return storageDAO.getStorageById(id);
	}

	@Override
	public List<Storage> showAllStorages() {
		return storageDAO.getAllStorages();
	}

	@Override
	public List<Storage> showAllStorages(Integer page, Integer pageSize) {
		return storageDAO.getAllStorages(page, pageSize);
	}

	@Override
	public List<Storage> showAllStorages(Integer page) {
		return storageDAO.getAllStorages(page);
	}

	@Override
	public Storage addStorage(Storage storage) throws ServiceException {
		Optional<Storage> savedStorageWrap = storageDAO.saveStorage(storage);

		if (savedStorageWrap.isEmpty()) {
			throw new ServiceException("Error on adding storage!");
		} else {
			return savedStorageWrap.get();
		}
	}

	@Override
	public Storage changeStorage(Storage storage) throws ServiceException {
		EntityManager entityManager = AppConnectionConfig.getInstance().getEntityManager();

		try {
			entityManager.getTransaction().begin();
			Optional<Storage> updatedStorageWrap = storageDAO.updateStorage(storage);
			if (updatedStorageWrap.isPresent()) {
				entityManager.getTransaction().commit();
				return updatedStorageWrap.get();
			} else {
				entityManager.getTransaction().rollback();
				throw new ServiceException("Error on adding storage!");
			}
		} finally {
			entityManager.close();
		}
	}
	
	@Override
	public void removeStorage(Storage storage) throws ServiceException {
		EntityManager entityManager = AppConnectionConfig.getInstance().getEntityManager();

		try {
			entityManager.getTransaction().begin();
			Boolean result = storageDAO.deleteStorage(storage.getId());
			if (result) {
				entityManager.getTransaction().commit();
			} else {
				entityManager.getTransaction().rollback();
				throw new ServiceException("Error on removing storage!");
			}
		} finally {
			entityManager.close();
		}
	}

}