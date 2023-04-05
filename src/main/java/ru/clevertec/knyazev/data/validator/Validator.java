package ru.clevertec.knyazev.data.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.clevertec.knyazev.data.exception.ValidatorException;

public interface Validator<T> {
	
	/**
	 * 
	 * @param <T>
	 * @param t value that need validation
	 * @throws ValidatorException when validation error occurred
	 */
	void validate(T t) throws ValidatorException;
	
	default boolean validateString(String t, int minStrSize, int maxStrSize) {
		return (t == null || t.length() < minStrSize || t.length() > maxStrSize) 
			   ? false 
		       : true;
	}
	
	default boolean validateEmail(String email) {
		if (email == null || email.length() < 6 || email.length() > 320) {
			return false;
		}
		
		Matcher emailMatcher = Pattern.compile("^[\\w-_]{0,}@[a-z]+\\.\\w+$").matcher(email);
		
		if (emailMatcher.matches()) {
			return true;
		}
		
		return false;
	}
	
	default boolean validateId(Long id) {
		return ((id != null && id > 0L) || id == null) 
			   ? true 
			   : false;
	}

}
