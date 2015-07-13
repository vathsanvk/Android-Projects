package com.example.selectiongame;

public class Item {
	
	String name;
	int draw_id;
	public Item(String name, int draw_id) {
		
		this.name = name;
		this.draw_id = draw_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDraw_id() {
		return draw_id;
	}
	public void setDraw_id(int draw_id) {
		this.draw_id = draw_id;
	}
	

}
