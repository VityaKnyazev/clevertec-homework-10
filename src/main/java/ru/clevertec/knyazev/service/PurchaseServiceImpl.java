package ru.clevertec.knyazev.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import ru.clevertec.knyazev.dao.connection.AppConnectionConfig;
import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.dto.PurchaseDTO;
import ru.clevertec.knyazev.dto.receipt.AbstractReceiptBuilder;
import ru.clevertec.knyazev.entity.Shop;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.service.discount.DiscountServiceComposite;
import ru.clevertec.knyazev.service.exception.ServiceException;

public class PurchaseServiceImpl implements PurchaseService {
	private StorageService storageServiceImpl;
	private CasherService casherServiceImpl;
	private ShopService shopServiceImpl;
	private AbstractReceiptBuilder receiptBuilder;

	public PurchaseServiceImpl(StorageService storageServiceImpl, CasherService casherServiceImpl,
			ShopService shopServiceImpl) {
		this.storageServiceImpl = storageServiceImpl;
		this.casherServiceImpl = casherServiceImpl;
		this.shopServiceImpl = shopServiceImpl;
		receiptBuilder = AbstractReceiptBuilder.createDefaultReceiptBuilder();
	}

	@Override
	public void implementReceiptBuilder(AbstractReceiptBuilder receiptBuilder) {
		this.receiptBuilder = receiptBuilder;
	}

	@Override
	public String buyPurchases(Map<ProductDTO, BigDecimal> productsDTO) throws ServiceException {
		if (productsDTO == null || productsDTO.isEmpty())
			throw new ServiceException("Error when buying purchases. Products are null or empty!");
		// Получаем купленные товары.
		Map<Long, List<Storage>> boughtProductsInStorages = new HashMap<>();

		EntityManager entityManager = AppConnectionConfig.getInstance().getEntityManager();
		try {
			entityManager.getTransaction().begin();

			for (Map.Entry<ProductDTO, BigDecimal> productsDTOQuantities : productsDTO.entrySet()) {
				ProductDTO productDTO = productsDTOQuantities.getKey();
				BigDecimal quantity = productsDTOQuantities.getValue();

				List<Storage> groupProductInStorages = storageServiceImpl.buyProductFromStorages(productDTO.getId(),
						quantity);
				if (!groupProductInStorages.isEmpty()) {
					boughtProductsInStorages.put(productDTO.getId(), groupProductInStorages);
				}
			}

			entityManager.getTransaction().commit();
		} catch (ServiceException e) {
			entityManager.getTransaction().rollback();
			throw e;
		} finally {
			entityManager.close();
		}

		if (boughtProductsInStorages.isEmpty())
			throw new ServiceException("Nothing to buy on current products ids and count!");

		// Расчет скидок. Делим товары на группы для расчета и применения скидок.
		BigDecimal totalProductGroupsDiscount = DiscountServiceComposite.getInstance()
				.getTotalProductGroupsDiscount(boughtProductsInStorages);
		BigDecimal totalCardsDiscount = DiscountServiceComposite.getInstance()
				.getTotalCardsDiscount(boughtProductsInStorages);

		BigDecimal totalPrice = storageServiceImpl.getBoughtProductsTotalPrice(boughtProductsInStorages).setScale(2,
				RoundingMode.HALF_UP);

		List<PurchaseDTO> purchases = new ru.clevertec.knyazev.dto.converter.PurchaseConverter()
				.convertToDTO(boughtProductsInStorages);

		Long shopId = 1L;
		Shop shop = shopServiceImpl.showShop(shopId).get();

		Long casherId = casherServiceImpl.showCasherId();
		casherServiceImpl.increaseCasherId();

		BigDecimal totalDiscountPrice = totalPrice.subtract(totalProductGroupsDiscount).subtract(totalCardsDiscount)
				.setScale(2, RoundingMode.HALF_UP);

		return receiptBuilder.setCasherIdWithDateTime(casherId).setShop(shop).setPurchases(purchases)
				.setTotalPrice(totalPrice).setProductGroupsDiscountValue(totalProductGroupsDiscount)
				.setDiscountCardsValue(totalCardsDiscount).setTotalDiscountPrice(totalDiscountPrice).build();

	}

}
