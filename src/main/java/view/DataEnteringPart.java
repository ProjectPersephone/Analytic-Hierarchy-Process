package view;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import AHPSolver.ConsistencyCalculator;
import Jama.Matrix;
import consistencyComputingMethods.ConsistencyComputeMethod;
import dataEnteringType.DataEnteringType;
import events.ChangeConsistencyEvent;
import events.EnteredValue;
import exceptions.MalformedTreeException;
import exceptions.notFoundException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.Criterium;
import model.CriteriumTree2;
import model.Goal;
import net.bytebuddy.asm.Advice.Enter;

public class DataEnteringPart extends ViewPart {
	private GridPane gridPane;
	private VBox labelPane;
	private HBox consistencyPane;
	private HBox dataEnteringTypePane;
	private Goal lastShowed;

	public DataEnteringPart(CriteriumTree2 tree) {
		super(tree);
		pane = createPane();
	}

	@Override
	protected Pane createPane() {
		Pane vBox = new VBox();
		vBox.getChildren().add(createScrollpane());
		return vBox;
	}

	private ScrollPane createScrollpane() {
		ScrollPane sp = new ScrollPane();
		sp.setPrefSize(DEFAULT_HEIGHT, DEFAULT_HEIGHT);
		sp.setContent(arrangeScrollPaneContent());
		return sp;
	}

	public void createDataEnteringComponents(Goal criterium) {
		lastShowed = criterium;
		showMainLabel(criterium);
		showEnteringTypeCB(criterium);

		try {
			createInputs(criterium);
		} catch (MalformedTreeException e) {
			showAlert(e);
		}
	}

	private void showEnteringTypeCB(Goal criterium) {
		dataEnteringTypePane.getChildren().clear();
		Label dataEnteringTypeLabel = createDataEnteringTypeLabel();
		dataEnteringTypePane.getChildren().add(dataEnteringTypeLabel);

		ComboBox<DataEnteringType> depMethods = createDataEnteringTypeComboBox(criterium);
		dataEnteringTypePane.getChildren().add(depMethods);
	}

	private void createInputs(Goal criterium) throws MalformedTreeException {
		gridPane.getChildren().clear();
		criterium.getDataEnteringType().create(criterium, tree, gridPane);
		gridPane.addEventHandler(EnteredValue.COMPARATION_VALUE_CHANGED, event -> {
			handleComparationValueChanged();
		});
	}

	public void handleComparationValueChanged() {
		try {
			calculateConsistency(lastShowed);
		} catch (MalformedTreeException e) {
			showAlert(e);
		}
	}

	private void calculateConsistency(Goal parent) throws MalformedTreeException {
		ConsistencyCalculator cc = new ConsistencyCalculator();
		parent.setConsistencyValue(cc.compute(createMatrix(), ccm));
		pane.fireEvent(new ChangeConsistencyEvent(ChangeConsistencyEvent.CHANGED_SINGLE_CONSISTENCY));
	}

	private Matrix createMatrix() throws MalformedTreeException {
		List<Criterium> children = tree.getChildren(lastShowed);
		Matrix m = new Matrix(children.size(), children.size());
		int i;
		for (int j = 0; j < children.size(); j++) {
			Criterium child = children.get(j);
			Map<String, Double> values = child.getValues();
			i = 0;
			double actual = values.get(child.getName());
			for (Entry<String, Double> entry : values.entrySet()) {
				m.set(i, j, entry.getValue() / actual);
				i++;
			}
		}
		WindowAHP.showMatrix(m);
		return m;
	}

	public void showConsistency(double consistencyValue) {

		try {
			if (lastShowed != null) {
				consistencyPane.getChildren().clear();
				int next = tree.getChildren(lastShowed).size() * 2;
				GridPane.setConstraints(consistencyPane, 1, next);
				Text c = new Text("consistency: ");
				c.setFont(Font.font("Verdena", FontWeight.BOLD, 12));

				String cv;
				if (consistencyValue > Integer.MAX_VALUE) {
					cv = String.format("%f", Double.POSITIVE_INFINITY);
				} else {
					cv = String.format("%3.10f", consistencyValue);
				}
				Text value = new Text(cv);
				consistencyPane.getChildren().addAll(c, value);
			}
		} catch (MalformedTreeException e) {
			showAlert(e);
		}
	}

	public void showMainLabel(Goal criterium) {
		labelPane.getChildren().clear();
		Label cNameLabel = new Label(criterium.getName());
		cNameLabel.setFont(Font.font("Verdena", FontWeight.EXTRA_BOLD, 20));
		labelPane.getChildren().add(cNameLabel);
	}

	public void refresh() {
		if (lastShowed != null) {
			try {
				createDataEnteringComponents(lastShowed);
				calculateConsistency(lastShowed);
			} catch (MalformedTreeException e) {
				showAlert(e);
			}
		}
	}

	private Pane arrangeScrollPaneContent() {
		Pane vBox = new VBox();

		arrangeLabelPane();
		arrangeEnteringTypePane();
		arrangeGridPane();
		arrangeConsistencyPane();

		vBox.getChildren().addAll(labelPane, dataEnteringTypePane, gridPane, consistencyPane);
		return vBox;
	}

	private void arrangeLabelPane() {
		labelPane = new VBox();
		labelPane.setAlignment(Pos.BOTTOM_CENTER);
		labelPane.setMinWidth(DEFAULT_HEIGHT - 10.0);
	}

	private void arrangeEnteringTypePane() {
		dataEnteringTypePane = new HBox();
		dataEnteringTypePane.setAlignment(Pos.BOTTOM_CENTER);
		dataEnteringTypePane.setMinWidth(DEFAULT_HEIGHT - 10.0);
	}

	private void arrangeGridPane() {
		gridPane = createGridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setMinWidth(DEFAULT_HEIGHT - 10.0);
	}

	private void arrangeConsistencyPane() {
		consistencyPane = new HBox();
		consistencyPane.setAlignment(Pos.CENTER);
		consistencyPane.setMinWidth(DEFAULT_HEIGHT - 10.0);
	}

	private Label createDataEnteringTypeLabel() {
		Label l = new Label("entering method ");
		GridPane.setConstraints(l, 0, 3);
		return l;
	}

	private ComboBox<DataEnteringType> createDataEnteringTypeComboBox(Goal c) {

		ObservableList<DataEnteringType> options = FXCollections
				.observableArrayList(DataEnteringType.getFullTypingConsistencyType(), DataEnteringType.getHalfConsistencyType());
		ComboBox<DataEnteringType> methods = new ComboBox<DataEnteringType>(options);
		methods.setValue(c.getDataEnteringType());
		methods.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				try {
					c.setDataEnteringType(methods.getValue());
					createInputs(c);
				} catch (MalformedTreeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		GridPane.setConstraints(methods, 1, 3);
		return methods;
	}

}
