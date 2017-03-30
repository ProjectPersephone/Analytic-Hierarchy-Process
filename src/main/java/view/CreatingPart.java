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

		ViewPart alternativesAndOptionsCreator = new AlternativesAndOptionsPart(tree);
		ViewPart criteriumTreeAndDataEntryCreator = new CriteriumTreeAndDataEnteringPart(tree);

		vBox.getChildren().add(alternativesAndOptionsCreator.getPart());
		vBox.getChildren().add(criteriumTreeAndDataEntryCreator.getPart());

		return vBox;

	}

}
