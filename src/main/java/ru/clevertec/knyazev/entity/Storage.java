package ru.clevertec.knyazev.entity;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import ru.clevertec.knyazev.entity.converter.UnitTypeConverter;
import ru.clevertec.knyazev.entity.util.Unit;

@Entity
@Table(name = "storage")
@Builder
public class Storage implements Comparable<Storage> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(nullable = false)
	@Convert(converter = UnitTypeConverter.class)
	private Unit unit;

	@Column(nullable = false, precision = 9, scale = 3)
	private BigDecimal price;

	@Column(nullable = false, precision = 11, scale = 2)
	private BigDecimal quantity;

	public Storage() {
	}

	public Storage(Long id, Product product, Unit unit, BigDecimal price, BigDecimal quantity) {
		this.id = id;
		this.product = product;
		this.unit = unit;
		this.price = price;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public Product getProduct() {
		return product;
	}

	public Unit getUnit() {
		return unit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, price, product, quantity, unit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Storage other = (Storage) obj;
		return Objects.equals(id, other.id) && Objects.equals(price, other.price)
				&& Objects.equals(product, other.product) && Objects.equals(quantity, other.quantity)
				&& unit == other.unit;
	}

	@Override
	public int compareTo(Storage o) {
		return price.compareTo(o.getPrice());
	}
}
