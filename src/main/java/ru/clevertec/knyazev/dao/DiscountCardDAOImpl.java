package ru.clevertec.knyazev.dao;

import java.util.ArrayList;
import java.util.List;

import ru.clevertec.knyazev.entity.DiscountCard;

public class DiscountCardDAOImpl implements DiscountCardDAO {
	private final static List<DiscountCard> DISCOUNT_CARDS = new ArrayList<>() {
		private static final long serialVersionUID = -3322679246236323540L;

		{
			add(new DiscountCard(1L, "15fd20181", 1));
			add(new DiscountCard(2L, "15fd20182", 3));
			add(new DiscountCard(3L, "15fd20183", 4));
			add(new DiscountCard(4L, "15fd20184", 2));
			add(new DiscountCard(5L, "15fd20185", 5));
			add(new DiscountCard(6L, "15fd20186", 1));
			add(new DiscountCard(7L, "15fd20187", 3));
			add(new DiscountCard(8L, "15fd20188", 6));
			add(new DiscountCard(9L, "15fd20189", 7));
			add(new DiscountCard(10L, "15fd20190", 8));
			add(new DiscountCard(11L, "15fd20191", 5));
			add(new DiscountCard(12L, "15fd20192", 1));
		}
	};

	@Override
	public DiscountCard getDiscountCardByNumber(String number) {
		for (DiscountCard discountCard : DISCOUNT_CARDS) {
			if (discountCard.getNumber().equals(number)) {
				return discountCard;
			}
		}

		return null;
	}

	@Override
	public DiscountCard saveDiscountCard(DiscountCard discountCard) {
		discountCard.setId(DISCOUNT_CARDS.size() + 1L);
		
		DISCOUNT_CARDS.add(discountCard);
		return discountCard;
	}

	@Override
	public boolean isDiscountCardExists(String discountCardNumber) {
		for (DiscountCard discountCard : DISCOUNT_CARDS) {
			if (discountCard.getNumber().equals(discountCardNumber)) {
				return true;
			}
		}

		return false;
	}

}
