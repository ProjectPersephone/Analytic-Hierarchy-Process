package model;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import exceptions.MalformedTreeException;

public class Criterium extends Alternative {

	private int parentId;
	private Map<String, Integer> values;

	public Criterium(int parentId, String name) throws MalformedTreeException {
		super(name);
		values = new TreeMap<String, Integer>();

		values.put(name, 1);
		this.parentId = parentId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	// @Override
	// public void setName(String newName) {
	// values.put(newName, values.get(name));
	// values.remove(this.name);
	// this.name = newName;
	// }

	public Map<String, Integer> getValues() {
		return values;
	}

	public void setValue(Map<String, Integer> values) {
		this.values = values;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "(id: " + id + " | parentId: " + parentId + " | name: " + name + " | values: " + values + ")";
	}

	public void addValuesOf(String name, int value) {
		values.put(name, value);
	}

}
