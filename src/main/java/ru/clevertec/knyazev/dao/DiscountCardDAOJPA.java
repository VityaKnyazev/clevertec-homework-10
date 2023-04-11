package ru.clevertec.knyazev.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.DiscountCard;

public class DiscountCardDAOJPA implements DiscountCardDAO {
	private static final Integer PAGE_EL_LIMIT = 20;

	private static final Logger logger = LoggerFactory.getLogger(DiscountCardDAOJPA.class);

	private AppConnectionConfig appConnectionConfig;

	public DiscountCardDAOJPA() {
	}

	public DiscountCardDAOJPA(AppConnectionConfig appConnectionConfig) {
		this.appConnectionConfig = appConnectionConfig;
	}

	@Override
	public Optional<DiscountCard> getDiscountCardByNumber(String number) {
		Optional<DiscountCard> discountCard = Optional.empty();

		if (number != null && number.length() > 0) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();
			try {
				DiscountCard discountCardDB = (DiscountCard) entityManager
						.createQuery(
								"SELECT discountCard FROM DiscountCard discountCard WHERE discountCard.number = ?1")
						.setParameter(1, number).getSingleResult();
				discountCard = Optional.of(discountCardDB);
			} catch (IllegalArgumentException | PersistenceException e) {
				logger.error("Error when getting discount card: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		}

		return discountCard;
	}
	
	@Override
	public Optional<DiscountCard> getDiscountCardById(Long id) {
		Optional<DiscountCard> discountCard = Optional.empty();

		if (id != null && id > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();
			try {
				DiscountCard discountCardDB = (DiscountCard) entityManager
						.createQuery(
								"SELECT discountCard FROM DiscountCard discountCard WHERE discountCard.id = ?1")
						.setParameter(1, id).getSingleResult();
				discountCard = Optional.of(discountCardDB);
			} catch (IllegalArgumentException | PersistenceException e) {
				logger.error("Error when getting discount card: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		}

		return discountCard;
	}

	@Override
	public List<DiscountCard> getAllDiscountCards() {
		List<DiscountCard> discountCards = new ArrayList<>();

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			discountCards = entityManager.createQuery("SELECT d FROM DiscountCard d", DiscountCard.class)
					.getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all discount cards: {}", e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return discountCards;
	}

	@Override
	public List<DiscountCard> getAllDiscountCards(Integer page, Integer elementsOnPage) {
		List<DiscountCard> discountCards = new ArrayList<>();

		if ((page == null || page < 1) && (elementsOnPage == null || elementsOnPage < 1)) {
			return discountCards;
		}

		Integer offsset = (page - 1) * elementsOnPage;

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			discountCards = entityManager.createQuery("SELECT d FROM DiscountCard d", DiscountCard.class)
					.setFirstResult(offsset).setMaxResults(elementsOnPage).getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all discount cards on page={}: {}", page, e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return discountCards;
	}

	@Override
	public List<DiscountCard> getAllDiscountCards(Integer page) {
		return getAllDiscountCards(page, PAGE_EL_LIMIT);
	}

	@Override
	public Optional<DiscountCard> saveDiscountCard(DiscountCard discountCard) {
		Optional<DiscountCard> discountCardWrap = Optional.empty();

		if (discountCard != null && discountCard.getId() == null) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				entityManager.persist(discountCard);
				entityManager.getTransaction().commit();

				discountCardWrap = Optional.of(discountCard);
			} catch (IllegalStateException | PersistenceException e) {
				entityManager.getTransaction().rollback();
				logger.error("Error when saving discount card: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Error saving on invalid discount card: {}", discountCard);
		}

		return discountCardWrap;
	}

	@Override
	public Optional<DiscountCard> updateDiscountCard(DiscountCard discountCard) {
		Optional<DiscountCard> discountCardWrap = Optional.empty();

		if (discountCard != null && discountCard.getId() != null && discountCard.getId() > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				DiscountCard discountCardDB = entityManager.find(DiscountCard.class, discountCard.getId());

				if (discountCardDB != null) {
					discountCardDB.setNumber(discountCard.getNumber());
					discountCardDB.setDiscountValue(discountCard.getDiscountValue());

					entityManager.getTransaction().commit();

					discountCardWrap = Optional.of(discountCard);
				} else {
					entityManager.getTransaction().rollback();
					logger.error("Error on updating. Discount card not exists on given id={}", discountCard.getId());
				}

			} catch (IllegalStateException | PersistenceException e) {
				entityManager.getTransaction().rollback();
				logger.error("Error when updating discount card: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Error updating on invalid discount card: {}", discountCard);
		}

		return discountCardWrap;
	}

	@Override
	public Boolean deleteDiscountCard(DiscountCard discountCard) {
		Boolean result = false;

		if (discountCard != null && discountCard.getId() != null && discountCard.getId() > 0L) {

			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				DiscountCard dbDiscountCard = entityManager.find(DiscountCard.class, discountCard.getId());

				if (dbDiscountCard != null) {
					entityManager.remove(dbDiscountCard);
					entityManager.getTransaction().commit();

					result = true;
				} else {
					entityManager.getTransaction().rollback();
					logger.error("Discount card not exists with given id=" + discountCard.getId());
				}
			} catch (PersistenceException | IllegalArgumentException e) {
				entityManager.getTransaction().rollback();
				logger.error("Error on deleting discount card: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Given invalid discount card={} for deleting", discountCard);
		}

		return result;
	}

	@Override
	public boolean isDiscountCardExists(String discountCardNumber) {
		long quantity = 0;

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		quantity = ((Number) entityManager.createNativeQuery("SELECT COUNT(id) FROM discount_card WHERE number = ?1")
				.setParameter(1, discountCardNumber).getSingleResult()).longValue();

		return quantity > 0;
	}

}
