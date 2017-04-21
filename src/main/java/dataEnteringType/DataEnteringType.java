package dataEnteringType;

import consistencyComputingMethods.MaximumEigenvalue;
import exceptions.MalformedTreeException;
import exceptions.notFoundException;
import javafx.scene.layout.GridPane;
import model.CriteriumTree2;
import model.Goal;

public abstract class DataEnteringType {
	protected static final double defInputWidth = 77.0;
	public abstract void create(Goal criterium, CriteriumTree2 tree, GridPane gridPane) throws MalformedTreeException, NumberFormatException, notFoundException;

	public static DataEnteringType getHalfConsistencyType() {
		return new HalfTypingDataEnteringType();
	}
	
	public static DataEnteringType getFullTypingConsistencyType(){
		return new FullTypingDataEnteringType();
	}
}
