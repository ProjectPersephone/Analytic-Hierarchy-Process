package view;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import AHPSolver.ConsistencyCalculator;
import Jama.Matrix;
import consistencyComputingMethods.ConsistencyComputeMethod;
import exceptions.MalformedTreeException;
import exceptions.notFoundException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
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

public class DataEnteringPart extends ViewPart {
	private GridPane gridPane;
	private HBox consistencyPane;
	private VBox labelPane;
	private Goal lastShowed;
	private ConsistencyComputeMethod ccm;

	private static final double defInputWidth = 77.0;

	public DataEnteringPart(CriteriumTree2 tree) {
		super(tree);
		pane = createPane();
		ccm = ConsistencyComputeMethod.maximumEigenvalueMethod();
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

	public void createInputTable(Goal criterium) {
		showLabel(criterium);
		lastShowed = criterium;
		gridPane.getChildren().clear();
		try {
			List<Criterium> cList = tree.getChildren(criterium);
			createInputs(cList);

		} catch (MalformedTreeException e) {
			showAlert(e);
		}
	}

	private void createInputs(List<Criterium> children) {
		int i;
		for (int j = 0; j < children.size(); j++) {
			Criterium c = children.get(j);
			Map<String, Double> values = c.getValues();
			createLabel(0, j * 2 + 1, c.getName());

			i = 1;
			for (Entry<String, Double> entry : values.entrySet()) {
				String key = entry.getKey();
				createLabel(i, j * 2, key);
				createCmpValuesImputs(i, j * 2 + 1, c, key, entry.getValue());
				i++;
			}
		}
	}

	private void createLabel(int i, int j, String key) {
		Label lH = new Label(key);
		lH.setMaxWidth(defInputWidth);
		lH.setMinWidth(defInputWidth);
		GridPane.setConstraints(lH, i, j);
		gridPane.getChildren().add(lH);
	}

	public void changeEnteredValue(Criterium c, String name, Double value) {
		try {
			tree.changeValue(c, name, value);
//			Goal parent = tree.getParent(c);
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

	private void createCmpValuesImputs(int i, int j, Criterium c, String name, Double v) {
		TextField nInput = new TextField();
		nInput.setMaxWidth(defInputWidth);
		nInput.setMinWidth(defInputWidth);
		nInput.setText(v.toString());
		nInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String regex = "(\\d{0,1}.\\d{0,5})|(\\d{0,2}.\\d{0,4})|(\\d{0,3}.\\d{0,3})|(\\d{0,4}.\\d{0,2})|(\\d{0,5}.\\d{0,1})";
				if (newValue.matches(regex)) {
					changeEnteredValue(c, name, Double.parseDouble(newValue));
				} else {
					nInput.setText(oldValue);
				}
			}
		});
		GridPane.setConstraints(nInput, i, j);
		gridPane.getChildren().add(nInput);
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

				// System.out.println(cv);
				Text value = new Text(cv);// String.format("%.2f",
											// consistencyValue));

				// GridPane.setConstraints(value, 2, next);

				consistencyPane.getChildren().addAll(c, value);
			}
		} catch (MalformedTreeException e) {
			showAlert(e);
		}
	}
	
	public void showLabel(Goal criterium){
		labelPane.getChildren().clear();
		Label cNameLabel = new Label(criterium.getName());
		cNameLabel.setFont(new Font(20));
		labelPane.getChildren().add(cNameLabel);
	}

	public void refresh() {
		if (lastShowed != null) {
			try {
				createInputTable(lastShowed);
				calculateConsistency(lastShowed);
			} catch (MalformedTreeException e) {
				showAlert(e);
			}
		}
	}

	private Pane arrangeScrollPaneContent() {
		Pane vBox = new VBox();

		arrangeLabelPane();
		arrangeGridPane();
		arrangeConsistencyPane();

		vBox.getChildren().addAll(labelPane, gridPane, consistencyPane);
		return vBox;
	}

	private void arrangeLabelPane() {
		labelPane = new VBox();
		labelPane.setMinWidth(200);
		labelPane.setAlignment(Pos.BOTTOM_CENTER);
//		labelPane.setAlignment(Pos.BASELINE_CENTER);
		labelPane.setMinWidth(DEFAULT_HEIGHT - 10.0);
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

}
