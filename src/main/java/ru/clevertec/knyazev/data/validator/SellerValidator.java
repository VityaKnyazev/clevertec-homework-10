package ru.clevertec.knyazev.data.validator;

import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.entity.Seller;
import ru.clevertec.knyazev.entity.util.Role;

public class SellerValidator implements Validator<Seller> {
	public static final String ERROR_MESSAGE = """			
			 Error on validating input Seller.
			 Fields must be: id > 0L or null,
			 name should contain from 2 to 30 symbols,
			 familyName should contain from 2 to 40 symbols, 
			 email not null and contains from 6 to 320 symbols like knyazev_vitya@mail.ru, 
			 role should be not null and equals to manager or cashier or consultant""";

	@Override
	public void validate(Seller t) throws ValidatorException {
		if (!validateId(t.getId()) || !validateString(t.getName(), 2, 30) || !validateString(t.getFamilyName(), 2, 40)
				|| !validateEmail(t.getEmail()) || !validateRole(t.getRole())) {
			throw new ValidatorException(ERROR_MESSAGE);
					
		}
	}

	private boolean validateRole(Role role) {
		return role == null 
			   ? false 
			   : true;
	}

}
