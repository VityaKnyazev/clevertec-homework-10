package ru.clevertec.knyazev.dao.proxy;

import java.lang.reflect.Method;
import java.util.Optional;

import ru.clevertec.knyazev.cache.SimpleCacheFactory;
import ru.clevertec.knyazev.dao.SellerDAO;
import ru.clevertec.knyazev.entity.Seller;

public class SellerDAOProxy extends AbstractDAOProxy<Long, Seller> {
	private SellerDAO sellerDAO;

	public SellerDAOProxy(SellerDAO sellerDAO) {
		super(new SimpleCacheFactory<Long, Seller>());
		this.sellerDAO = sellerDAO;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
			
			return switch (methodName) {
			case "getSellerById" -> whenGetSeller(args);
			case "getAllSellers" -> method.invoke(sellerDAO, args);
			case "saveSeller" -> whenSaveSeller(args);
			case "updateSeller" -> whenUpdateSeller(args);
			case "deleteSeller" -> whenDeleteSeller(args);
			default -> throw new IllegalArgumentException("Unexpected method name: " + methodName);
			};

	}

	private Optional<Seller> whenGetSeller(Object[] args) {
		Optional<Seller> seller = null;
		Long id = (Long) args[0];

		Seller cacheSeller = cache.get(id);

		if (cacheSeller != null) {
			seller = Optional.of(cacheSeller);
		} else {
			seller = sellerDAO.getSellerById(id);
			if (seller.isPresent()) {
				cache.put(id, seller.get());
			}
		}

		return seller;
	}

	private Optional<Seller> whenSaveSeller(Object[] args) {
		Seller savingSeller = (Seller) args[0];

		Optional<Seller> savedSellerWrap = sellerDAO.saveSeller(savingSeller);
		
		if (savedSellerWrap.isPresent()) {
			Seller savedSeller = savedSellerWrap.get();
			cache.put(savedSeller.getId(), savedSeller);
		}
		
		return savedSellerWrap;
	}

	private Optional<Seller> whenUpdateSeller(Object[] args)  {
		Seller updatingSeller = (Seller) args[0];

		Optional<Seller> updatedSellerWrap =  sellerDAO.updateSeller(updatingSeller);

		if (updatedSellerWrap.isPresent()) {
			Seller updatedSeller = updatedSellerWrap.get();
			cache.put(updatedSeller.getId(), updatedSeller);
		}
		
		return updatedSellerWrap;
	}

	private Boolean whenDeleteSeller(Object[] args) {
		Seller deletingSeller = (Seller) args[0];

		Boolean result = sellerDAO.deleteSeller(deletingSeller);
		
		if (result) {
			Long deletingSellerId = deletingSeller.getId();
			cache.remove(deletingSellerId);
		}
		
		return result;
	}
}
