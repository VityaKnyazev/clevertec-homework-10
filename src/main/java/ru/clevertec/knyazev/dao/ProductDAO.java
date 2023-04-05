package ru.clevertec.knyazev.dao;

import ru.clevertec.knyazev.entity.Product;

public interface ProductDAO {

	Product getProductById(Long id);
	Product saveProduct(Product product);	
}