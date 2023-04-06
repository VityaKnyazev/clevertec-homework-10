package ru.clevertec.knyazev.service;

import ru.clevertec.knyazev.dao.CasherDAO;

public class CasherServiceImpl implements CasherService {
	private CasherDAO casherDAO;

	public CasherServiceImpl(CasherDAO casherDAO) {
		this.casherDAO = casherDAO;
	}

	@Override
	public Long showCasherId() {
		return casherDAO.getCasherId();
	}

	@Override
	public void increaseCasherId() {
		casherDAO.increaseCasherId();
	}

}
