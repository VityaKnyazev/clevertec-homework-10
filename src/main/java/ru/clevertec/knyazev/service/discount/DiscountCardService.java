package ru.clevertec.knyazev.service.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.clevertec.knyazev.dao.DiscountCardDAO;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.entity.DiscountCard;
import ru.clevertec.knyazev.entity.Storage;

public class DiscountCardService extends AbstractDiscountService<DiscountCardDTO, Integer> {

	private DiscountCardDAO discountCardDAO;
	private Set<DiscountCardDTO> discountCardsDTO;

	public DiscountCardService(DiscountCardDAO discountCardDAO) {
		this.discountCardDAO = discountCardDAO;
	}
	
	public void setDiscountCardsDTO(Set<DiscountCardDTO> discountCardsDTO) {
		this.discountCardsDTO = discountCardsDTO;
	}

	@Override
	Integer calculateDiscount(Collection<DiscountCardDTO> discountCardsDTO) {
		int discountValueInPercent = 0;

		Iterator<DiscountCardDTO> discountCardDTOIterator = discountCardsDTO.iterator();

		while (discountCardDTOIterator.hasNext()) {
			DiscountCardDTO discountCardDTO = discountCardDTOIterator.next();
			String discountCardNumber = discountCardDTO.getNumber();
			if (!discountCardDAO.isDiscountCardExists(discountCardNumber)) {
				discountCardDTOIterator.remove();
			} else {
				DiscountCard discountCard = discountCardDAO.getDiscountCardByNumber(discountCardNumber);

				discountValueInPercent += discountCard.getDiscountValue();
			}
		}

		return discountValueInPercent >= MAX_DISCOUNT_CARD_VAUE ? MAX_DISCOUNT_CARD_VAUE
				: discountValueInPercent;
	}

	@Override
	Map<Long, List<Storage>> divideOnGroup(Map<Long, List<Storage>> boughtProductsInStorages) {
		Map<Long, List<Storage>> productGroupForDiscountCard = new HashMap<>();

		boughtProductsInStorages.forEach((id, storageList) -> {
			if (!storageList.get(0).getProduct().getAuction()) {
				productGroupForDiscountCard.put(id, storageList);
			}
		});

		return productGroupForDiscountCard;
	}

	@Override
	public BigDecimal applyDiscount(Map<Long, List<Storage>> boughtProductsInStorages) {
		BigDecimal totalDiscountCardValue = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

		if (discountCardsDTO == null || discountCardsDTO.isEmpty()) {
			return totalDiscountCardValue;
		}

		if (boughtProductsInStorages == null || boughtProductsInStorages.isEmpty()) {
			return totalDiscountCardValue;
		}

		Map<Long, List<Storage>> productGroups = divideOnGroup(boughtProductsInStorages);
		if (productGroups.isEmpty()) {
			return totalDiscountCardValue;
		}

		Integer discountPercent = super.calculateFinalCardsDiscount(calculateDiscount(discountCardsDTO));

		for (Map.Entry<Long, List<Storage>> listStorages : productGroups.entrySet()) {
			totalDiscountCardValue = totalDiscountCardValue.add(listStorages.getValue().stream()
					.map(product -> product.getPrice().multiply(product.getQuantity())).reduce((a, b) -> a.add(b))
					.orElse(new BigDecimal(0)).multiply(new BigDecimal(discountPercent).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
		}

		return totalDiscountCardValue;
	}

}
