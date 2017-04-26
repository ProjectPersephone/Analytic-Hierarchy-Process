package dataEnteringType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import events.EnteredValue;
import exceptions.MalformedTreeException;
import exceptions.notFoundException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import model.Criterium;
import model.CriteriumTree2;
import model.Goal;

public class OneVectorSlider extends DataEnteringType {

	//TODO SLIDERS
	GridPane gridPane;

	@Override
	public void create(Goal criterium, CriteriumTree2 tree, GridPane gridPane)
			throws MalformedTreeException, NumberFormatException, notFoundException {
		List<Criterium> children = tree.getChildren(criterium);
		this.gridPane = gridPane;
		List<InputListenerValues> list = new ArrayList<>();
		int i = 0;
		for (Criterium c : children) {
			Map<String, Double> values = c.getValues();

			int j = 0;
			for (Entry<String, Double> e : values.entrySet()) {
				if (i == 0) {
					createLabel(0, j, c.getName());
					Slider slider = createSlider(1, j, c, e, tree);
					createLabel(2, j, e.getKey());

					InputListenerValues inputLV = new InputListenerValues(c, e.getKey(), i, j);
					inputLV.setSlider(slider);
					list.add(inputLV);
				} else {
					InputListenerValues inputLV = new InputListenerValues(c, e.getKey(), i, j);
					list.add(inputLV);
				}

				j++;
			}

			i++;

		}

		for (InputListenerValues ilv : list.stream().filter(i2 -> i2.checkMembershipToFirstRow())
				.collect(Collectors.toList())) {
			List<InputListenerValues> toChangeList = new ArrayList<>();
			toChangeList = list.stream().filter(i2 -> (!i2.checkMembershipToFirstRow() && (i2.getI() == ilv.getI())))
					.collect(Collectors.toList());
			toChangeList.stream().forEach(l -> System.out.println("ilvc: " + ilv.getName() + " lc: " + l.getName()));
			System.out.println(" ");
			addListenerToSlider(ilv, toChangeList, tree);

			// if (ilv.checkMembershipToFirstRow()) {
			// list.stream().filter(i2 ->
			// !i2.checkMembershipToFirstRow()).filter(i2 -> (i2.getI() ==
			// ilv.getI()))
			// .forEach(i2 -> {
			// try {
			// changeValue(tree,
			// ilv.getCriterium().getValues().get(ilv.getName()),
			// i2.getCriterium(),
			// i2.getName());
			// addListenerToSlider(ilv, i2, tree);
			// } catch (notFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// });
			// }
		}

	}
	

	private void addListenerToSlider(InputListenerValues ilv, List<InputListenerValues> otherVectorsIlv,
			CriteriumTree2 tree) {
//		if (ilv.getSlider() != null) {
			Slider slider = ilv.getSlider();
			slider.valueProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
					// cappuccino.setOpacity(new_val.doubleValue());
					try {
						double value = (double) new_val;
						changeValue(tree, value, ilv.getCriterium(), ilv.getName());
						for (InputListenerValues oilv : otherVectorsIlv) {
							changeValue(tree, value, oilv.getCriterium(), oilv.getName());
						}
						// System.out.println(String.format("%.2f", new_val));
					} catch (notFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
//		}

	}

	private void addListenerToSlider(InputListenerValues ilv, InputListenerValues otherVectorsIlv,
			CriteriumTree2 tree) {
		if (ilv.getSlider() != null) {
			Slider slider = ilv.getSlider();
			slider.valueProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
					// cappuccino.setOpacity(new_val.doubleValue());
					try {
						double value = (double) new_val;
						changeValue(tree, value, ilv.getCriterium(), ilv.getName());
						changeValue(tree, value, otherVectorsIlv.getCriterium(), otherVectorsIlv.getName());
						// System.out.println(String.format("%.2f", new_val));
					} catch (notFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}

	}

	private Slider createSlider(int i, int j, Criterium c, Entry<String, Double> e, CriteriumTree2 tree) {
		Slider slider = new Slider();
		slider.setMin(1);
		slider.setMax(10);
		slider.setValue(e.getValue());
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		// slider.setSho
		// slider.setMajorTickUnit(8);
		// slider.setMinorTickCount(3);
		slider.setBlockIncrement(1);

		GridPane.setConstraints(slider, i, j);
		gridPane.getChildren().add(slider);
		return slider;

	}

	private void changeValue(CriteriumTree2 tree, double value, Criterium c, String name) throws notFoundException {
		tree.changeValue(c, name, value);
		gridPane.fireEvent(new EnteredValue(EnteredValue.COMPARATION_VALUE_CHANGED, tree.getParent(c)));
	}

	private void createLabel(int i, int j, String key) {
		Label lH = new Label(key);
		lH.setMaxWidth(defInputWidth);
		lH.setMinWidth(defInputWidth);
		GridPane.setConstraints(lH, i, j);
		gridPane.getChildren().add(lH);
	}

	@Override
	public String toString() {
		return "one vector slider";
	}
}
