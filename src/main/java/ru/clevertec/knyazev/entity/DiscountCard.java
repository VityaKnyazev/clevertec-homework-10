package ru.clevertec.knyazev.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "discount_card")
@Builder
public class DiscountCard implements Comparable<DiscountCard> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 9, unique = true)
	private String number;

	@Column(name = "discount_value", nullable = false)
	private int discountValue;

	public DiscountCard() {
	}

	public DiscountCard(Long id, String number, Integer discountValue) {
		this.id = id;
		this.number = number;
		this.discountValue = discountValue;
	}

	public Long getId() {
		return id;
	}

	public String getNumber() {
		return number;
	}

	public Integer getDiscountValue() {
		return discountValue;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setDiscountValue(Integer discountValue) {
		this.discountValue = discountValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(discountValue, id, number);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (getClass() != obj.getClass()) {
			return false;
		}

		DiscountCard other = (DiscountCard) obj;
		return discountValue == other.discountValue && Objects.equals(id, other.id)
				&& Objects.equals(number, other.number);
	}

	@Override
	public String toString() {
		return "DiscountCard [id=" + id + ", number=" + number + ", discountValue=" + discountValue + "]";
	}

	@Override
	public int compareTo(DiscountCard anotherDiscountCard) {
		return number.compareTo(anotherDiscountCard.getNumber());
	}

}