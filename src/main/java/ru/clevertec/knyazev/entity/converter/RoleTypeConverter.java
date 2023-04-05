package ru.clevertec.knyazev.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.clevertec.knyazev.entity.util.Role;

@Converter
public class RoleTypeConverter implements AttributeConverter<Role, String> {

	@Override
	public String convertToDatabaseColumn(Role role) {
		if (role == null) {
			throw new IllegalArgumentException("null not supported.");
		}
		
		switch (role) {
		case cashier :
			return Role.cashier.name();
		case consultant :
			return Role.consultant.name();
		case manager :
			return Role.manager.name();
		default:
			throw new IllegalArgumentException(role.name() + " not supported.");
		}		
		
	}

	@Override
	public Role convertToEntityAttribute(String dbRole) {
		if (dbRole == null) {
			throw new IllegalArgumentException("null not supported.");
		}
		
		if (dbRole.equals(Role.cashier.name())) {
			return Role.cashier;
		} else if (dbRole.equals(Role.consultant.name())) {
			return Role.consultant;
		}  else if (dbRole.equals(Role.manager.name())) {
			return Role.manager;
		} else {
			throw new IllegalArgumentException(dbRole + " not supported.");
		}	
	}

}
