package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import exceptions.MalformedTreeException;

public class Criterium extends Alternative {

	private int parentId;
	private Map<String, Double> values;

	public Criterium(int parentId, String name) throws MalformedTreeException {
		super(name);
		values = new HashMap<String, Double>();

		values.put(name, 1.);
		this.parentId = parentId;
	}

	public Criterium(Criterium c) throws MalformedTreeException {
		this(c.getParentId(), c.getName());
		this.values = new HashMap<String, Double>(c.getValues());
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

	public Map<String, Double> getValues() {
		return values;
	}

	public void setValue(Map<String, Double> values) {
		this.values = values;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "(id: " + id + " | parentId: " + parentId + " | name: " + name + " | values: " + values + ")";
	}

	public void addValuesOf(String name, double value) {
		values.put(name, value);
	}

	public void removeValueOf(String name) {
		values.remove(name);
	}
}
