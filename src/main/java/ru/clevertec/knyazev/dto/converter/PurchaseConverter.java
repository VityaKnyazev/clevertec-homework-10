package ru.clevertec.knyazev.dto.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.clevertec.knyazev.dto.PurchaseDTO;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;

public class PurchaseConverter extends AbstractDTOConverter<Map<Long, List<Storage>>, List<PurchaseDTO>> {

	public PurchaseConverter() {
		super(PurchaseConverter::convertFromBoughtProductsToPurchasesDTO);
	}
	
	private static final List<PurchaseDTO> convertFromBoughtProductsToPurchasesDTO(Map<Long, List<Storage>> groupsProducts) {
		List<PurchaseDTO> purchasesDTO = new ArrayList<>();
		
		for (Map.Entry<Long, List<Storage>> productGroup : groupsProducts.entrySet()) {
			for (int i = 0; i < productGroup.getValue().size(); i++) {
				List<Storage> productInStorages = productGroup.getValue();				
				String description = productInStorages.get(i).getProduct().getDescription();
				Unit unit = productInStorages.get(i).getUnit();
				BigDecimal price = productInStorages.get(i).getPrice();
				BigDecimal quantity = productInStorages.get(i).getQuantity();
				
				PurchaseDTO purchaseDTO = new PurchaseDTO(quantity, unit, description, price);
				purchasesDTO.add(purchaseDTO);
			}
		}
		
		return purchasesDTO;
	}
}
