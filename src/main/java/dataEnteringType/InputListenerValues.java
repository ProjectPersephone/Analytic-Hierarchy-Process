package dataEnteringType;

import javafx.scene.control.TextField;
import model.Criterium;

class InputListenerValues {
	private TextField textField;
	private Criterium criterium;
	private String name;
	
	private int i;
	private int j;
	
	
	public InputListenerValues(TextField textField, Criterium criterium, String name, int i, int j) {
		this.textField = textField;
		this.criterium = criterium;
		this.name = name;
		this.i = i;
		this.j = j;
	}
	
	public boolean checkMembershipToUpperTriangular(){
		return (i>j);
	}
	
	public boolean isOnDiagonal(){
		return (i==j);
	}

	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}

	public TextField getTextField() {
		return textField;
	}

	public Criterium getCriterium() {
		return criterium;
	}

	public String getName() {
		return name;
	}

}
