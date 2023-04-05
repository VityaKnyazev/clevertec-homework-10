package ru.clevertec.knyazev.service;

import java.math.BigDecimal;
import java.util.Map;

import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.dto.receipt.AbstractReceiptBuilder;
import ru.clevertec.knyazev.service.exception.ServiceException;

public interface PurchaseService {
	
	/**
	 *
	 * Method used for buying product from storage if product exists and product quantity present.
	 * Else can throw ServiceException.
	 * 
	 * @param purchases Map<ProductDTO, BigDecimal> that contains product data (id) in key
	 * and product quantity in map value.
	 * 
	 * @return String representation of cash receipt or string file name that contains cash receipt.
	 * 
	 * @throws ServiceException
	 */
	String buyPurchases(Map<ProductDTO, BigDecimal> purchases) throws ServiceException;
	
	/**
	 * 
	 * Method used for declaring concrete realization of AbstractReceiptBuilder that will be
	 * used in service to build a concrete cash receipt. For example simple cash receipt or
	 * pdf cash receipt or another cash receipt.
	 * 
	 * @param receiptBuilder is the implementation of concrete AbstractReceiptBuilder
	 */
	void implementReceiptBuilder(AbstractReceiptBuilder receiptBuilder);
}