package model;

import dataEnteringType.DataEnteringType;

public class Goal implements Comparable<Goal> {

	protected int id;
	protected String name;
	protected double consistencyValue;
	protected DataEnteringType dataEnteringType;

	public Goal(String name) {
		this(name, 0, 0);
	}

	public Goal(String name, int id, double consistencyValue) {
		this.name = name;
		this.id = id;
		this.consistencyValue = consistencyValue;
		dataEnteringType = DataEnteringType.getFullTypingConsistencyType();
	}

	public double getConsistencyValue() {
		return consistencyValue;
	}

	public void setConsistencyValue(double consistencyValue) {
		this.consistencyValue = consistencyValue;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataEnteringType getDataEnteringType() {
		return dataEnteringType;
	}

	public void setDataEnteringType(DataEnteringType dataEnteringType) {
		this.dataEnteringType = dataEnteringType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Goal other = (Goal) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Goal [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int compareTo(Goal g) {
		int res = String.CASE_INSENSITIVE_ORDER.compare(g.getName(), name);
		if (res == 0) {
			res = g.getName().compareTo(name);
		}
		System.out.println("res: " + res);
		return res;
	}

}
