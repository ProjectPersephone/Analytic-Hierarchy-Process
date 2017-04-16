package view;

import java.util.ArrayList;
import java.util.List;

import consistencyComputingMethods.ConsistencyComputeMethod;
import exceptions.FirstStageMustBeGoalException;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Alternative;
import model.CriteriumTree2;
import model.Goal;

public abstract class ViewPart {
	protected Pane pane;
	protected final double DEFAULT_HEIGHT = 350.0;
	protected final double DEFAULT_BUTTON_WIDTH = 150.0;
	protected CriteriumTree2 tree;
//	protected ConsistencyComputeMethod ccm;

	public ViewPart(CriteriumTree2 tree){
		this.tree = tree;
//		ccm=ConsistencyComputeMethod.getMaximumEigenvalueMethod();
	}

	public Pane getPart() {
		return pane;
	}

	protected GridPane createGridPane() {
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 10, 10, 10));
//		gridPane.setAlignment(Pos.TOP_CENTER);
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		// optionsPane.setAlignment(Pos.CENTER);
		return gridPane;
	}

	protected abstract Pane createPane();

	protected void showAlert(Exception e) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setContentText(e.getMessage());
		alert.showAndWait();
		e.printStackTrace();
	}
}
