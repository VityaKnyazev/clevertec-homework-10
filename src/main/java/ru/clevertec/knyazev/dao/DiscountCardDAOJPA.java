package ru.clevertec.knyazev.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.clevertec.knyazev.entity.DiscountCard;

public class DiscountCardDAOJPA implements DiscountCardDAO {
	@PersistenceContext()
	EntityManager entityManager;

	public DiscountCardDAOJPA() {
	}

	public DiscountCardDAOJPA(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public DiscountCard getDiscountCardByNumber(String number) {
		DiscountCard discountCard = null;

		if (number != null && number.length() > 0) {
			discountCard = (DiscountCard) entityManager
					.createQuery("SELECT discountCard FROM DiscountCard discountCard WHERE discountCard.number = ?1")
					.setParameter(1, number).getSingleResult();
		}

		return discountCard;
	}

	@Override
	public DiscountCard saveDiscountCard(DiscountCard discountCard) {
		if (discountCard.getId() == null) {
			entityManager.persist(discountCard);
			return discountCard;
		}

		return null;
	}

	@Override
	public boolean isDiscountCardExists(String discountCardNumber) {
		long quantity = 0;
		quantity = ((Number) entityManager.createNativeQuery("SELECT COUNT(id) FROM discount_card WHERE number = ?1")
				.setParameter(1, discountCardNumber).getSingleResult()).longValue();

		return quantity > 0;
	}

}
