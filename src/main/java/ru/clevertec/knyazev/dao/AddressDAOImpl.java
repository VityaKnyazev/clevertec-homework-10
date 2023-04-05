package ru.clevertec.knyazev.dao;

import ru.clevertec.knyazev.entity.Address;

public class AddressDAOImpl implements AddressDAO {
	private static final Address ADDRESS = new Address(1L, "128546", "Belarus", "Mogilev", "Bulgakov str", "15А");
	private static final Address DEFAULT_ADDRESS = new Address(null, "n/a", "n/a", "n/a", "n/a", "n/a");

	@Override
	public Address findById(Long id) {
		if (id == 1L) {
			return ADDRESS;
		}
		
		return DEFAULT_ADDRESS;
	}
}