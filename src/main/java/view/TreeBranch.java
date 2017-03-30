package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.Criterium;
import model.Goal;

public class TreeBranch extends StackPane {

	private CriteriumTreeAndDataEnteringPart criteriumTreeAndDataEnteringPart;
	private Goal criterium;
	private Label lName;

	private final static Border defBorder = new Border(
			new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));

	public TreeBranch(Goal goal, CriteriumTreeAndDataEnteringPart criteriumTreeAndDataEnteringPart) {
		this.criteriumTreeAndDataEnteringPart = criteriumTreeAndDataEnteringPart;
		this.criterium = goal;
		changeName(goal);
		createBorder();
		setBranchMenu();
	}

	private void setBranchMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem renameItem = createRenameItem();
		contextMenu.getItems().add(renameItem);

		MenuItem addChildItem = createAddChildItem();
		contextMenu.getItems().add(addChildItem);

		MenuItem compItem = createCompItem();
		contextMenu.getItems().add(compItem);
		
		if (criterium instanceof Criterium) {
			MenuItem delChildItem = createDelItem();
			contextMenu.getItems().add(delChildItem);
		}

		this.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				contextMenu.show(TreeBranch.this, event.getScreenX(), event.getScreenY());
			}
		});
	}

	private MenuItem createRenameItem() {
		MenuItem childItem = new MenuItem("rename");
		childItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				renameCriterium();
			}

		});
		return childItem;
	}

	private void renameCriterium() {
		TextField tf = new TextField();
		TreeBranch.this.getChildren().add(tf);

		tf.setOnAction(e -> {
			// TODO regex
			String newName = tf.getText();
			criteriumTreeAndDataEnteringPart.renameCriterium(TreeBranch.this, newName);
			criterium.setName(newName);
			changeName(TreeBranch.this.getCriterium());
			TreeBranch.this.getChildren().remove(tf);

		});

	}

	private MenuItem createAddChildItem() {
		MenuItem childItem = new MenuItem("add child");
		childItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				criteriumTreeAndDataEnteringPart.AddChild(TreeBranch.this);
			}
		});
		return childItem;
	}

	private MenuItem createDelItem() {
		MenuItem childItem = new MenuItem("remove");
		childItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				criteriumTreeAndDataEnteringPart.removeChild(TreeBranch.this);
			}
		});
		return childItem;
	}

	private MenuItem createCompItem() {
		MenuItem addChildItem = new MenuItem("add comparasion");
		addChildItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				criteriumTreeAndDataEnteringPart.createComparisons(TreeBranch.this);
			}
		});
		return addChildItem;
	}

	public Goal getCriterium() {
		return criterium;
	}

	public void setCriterium(Goal goal) {
		this.criterium = goal;
	}

	private void createBorder() {
		this.setBorder(defBorder);
	}

	private void changeName(Goal goal) {
		this.getChildren().remove(lName);
		lName = new Label(goal.getName());
		this.getChildren().add(lName);
	}
}
