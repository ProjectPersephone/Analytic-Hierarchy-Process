package view;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import AHPSolver.XMLCreatorLogic;
import AHPSolver.XMLCreatorLogic2;
import exceptions.AlreadyExistsException;
import exceptions.FIleAlreadyExistsException;
import exceptions.InvalidXMLStructureException;
import exceptions.MalformedTreeException;
import exceptions.UnspecyfiedParameterException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.Alternative;
import model.CriteriumTree2;

public class AlternativesAndOptionsPart extends ViewPart {
	private String sourceFolder;
	private String fileName;

	// private String alternativeName;
	private TextField tfalternativeName;

	public AlternativesAndOptionsPart(CriteriumTree2 tree) {
		super(tree);
		sourceFolder = "src/XML/";
		fileName = "testXML.xml";
		System.out.println("bef: "+tree);
		tree.setAlternatives(FXCollections.observableArrayList());
		pane = createPane();
	}

	@Override
	protected Pane createPane() {
		Pane hBox = new HBox();

		Pane alternativesPane = getAlternativesPane();
		Pane optionsPane = getOptionsPane();

		hBox.getChildren().add(alternativesPane);
		hBox.getChildren().add(optionsPane);
		return hBox;

	}

	private Pane getOptionsPane() {
		GridPane optionsPane = createGridPane();
		Label lOptions = createOptionsLabel();
		optionsPane.getChildren().add(lOptions);

		TextField tfpath = createFileTextField();
		optionsPane.getChildren().add(tfpath);

		Button addAlternativeButton = setCreateButton();
		optionsPane.getChildren().add(addAlternativeButton);
		// TODO creating file (source)
		// TODO consistency value
		// TODO consistency method
		// TODO (optional) adding values method

		return optionsPane;

	}

	private Button setCreateButton() {
		Button bCompute = new Button();
		bCompute.setText("create");
		GridPane.setConstraints(bCompute, 1, 2);
//		System.out.println(tree);
		bCompute.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				try {
					String filePath = sourceFolder + fileName;
					XMLCreatorLogic2 xml;
					xml = new XMLCreatorLogic2();
					System.out.println(tree);
					xml.execute(filePath, tree);
				} catch (ParserConfigurationException e) {
					showAlert(e);
				} catch (FIleAlreadyExistsException e) {
					showAlert(e);
				} catch (IOException e) {
					showAlert(e);
				} catch (TransformerException e) {
					showAlert(e);
				} catch (MalformedTreeException e) {
					showAlert(e);
				}

			}
		});
		return bCompute;
	}

	private TextField createFileTextField() {
		TextField tfFileName = new TextField();
		tfFileName.setText(fileName);
		tfFileName.setPrefColumnCount(10);
		tfFileName.setOnKeyReleased(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {

				// TODO REGEX
				fileName = tfFileName.getText();
			}
		});

		tfFileName.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO

			}
		});

		GridPane.setConstraints(tfFileName, 1, 1);
		return tfFileName;
	}

	private Label createOptionsLabel() {
		Label lAlts = new Label("options");
		GridPane.setConstraints(lAlts, 0, 0);
		return lAlts;
	}

	private Pane getAlternativesPane() {

		GridPane alternativesPane = createGridPane();

		Label lAlts = createAlternativesLabel();
		alternativesPane.getChildren().add(lAlts);

		Text clickToDelete = new Text("click on alternative to delete");
		GridPane.setConstraints(clickToDelete, 0, 1);
		alternativesPane.getChildren().add(clickToDelete);

		ListView<Alternative> list = createAlternativesList();
		alternativesPane.getChildren().addAll(list);

		TextField tfalternativeName = createAlternativeNameTextFile();
		alternativesPane.getChildren().add(tfalternativeName);

		Button addAlternativeButton = createAddAlternativeButton();
		alternativesPane.getChildren().add(addAlternativeButton);

		return alternativesPane;
	}

	private ListView<Alternative> createAlternativesList() {
		ListView<Alternative> list = new ListView<Alternative>();
		list.setItems((ObservableList<Alternative>) tree.getAlternatives());
		list.setMaxHeight(120);
		list.setCellFactory(new Callback<ListView<Alternative>, ListCell<Alternative>>() {
			@Override
			public ListCell<Alternative> call(ListView<Alternative> list) {
				return new ListCell<Alternative>() {
					@Override
					protected void updateItem(Alternative item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							// GridPane grid = new GridPane();
							// grid.setPadding(new Insets(10, 0, 10, 0));
							//
							// ColumnConstraints alt = new ColumnConstraints();
							// alt.setHalignment(HPos.RIGHT);
							// grid.getColumnConstraints().add(alt);
							//
							// ColumnConstraints btn = new ColumnConstraints();
							// btn.setHalignment(HPos.LEFT);
							// grid.getColumnConstraints().add(btn);
							//
							setText(item.getName());
							// Button deleteButton = new Button("delete");
							//
							// GridPane.setConstraints(new Text(item.getName()),
							// 0, 0);
							// GridPane.setConstraints(deleteButton, 1, 0);
							// getChildren().add(grid);
							// set
						}
					}
				};
			}
		});
		GridPane.setConstraints(list, 0, 2);
		VBox.setVgrow(list, Priority.ALWAYS);

		list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Alternative>() {

			public void changed(ObservableValue<? extends Alternative> ov, Alternative old_val, Alternative new_val) {
				// TODO delete alternative
			}
		});

		return list;
	}

	private Label createAlternativesLabel() {
		Label lAlts = new Label("alternatives");
		GridPane.setConstraints(lAlts, 0, 0);
		return lAlts;
	}

	private Button createAddAlternativeButton() {
		Button addAlternativeButton = new Button("add");
		addAlternativeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					createNewAlternative();
					tfalternativeName.setText("");
					
				} catch (AlreadyExistsException | UnspecyfiedParameterException e) {
					e.printStackTrace();
				}
			}
		});
		GridPane.setConstraints(addAlternativeButton, 1, 3);
		return addAlternativeButton;
	}

	private TextField createAlternativeNameTextFile() {
		tfalternativeName = new TextField();
		// tfalternativeName.setText(alternativeName);
		tfalternativeName.setPrefColumnCount(10);
		tfalternativeName.setPromptText("alternative name");

		tfalternativeName.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				try {
					createNewAlternative();
					tfalternativeName.setText("");
				} catch (AlreadyExistsException | UnspecyfiedParameterException e) {
					e.printStackTrace();
				}
			}
		});

		GridPane.setConstraints(tfalternativeName, 0, 3);
		return tfalternativeName;
	}

	private Alternative createNewAlternative() throws AlreadyExistsException, UnspecyfiedParameterException {
		String name = tfalternativeName.getText();
		if (name.equals("")) {
			throw new UnspecyfiedParameterException();
		}
		for (Alternative alt : tree.getAlternatives()) {
			if (alt.getName().equals(name)) {
				throw new AlreadyExistsException();
			}
		}
		Alternative a = new Alternative(name);
		tree.addAlternative(a);
		return a;
	}
}
