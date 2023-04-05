package ru.clevertec.knyazev.data;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ru.clevertec.knyazev.data.converter.CardsConverter;
import ru.clevertec.knyazev.data.converter.Converter;
import ru.clevertec.knyazev.data.converter.PurchaseConverter;
import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.data.validator.DiscountCardNumberValidator;
import ru.clevertec.knyazev.data.validator.ProductQuantityValidator;
import ru.clevertec.knyazev.data.validator.ProductValidator;
import ru.clevertec.knyazev.data.validator.Validator;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.dto.ProductDTO;

public class ConverterValidatorDataReaderWriter extends DataReaderWriterDecorator {
	private Map<String[], String[]> inputData;

	public ConverterValidatorDataReaderWriter(DataReader dataReader, DataWriter dataWriter) {
		super(dataReader, dataWriter);
	}

	@Override
	public Map<ProductDTO, BigDecimal> readPurchases() throws IOException, ConverterException, ValidatorException {
		fetchInputData();
		String[] purchaseData = inputData.keySet().iterator().next();

		Converter<Map<ProductDTO, BigDecimal>, String[]> purchasesConverter = new PurchaseConverter();
		Map<ProductDTO, BigDecimal> purchases = purchasesConverter.convert(purchaseData);

		Validator<ProductDTO> productValidator = new ProductValidator();
		Validator<BigDecimal> productQuantityValidator = new ProductQuantityValidator();

		for (Map.Entry<ProductDTO, BigDecimal> purchase : purchases.entrySet()) {
			productValidator.validate(purchase.getKey());
			productQuantityValidator.validate(purchase.getValue());
		}

		return purchases;
	}

	@Override
	public Set<DiscountCardDTO> readDiscountCards() throws IOException, ConverterException {
		fetchInputData();
		String[] discountCardsData = inputData.values().iterator().next();

		Converter<Set<DiscountCardDTO>, String[]> discountCardsConverter = new CardsConverter();
		Set<DiscountCardDTO> discauntCards = discountCardsConverter.convert(discountCardsData);

		Validator<DiscountCardDTO> discauntCardsValidator = new DiscountCardNumberValidator();

		Iterator<DiscountCardDTO> iterator = discauntCards.iterator();

		while (iterator.hasNext()) {
			try {
				discauntCardsValidator.validate(iterator.next());
			} catch (ValidatorException e) {
				//logger.error(e);
				iterator.remove();
			}
		}

		return discauntCards;
	}

	private Map<String[], String[]> fetchInputData() throws IOException {

		if (inputData == null) {
			inputData = readData();
		}

		return inputData;
	}

}