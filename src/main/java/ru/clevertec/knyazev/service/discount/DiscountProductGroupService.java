package ru.clevertec.knyazev.service.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.clevertec.knyazev.entity.Storage;

public class DiscountProductGroupService extends AbstractDiscountService<Storage, BigDecimal>{

	@Override
	BigDecimal calculateDiscount(Collection<Storage> storeagesGroupProduct) {
		BigDecimal discountSumValue = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
		
		BigDecimal realProductGroupQuantity = new BigDecimal(0).setScale(3, RoundingMode.HALF_UP);
		
		for (Storage storage : storeagesGroupProduct) {
			realProductGroupQuantity = realProductGroupQuantity.add(storage.getQuantity());
		}
		
		if ((realProductGroupQuantity.compareTo(PRODUCT_QUANTITY_FOR_DISCOUNT) == 0) || (realProductGroupQuantity.compareTo(PRODUCT_QUANTITY_FOR_DISCOUNT) == 1)) {
			for (Storage storage : storeagesGroupProduct) {
				discountSumValue = discountSumValue.add(storage.getPrice().multiply(storage.getQuantity()));
			}
			
			discountSumValue = discountSumValue.multiply(new BigDecimal(DISCOUNT_VALUE_PERCENT_FOR_PRODUCT_GROUP)).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
		}
		
		return discountSumValue;
	}

	@Override
	Map<Long, List<Storage>> divideOnGroup(Map<Long, List<Storage>> boughtProductsInStorages) {
		Map<Long, List<Storage>> productGroupForDiscountOnQuantity = new HashMap<>();
		
		boughtProductsInStorages.forEach((id, storageList) -> {
			if (storageList.get(0).getProduct().getAuction()) {
				productGroupForDiscountOnQuantity.put(id, storageList);
			}
		});
		
		return productGroupForDiscountOnQuantity;
	}

	@Override
	public BigDecimal applyDiscount(Map<Long, List<Storage>> boughtProductsInStorages) {
		BigDecimal totalDiscountProductGroupValue = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
		
		if (boughtProductsInStorages == null || boughtProductsInStorages.isEmpty()) {
			return totalDiscountProductGroupValue;
		}
		
		Map<Long, List<Storage>> productGroups = divideOnGroup(boughtProductsInStorages);
		
		if (productGroups.isEmpty()) return totalDiscountProductGroupValue;
		
		
		for (Map.Entry<Long, List<Storage>> productGroup : productGroups.entrySet()) {
			totalDiscountProductGroupValue = totalDiscountProductGroupValue.add(calculateDiscount(productGroup.getValue()));
		}
		
		return totalDiscountProductGroupValue;
	}

}
