package ru.clevertec.knyazev.dto;

import java.util.Objects;

public class DiscountCardDTO {
	private String number;

	public DiscountCardDTO(String number) {
		this.number = number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiscountCardDTO other = (DiscountCardDTO) obj;
		return Objects.equals(number, other.number);
	}
}
