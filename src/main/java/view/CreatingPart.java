package view;

import java.util.ArrayList;
import java.util.List;

import exceptions.FirstStageMustBeGoalException;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Alternative;
import model.CriteriumTree2;
import model.Goal;

public class CreatingPart extends ViewPart {

	public CreatingPart(CriteriumTree2 tree) {
		super(tree);
		pane = createPane();
	}

	@Override
	protected Pane createPane() {

		Pane vBox = new VBox();

		AlternativesAndOptionsPart alternativesAndOptionsCreator = new AlternativesAndOptionsPart(tree);
		Pane alternativesAndOptionsPart = alternativesAndOptionsCreator.getPart();
		ViewPart criteriumTreeAndDataEntryCreator = new CriteriumTreeAndDataEnteringPart(tree, alternativesAndOptionsPart);

		vBox.getChildren().add(alternativesAndOptionsPart);
		vBox.getChildren().add(criteriumTreeAndDataEntryCreator.getPart());

		return vBox;

	}

}
