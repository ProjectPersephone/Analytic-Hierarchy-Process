package model;

public class Alternative extends Goal{	
	private static int i=0;
	public static int generateNewCriteriumId(){
		i++;
		return i;
	}
	
	public Alternative(String name){
		super(name, generateNewCriteriumId(), 0);
	}
	
	public Alternative(String name, double consistencyValue){
		super(name, generateNewCriteriumId(), consistencyValue);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
