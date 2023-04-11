package ru.clevertec.knyazev.service;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.dao.AddressDAO;
import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.service.exception.ServiceException;

public class AddressServiceImpl implements AddressService {
	private AddressDAO addressDAO;
	
	public AddressServiceImpl(AddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}

	@Override
	public Optional<Address> showAddress(Long id) {
		return addressDAO.getAddressById(id);
	}
	
	@Override
	public List<Address> showAllAddresses() {
		return addressDAO.getAllAddresses();
	}
	
	@Override
	public List<Address> showAllAddresses(Integer page, Integer pageSize) {
		return addressDAO.getAllAddresses(page, pageSize);
	}
	
	@Override
	public List<Address> showAllAddresses(Integer page) {
		return addressDAO.getAllAddresses(page);
	}
	
	@Override
	public Address addAddress(Address address) throws ServiceException {
		Optional<Address> savedAddressWrap = addressDAO.saveAddress(address);
		
		if (savedAddressWrap.isEmpty()) {
			throw new ServiceException("Error on adding shop!");
		} else {
			return savedAddressWrap.get();
		}
	}

	@Override
	public Address changeAddress(Address address) throws ServiceException {
		Optional<Address> updatedAddressWrap = addressDAO.updateAddress(address);
		
		if (updatedAddressWrap.isEmpty()) {
			throw new ServiceException("Error on changing shop!");
		} else {
			return updatedAddressWrap.get();
		}
	}

	@Override
	public void removeAddress(Address address) throws ServiceException {
		Boolean result = addressDAO.deleteAddress(address);
		
		if (!result) {
			throw new ServiceException("Error on deleting seller!");
		}
	}	

}
