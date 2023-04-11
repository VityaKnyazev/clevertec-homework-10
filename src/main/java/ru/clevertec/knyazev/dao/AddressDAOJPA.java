package ru.clevertec.knyazev.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.entity.Address;

public class AddressDAOJPA implements AddressDAO {
	private static final Integer PAGE_EL_LIMIT = 20;

	private static final Logger logger = LoggerFactory.getLogger(AddressDAOJPA.class);

	private AppConnectionConfig appConnectionConfig;

	public AddressDAOJPA() {
	}

	public AddressDAOJPA(AppConnectionConfig appConnectionConfig) {
		this.appConnectionConfig = appConnectionConfig;
	}

	@Override
	public Optional<Address> getAddressById(Long id) {
		Optional<Address> addressWrap = Optional.empty();

		if (id != null && id > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				Address dbAddress = entityManager.find(Address.class, id);
				if (dbAddress != null) {
					addressWrap = Optional.of(dbAddress);
				}
			} catch (IllegalArgumentException e) {
				logger.error("Error when getting address: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		}

		return addressWrap;
	}

	@Override
	public List<Address> getAllAddresses() {
		List<Address> addresses = new ArrayList<>();

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			addresses = entityManager.createQuery("SELECT a FROM Address a", Address.class).getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all addresses: {}", e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return addresses;
	}
	
	@Override
	public List<Address> getAllAddresses(Integer page, Integer elementsOnPage) {
		List<Address> addresses = new ArrayList<>();

		if ((page == null || page < 1) && (elementsOnPage == null || elementsOnPage < 1)) {
			return addresses;
		}

		Integer offsset = (page - 1) * elementsOnPage;

		EntityManager entityManager = appConnectionConfig.getEntityManager();
		try {
			addresses = entityManager.createQuery("SELECT a FROM Address a", Address.class).setFirstResult(offsset)
					.setMaxResults(elementsOnPage).getResultList();
		} catch (IllegalArgumentException | PersistenceException e) {
			logger.error("Error when getting all addresses on page={}: {}", page, e.getMessage(), e);
		} finally {
			entityManager.close();
		}

		return addresses;
	}
	
	@Override
	public List<Address> getAllAddresses(Integer page) {
		return getAllAddresses(page, PAGE_EL_LIMIT);
	}

	@Override
	public Optional<Address> saveAddress(Address address) {
		Optional<Address> addressWrap = Optional.empty();

		if (address != null && address.getId() == null) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				entityManager.persist(address);
				entityManager.getTransaction().commit();

				addressWrap = Optional.of(address);
			} catch (IllegalStateException | PersistenceException e) {
				entityManager.getTransaction().rollback();
				logger.error("Error when saving address: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Error saving on invalid address: {}", address);
		}

		return addressWrap;
	}

	@Override
	public Optional<Address> updateAddress(Address address) {
		Optional<Address> addressWrap = Optional.empty();

		if (address != null && address.getId() != null && address.getId() > 0L) {
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				Address addressDB = entityManager.find(Address.class, address.getId());

				if (addressDB != null) {
					addressDB.setPostalCode(address.getPostalCode());
					addressDB.setCountry(address.getCountry());
					addressDB.setCity(address.getCity());
					addressDB.setStreet(address.getStreet());
					addressDB.setBuildingNumber(address.getBuildingNumber());

					entityManager.getTransaction().commit();

					addressWrap = Optional.of(address);
				} else {
					entityManager.getTransaction().rollback();
					logger.error("Error on updating. Address not exists on given id={}", address.getId());
				}
				
			} catch (IllegalStateException | PersistenceException e) {
				entityManager.getTransaction().rollback();
				logger.error("Error when updating address: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Error updating on invalid address: {}", address);
		}

		return addressWrap;
	}

	@Override
	public Boolean deleteAddress(Address address) {
		Boolean result = false;
		
		if (address != null && address.getId() != null && address.getId() > 0L) {			
			
			EntityManager entityManager = appConnectionConfig.getEntityManager();

			try {
				entityManager.getTransaction().begin();
				Address dbAddress = entityManager.find(Address.class, address.getId());

				if (dbAddress != null) {
					entityManager.remove(dbAddress);
					entityManager.getTransaction().commit();
					
					result = true;
				} else {
					entityManager.getTransaction().rollback();
					logger.error("Address not exists with given id=" + address.getId());
				}
			} catch (PersistenceException | IllegalArgumentException e) {
				entityManager.getTransaction().rollback();
				logger.error("Error on deleting address: {}", e.getMessage(), e);
			} finally {
				entityManager.close();
			}
		} else {
			logger.error("Given invalid address={} for deleting", address);
		}
		
		return result;
	}

}
