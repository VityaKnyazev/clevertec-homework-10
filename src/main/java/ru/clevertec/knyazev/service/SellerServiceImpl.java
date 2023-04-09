package ru.clevertec.knyazev.service;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.dao.SellerDAO;
import ru.clevertec.knyazev.dao.proxy.SellerDAOProxy;
import ru.clevertec.knyazev.entity.Seller;
import ru.clevertec.knyazev.service.exception.ServiceException;

public class SellerServiceImpl implements SellerService {
	private SellerDAO sellerDAO;

	public SellerServiceImpl(SellerDAO sellerDAOJPA) {
		SellerDAOProxy sellerDAOProxy = new SellerDAOProxy(sellerDAOJPA);
		this.sellerDAO =  (SellerDAO) Proxy.newProxyInstance(SellerDAO.class.getClassLoader(),
				new Class[] { SellerDAO.class }, sellerDAOProxy);
	}

	@Override
	public Optional<Seller> showSeller(Long id) {
		return sellerDAO.getSellerById(id);
	}
	
	@Override
	public List<Seller> showAllSellers() {
		return sellerDAO.getAllSellers();
	}
	
	@Override
	public List<Seller> showAllSellers(Integer page, Integer pageSize) {
		return sellerDAO.getAllSellers(page, pageSize);
	}
	
	@Override
	public List<Seller> showAllSellers(Integer page) {
		return sellerDAO.getAllSellers(page);
	}
	
	@Override
	public void addSeller(Seller seller) throws ServiceException {
		Optional<Seller> savedSellerWrap = sellerDAO.saveSeller(seller);
		
		if (savedSellerWrap.isEmpty()) {
			throw new ServiceException("Error on adding seller!");
		}
	}

	@Override
	public void changeSeller(Seller seller) throws ServiceException {
		Optional<Seller> updatedSellerWrap = sellerDAO.updateSeller(seller);
		
		if (updatedSellerWrap.isEmpty()) {
			throw new ServiceException("Error on changing seller!");
		}
	}

	@Override
	public void removeSeller(Seller seller) throws ServiceException {
		Boolean result = sellerDAO.deleteSeller(seller);
		
		if (!result) {
			throw new ServiceException("Error on deleting seller!");
		}
	}

}
