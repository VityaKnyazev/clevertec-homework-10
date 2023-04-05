package ru.clevertec.knyazev.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "address")
@Builder
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name = "postal_code", nullable = false, length = 9)
	String postalCode;

	@Column(nullable = false, length = 30)
	String country;

	@Column(nullable = false, length = 30)
	String city;

	@Column(nullable = false, length = 30)
	String street;

	@Column(name = "building_number", nullable = false, length = 7)
	String buildingNumber;

	public Address() {
	}

	public Address(Long id, String postalCode, String country, String city, String street, String buildingNumber) {
		this.id = id;
		this.postalCode = postalCode;
		this.country = country;
		this.city = city;
		this.street = street;
		this.buildingNumber = buildingNumber;
	}

	public Long getId() {
		return id;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}
	
	@Override
	public String toString() {
		return postalCode + ", " + country + ", " + city + ", "
				+ street + ", " + buildingNumber;
	}
}
