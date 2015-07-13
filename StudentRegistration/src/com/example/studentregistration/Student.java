package com.example.studentregistration;

import java.io.Serializable;

public class Student implements Serializable{
	
	@Override
	public String toString() {
		return "Student [name=" + name + ", email=" + email + ", proLang="
				+ proLang + ", acctState=" + acctState + ", mood=" + mood + "]";
	}
	private String name;
	private String email;
	private String proLang;
	private String acctState;
	private int mood;
	public Student(String name, String email, String proLang, String acctState,
			int mood) {
	
		this.name = name;
		this.email = email;
		this.proLang = proLang;
		this.acctState = acctState;
		this.mood = mood;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProLang() {
		return proLang;
	}
	public void setProLang(String proLang) {
		this.proLang = proLang;
	}
	public String getAcctState() {
		return acctState;
	}
	public void setAcctState(String acctState) {
		this.acctState = acctState;
	}
	public int getMood() {
		return mood;
	}
	public void setMood(int mood) {
		this.mood = mood;
	}
	
	

}
