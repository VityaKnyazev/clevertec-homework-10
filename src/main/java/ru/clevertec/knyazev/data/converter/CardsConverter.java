package ru.clevertec.knyazev.data.converter;

import java.util.HashSet;
import java.util.Set;

import ru.clevertec.knyazev.dto.DiscountCardDTO;

/**
 * 
 * Correct data for CardsConverter is data that starts with card-
 * Like data ["card- card-f card-1 card-dsfs card-1222222222215"] is correct
 * and be added to Set<DiscountCardDTO>.
 * 
 * @author Vitya Knyazev
 *
 */
public class CardsConverter implements Converter<Set<DiscountCardDTO>, String[]> {
	private static final String CARD_PREFIX = "card-";

	@Override
	public Set<DiscountCardDTO> convert(String[] cardsData) {
		Set<DiscountCardDTO> discountCardsDTO = new HashSet<>();

		if ((cardsData != null) && (cardsData.length > 0)) {
			
			for (int i = 0; i < cardsData.length; i++) {
				if (cardsData[i] == null) continue;
				if (cardsData[i].contains(CARD_PREFIX) && cardsData[i].startsWith(CARD_PREFIX)) {
					String cardNumber = cardsData[i].substring(CARD_PREFIX.length());
					discountCardsDTO.add(new DiscountCardDTO(cardNumber));
				}
			}
		}

		return discountCardsDTO;
	}
}