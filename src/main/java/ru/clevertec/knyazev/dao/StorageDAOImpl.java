package ru.clevertec.knyazev.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;

public class StorageDAOImpl implements StorageDAO {
	private ProductDAO productDAO;
	private List<Storage> storages = new ArrayList<>();

	public StorageDAOImpl(ProductDAO productDAO) {
		this.productDAO = productDAO;
		makeStubStoragesList();
	}
	
	public List<Storage> getAll() {
		return storages;
	}

	@Override
	public Boolean isStorageExistsByProductId(Long productId) {
		return storages.stream().anyMatch(storage -> storage.getProduct().getId() == productId);
	}

	@Override
	public BigDecimal getProductQuantityInStorages(Long productId) {
		return storages.stream().filter(storage -> storage.getProduct().getId() == productId).map(storage -> storage.getQuantity())
				.reduce((a, b) -> a.add(b)).orElse(new BigDecimal(0));
	}

	@Override
	public List<Storage> getProductGroupInStoragesById(Long productId) {
		List<Storage> productGroup = new ArrayList<>();
		
		for (Storage storage : storages) {
			if (storage.getProduct().getId() == productId) {
				productGroup.add(storage);
			}
		}
		
		return productGroup;
	}

	@Override
	public Storage updateStorage(Storage storage) {
			for (int i = 0; i < storages.size(); i++) {
				if (storages.get(i).getId() == storage.getId()) {
					storages.set(i, storage);
					return storage;
				}
			}
		
		return null;
	}

	@Override
	public void deleteStorage(Long storageId) {
		Iterator<Storage> storagesIterator = storages.iterator();
		
		while (storagesIterator.hasNext()) {
			Storage storage = storagesIterator.next();
			
			if (storage.getId() == storageId) {
				storagesIterator.remove();
				break;
			}
		}
	}

	private final void makeStubStoragesList() {
		Product product1 = productDAO.getProductById(1L);
		Product product2 = productDAO.getProductById(2L);
		Product product3 = productDAO.getProductById(3L);
		Product product4 = productDAO.getProductById(4L);
		Product product5 = productDAO.getProductById(5L);
		Product product6 = productDAO.getProductById(6L);
		Product product7 = productDAO.getProductById(7L);
		Product product8 = productDAO.getProductById(8L);
		Product product10 = productDAO.getProductById(10L);
		Product product11 = productDAO.getProductById(11L);
		Product product12 = productDAO.getProductById(12L);

		storages.add(new Storage(1L, product1, Unit.t, new BigDecimal(2000), new BigDecimal(3.2)));
		storages.add(new Storage(2L, product5, Unit.pcs, new BigDecimal(4.32), new BigDecimal(6)));
		storages.add(new Storage(3L, product7, Unit.unit, new BigDecimal(52.48), new BigDecimal(2)));
		storages.add(new Storage(4L, product4, Unit.l, new BigDecimal(12.8), new BigDecimal(4)));
		storages.add(new Storage(5L, product2, Unit.pcs, new BigDecimal(25.49), new BigDecimal(2)));
		storages.add(new Storage(6L, product3, Unit.kg, new BigDecimal(7.82), new BigDecimal(12.489)));
		storages.add(new Storage(7L, product12, Unit.unit, new BigDecimal(6.21), new BigDecimal(2)));
		storages.add(new Storage(8L, product8, Unit.pcs, new BigDecimal(45.65), new BigDecimal(3)));
		storages.add(new Storage(9L, product10, Unit.g, new BigDecimal(0.05), new BigDecimal(518)));
		storages.add(new Storage(10L, product11, Unit.pcs, new BigDecimal(8.91), new BigDecimal(12)));
		storages.add(new Storage(11L, product2, Unit.pcs, new BigDecimal(21.43), new BigDecimal(1)));
		storages.add(new Storage(12L, product3, Unit.kg, new BigDecimal(6.54), new BigDecimal(5.047)));
		storages.add(new Storage(13L, product4, Unit.l, new BigDecimal(13.4), new BigDecimal(8)));
		storages.add(new Storage(14L, product5, Unit.pcs, new BigDecimal(3.99), new BigDecimal(2)));
		storages.add(new Storage(15L, product8, Unit.pcs, new BigDecimal(45.99), new BigDecimal(3)));
		storages.add(new Storage(16L, product6, Unit.pcs, new BigDecimal(45.65), new BigDecimal(3)));
		storages.add(new Storage(17L, product4, Unit.l, new BigDecimal(8.1), new BigDecimal(2)));
		storages.add(new Storage(18L, product3, Unit.kg, new BigDecimal(6.98), new BigDecimal(2.113)));
		storages.add(new Storage(19L, product3, Unit.kg, new BigDecimal(8.21), new BigDecimal(8.653)));
		storages.add(new Storage(20L, product12, Unit.unit, new BigDecimal(8.12), new BigDecimal(4)));
		storages.add(new Storage(21L, product1, Unit.t, new BigDecimal(3200), new BigDecimal(0.2)));
	}

}
