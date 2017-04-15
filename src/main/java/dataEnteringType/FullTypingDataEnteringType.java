package dataEnteringType;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import exceptions.MalformedTreeException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Criterium;
import model.CriteriumTree2;
import model.Goal;
import events.EnteredValue;

public class FullTypingDataEnteringType extends DataEnteringType {
	GridPane gridPane;

	@Override
	public void create(Goal criterium, CriteriumTree2 tree, GridPane gridPane) throws MalformedTreeException {
		this.gridPane = gridPane;
		List<Criterium> children = tree.getChildren(criterium);
		int i;
		for (int j = 0; j < children.size(); j++) {
			Criterium c = children.get(j);
			Map<String, Double> values = c.getValues();
			createLabel(0, j * 2 + 1, c.getName());

			i = 1;
			for (Entry<String, Double> entry : values.entrySet()) {
				String key = entry.getKey();
				createLabel(i, j * 2, key);
				createCmpValuesImputs(i, j * 2 + 1, c, key, entry.getValue(), tree);
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

	private void createCmpValuesImputs(int i, int j, Criterium c, String name, Double v, CriteriumTree2 tree) {
		TextField nInput = new TextField();
		nInput.setMaxWidth(defInputWidth);
		nInput.setMinWidth(defInputWidth);
		nInput.setText(v.toString());
		nInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String regex = "(\\d{0,1}.\\d{0,5})|(\\d{0,2}.\\d{0,4})|(\\d{0,3}.\\d{0,3})|(\\d{0,4}.\\d{0,2})|(\\d{0,5}.\\d{0,1})";
				if (newValue.matches(regex)) {
					tree.changeValue(c, name, Double.parseDouble(newValue));
					gridPane.fireEvent(new EnteredValue(EnteredValue.COMPARATION_VALUE_CHANGED));
				} else {
					nInput.setText(oldValue);
				}
			}
		});
		GridPane.setConstraints(nInput, i, j);
		gridPane.getChildren().add(nInput);
	}

	@Override
	public String toString() {
		return "full typing";
	}
}
