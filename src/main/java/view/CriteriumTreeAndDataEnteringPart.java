package view;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import exceptions.MalformedTreeException;
import exceptions.notFoundException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Criterium;
import model.CriteriumTree2;
import model.Goal;

public class CriteriumTreeAndDataEnteringPart extends ViewPart {

	// TODO menu with clear

	// private Goal goal;
	// private List<Alternative> alternatives;
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
			// TODO max consistency changed
			// handleConsistency(lastModifiedBranch);
			System.out.println("TODO max consistency changed");
			// refresh();
		});
		alternativesAndOptionsPart.addEventHandler(ChangeConsistencyEvent.CHANGED_NUBER_OF_CRITERIA, event -> {
			// TODO max consistency changed
			// handleConsistency(lastModifiedBranch);
			// handleConsistenyOfTree();
			refresh();
			System.out.println("TODO whole stage change changed");
			// refresh();
		});
		createTreeBranchList();
		criteriumIndex = 0;
		pane = createPane();
		lastModifiedBranch = (TreeBranch) treeBranchMap.keySet().toArray()[0];
	}

	private void handleConsistenyOfTree() {
		for (Entry<TreeBranch, Pane> e : treeBranchMap.entrySet()) {
			handleConsistency(e.getKey());
		}
	}

	private void createTreeBranchList() {
		treeBranchMap = new HashMap<TreeBranch, Pane>();
		// goal = new Goal("goal");
		createNewBranch(tree.getGoal());
	}

	@Override
	protected Pane createPane() {
		Pane hBox = new HBox();
		ScrollPane criteriumTreePane = getCriteriumTreePane();
		dataEnteringPartBuilder = new DataEnteringPart(tree);
		Pane dataEnteringPart = dataEnteringPartBuilder.getPart();
		hBox.getChildren().addAll(criteriumTreePane, dataEnteringPart);
		dataEnteringPart.addEventHandler(ChangeConsistencyEvent.CHANGED_SINGLE_CONSISTENCY, event -> {
			// TODO max single changed
			System.out.println("TODO single consistency changed");
			handleConsistency(lastModifiedBranch);

		});

		return hBox;

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
		// hBox.toBack();
		vBox.getChildren().addAll(tb, box);
		vBox.setAlignment(Pos.TOP_CENTER);
		parentPane.getChildren().add(vBox);

		tb.setMaxWidth(branchWidth);
		tb.setMinSize(branchWidth, branchHeight);
		// System.out.println(tb.getMaxWidth());
		// box.getChildren().add(tb);

	}

	public void AddChild(TreeBranch parentTreeBranch) {
		try {

			Pane parentPane = treeBranchMap.get(parentTreeBranch);
			Goal pc = parentTreeBranch.getCriterium();
			String cName = "criterium" + getCriteriumIndex();

			Criterium c = new Criterium(pc.getId(), cName);
			tree.addCriteriumTo(pc, c);
			TreeBranch tb = createNewBranch(c);
			// System.out.println(tb.isRemoveButton());
			showNewCriterium(parentPane, tb, treeBranchMap.get(tb));

			refresh();
		} catch (MalformedTreeException e) {
			e.printStackTrace();
		}
	}

	public void removeChild(TreeBranch treeBranch) {
		try {
			tree.deleteCriterium((Criterium) treeBranch.getCriterium());
		} catch (MalformedTreeException e) {
			showAlert(e);
		}
		refresh();
		Pane p = treeBranchMap.get(treeBranch);
		Node pp = p.getParent();
		((Pane) pp.getParent()).getChildren().remove(pp);
		treeBranchMap.remove(treeBranch);

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
		// handleConsistency(lastModifiedBranch,
		// lastModifiedBranch.getCriterium());
	}

	public void createComparisons(TreeBranch treeBranch) {
		lastModifiedBranch = treeBranch;
		dataEnteringPartBuilder.createInputTable(treeBranch.getCriterium());
		handleConsistency(treeBranch);

	}

	private void handleConsistency(TreeBranch treeBranch) {
		System.out.println("handle consistency CTADEP");
		treeBranch.setConsistencyLook(tree.isConsistent(treeBranch.getCriterium()));
		dataEnteringPartBuilder.showConsistency(treeBranch.getCriterium().getConsistencyValue());
	}

}
