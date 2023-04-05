package ru.clevertec.knyazev.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "shop")
@Builder
public class Shop {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id")
	private Address address;

	@Column(nullable = false, length = 18)
	private String phone;

	public Shop() {
	}

	public Shop(Long id, String name, Address address, String phone) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.phone = phone;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
