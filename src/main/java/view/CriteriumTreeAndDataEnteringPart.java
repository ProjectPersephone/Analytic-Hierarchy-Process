package view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import events.ChangeComputingUnit;
import events.ChangeConsistencyEvent;
import exceptions.MalformedTreeException;
import exceptions.notFoundException;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Criterium;
import model.CriteriumTree2;
import model.Goal;

public class CriteriumTreeAndDataEnteringPart extends ViewPart {

	// TODO menu with clear

	private Map<TreeBranch, Pane> treeBranchMap;
	private int criteriumIndex;
	private DataEnteringPart dataEnteringPartBuilder;
	private TreeBranch lastModifiedBranch;

	private int getCriteriumIndex() {
		criteriumIndex++;
		return criteriumIndex;
	}

	private final double treeWidth = 700.;
	private final double branchHeight = 40.;
	private final double branchWidth = 110.;

	private final double defTop = 5.;
	private final double defBottom = 10.;
	private final double defLeft = 2.;
	private final double defRight = 2.;

	public CriteriumTreeAndDataEnteringPart(CriteriumTree2 tree, Pane alternativesAndOptionsPart) {
		super(tree);
		alternativesAndOptionsPart.addEventHandler(ChangeConsistencyEvent.CHANGED_MAX_CONSISTENCY, event -> {
			handleConsistenyOfTree();
		});
		alternativesAndOptionsPart.addEventHandler(ChangeConsistencyEvent.CHANGED_NUBER_OF_ALTERNATIVES, event -> {
			List<Goal> lChildren = tree.getLastChildren();
			for (Goal c : lChildren) {
				dataEnteringPartBuilder.refresh(c);
			}
			handleConsistenyOfTree();
			refresh();
		});
		alternativesAndOptionsPart.addEventHandler(ChangeConsistencyEvent.CHANGED_CONSISTENCY_METHOD, event -> {

			dataEnteringPartBuilder.setCcm(event.getCcm());
			// refresh(); TODO all

			handleConsistenyOfTree();
			refresh();

		});
		createTreeBranchList();
		criteriumIndex = 0;
		pane = createPane();
		lastModifiedBranch = (TreeBranch) treeBranchMap.keySet().toArray()[0];
	}

	private void handleConsistenyOfTree() {
		for (Entry<TreeBranch, Pane> e : treeBranchMap.entrySet()) {
			TreeBranch tb = e.getKey();
			if (!tb.getConsistent().equals(ConsistencyLook.UNDEFINIED)) {
				setTreeBranchConsistency(tb);
			}
		}
	}

	private void createTreeBranchList() {
		treeBranchMap = new HashMap<TreeBranch, Pane>();
		createNewBranch(tree.getGoal());
	}

	@Override
	protected Pane createPane() {
		Pane hBox = new HBox();
		VBox vCriteriumTreePane = createCriteriumTreePane();
		Pane vDataEnteringPartPane = createDataEnteringPartPane();
		hBox.getChildren().addAll(vCriteriumTreePane, vDataEnteringPartPane);
		return hBox;
	}

	private VBox createCriteriumTreePane() {
		VBox vCriteriumTreePane = new VBox();
		vCriteriumTreePane.setAlignment(Pos.CENTER);
		ScrollPane criteriumTreePane = getCriteriumTreePane();
		vCriteriumTreePane.getChildren().addAll(createLabel("criteria tree"), criteriumTreePane);
		return vCriteriumTreePane;
	}

	private Pane createDataEnteringPartPane() {
		dataEnteringPartBuilder = new DataEnteringPart(tree);

		VBox vDataEnteringPartPane = new VBox();
		vDataEnteringPartPane.setAlignment(Pos.CENTER);
		Pane dataEnteringPart = dataEnteringPartBuilder.getPart();
		vDataEnteringPartPane.getChildren().addAll(createLabel("comparation values"), dataEnteringPart);

		dataEnteringPart.addEventHandler(ChangeConsistencyEvent.CHANGED_SINGLE_CONSISTENCY, event -> {
			setTreeBranchConsistency(lastModifiedBranch);
			setDataEnteringPartConsistency(lastModifiedBranch);
		});
		return vDataEnteringPartPane;
	}

	private Label createLabel(String labelName) {
		Label label = new Label(labelName);
		label.setFont(Font.font("Verdena", FontWeight.BOLD, 18));
		return label;
	}

	private ScrollPane getCriteriumTreePane() {
		ScrollPane sp = new ScrollPane();
		sp.setPrefSize(treeWidth, DEFAULT_HEIGHT);
		VBox treeBox = new VBox();
		treeBox.setAlignment(Pos.CENTER);
		treeBox.setMinWidth(treeWidth - 20);
		createGoalPane(treeBox);
		sp.setContent(treeBox);
		return sp;
	}

	private void createGoalPane(Pane treeBox) {
		TreeBranch key = (TreeBranch) treeBranchMap.keySet().toArray()[0];
		key.setPrefWidth(treeWidth);
		showNewCriterium(treeBox, key, treeBranchMap.get(key));
	}

	private TreeBranch createNewBranch(Goal c) {
		TreeBranch tb = new TreeBranch(c, this);
		tb.addEventHandler(ChangeComputingUnit.CHANGED_COMPUTING_BRANCH, event -> {
			handleConsistenyOfTree();
			for (Entry<TreeBranch, Pane> e : treeBranchMap.entrySet()) {
				e.getKey().setIsComparing(false);
			}
		});
		HBox hb = new HBox();
		hb.setAlignment(Pos.TOP_CENTER);
		this.treeBranchMap.put(tb, hb);
		return tb;
	}

	private void showNewCriterium(Pane parentPane, TreeBranch tb, Pane box) {
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(defTop, defRight, defBottom, defLeft));
		vBox.getChildren().addAll(tb, box);
		vBox.setAlignment(Pos.TOP_CENTER);
		parentPane.getChildren().add(vBox);
		tb.setMaxWidth(branchWidth);
		tb.setMinSize(branchWidth, branchHeight);
	}

	public void AddChild(TreeBranch parentTreeBranch) {
		try {
			Pane parentPane = treeBranchMap.get(parentTreeBranch);
			Goal pc = parentTreeBranch.getCriterium();
			String cName = "criterium" + getCriteriumIndex();

			Criterium c = new Criterium(pc.getId(), cName);
			tree.addCriteriumTo(pc, c);
			TreeBranch tb = createNewBranch(c);
			showNewCriterium(parentPane, tb, treeBranchMap.get(tb));

			refresh();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		}
	}

	public void removeChild(TreeBranch treeBranch) {
		try {

			Criterium c = (Criterium) treeBranch.getCriterium();
			// Goal parent = tree.getParent(c);
			tree.deleteCriterium(c);
			Pane p = treeBranchMap.get(treeBranch);
			Node pp = p.getParent();
			((Pane) pp.getParent()).getChildren().remove(pp);
			treeBranchMap.remove(treeBranch);

			refresh();
		} catch (MalformedTreeException e) {
			showAlert(e);
		}

	}

	public void renameCriterium(TreeBranch treeBranch, String newName) {
		try {
			tree.renameCriterium(treeBranch.getCriterium(), newName);
		} catch (notFoundException e) {
			showAlert(e);
		} catch (MalformedTreeException e) {
			showAlert(e);
		}
		refresh();
	}

	private void refresh() {
		dataEnteringPartBuilder.refresh();
	}

	public void createComparisons(TreeBranch treeBranch) {
		lastModifiedBranch = treeBranch;
		dataEnteringPartBuilder.createDataEnteringComponents(treeBranch.getCriterium());
		setTreeBranchConsistency(treeBranch);
		setDataEnteringPartConsistency(treeBranch);
	}

	private void setDataEnteringPartConsistency(TreeBranch treeBranch) {
		dataEnteringPartBuilder.showConsistency(treeBranch.getCriterium().getConsistencyValue());
	}

	private void setTreeBranchConsistency(TreeBranch treeBranch) {
		Goal c = treeBranch.getCriterium();
		dataEnteringPartBuilder.computeConsistency(c);

		ConsistencyLook cl;
		if (tree.isConsistent(treeBranch.getCriterium())) {
			cl = ConsistencyLook.CONSISTENT;
		} else {
			cl = ConsistencyLook.NOT_CONSISTENT;
		}
		treeBranch.setConsistencyLook(cl);
	}

}
