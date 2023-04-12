package ru.clevertec.knyazev.service;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.service.exception.ServiceException;

/**
 * 
 * Address service represents service for managing address objects.
 * 
 * @author Vitya Knyazev
 *
 */
public interface AddressService {

	Optional<Address> showAddress(Long id);
	
	List<Address> showAllAddresses();

	List<Address> showAllAddresses(Integer page, Integer pageSize);

	List<Address> showAllAddresses(Integer page);

	Address addAddress(Address address) throws ServiceException;

	Address changeAddress(Address address) throws ServiceException;

	void removeAddress(Address address) throws ServiceException;

}
