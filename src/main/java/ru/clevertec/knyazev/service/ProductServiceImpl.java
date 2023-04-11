package ru.clevertec.knyazev.service;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.dao.ProductDAO;
import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.service.exception.ServiceException;

public class ProductServiceImpl implements ProductService {
	private ProductDAO productDAO;
	
	public ProductServiceImpl(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	@Override
	public Optional<Product> showProduct(Long id) {
		return productDAO.getProductById(id);
	}
	
	@Override
	public List<Product> showAllProducts() {
		return productDAO.getAllProducts();
	}
	
	@Override
	public List<Product> showAllProducts(Integer page, Integer pageSize) {
		return productDAO.getAllProducts(page, pageSize);
	}
	
	@Override
	public List<Product> showAllProducts(Integer page) {
		return productDAO.getAllProducts(page);
	}
	
	@Override
	public Product addProduct(Product product) throws ServiceException {
		Optional<Product> savedProductWrap = productDAO.saveProduct(product);
		
		if (savedProductWrap.isEmpty()) {
			throw new ServiceException("Error on adding product!");
		} else {
			return savedProductWrap.get();
		}
	}

	@Override
	public Product changeProduct(Product product) throws ServiceException {
		Optional<Product> updatedProductWrap = productDAO.updateProduct(product);
		
		if (updatedProductWrap.isEmpty()) {
			throw new ServiceException("Error on changing product!");
		} else {
			return updatedProductWrap.get();
		}
	}

	@Override
	public void removeProduct(Product product) throws ServiceException {
		Boolean result = productDAO.deleteProduct(product);
		
		if (!result) {
			throw new ServiceException("Error on deleting product!");
		}
	}	

}
