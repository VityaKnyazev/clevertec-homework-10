package ru.clevertec.knyazev.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.Seller;

public class SellerDAOJPA implements SellerDAO {
	private static final Integer PAGE_EL_LIMIT = 20;
	
	private static final Logger logger = LoggerFactory.getLogger(SellerDAOJPA.class);
	
	private AppConnectionConfig appConnectionConfig;
	
	public SellerDAOJPA(AppConnectionConfig appConnectionConfig) {
		this.appConnectionConfig = appConnectionConfig;
	}
	
	public SellerDAOJPA() {}

	@Override
	public Optional<Seller> getSellerById(Long id) {
		Optional<Seller> sellerWrap = Optional.empty();

		if (id != null && id > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				Seller dbSeller = entityManager.find(Seller.class, id);
				if (dbSeller != null) {
					sellerWrap = Optional.of(dbSeller);
				}
			} catch (IllegalArgumentException e) {
				logger.error("Error when getting seller: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		}

		return sellerWrap;
	}

	@Override
	public List<Seller> getAllSellers() {
		List<Seller> seller = new ArrayList<>();

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			seller = entityManager.createQuery("SELECT s FROM Seller s", Seller.class).getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all sellers: {}", e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return seller;
	}
	
	@Override
	public List<Seller> getAllSellers(Integer page, Integer elementsOnPage) {
		List<Seller> sellers = new ArrayList<>();

		if ((page == null || page < 1) && (elementsOnPage == null || elementsOnPage < 1)) {
			return sellers;
		}

		Integer offsset = (page - 1) * elementsOnPage;

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			sellers = entityManager.createQuery("SELECT s FROM Seller s", Seller.class).setFirstResult(offsset)
					.setMaxResults(elementsOnPage).getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all sellers on page={}: {}", page, e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return sellers;
	}
	
	@Override
	public List<Seller> getAllSellers(Integer page) {
		return getAllSellers(page, PAGE_EL_LIMIT);
	}

	@Override
	public Optional<Seller> saveSeller(Seller seller) {
		Optional<Seller> sellerWrap = Optional.empty();

		if (seller != null && seller.getId() == null) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				entityManager.persist(seller);
				entityManager.getTransaction().commit();

				sellerWrap = Optional.of(seller);
			} catch (IllegalStateException | PersistenceException e) {
				logger.error("Error when saving seller: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Error saving on invalid seller: {}", seller);
		}

		return sellerWrap;

	}

	@Override
	public Optional<Seller> updateSeller(Seller seller) {
		Optional<Seller> sellerWrap = Optional.empty();

		if (seller != null && seller.getId() != null && seller.getId() > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				Seller sellerDB = entityManager.find(Seller.class, seller.getId());

				if (sellerDB != null) {
					sellerDB.setName(seller.getName());
					sellerDB.setFamilyName(seller.getFamilyName());
					sellerDB.setEmail(seller.getEmail());
					sellerDB.setRole(seller.getRole());
					
					entityManager.getTransaction().commit();
					
					sellerWrap = Optional.of(seller);
				} else {
					logger.error("Error on updating. Seller not exists on given id={}", seller.getId());
				}

			} catch (IllegalStateException | PersistenceException e) {
				logger.error("Error when updating seller: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Error updating on invalid seller: {}", seller);
		}

		return sellerWrap;
	}

	@Override
	public Boolean deleteSeller(Seller seller) {
		Boolean result = false;

		if (seller != null && seller.getId() != null && seller.getId() > 0L) {

			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				Seller dbSeller = entityManager.find(Seller.class, seller.getId());

				if (dbSeller != null) {
					entityManager.remove(dbSeller);
					entityManager.getTransaction().commit();

					result = true;
				} else {
					logger.error("Seller not exists with given id=" + seller.getId());
				}
			} catch (PersistenceException | IllegalArgumentException e) {
				logger.error("Error on deleting seller: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Given invalid seller={} for deleting", seller);
		}

		return result;
	}

}
