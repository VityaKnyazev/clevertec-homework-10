package ru.clevertec.knyazev.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.clevertec.knyazev.entity.util.Unit;

@Converter
public class UnitTypeConverter implements AttributeConverter<Unit, String> {

	@Override
	public String convertToDatabaseColumn(Unit unit) {
		if (unit == null) {
			throw new IllegalArgumentException("null not supported.");
		}
		
		switch (unit) {
		case g :
			return Unit.g.name();
		case kg :
			return Unit.kg.name();
		case unit :
			return Unit.unit.name();
		case t :
			return Unit.t.name();
		case pcs :
			return Unit.pcs.name();
		case l :
			return Unit.l.name();
		default:
			throw new IllegalArgumentException(unit.name() + " not supported.");
		}		
		
	}

	@Override
	public Unit convertToEntityAttribute(String dbUnit) {
		if (dbUnit == null) {
			throw new IllegalArgumentException("null not supported.");
		}
		
		if (dbUnit.equals(Unit.g.name())) {
			return Unit.g;
		} else if (dbUnit.equals(Unit.kg.name())) {
			return Unit.kg;
		}  else if (dbUnit.equals(Unit.unit.name())) {
			return Unit.unit;
		}  else if (dbUnit.equals(Unit.t.name())) {
			return Unit.t;
		}  else if (dbUnit.equals(Unit.pcs.name())) {
			return Unit.pcs;
		}  else if (dbUnit.equals(Unit.l.name())) {
			return Unit.l;
		} else {
			throw new IllegalArgumentException(dbUnit + " not supported.");
		}	
	}

}
