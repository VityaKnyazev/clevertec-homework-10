package ru.clevertec.knyazev.service;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.service.exception.ServiceException;

public interface ProductService {
	
	Optional<Product> showProduct(Long id);
	
	List<Product> showAllProducts();

	List<Product> showAllProducts(Integer page, Integer pageSize);

	List<Product> showAllProducts(Integer page);

	Product addProduct(Product product) throws ServiceException;

	Product changeProduct(Product product) throws ServiceException;

	void removeProduct(Product product) throws ServiceException;
}
