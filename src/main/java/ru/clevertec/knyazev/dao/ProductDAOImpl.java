package ru.clevertec.knyazev.dao;

import java.util.ArrayList;
import java.util.List;

import ru.clevertec.knyazev.entity.Product;

public class ProductDAOImpl implements ProductDAO {
	private static final List<Product> PRODUCTS = new ArrayList<>() {
		private static final long serialVersionUID = 2136721681299506628L;

	{
		add(new Product(1L, "Сахар СЛАДКИЙ для чая", false));
		add(new Product(2L, "Самовар для водки", true));
		add(new Product(3L, "Чаинки подарочные инсталяционные", true));
		add(new Product(4L, "Вино домашнее красное", false));
		add(new Product(5L, "Штопор гипоаллергенный", false));
		add(new Product(6L, "Вино Кавказское КЛАСИК", false));
		add(new Product(7L, "Набор бокалов для вина", true));
		add(new Product(8L, "Шейкер КАРУСЕЛЬ", true));
		add(new Product(9L, "Подарочный набор ВИНОДЕЛ", true));
		add(new Product(10L, "Холодный лед намороженный", false));
		add(new Product(11L, "Бутылка подарочная именинник", true));
		add(new Product(12L, "Стакан МАЛИНОВСКИЙ", false));
	}};
	
	@Override
	public Product getProductById(Long id) {
		for (Product product : PRODUCTS) {
			if (product.getId() == id) {
				return product;
			}
		}
		
		return null;
	}

	@Override
	public Product saveProduct(Product product) {
		product.setId(PRODUCTS.size() + 1L);
		
		PRODUCTS.add(product);
		return product;
	}

}
