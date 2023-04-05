package ru.clevertec.knyazev.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.entity.Seller;

public class SellerDAOJPA implements SellerDAO {
	private static final Logger logger = LoggerFactory.getLogger(SellerDAOJPA.class);
	
	private static final String SAVING_ERROR = "Error on saving seller entity. ";
	private static final String UPDATING_ERROR = "Error on updating seller entity. ";
	private static final String DELETING_ERROR = "Error on deleting seller entity. ";
	
	private EntityManager entityManager;
	
	public SellerDAOJPA(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public SellerDAOJPA() {}

	@Override
	public Seller getSeller(Long id) {
		Seller seller = null;

		if (id != null && id > 0L) {
			seller = entityManager.find(Seller.class, id);
		}

		return seller;
	}

	@Override
	public List<Seller> getAllSellers() {
		List<Seller> sellers = new ArrayList<>();

		sellers = entityManager.createQuery("SELECT s FROM Seller s", Seller.class).getResultList();
		return sellers;
	}

	@Override
	public void saveSeller(Seller seller) {

		if (seller != null && seller.getId() == null) {
			try {
				entityManager.persist(seller);
			} catch (PersistenceException | IllegalArgumentException e) {
				logger.error(SAVING_ERROR, e);
				throw new DAOException(SAVING_ERROR + e.getMessage(), e);
			}
		} else {
			logger.error(SAVING_ERROR + "Giving seller is null or has not null id.");
			throw new DAOException(SAVING_ERROR);
		}

	}

	@Override
	public void updateSeller(Seller seller) {

		if (seller != null && seller.getId() != null && seller.getId() > 0L) {
			try {
				Seller dbSeller = entityManager.find(Seller.class, seller.getId());
				if (dbSeller != null) {
					dbSeller.setName(seller.getName());
					dbSeller.setFamilyName(seller.getFamilyName());
					dbSeller.setEmail(seller.getEmail());
					dbSeller.setRole(seller.getRole());
					entityManager.flush();
				} else {
					throw new DAOException(UPDATING_ERROR + "Seller entity not exists with given id=" + seller.getId());
				}
			} catch (PersistenceException | IllegalArgumentException e) {
				logger.error(UPDATING_ERROR, e);
				throw new DAOException(UPDATING_ERROR + e.getMessage(), e);
			}
		} else {
			logger.error(UPDATING_ERROR + "Giving seller is null or has null or bad id.");
			throw new DAOException(UPDATING_ERROR);
		}

	}

	@Override
	public void deleteSeller(Seller seller) {

		if (seller != null && seller.getId() != null && seller.getId() > 0L) {
			try {
				Seller dbSeller = entityManager.find(Seller.class, seller.getId());
				if (dbSeller != null) {
					entityManager.remove(dbSeller);
					entityManager.flush();
				} else {
					throw new DAOException(DELETING_ERROR + "Seller entity not exists with given id=" + seller.getId());
				}
			} catch (PersistenceException | IllegalArgumentException e) {
				logger.error(DELETING_ERROR, e);
				throw new DAOException(DELETING_ERROR + e.getMessage(), e);
			}
		} else {
			logger.error(DELETING_ERROR + "Giving seller is null or has null or bad id.");
			throw new DAOException(DELETING_ERROR);
		}

	}

}
