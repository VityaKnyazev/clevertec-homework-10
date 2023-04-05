package ru.clevertec.knyazev.dto;

import java.math.BigDecimal;

import ru.clevertec.knyazev.entity.util.Unit;

public class PurchaseDTO {
	

	private BigDecimal quantity;
	private Unit unit;
	private String description;
	private BigDecimal price;

	public PurchaseDTO(BigDecimal quantity, Unit unit, String description, BigDecimal price) {
		this.quantity = quantity;
		this.unit = unit;
		this.description = description;
		this.price = price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public Unit getUnit() {
		return unit;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getPrice() {
		return price;
	}
}