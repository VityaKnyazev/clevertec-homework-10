package ru.clevertec.knyazev.dao;

import ru.clevertec.knyazev.entity.Address;


/**
 * 
 * Represents interface for Address entity
 *
 */

public interface AddressDAO {
	Address findById(Long id);
}
