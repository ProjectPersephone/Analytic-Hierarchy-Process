package dataEnteringType;

import java.util.Optional;

import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import model.Criterium;

class InputListenerValues {
	private TextField textField;
	private Slider slider;

	private Criterium criterium;
	private String name;

	private int i;
	private int j;

	public InputListenerValues(Criterium criterium, String name, int i, int j) {
		this.criterium = criterium;
		this.name = name;
		this.i = i;
		this.j = j;
	}

	// public InputListenerValues(TextField textField, Criterium criterium,
	// String name, int i, int j) {
	// this(criterium, name, i, j);
	// this.textField = textField;
	// }
	//
	// public InputListenerValues(Slider slider, Criterium criterium, String
	// name, int i, int j) {
	// this(criterium, name, i, j);
	// this.slider = slider;
	// }

	public boolean checkMembershipToUpperTriangular() {
		return (i > j);
	}

	public boolean isOnDiagonal() {
		return (i == j);
	}

	public boolean checkMembershipToFirstRow() {
		return (j == 0);
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

	public void setTextField(TextField textField) {
		this.textField = textField;
	}

	public Criterium getCriterium() {
		return criterium;
	}

	public String getName() {
		return name;
	}

	public Slider getSlider() {
		return slider;
	}

	public void setSlider(Slider slider) {
		this.slider = slider;
	}

}
