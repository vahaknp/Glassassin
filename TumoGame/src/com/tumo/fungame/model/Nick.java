package com.tumo.fungame.model;

import java.io.Serializable;

public class Nick implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String dbName;
	private long id;
	private String name;

	public Nick() {
	}

	public Nick(String dbName, long id, String name) {
		this.dbName = dbName;
		this.id = id;
		this.name = name;
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

	@Override
	public String toString() {
		return name;
	}

}
