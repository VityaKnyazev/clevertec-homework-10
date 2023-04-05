package ru.clevertec.knyazev.service;

import ru.clevertec.knyazev.dao.CasherDAO;

public class CasherServiceImpl implements CasherService {
	private CasherDAO casherDAOJPA;

	public CasherServiceImpl(CasherDAO casherDAOJPA) {
		this.casherDAOJPA = casherDAOJPA;
	}

	@Override
	public Long showCasherId() {
		return casherDAOJPA.getCasherId();
	}

	@Override
	public void increaseCasherId() {
		casherDAOJPA.increaseCasherId();
	}

}
