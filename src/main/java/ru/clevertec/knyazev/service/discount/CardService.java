package ru.clevertec.knyazev.service.discount;

import java.util.List;
import java.util.Optional;

import ru.clevertec.knyazev.entity.DiscountCard;
import ru.clevertec.knyazev.service.exception.ServiceException;

/**
 * 
 * CardService service interface for adding CRUD operations to
 * DiscountCardService.
 * 
 * @author Vitya Knyazev
 *
 */
public interface CardService {
	Optional<DiscountCard> showDiscountCard(Long id);

	public List<DiscountCard> showAllDiscountCards();

	public List<DiscountCard> showAllDiscountCards(Integer page, Integer pageSize);

	public List<DiscountCard> showAllDiscountCards(Integer page);

	public DiscountCard addDiscountCard(DiscountCard discountCard) throws ServiceException;

	public DiscountCard changeDiscountCard(DiscountCard discountCard) throws ServiceException;

	public void removeDiscountCard(DiscountCard discountCard) throws ServiceException;
}
