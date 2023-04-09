package ru.clevertec.knyazev.service;

import java.util.Optional;

import ru.clevertec.knyazev.entity.Shop;

/**
 * 
 * Shop service represents service for managing shop objects.
 * 
 * @author Vitya Knyazev
 *
 */
public interface ShopService {

	/**
	 * 
	 * Get shop on given Long id
	 * 
	 * @param Long id shop id for searching in database
	 * @return Optional<Shop> shop wrap in Optional. if shop exists in database on given id - return 
	 *         shop wrap in Optional, otherwise - Optional empty.
	 */
	Optional<Shop> showShop(Long id);
	
}
