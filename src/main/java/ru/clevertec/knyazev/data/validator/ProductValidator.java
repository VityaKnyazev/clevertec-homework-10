package ru.clevertec.knyazev.data.validator;

import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.ProductDTO;

public class ProductValidator implements Validator<ProductDTO> {
	private static final Long ID_MIN_VALUE = 1L;

	@Override
	public void validate(ProductDTO productDTO) throws ValidatorException {
		if (productDTO == null || productDTO.getId() == null ||  (productDTO.getId() < ID_MIN_VALUE)) {
			Long id = (productDTO == null) ? null : productDTO.getId();
			throw new ValidatorException("Error in id value. id=" + id + ". Id must be equals or above 1");
		}
	}

}