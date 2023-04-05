package ru.clevertec.knyazev.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.clevertec.knyazev.entity.Shop;

public class ShopDAOJPA implements ShopDAO {
	@PersistenceContext
	EntityManager entityManager;

	public ShopDAOJPA() {
	}

	public ShopDAOJPA(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Shop findById(Long id) {
		Shop shop = null;

		if (id != null && id > 0L) {
			shop = entityManager.find(Shop.class, id);
		}
		return shop;
	}
}
