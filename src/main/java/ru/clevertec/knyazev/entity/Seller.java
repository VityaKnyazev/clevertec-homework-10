package ru.clevertec.knyazev.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.clevertec.knyazev.entity.converter.RoleTypeConverter;
import ru.clevertec.knyazev.entity.util.Role;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "seller")
public class Seller {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(name = "family_name", nullable = false, length = 40)
	private String familyName;

	@Column(nullable = false, length = 30)
	private String email;

	@Column(nullable = false)
	@Convert(converter = RoleTypeConverter.class)
	private Role role;

	@Override
	public String toString() {
		return "{\"id\":" + id + ",\"name\":" + "\"" + name + "\"" + ",\"familyName\":" + "\"" + familyName + "\"" + ",\"email\":" + "\"" + email + "\"" + ",\"role\":" + "\"" + role + "\"" + "}";
	}
	
	public String toXML() {
		return "<seller>" + System.lineSeparator()
				+ "<id>" + id + "</id>" + System.lineSeparator()
				+ "<name>" + name + "</name>" + System.lineSeparator()
				+ "<familyName>" + familyName + "</familyName>" + System.lineSeparator() 
				+ "<email>" + email + "</email>" + System.lineSeparator()
				+ "<role>" + role.name() + "</role>" + System.lineSeparator()
				+ "</seller>";
	}
}
