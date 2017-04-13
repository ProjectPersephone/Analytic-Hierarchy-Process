package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import AHPSolver.AHPLogic;
import exceptions.InvalidXMLStructureException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.CriteriumTree2;
import model.PriorityVector;
import priorityVecorComputingMethods.PriorityVectorComputeMethod;

public class ProcessingPart extends ViewPart {

	private String sourceFolder;
	private String fileName;
	private PriorityVectorComputeMethod computingMethod;

	private Pane resultPane;

	public ProcessingPart(CriteriumTree2 tree) {
		super(tree);
		this.sourceFolder = "XML/";
		this.fileName = "testXML.xml";
		this.computingMethod = PriorityVectorComputeMethod.eigenvectorMethod();
		pane = createPane();

	}

	@Override
	protected Pane createPane() {

		Pane vBox = new VBox();
		Pane grid = createGridPane();

		// 0
		Label computeXMLLabel = createComputeXMLLabel();
		grid.getChildren().add(computeXMLLabel);

		// 1
		Label lComputingMethod = createComputingMethodsLabel();
		grid.getChildren().add(lComputingMethod);

		ComboBox<PriorityVectorComputeMethod> cbMethods = createComputingMethodsComboBox();
		grid.getChildren().add(cbMethods);

		// 2
		Label lSourceFolder = setSourceFolderLabel();
		grid.getChildren().add(lSourceFolder);

		TextField tfFileName = createFileNameTextField();
		grid.getChildren().add(tfFileName);

		// 3
		Button bCompute = setComputeButton();
		grid.getChildren().add(bCompute);

		resultPane = createGridPane();
		grid.getChildren().add(resultPane);

		vBox.getChildren().addAll(grid, resultPane);
		return vBox;

	}

	private Label createComputeXMLLabel() {
		Label label = new Label("compute XML");
		GridPane.setConstraints(label, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);
		label.setFont(Font.font("Verdena", FontWeight.BOLD, 18));
		return label;
	}

	private Label createComputingMethodsLabel() {
		Label lComputingMethod = new Label("priority method ");
		GridPane.setConstraints(lComputingMethod, 0, 1);
		return lComputingMethod;
	}

	private ComboBox<PriorityVectorComputeMethod> createComputingMethodsComboBox() {

		ObservableList<PriorityVectorComputeMethod> options = FXCollections.observableArrayList(
				PriorityVectorComputeMethod.eigenvectorMethod(), PriorityVectorComputeMethod.geometricMethod(),
				PriorityVectorComputeMethod.simpleMethod());
		ComboBox<PriorityVectorComputeMethod> cbMethods = new ComboBox<PriorityVectorComputeMethod>(options);

		cbMethods.setValue(PriorityVectorComputeMethod.eigenvectorMethod());
		cbMethods.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				computingMethod = cbMethods.getValue();
			}
		});
		GridPane.setConstraints(cbMethods, 1, 1);
		return cbMethods;
	}

	private Button setComputeButton() {
		Button bCompute = new Button();
		bCompute.setText("compute");
		bCompute.setMinWidth(DEFAULT_BUTTON_WIDTH);
		GridPane.setConstraints(bCompute, 0, 3, 2, 1, HPos.CENTER, VPos.CENTER);
		bCompute.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					compute();
				} catch (SAXException | IOException | ParserConfigurationException | InvalidXMLStructureException e) {
					e.printStackTrace();
				}
			}
		});
		return bCompute;
	}

	private TextField createFileNameTextField() {
		TextField tfFileName = new TextField();
		tfFileName.setText(fileName);
		tfFileName.setPrefColumnCount(10);
		tfFileName.setOnKeyReleased(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {

				// TODO REGEX
				fileName = tfFileName.getText();
			}
		});

		tfFileName.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					compute();
				} catch (SAXException | IOException | ParserConfigurationException | InvalidXMLStructureException e) {
					e.printStackTrace();
				}
			}
		});

		GridPane.setConstraints(tfFileName, 1, 2);
		return tfFileName;
	}

	private Label setSourceFolderLabel() {
		Label lSourceFolder = new Label(sourceFolder);
		GridPane.setConstraints(lSourceFolder, 0, 2);
		return lSourceFolder;
	}

	private void compute()
			throws SAXException, IOException, ParserConfigurationException, InvalidXMLStructureException {
		String filePath = sourceFolder + fileName;
		AHPLogic logic = new AHPLogic();
		Document doc = logic.getXMLDocument(filePath);

		org.w3c.dom.Node goal = logic.getGoal(doc);
		List<String> alternatives = logic.getAlternativesNames(logic.getAlternatives(doc));

		PriorityVector spv = logic.execute(goal, computingMethod);
		showResult(alternatives, spv);
	}

	private void showResult(List<String> alternatives, PriorityVector spv) {

		resultPane.getChildren().clear();

		for (int j = 0; j < alternatives.size(); j++) {
			Text alt = new Text(String.format("%4s", alternatives.get(j)) + ":");
			alt.setFont(Font.font("Verdena", FontWeight.BOLD, 12));
			Text value = new Text(String.format("%.5f", spv.get(j)));
			GridPane.setConstraints(alt, 0, j);
			GridPane.setConstraints(value, 1, j);
			resultPane.getChildren().addAll(alt, value);
		}
	}
}
