package ru.clevertec.knyazev.entity;

import lombok.Builder;

@Builder
public class Casher {
	private Long id;

	public Casher(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
