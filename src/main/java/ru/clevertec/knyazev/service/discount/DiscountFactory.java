package ru.clevertec.knyazev.service.discount;

import java.util.Set;

import ru.clevertec.knyazev.dao.DiscountCardDAOImpl;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.service.discount.DiscountService.Group;
import ru.clevertec.knyazev.service.exception.ServiceException;

public class DiscountFactory {
	private static DiscountFactory discountFactory;

	private DiscountFactory() {
	}

	private DiscountService createDiscountService(Group group) throws ServiceException {
		return switch (group) {
		case DISCOUNT_PRODUCT_GROUP -> new DiscountProductGroupService();
		default -> throw new ServiceException("Error occured when choosing discount type policy!");
		};
	}

	public DiscountService createDiscountService(Group group, Set<DiscountCardDTO> discountCardsDTO)
			throws ServiceException {

		if (discountCardsDTO != null) {
			return switch (group) {
			case DISCOUNT_CARD_GROUP -> { 
				DiscountCardService discountCardService = new DiscountCardService(new DiscountCardDAOImpl());
				discountCardService.setDiscountCardsDTO(discountCardsDTO);
				yield discountCardService;
			}
			default -> throw new ServiceException("Error occured when choosing discount card type policy!");
			};
		}

		return createDiscountService(group);
	}

	public static DiscountFactory getInstance() {
		if (discountFactory == null) {
			discountFactory = new DiscountFactory();
		}

		return discountFactory;
	}
}
