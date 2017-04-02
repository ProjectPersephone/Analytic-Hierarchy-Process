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
	private Goal lastShowed;
	private ConsistencyComputeMethod ccm;

	private static final double defInputWidth = 77.0;

	public DataEnteringPart(CriteriumTree2 tree) {
		super(tree);
		pane = createPane();
		ccm = ConsistencyComputeMethod.maximumEigenvalueMethod();
		// pane.addEventHandler(ActionEvent.ACTION, new
		// EventHandler<ActionEvent>() {
		//
		// @Override
		// public void handle(ActionEvent event) {
		// System.out.println("addEventHandler DEP");
		//
		// }
		//
		// });
	}

	@Override
	protected Pane createPane() {
		Pane vBox = new VBox();
		ScrollPane criteriumTreePane = createScrollpane();

		vBox.getChildren().add(criteriumTreePane);
		return vBox;
	}

	private ScrollPane createScrollpane() {
		ScrollPane sp = new ScrollPane();
		sp.setPrefSize(DEFAULT_HEIGHT, DEFAULT_HEIGHT);
		Pane vBox = new VBox();

		gridPane = createGridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setMinWidth(DEFAULT_HEIGHT - 10.0);
		consistencyPane = new HBox();
		consistencyPane.setAlignment(Pos.CENTER);
		consistencyPane.setMinWidth(DEFAULT_HEIGHT - 10.0);
		// gridPane.getChildren().add(consistencyPane);

		vBox.getChildren().addAll(gridPane, consistencyPane);
		sp.setContent(vBox);
		return sp;
	}

	public void createInputTable(Goal criterium) {
		lastShowed = criterium;
		gridPane.getChildren().clear();
		try {
			if (tree.hasChildren(criterium)) {
				createInputs2(tree.getChildren(criterium));
			} else {
				// TODO alternatives input data
			}
		} catch (MalformedTreeException e) {
			showAlert(e);
		}
	}

	private void createInputs2(List<Criterium> children) {
		for (int j = 0; j < children.size(); j++) {
			Criterium c = children.get(j);
			Map<String, Double> values = c.getValues();
			createLabel(0, j * 2 + 1, c.getName());

			int i = 1;
			for (Entry<String, Double> entry : values.entrySet()) {
				String key = entry.getKey();
				// if (!c.getName().equals(key)) {
				createLabel(i, j * 2, key);
				createCmpValuesImputs(i, j * 2 + 1, c, key, entry.getValue());
				i++;
				// }

				// if (j == 0) {
				// createLabel(i, 0, key);
				// createLabel(0, i, key);
				// }
				//
				// createCmpValuesImputs(i, j + 1, c, key, entry.getValue());

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
			Goal parent = tree.getParent(c);

			calculateConsistency(parent);

		} catch (MalformedTreeException e) {
			showAlert(e);
		} catch (notFoundException e) {
			showAlert(e);
		}
	}

	private void calculateConsistency(Goal parent) throws MalformedTreeException {
		ConsistencyCalculator cc = new ConsistencyCalculator();
		Matrix m = createMatrix();

		parent.setConsistencyValue(cc.compute(m, ccm));
		pane.fireEvent(new ChangeConsistencyEvent(parent));
	}

	private Matrix createMatrix() throws MalformedTreeException {
		List<Criterium> children = tree.getChildren(lastShowed);
		Matrix m = new Matrix(children.size(), children.size());

		for (int j = 0; j < children.size(); j++) {
			Criterium child = children.get(j);
			Map<String, Double> values = child.getValues();
			int i = 0;
			double actual = values.get(child.getName());
			for (Entry<String, Double> entry : values.entrySet()) {
				double v = (double) entry.getValue();

				// TODO to double

				m.set(i, j, v / actual);
				// System.out.println("ac: " + child.getName() + " " + actual +
				// " " + v);
				// System.out.print(v + " ");
				i++;
			}
		}

		for (int k = 0; k < m.getColumnDimension(); k++) {
			for (int l = 0; l < m.getRowDimension(); l++) {

				System.out.print(m.get(l, k) + " ");
			}
			System.out.println();
		}
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
				if (newValue.matches(regex)) {// ("[1-9]+[0-9]*")) {
					// Integer value = Integer.parseInt(newValue);
					// Double.
					Double value = Double.parseDouble(newValue);
					// System.out.println("---- "+value);
					changeEnteredValue(c, name, value);
				} else {
					nInput.setText(oldValue);
				}
			}

		});

		GridPane.setConstraints(nInput, i, j);

		gridPane.getChildren().add(nInput);
	}

	public void refresh() {
		if (lastShowed != null) {
			try {
				createInputTable(lastShowed);
				calculateConsistency(lastShowed);
				// showConsistency(lastShowed.getConsistencyValue());
			} catch (MalformedTreeException e) {
				showAlert(e);
			}
		}
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
					cv = String.format("%2.4f", consistencyValue);
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

}
