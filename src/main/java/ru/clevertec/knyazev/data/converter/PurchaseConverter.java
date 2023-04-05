package ru.clevertec.knyazev.data.converter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.dto.ProductDTO;

public class PurchaseConverter implements Converter<Map<ProductDTO, BigDecimal>, String[]> {

	@Override
	public Map<ProductDTO, BigDecimal> convert(String[] productsData) throws ConverterException {
		Map<ProductDTO, BigDecimal> purchases = new HashMap<>();

		if ((productsData != null) && (productsData.length > 0)) {
			for (int i = 0; i < productsData.length; i++) {
				String[] productQuantiy = productsData[i].split("-");
				
				if (productQuantiy.length != 2) {
					throw new ConverterException("Incorect in format of product data! Product data should be in format: product id - product quantity. For example 8-25.635 or 14-32.");
				}

				ProductDTO productDTO = null;
				BigDecimal quantity = null;

				try {
					productDTO = new ProductDTO(convertToLong(productQuantiy[0]));
					quantity = convertToBigDecimal(productQuantiy[1]);
				} catch (NumberFormatException e) {
					throw new ConverterException("Error in purchase id or quantity. Incorrect data given!", e);
				}

				if (purchases.containsKey(productDTO)) {
					for (Map.Entry<ProductDTO, BigDecimal> purchase : purchases.entrySet()) {
						if (purchase.getKey().getId() == productDTO.getId()) {
							purchase.setValue(purchase.getValue().add(quantity));
						}
					}
				} else {
					purchases.put(productDTO, quantity);
				}
			}
		}

		return purchases;
	}
}