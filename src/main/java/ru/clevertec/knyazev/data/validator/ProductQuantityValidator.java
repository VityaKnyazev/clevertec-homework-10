package ru.clevertec.knyazev.data.validator;

import java.math.BigDecimal;

import ru.clevertec.knyazev.data.exception.ValidatorException;

public class ProductQuantityValidator implements Validator<BigDecimal>{
	private final static BigDecimal MIN_QUANTITY = new BigDecimal("0");

	@Override
	public void validate(BigDecimal quantity) throws ValidatorException {
		if (quantity == null || (quantity.compareTo(MIN_QUANTITY) == -1) || (quantity.compareTo(MIN_QUANTITY) == 0)) {
			throw new ValidatorException("Error in given product quantity=" + quantity + " Quantity value must be above 0");
		}
	}
}
