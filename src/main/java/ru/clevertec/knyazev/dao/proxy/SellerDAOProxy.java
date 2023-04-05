package ru.clevertec.knyazev.dao.proxy;

import java.lang.reflect.Method;

import ru.clevertec.knyazev.cache.SimpleCacheFactory;
import ru.clevertec.knyazev.dao.SellerDAO;
import ru.clevertec.knyazev.dao.exception.DAOException;
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
			case "getSeller" -> whenGetSeller(args);
			case "getAllSellers" -> method.invoke(sellerDAO, args);
			case "saveSeller" -> {
				whenSaveSeller(args);
				yield null;
			}
			case "updateSeller" -> {
				whenUpdateSeller(args);
				yield null;
			}
			case "deleteSeller" -> {
				whenDeleteSeller(args);
				yield null;
			}
			default -> throw new IllegalArgumentException("Unexpected method name: " + methodName);
			};

	}

	private Seller whenGetSeller(Object[] args) {
		Seller seller = null;
		Long id = (Long) args[0];

		Seller cacheSeller = cache.get(id);

		if (cacheSeller != null) {
			seller = cacheSeller;
		} else {
			seller = sellerDAO.getSeller(id);
			if (seller != null) {
				cache.put(id, seller);
			}
		}

		return seller;
	}

	private void whenSaveSeller(Object[] args) throws DAOException {
		Seller savingSeller = (Seller) args[0];
		Long savingSellerId = savingSeller.getId();

		sellerDAO.saveSeller(savingSeller);
		cache.put(savingSellerId, savingSeller);
	}

	private void whenUpdateSeller(Object[] args) throws DAOException {
		Seller updatingSeller = (Seller) args[0];
		Long updatingSellererId = updatingSeller.getId();

		sellerDAO.updateSeller(updatingSeller);
		cache.put(updatingSellererId, updatingSeller);
	}

	private void whenDeleteSeller(Object[] args) throws DAOException {
		Seller deletingSeller = (Seller) args[0];
		Long deletingSellerId = deletingSeller.getId();

		sellerDAO.deleteSeller(deletingSeller);
		cache.remove(deletingSellerId);
	}
}
