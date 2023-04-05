package ru.clevertec.knyazev.dao;

import jakarta.persistence.EntityManager;
import ru.clevertec.knyazev.entity.Address;

public class AddressDAOJPA implements AddressDAO {
	private EntityManager entityManager;
	
	public AddressDAOJPA() {
	}

	public AddressDAOJPA(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Address findById(Long id) {
		Address address = null;
		
		if (id != null && id > 0L) {
			address = entityManager.find(Address.class, id);
		} 

		return address;
	}

}
