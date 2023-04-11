package ru.clevertec.knyazev.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.Shop;

public class ShopDAOJPA implements ShopDAO {
	private static final Integer PAGE_EL_LIMIT = 20;

	private static final Logger logger = LoggerFactory.getLogger(ShopDAOJPA.class);

	private AppConnectionConfig appConnectionConfig;

	public ShopDAOJPA() {
	}

	public ShopDAOJPA(AppConnectionConfig appConnectionConfig) {
		this.appConnectionConfig = appConnectionConfig;
	}

	@Override
	public Optional<Shop> getShopById(Long id) {
		Optional<Shop> shopWrap = Optional.empty();

		if (id != null && id > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				Shop dbshop = entityManager.find(Shop.class, id);
				if (dbshop != null) {
					shopWrap = Optional.of(dbshop);
				}
			} catch (IllegalArgumentException e) {
				logger.error("Error when getting shop: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		}

		return shopWrap;
	}

	@Override
	public List<Shop> getAllShops() {
		List<Shop> shops = new ArrayList<>();

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			shops = entityManager.createQuery("SELECT s FROM Shop s", Shop.class).getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all shops: {}", e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return shops;
	}

	@Override
	public List<Shop> getAllShops(Integer page, Integer elementsOnPage) {
		List<Shop> shops = new ArrayList<>();

		if ((page == null || page < 1) && (elementsOnPage == null || elementsOnPage < 1)) {
			return shops;
		}

		Integer offsset = (page - 1) * elementsOnPage;

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			shops = entityManager.createQuery("SELECT s FROM Shop s", Shop.class).setFirstResult(offsset)
					.setMaxResults(elementsOnPage).getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all shops on page={}: {}", page, e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return shops;
	}

	@Override
	public List<Shop> getAllShops(Integer page) {
		return getAllShops(page, PAGE_EL_LIMIT);
	}

	@Override
	public Optional<Shop> saveShop(Shop shop) {
		Optional<Shop> shopWrap = Optional.empty();

		if (shop != null && shop.getId() == null) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				entityManager.persist(shop);
				entityManager.getTransaction().commit();

				shopWrap = Optional.of(shop);
			} catch (IllegalStateException | PersistenceException e) {
				entityManager.getTransaction().rollback();
				logger.error("Error when saving shop: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Error saving on invalid shop: {}", shop);
		}

		return shopWrap;
	}

	@Override
	public Optional<Shop> updateShop(Shop shop) {
		Optional<Shop> shopdWrap = Optional.empty();

		if (shop != null && shop.getId() != null && shop.getId() > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				Shop shopDB = entityManager.find(Shop.class, shop.getId());

				if (shopDB != null) {
					shopDB.setName(shop.getName());
					shopDB.setAddress(shop.getAddress());
					shopDB.setPhone(shop.getPhone());

					entityManager.getTransaction().commit();

					shopdWrap = Optional.of(shopDB);
				} else {
					entityManager.getTransaction().rollback();
					logger.error("Error on updating. Shop not exists on given id={}", shop.getId());
				}

			} catch (IllegalStateException | PersistenceException e) {
				entityManager.getTransaction().rollback();
				logger.error("Error when updating shop: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Error updating on invalid shop: {}", shop);
		}

		return shopdWrap;
	}

	@Override
	public Boolean deleteShop(Shop shop) {
		Boolean result = false;

		if (shop != null && shop.getId() != null && shop.getId() > 0L) {

			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				Shop dbShop = entityManager.find(Shop.class, shop.getId());

				if (dbShop != null) {
					entityManager.remove(dbShop);
					entityManager.getTransaction().commit();

					result = true;
				} else {
					entityManager.getTransaction().rollback();
					logger.error("Shop not exists with given id=" + shop.getId());
				}
			} catch (PersistenceException | IllegalArgumentException e) {
				entityManager.getTransaction().rollback();
				logger.error("Error on deleting shop: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Given invalid shop={} for deleting", shop);
		}

		return result;
	}
}
