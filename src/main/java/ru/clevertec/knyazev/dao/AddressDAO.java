package ru.clevertec.knyazev.dao;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.entity.Address;


/**
 * 
 * Interface for Address data access object.
 *
 */

public interface AddressDAO {
	
	/**
	 * 
	 * Get Optional<Address> from database on giving id.
	 * 
	 * @param Long id for searching address in database.
	 * @return Optional<Address> address if address not exists in database - Optional empty.
	 */
	Optional<Address> getAddressById(Long id);
	
	/**
	 * 
	 * Get all addresses from database
	 * 
	 * @return List<Address> all addresses from database or empty list.
	 */
	List<Address> getAllAddresses();
	
	/**
	 * 
	 * Get all addresses from database on given Integer page of given quantity of elements.
	 * 
	 * @param Integer page number.
	 * @param Integer elementsOnPage quantity of elements on page.
	 * @return List<Address> addresses on given Integer page of given Integer quantity of elements
	 *         from database or empty list.
	 */
	List<Address> getAllAddresses(Integer page, Integer elementsOnPage);
	
	/**
	 * 
	 * Get all addresses from database on given Integer page.
	 * 
	 * @param Integer page number.
	 * @return List<Address> addresses on given Integer page from database or empty list.
	 */
	List<Address> getAllAddresses(Integer page);
	
	
	/**
	 * 
	 * Save address to database
	 * 
	 * @param Address address for saving
	 * @return Optional<Address> if was success on saving - optional address, otherwise - optional empty.
	 */
	Optional<Address> saveAddress(Address address);
	
	/**
	 * 
	 * Update address in database
	 * 
	 * @param Address address for updating
	 * @return Optional<Address> if was success on updating - optional address, otherwise - optional empty
	 */
	Optional<Address> updateAddress(Address address);
	
	/**
	 * 
	 * Delete address in database
	 * 
	 * @param Address address for deleting
	 * @return Boolean true on success deleting, otherwise - false.
	 */
	Boolean deleteAddress(Address address);
}
