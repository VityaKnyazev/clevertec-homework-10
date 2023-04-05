package ru.clevertec.knyazev.data.validator;

import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;

public class DiscountCardNumberValidator implements Validator<DiscountCardDTO>{
	private static final int LENGTH = 9;

	@Override
	public void validate(DiscountCardDTO t) throws ValidatorException {
		if (t == null || t.getNumber() == null || t.getNumber().matches("\\s{9}") || t.getNumber().length() != LENGTH) {
			throw new ValidatorException("Error in discount card length. Discount card length must be equals to " + LENGTH);
		}
	}

}