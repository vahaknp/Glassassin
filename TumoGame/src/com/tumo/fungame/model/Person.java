package com.tumo.fungame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Person implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private String surname;
	private int gender;
	private String picture;

	private List<Nick> nicks = new ArrayList<Nick>();

	public Person() {
	}

	public Person(long id, String name, String surname, int gender,
			String picture) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.picture = picture;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public List<Nick> getNicks() {
		return nicks;
	}

	public void setNicks(List<Nick> nicks) {
		this.nicks = nicks;
	}

	public String nicksToString() {
		String ans = "";
		for (Nick nick : nicks) {
			ans += nick.toString() + "\n";
		}
		return ans;
	}

	private static class Gender {
		public static final int MAN = 1;
		public static final int WOMAN = 2;
	}
}
