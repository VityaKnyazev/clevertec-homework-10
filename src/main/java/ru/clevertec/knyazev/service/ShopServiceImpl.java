package ru.clevertec.knyazev.service;

import ru.clevertec.knyazev.dao.ShopDAO;
import ru.clevertec.knyazev.entity.Shop;

public class ShopServiceImpl implements ShopService {
	private ShopDAO shopDAOJPA;
	
	public ShopServiceImpl(ShopDAO shopDAOJPA) {
		this.shopDAOJPA = shopDAOJPA;
	}

	@Override
	public Shop showShop() {
		return shopDAOJPA.findById(1L);
	}

}
