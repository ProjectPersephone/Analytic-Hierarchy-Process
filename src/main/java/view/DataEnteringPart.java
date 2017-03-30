package view;

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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Criterium;
import model.CriteriumTree2;
import model.Goal;

public class DataEnteringPart extends ViewPart {
	private GridPane gridPane;
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
		ScrollPane criteriumTreePane = createScrollpane();

		vBox.getChildren().add(criteriumTreePane);
		return vBox;
	}

	private ScrollPane createScrollpane() {
		ScrollPane sp = new ScrollPane();
		sp.setPrefSize(DEFAULT_HEIGHT, DEFAULT_HEIGHT);
		gridPane = createGridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setMinWidth(DEFAULT_HEIGHT - 10.0);

		sp.setContent(gridPane);
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
			Map<String, Integer> values = c.getValues();
			createLabel(0, j * 2 + 1, c.getName());

			int i = 1;
			for (Entry<String, Integer> entry : values.entrySet()) {
				String key = entry.getKey();
				if (!c.getName().equals(key)) {
					createLabel(i, j * 2, key);
					createCmpValuesImputs(i, j*2+1, c, key, entry.getValue());
					i++;
				}

				// if (j == 0) {
				// createLabel(i, 0, key);
				// createLabel(0, i, key);
				// }
				//
				// createCmpValuesImputs(i, j + 1, c, key, entry.getValue());

			}
		}
	}

	private void createInputs(List<Criterium> children) {
		for (int j = 0; j < children.size(); j++) {
			Criterium c = children.get(j);
			Map<String, Integer> values = c.getValues();
			int i = 1;
			for (Entry<String, Integer> entry : values.entrySet()) {
				String key = entry.getKey();

				if (j == 0) {
					createLabel(0, i, key);
					createLabel(i, 0, key);
				}

				createCmpValuesImputs(i, j + 1, c, key, entry.getValue());
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

	public void changeEnteredValue(Criterium c, String name, Integer value) {
		tree.changeValue(c, name, value);
		ConsistencyCalculator cc = new ConsistencyCalculator();

		try {
			Matrix m = createMatrix();
			cc.compute(m, ccm);
		} catch (MalformedTreeException e) {
			showAlert(e);
		}

	}

	private Matrix createMatrix() throws MalformedTreeException {
		List<Criterium> children = tree.getChildren(lastShowed);
		Matrix m = new Matrix(children.size(), children.size());

		
		for (int j = 0; j < children.size(); j++) {
			Criterium child = children.get(j);
			Map<String, Integer> values = child.getValues();
			int i = 0;
			double actual = values.get(child.getName());
			for (Entry<String, Integer> entry : values.entrySet()) {
				double v = (double) entry.getValue();

				m.set(i, j, v / actual);
				System.out.println("ac: " + child.getName() + " " + actual + " " + v);
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

	private void createCmpValuesImputs(int i, int j, Criterium c, String name, Integer v) {
		TextField nInput = new TextField();
		nInput.setMaxWidth(defInputWidth);
		nInput.setMinWidth(defInputWidth);
		nInput.setText(v.toString());
		nInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.matches("[1-9]+[0-9]*")) {
					Integer value = Integer.parseInt(newValue);
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
			createInputTable(lastShowed);
			System.out.println("hasProperities");
		}
	}

}
