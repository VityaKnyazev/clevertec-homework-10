package ru.clevertec.knyazev.dao;

import ru.clevertec.knyazev.entity.Casher;

public class CasherDaoImpl implements CasherDAO {
	private Casher casher = new Casher(1L);

	@Override
	public Long getCasherId() {
		return casher.getId();
	}

	@Override
	public void increaseCasherId() {
		Long currentId = casher.getId();
		casher.setId(++currentId);
	}
	
}
