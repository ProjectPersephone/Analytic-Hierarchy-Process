package view;

import java.util.ArrayList;
import java.util.List;

import exceptions.FirstStageMustBeGoalException;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Alternative;
import model.CriteriumTree2;
import model.Goal;

public abstract class ViewPart {
	protected Pane pane;
	protected final double DEFAULT_HEIGHT = 350.0;
	protected CriteriumTree2 tree;

	public ViewPart(CriteriumTree2 tree){
		this.tree = tree;
	}

	public Pane getPart() {
		return pane;
	}

	protected GridPane createGridPane() {
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 10, 10, 10));
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