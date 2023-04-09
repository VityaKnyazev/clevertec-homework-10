package ru.clevertec.knyazev.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.Product;

public class ProductDAOJPA implements ProductDAO {
	private static final Integer PAGE_EL_LIMIT = 20;
	
	private static final Logger logger = LoggerFactory.getLogger(ProductDAOJPA.class);
	
	private AppConnectionConfig appConnectionConfig;
	
	public ProductDAOJPA() {}

	public ProductDAOJPA(AppConnectionConfig appConnectionConfig) {
		this.appConnectionConfig = appConnectionConfig;
	}
	
	@Override
	public Optional<Product> getProductById(Long id) {
		Optional<Product> productWrap = Optional.empty();

		if (id != null && id > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				Product dbproduct = entityManager.find(Product.class, id);
				if (dbproduct != null) {
					productWrap = Optional.of(dbproduct);
				}
			} catch (IllegalArgumentException e) {
				logger.error("Error when getting product: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		}

		return productWrap;
	}
	
	@Override
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			products = entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all products: {}", e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return products;
	}
	
	@Override
	public List<Product> getAllProducts(Integer page, Integer elementsOnPage) {
		List<Product> products = new ArrayList<>();

		if ((page == null || page < 1) && (elementsOnPage == null || elementsOnPage < 1)) {
			return products;
		}

		Integer offsset = (page - 1) * elementsOnPage;

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			products = entityManager.createQuery("SELECT p FROM Product p", Product.class).setFirstResult(offsset)
					.setMaxResults(elementsOnPage).getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all products on page={}: {}", page, e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return products;
	}
	
	@Override
	public List<Product> getAllProducts(Integer page) {
		return getAllProducts(page, PAGE_EL_LIMIT);
	}
	

	@Override
	public Optional<Product> saveProduct(Product product) {
		Optional<Product> productdWrap = Optional.empty();

		if (product != null && product.getId() == null) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				entityManager.persist(product);
				entityManager.getTransaction().commit();

				productdWrap = Optional.of(product);
			} catch (IllegalStateException | PersistenceException e) {
				logger.error("Error when saving product: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Error saving on invalid product: {}", product);
		}

		return productdWrap;
	}
	
	@Override
	public Optional<Product> updateProduct(Product product) {
		Optional<Product> productdWrap = Optional.empty();

		if (product != null && product.getId() != null && product.getId() > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				Product productDB = entityManager.find(Product.class, product.getId());

				if (productDB != null) {
					productDB.setDescription(product.getDescription());
					productDB.setAuction(product.getAuction());

					entityManager.getTransaction().commit();

					productdWrap = Optional.of(product);
				} else {
					logger.error("Error on updating. Product not exists on given id={}", product.getId());
				}

			} catch (IllegalStateException | PersistenceException e) {
				logger.error("Error when updating product: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Error updating on invalid product: {}", product);
		}

		return productdWrap;
	}
	
	@Override
	public Boolean deleteProduct(Product product) {
		Boolean result = false;

		if (product != null && product.getId() != null && product.getId() > 0L) {

			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				Product dbProduct = entityManager.find(Product.class, product.getId());

				if (dbProduct != null) {
					entityManager.remove(dbProduct);
					entityManager.getTransaction().commit();

					result = true;
				} else {
					logger.error("Product not exists with given id=" + product.getId());
				}
			} catch (PersistenceException | IllegalArgumentException e) {
				logger.error("Error on deleting product: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Given invalid product={} for deleting", product);
		}

		return result;
	}

}
