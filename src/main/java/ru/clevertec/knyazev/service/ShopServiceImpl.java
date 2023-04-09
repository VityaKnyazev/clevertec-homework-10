package ru.clevertec.knyazev.service;

import java.util.Optional;

import ru.clevertec.knyazev.dao.ShopDAO;
import ru.clevertec.knyazev.entity.Shop;

public class ShopServiceImpl implements ShopService {
	private ShopDAO shopDAO;
	
	public ShopServiceImpl(ShopDAO shopDAO) {
		this.shopDAO = shopDAO;
	}

	@Override
	public Optional<Shop> showShop(Long id) {
		return (id != null && id > 0L) 
				? shopDAO.getShopById(id) 
				: Optional.empty();
	}

}
