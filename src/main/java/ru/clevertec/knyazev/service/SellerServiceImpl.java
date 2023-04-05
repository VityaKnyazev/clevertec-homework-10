package ru.clevertec.knyazev.service;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.clevertec.knyazev.dao.SellerDAO;
import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.dao.proxy.SellerDAOProxy;
import ru.clevertec.knyazev.entity.Seller;
import ru.clevertec.knyazev.service.exception.ServiceException;

public class SellerServiceImpl implements SellerService {
	private static final Logger logger = LoggerFactory.getLogger(SellerServiceImpl.class);
	private SellerDAO sellerDAOJPA;

	public SellerServiceImpl(SellerDAO sellerDAOJPA) {
		SellerDAOProxy sellerDAOProxy = new SellerDAOProxy(sellerDAOJPA);
		this.sellerDAOJPA =  (SellerDAO) Proxy.newProxyInstance(SellerDAO.class.getClassLoader(),
				new Class[] { SellerDAO.class }, sellerDAOProxy);
	}

	@Override
	public Optional<Seller> showSeller(Long id) {
		Seller seller = sellerDAOJPA.getSeller(id);
		return (seller != null) ? Optional.of(seller) : Optional.empty();
	}
	
	@Override
	public List<Seller> showAllSellers() {
		return sellerDAOJPA.getAllSellers();
	}
	
	@Override
	public void addSeller(Seller seller) throws ServiceException {
		try {
			sellerDAOJPA.saveSeller(seller);
		} catch (DAOException e) {
			logger.error("Error on adding seller:", e);
			throw new ServiceException("Error on adding seller! " + e.getMessage(), e);
		}
	}

	@Override
	public void changeSeller(Seller seller) throws ServiceException {
		try {
			sellerDAOJPA.updateSeller(seller);
		} catch (DAOException e) {
			logger.error("Error on changing seller:", e);
			throw new ServiceException("Error on changing seller! " + e.getMessage(), e);
		}
	}

	@Override
	public void removeSeller(Seller seller) throws ServiceException {
		try {
			sellerDAOJPA.deleteSeller(seller);
		} catch (DAOException e) {
			logger.error("Error on deleting seller:", e);
			throw new ServiceException("Error on deleting seller! " + e.getMessage(), e);
		}
	}

}
