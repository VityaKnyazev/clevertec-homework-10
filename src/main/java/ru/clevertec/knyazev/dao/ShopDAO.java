package ru.clevertec.knyazev.dao;

import ru.clevertec.knyazev.entity.Shop;

public interface ShopDAO {
	Shop findById(Long id);
}
