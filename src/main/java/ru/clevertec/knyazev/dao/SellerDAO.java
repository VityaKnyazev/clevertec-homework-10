package ru.clevertec.knyazev.dao;

import java.util.List;

import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.entity.Seller;

public interface SellerDAO {
	
	Seller getSeller(Long id);
	
	List<Seller> getAllSellers();
	
	void saveSeller(Seller seller) throws DAOException;
	
	void updateSeller(Seller seller) throws DAOException;
	
	void deleteSeller(Seller seller) throws DAOException;
}
