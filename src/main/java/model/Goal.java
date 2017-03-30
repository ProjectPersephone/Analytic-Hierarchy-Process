package model;

public class Goal implements Comparable {

	protected int id;
	protected String name;

	public Goal(String name) {
		this(name, 0);
	}

	public Goal(String name, int id) {
		this.name = name;
		this.id = id;
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
	public int compareTo(Object o) {
		Goal g = (Goal)o;
		int res = String.CASE_INSENSITIVE_ORDER.compare(g.getName(), name);
		if (res == 0) {
			res = g.getName().compareTo(name);
		}
		System.out.println("res: " + res);
		return res;
	}

	@Override
	public String toString() {
		return "Goal [id=" + id + ", name=" + name + "]";
	}

}
