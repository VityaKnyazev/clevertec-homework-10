package ru.clevertec.knyazev.dto.converter;

import java.util.function.Function;

public abstract class AbstractDTOConverter<T, U> {
	private Function<T, U> functionToDTO;
	private Function<U, T> functionFromDTO;
	
	AbstractDTOConverter(Function<T, U> functionToDTO) {
		this.functionToDTO = functionToDTO;
	}
	
	AbstractDTOConverter(Function<T, U> functionToDTO, Function<U, T> functionFromDTO) {
		this.functionToDTO = functionToDTO;
		this.functionFromDTO = functionFromDTO;
	}
	
	public U convertToDTO(T convertingObject) {
		return functionToDTO.apply(convertingObject);
	}
	
	public T convertFromDTO(U dtoObject) {
		return functionFromDTO.apply(dtoObject);
	}
}
