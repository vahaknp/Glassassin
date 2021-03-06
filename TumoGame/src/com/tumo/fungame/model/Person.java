package com.tumo.fungame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

public class Person implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String dbName;
	private long id;
	private String name;
	private String surname;
	private int gender;
	private String picture;
	private String location;

	private List<Nick> nicks;

	public Person() {
		nicks = new ArrayList<Nick>();
	}

	public Person(String dbName, long id, String name, String surname,
			int gender, String picture, String location) {
		super();
		this.dbName = dbName;
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.picture = picture;
		this.location = location;
		nicks = new ArrayList<Nick>();
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public Pair<String, String> beautify() {
		String res1 = (getGender() == 1 ? "Male" : "Female") + "\n";
		String res2 = getLocation() + "\n";

		for (int i = 0; i < nicks.size(); i++) {
			if (i % 2 == 0)
				res1 += nicks.get(i).toString() + "\n";
			else
				res2 += nicks.get(i).toString() + "\n";
		}

		return new Pair<String, String>(res1, res2);
	}

	public String beautifyCard() {
		String res1 = (getGender() == 1 ? "Male" : "Female") + "\n"
				+ getLocation() + "\n";

		for (int i = 0; i < nicks.size(); i++) {
			res1 += nicks.get(i).toString() + "\n";
		}
		return res1;
	}

	public static class Gender {
		public static final int MAN = 1;
		public static final int WOMAN = 2;
	}
}
