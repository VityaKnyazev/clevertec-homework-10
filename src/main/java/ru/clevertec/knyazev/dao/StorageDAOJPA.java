package ru.clevertec.knyazev.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.clevertec.knyazev.entity.Storage;

public class StorageDAOJPA implements StorageDAO {
	@PersistenceContext
	EntityManager entityManager;
	
	public StorageDAOJPA() {}
	
	public StorageDAOJPA(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Boolean isStorageExistsByProductId(Long productId) {
		long quantity = 0;		
		quantity = ((Number) entityManager.createNativeQuery("SELECT COUNT(id) FROM storage WHERE product_id = ?1").setParameter(1, productId).getSingleResult()).longValue();
		return quantity > 0;
	}

	@Override
	public BigDecimal getProductQuantityInStorages(Long productId) {
		Number dbResultQuantity = 0f;
		dbResultQuantity = (Number) entityManager.createNativeQuery("SELECT SUM(quantity) FROM storage WHERE product_id = ?1").setParameter(1, productId).getSingleResult();
		BigDecimal productQuantity = new BigDecimal(dbResultQuantity.floatValue());
		productQuantity = productQuantity.setScale(3, RoundingMode.HALF_UP);
		
		return productQuantity;
	}

	@Override
	public List<Storage> getProductGroupInStoragesById(Long productId) {
		List<Storage> productsGroup = new ArrayList<>();
				
		List<Storage>  productsGroupResult = entityManager.createQuery("SELECT storage FROM Storage storage WHERE storage.product.id = ?1", Storage.class).setParameter(1, productId).getResultList();
		if ((productsGroupResult != null) && !productsGroupResult.isEmpty()) {
			productsGroup.addAll(productsGroupResult);
		}
		
		return productsGroup;		
	}

	@Override
	public Storage updateStorage(Storage storage) {		
		if (storage.getId() != null) {
			entityManager.persist(storage);
			return storage;
		}
		
		return null;
	}

	@Override
	public void deleteStorage(Long storageId) {
		entityManager.createQuery("DELETE FROM Storage storage WHERE storage.id = ?1").setParameter(1, storageId).executeUpdate();		
	}

}
