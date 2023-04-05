package ru.clevertec.knyazev.dao;

import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.entity.Shop;

public class ShopDAOImpl implements ShopDAO {
	private AddressDAO addressDAO;

	public ShopDAOImpl(AddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}

	@Override
	public Shop findById(Long id) {
		Address address;
		
		if (id == 1L) {
			address = addressDAO.findById(1L);
			Shop shop = new Shop(1L, "SHoP Of Art ANd Wine", address, "+375 44 569 25 64");
			return shop;
		}
		
		address = addressDAO.findById(null);		
		return new Shop(null, "n/a", address, "n/a");
	}
}
