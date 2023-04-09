package ru.clevertec.knyazev.service;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.entity.Seller;
import ru.clevertec.knyazev.service.exception.ServiceException;

public interface SellerService {
	
	public Optional<Seller> showSeller(Long id);
	
	public List<Seller> showAllSellers();
	
	public List<Seller> showAllSellers(Integer page, Integer pageSize);
	
	public List<Seller> showAllSellers(Integer page);
	
	public void addSeller(Seller seller) throws ServiceException;
	
	public void changeSeller(Seller seller) throws ServiceException;
	
	public void removeSeller(Seller seller) throws ServiceException;
}
