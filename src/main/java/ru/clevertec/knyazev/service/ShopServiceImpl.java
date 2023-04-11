package ru.clevertec.knyazev.service;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.dao.ShopDAO;
import ru.clevertec.knyazev.entity.Shop;
import ru.clevertec.knyazev.service.exception.ServiceException;

public class ShopServiceImpl implements ShopService {
	private ShopDAO shopDAO;
	
	public ShopServiceImpl(ShopDAO shopDAO) {
		this.shopDAO = shopDAO;
	}

	@Override
	public Optional<Shop> showShop(Long id) {
		return shopDAO.getShopById(id);
	}
	
	@Override
	public List<Shop> showAllShops() {
		return shopDAO.getAllShops();
	}
	
	@Override
	public List<Shop> showAllShops(Integer page, Integer pageSize) {
		return shopDAO.getAllShops(page, pageSize);
	}
	
	@Override
	public List<Shop> showAllShops(Integer page) {
		return shopDAO.getAllShops(page);
	}
	
	@Override
	public Shop addShop(Shop shop) throws ServiceException {
		Optional<Shop> savedShopWrap = shopDAO.saveShop(shop);
		
		if (savedShopWrap.isEmpty()) {
			throw new ServiceException("Error on adding shop!");
		} else {
			return savedShopWrap.get();
		}
	}

	@Override
	public Shop changeShop(Shop shop) throws ServiceException {
		Optional<Shop> updatedShopWrap = shopDAO.updateShop(shop);
		
		if (updatedShopWrap.isEmpty()) {
			throw new ServiceException("Error on changing shop!");
		} else {
			return updatedShopWrap.get();
		}
	}

	@Override
	public void removeShop(Shop shop) throws ServiceException {
		Boolean result = shopDAO.deleteShop(shop);
		
		if (!result) {
			throw new ServiceException("Error on deleting seller!");
		}
	}
	

}
