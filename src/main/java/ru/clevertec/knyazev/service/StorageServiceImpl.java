package ru.clevertec.knyazev.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.clevertec.knyazev.dao.StorageDAO;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.service.exception.ServiceException;

public class StorageServiceImpl implements StorageService {
	private StorageDAO storageDAOJPA;

	
	public StorageServiceImpl(StorageDAO storageDAOJPA) {
		this.storageDAOJPA = storageDAOJPA;
	}

	@Override
	public List<Storage> buyProductFromStorages(Long productId, BigDecimal quantity) throws ServiceException {
		// Проверяем существование продукта в хранилищах
		if (!storageDAOJPA.isStorageExistsByProductId(productId)) {
			throw new ServiceException(
					"Error. All or some products that passed are not exists or not added in storage!");
		}

		// Проверяем хватает ли товара по количеству для покупки (товары могут быть с
		// разной ценой)
		BigDecimal productQuantityInStoreages = storageDAOJPA.getProductQuantityInStorages(productId);
		if (quantity.compareTo(productQuantityInStoreages) == 1) {
			throw new ServiceException(
					"Error. Given product quantity is exceeding that storages contain. Error for product with id="
							+ productId);
		}

		List<Storage> productGroupInStorages = storageDAOJPA.getProductGroupInStoragesById(productId);
		Collections.sort(productGroupInStorages);

		// Покупаем продукты, удаляя продукты из хранилища с нулевым остатком
		List<Long> storageIdsForDeleting = new ArrayList<>();
		
		List<Storage> boughtStorages = new ArrayList<>();
		for (int  i = 0; i < productGroupInStorages.size(); i++) {
			Storage storage = productGroupInStorages.get(i);

			BigDecimal storageQuantity = storage.getQuantity();

			if (quantity.compareTo(storageQuantity) == 1) {
				quantity = quantity.subtract(storageQuantity);
				boughtStorages.add(storage);
				storageIdsForDeleting.add(storage.getId());
			} else if (quantity.compareTo(storageQuantity) == -1) {
				Storage storageForBought = new Storage(storage.getId(), storage.getProduct(), storage.getUnit(), storage.getPrice(), quantity);
				boughtStorages.add(storageForBought);
				storage.setQuantity(storageQuantity.subtract(quantity));
				storageDAOJPA.updateStorage(storage);
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
			storageDAOJPA.deleteStorage(storageIdForDeleting);
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
	
}