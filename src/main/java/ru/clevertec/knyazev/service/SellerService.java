package ru.clevertec.knyazev.service;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.entity.Seller;
import ru.clevertec.knyazev.service.exception.ServiceException;

public interface SellerService {
	
	Optional<Seller> showSeller(Long id);
	
	List<Seller> showAllSellers();
	
	List<Seller> showAllSellers(Integer page, Integer pageSize);
	
	List<Seller> showAllSellers(Integer page);
	
	Seller addSeller(Seller seller) throws ServiceException;
	
	Seller changeSeller(Seller seller) throws ServiceException;
	
	void removeSeller(Seller seller) throws ServiceException;
}
