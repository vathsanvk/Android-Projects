package com.example.nprclient;

public class Topic {
	@Override
	public String toString() {
		return title;
	}

	private String title;
	private int id;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
