package dataEnteringType;

import consistencyComputingMethods.MaximumEigenvalueConsistencyComputingMethod;
import exceptions.MalformedTreeException;
import javafx.scene.layout.GridPane;
import model.CriteriumTree2;
import model.Goal;

public abstract class DataEnteringType {
	protected static final double defInputWidth = 77.0;
	public abstract void create(Goal criterium, CriteriumTree2 tree, GridPane gridPane) throws MalformedTreeException;

	public static DataEnteringType getHalfConsistencyType() {
		return new HalfConsistecyDataEnteringType();
	}
	
	public static DataEnteringType getFullTypingConsistencyType(){
		return new FullTypingDataEnteringType();
	}
}
