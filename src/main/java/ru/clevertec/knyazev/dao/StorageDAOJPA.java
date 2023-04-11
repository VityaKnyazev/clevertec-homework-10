package ru.clevertec.knyazev.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.Storage;

public class StorageDAOJPA implements StorageDAO {
	private static final Integer PAGE_EL_LIMIT = 20;

	private static final Logger logger = LoggerFactory.getLogger(AddressDAOJPA.class);

	private AppConnectionConfig appConnectionConfig;

	public StorageDAOJPA() {
	}

	public StorageDAOJPA(AppConnectionConfig appConnectionConfig) {
		this.appConnectionConfig = appConnectionConfig;
	}

	/**
	 * 
	 * Method should be calling in transactional context
	 * entityManager.close();
	 * 
	 */
	@Override
	public Boolean isStorageExistsByProductId(Long productId) {
		long quantity = 0;

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			quantity = ((Number) entityManager.createNativeQuery("SELECT COUNT(id) FROM storage WHERE product_id = ?1")
					.setParameter(1, productId).getSingleResult()).longValue();
		} catch (PersistenceException | IllegalArgumentException e) {
			logger.error("Nothing found in storage on product id={}", productId, e);
		}

		return quantity > 0;
	}

	
	/**
	 * 
	 * Method should be calling in transactional context
	 * entityManager.close();
	 * 
	 */
	@Override
	public BigDecimal getProductQuantityInStorages(Long productId) {
		Number dbResultQuantity = 0f;

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			dbResultQuantity = (Number) entityManager
					.createNativeQuery("SELECT SUM(quantity) FROM storage WHERE product_id = ?1")
					.setParameter(1, productId).getSingleResult();
		} catch (PersistenceException | IllegalArgumentException e) {
			logger.error("No product quantity found in storage on product id={}", productId, e);
		}

		BigDecimal productQuantity = new BigDecimal(dbResultQuantity.floatValue());
		productQuantity = productQuantity.setScale(3, RoundingMode.HALF_UP);

		return productQuantity;
	}

	/**
	 * 
	 * Method should be calling in transactional context
	 * entityManager.close();
	 * 
	 */
	@Override
	public List<Storage> getProductGroupInStoragesById(Long productId) {
		List<Storage> productsGroup = new ArrayList<>();
		List<Storage> productsGroupResult = null;

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			productsGroupResult = entityManager
					.createQuery("SELECT storage FROM Storage storage WHERE storage.product.id = ?1", Storage.class)
					.setParameter(1, productId).getResultList();
		} catch (PersistenceException | IllegalArgumentException e) {
			logger.error("No product group found in storage on product id={}", productId, e);
		}
		if ((productsGroupResult != null) && !productsGroupResult.isEmpty()) {
			productsGroup.addAll(productsGroupResult);
		}

		return productsGroup;
	}
	
	@Override
	public Optional<Storage> getStorageById(Long id) {
		Optional<Storage> storageWrap = Optional.empty();

		if (id != null && id > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				Storage dbStorage = entityManager.find(Storage.class, id);
				if (dbStorage != null) {
					storageWrap = Optional.of(dbStorage);
				}
			} catch (IllegalArgumentException e) {
				logger.error("Error when getting storage: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		}

		return storageWrap;
	}
	
	@Override
	public List<Storage> getAllStorages() {
		List<Storage> storages = new ArrayList<>();

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			storages = entityManager.createQuery("SELECT s FROM Storage s", Storage.class).getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all storages: {}", e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return storages;
	}
	
	@Override
	public List<Storage> getAllStorages(Integer page, Integer elementsOnPage) {
		List<Storage> storages = new ArrayList<>();

		if ((page == null || page < 1) && (elementsOnPage == null || elementsOnPage < 1)) {
			return storages;
		}

		Integer offsset = (page - 1) * elementsOnPage;

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			storages = entityManager.createQuery("SELECT s FROM Storage s", Storage.class).setFirstResult(offsset)
					.setMaxResults(elementsOnPage).getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all storages on page={}: {}", page, e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return storages;
	}
	
	@Override
	public List<Storage> getAllStorages(Integer page) {
		return getAllStorages(page, PAGE_EL_LIMIT);
	}
	
	@Override
	public Optional<Storage> saveStorage(Storage storage) {
		Optional<Storage> storageWrap = Optional.empty();

		if (storage != null && storage.getId() == null) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				entityManager.persist(storage);
				entityManager.getTransaction().commit();

				storageWrap = Optional.of(storage);
			} catch (IllegalStateException | PersistenceException e) {
				logger.error("Error when saving storage: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Error saving on invalid storage: {}", storage);
		}

		return storageWrap;
	}

	/**
	 * 
	 * Method should be calling in transactional context
	 * entityManager.close();
	 */
	@Override
	public Optional<Storage> updateStorage(Storage storage) {
		Optional<Storage> storageWrap = Optional.empty();

		if (storage != null && storage.getId() != null && storage.getId() > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				Storage storageDB = entityManager.find(Storage.class, storage.getId());

				if (storageDB != null) {
					storageDB.setProduct(storage.getProduct());
					storageDB.setUnit(storage.getUnit());
					storageDB.setPrice(storage.getPrice());
					storageDB.setQuantity(storage.getQuantity());

					entityManager.flush();

					storageWrap = Optional.of(storage);
				} else {
					logger.error("Error on updating. Storage not exists on given id={}", storage.getId());
				}
				
			} catch (IllegalStateException | PersistenceException e) {
				logger.error("Error when updating storage: {}", e.getMessage(), e);
			}
		} else {
			logger.error("Error updating on invalid storage: {}", storage);
		}

		return storageWrap;
	}

	/**
	 * 
	 * Method should be calling in transactional context
	 * entityManager.close(); 
	 */
	@Override
	public Boolean deleteStorage(Long storageId) {
		Boolean result = false;
		
		if (storageId != null && storageId > 0L) {			
			
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				Storage dbStorage = entityManager.find(Storage.class, storageId);

				if (dbStorage != null) {
					entityManager.remove(dbStorage);
					entityManager.flush();
					
					result = true;
				} else {
					logger.error("Storage not exists with given id=" + storageId);
				}
			} catch (PersistenceException | IllegalArgumentException e) {
				logger.error("Error on deleting storage: {}", e.getMessage(), e);
			}
		} else {
			logger.error("Given invalid storage id={} for deleting", storageId);
		}
		
		return result;
	}

}
