package dataEnteringType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import events.EnteredValue;
import exceptions.MalformedTreeException;
import exceptions.notFoundException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Criterium;
import model.CriteriumTree2;
import model.Goal;

public class HalfTypingDataEnteringType extends DataEnteringType {

	GridPane gridPane;

	@Override
	public void create(Goal criterium, CriteriumTree2 tree, GridPane gridPane)
			throws MalformedTreeException, NumberFormatException, notFoundException {
		this.gridPane = gridPane;
		List<Criterium> children = tree.getChildren(criterium);

		List<InputListenerValues> list = new ArrayList<>();

		int rows = children.size();
		// System.out.println("rows: " + rows);
		int i;
		for (int j = 0; j < rows; j++) {
			Criterium c = children.get(j);
			Map<String, Double> values = c.getValues();
			createLabel(0, j * 2 + 1, c.getName());

			i = 1;
			for (Entry<String, Double> entry : values.entrySet()) {

				String name = entry.getKey();
				createLabel(i, j * 2, name);
				TextField tf = createCmpValuesImputs(i, j, entry.getValue());
				if (i - 1 != j) {
					list.add(new InputListenerValues(tf, c, name, i - 1, j));
				} else {
					changeValue(tree, 1, c, name, tf);
				}
				i++;
			}
		}

		for (InputListenerValues ilv : list) {
			if (ilv.checkMembershipToUpperTriangular()) {
				for (InputListenerValues inverseIlv : list) {
					if ((inverseIlv.getI() == ilv.getJ()) && (inverseIlv.getJ() == ilv.getI())) {
						addListenerToInput(ilv, inverseIlv, tree);
					}
				}
			}
		}

	}

	private void addListenerToInput(InputListenerValues ilv, InputListenerValues inverseIlv, CriteriumTree2 tree)
			throws NumberFormatException, notFoundException {
		TextField input = ilv.getTextField();
		TextField inverseInput = inverseIlv.getTextField();
		double value = Double.parseDouble(input.getText());
		value = checkIfInfinity(value);
		changeValue(tree, 1 / value, inverseIlv.getCriterium(), inverseIlv.getName(), inverseInput);
		input.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String regex = "([0-9]{0,1}\\.{0,1}[0-9]{0,5})|([0-9]{0,2}\\.{0,1}[0-9]{0,4})|([0-9]{0,3}\\.{0,1}[0-9]{0,3})|([0-9]{0,4}\\.{0,1}[0-9]{0,2})|([0-9]{0,5}\\.{0,1}[0-9]{0,1})";
				if (newValue.matches(regex)) {

					double value = Double.parseDouble(newValue);
					double inverseValue = 1. / checkIfInfinity(value);

					// System.out.println("tf: " + input.getText() + " " +
					// inverseInput.getText());
					try {
						changeValue(tree, value, ilv.getCriterium(), ilv.getName());
						changeValue(tree, inverseValue, inverseIlv.getCriterium(), inverseIlv.getName(), inverseInput);
					} catch (notFoundException e) {
						e.printStackTrace();
					}
					inverseInput.setText(String.valueOf(inverseValue));
				} else {
					input.setText(oldValue);
				}
			}

		});

	}

	private double checkIfInfinity(double value) {
		if (value == 0) {
			value = Double.POSITIVE_INFINITY;
		}
		return value;
	}

	private void changeValue(CriteriumTree2 tree, double value, Criterium c, String name) throws notFoundException {
		tree.changeValue(c, name, value);
		gridPane.fireEvent(new EnteredValue(EnteredValue.COMPARATION_VALUE_CHANGED, tree.getParent(c)));
	}

	private void changeValue(CriteriumTree2 tree, double value, Criterium c, String name, TextField tf)
			throws notFoundException {
		tree.changeValue(c, name, value);
		gridPane.fireEvent(new EnteredValue(EnteredValue.COMPARATION_VALUE_CHANGED, tree.getParent(c)));
		tf.setText(String.valueOf(value));
	}

	private void createLabel(int i, int j, String key) {
		Label lH = new Label(key);
		lH.setMaxWidth(defInputWidth);
		lH.setMinWidth(defInputWidth);
		GridPane.setConstraints(lH, i, j);
		gridPane.getChildren().add(lH);
	}

	private TextField createCmpValuesImputs(int i, int j, Double v) {
		int k = j * 2 + 1;
		TextField nInput = new TextField();
		if (i - 1 <= j) {
			nInput.setDisable(true);
		}
		nInput.setMaxWidth(defInputWidth);
		nInput.setMinWidth(defInputWidth);
		nInput.setText(v.toString());

		GridPane.setConstraints(nInput, i, k);
		gridPane.getChildren().add(nInput);
		return nInput;
	}

	@Override
	public String toString() {
		return "half typing";
	}
}
