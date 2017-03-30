package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import AHPSolver.AHPLogic;
import exceptions.FirstStageMustBeGoalException;
import exceptions.InvalidXMLStructureException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Alternative;
import model.CriteriumTree2;
import model.Goal;
import model.PriorityVector;
import priorityVecorComputingMethods.PriorityVectorComputeMethod;

public class WindowAHP extends Application {
	private final double sceneHeight = 650;
	private final double sceneWidth = 1400;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		try {
			primaryStage.setTitle("AHP");
			Pane root = new HBox();

			Goal goal = new Goal("goal");
			List<Alternative> alternativesList = new ArrayList<Alternative>();
			CriteriumTree2 tree = new CriteriumTree2(goal, alternativesList);

			ProcessingPart processing = new ProcessingPart(tree);
			CreatingPart creating = new CreatingPart(tree);

			root.getChildren().add(creating.getPart());
			root.getChildren().add(processing.getPart());

			primaryStage.setScene(new Scene(root, sceneWidth, sceneHeight));
			primaryStage.show();
		} catch (FirstStageMustBeGoalException e) {
			showAlert(e);
		}
	}

	private void showAlert(FirstStageMustBeGoalException e) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setContentText(e.getMessage());
		alert.showAndWait();
		e.printStackTrace();
	}

}