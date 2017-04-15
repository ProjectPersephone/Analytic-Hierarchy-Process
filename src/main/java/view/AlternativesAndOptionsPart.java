package view;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import AHPSolver.XMLCreatorLogic2;
import consistencyComputingMethods.ConsistencyComputeMethod;
import dataEnteringType.DataEnteringType;
import events.ChangeConsistencyEvent;
import exceptions.AlreadyExistsException;
import exceptions.FileAlreadyExistsException;
import exceptions.MalformedTreeException;
import exceptions.UnspecyfiedParameterException;
import exceptions.WrongFileNameException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.Alternative;
import model.CriteriumTree2;
import priorityVecorComputingMethods.PriorityVectorComputeMethod;

public class AlternativesAndOptionsPart extends ViewPart {
	private String sourceFolder;
	private String fileName;
	private ListView<Alternative> list;
	// private String alternativeName;
	private TextField tfalternativeName;

	public AlternativesAndOptionsPart(CriteriumTree2 tree) {
		super(tree);
		sourceFolder = "XML/";
		fileName = "testXML.xml";
		tree.setAlternatives(FXCollections.observableArrayList());
		tree.setMaxConsistencyValue(0.1);
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

		// 0
		Label lOptions = createOptionsLabel();
		optionsPane.getChildren().add(lOptions);

		// 1
		Label maxConsistencyLabel = createMaxConsistencyLabel();
		optionsPane.getChildren().add(maxConsistencyLabel);

		TextField tfConsistencyValue = createMaxConsistencyValueTextField();
		optionsPane.getChildren().add(tfConsistencyValue);

		// 2
		Label consistencyMethodLabel = createConsistencyMethodsLabel();
		optionsPane.getChildren().add(consistencyMethodLabel);

		ComboBox<ConsistencyComputeMethod> cMethods = createConsistencyMethodsComboBox();
		optionsPane.getChildren().add(cMethods);

		//TODO change to 3 

		// 4
		Label sourceFolderLabel = createSourceFolderLabel();
		optionsPane.getChildren().add(sourceFolderLabel);

		TextField tfpath = createFileTextField();
		optionsPane.getChildren().add(tfpath);

		// 5
		Button addAlternativeButton = setCreateXMLButton();
		optionsPane.getChildren().add(addAlternativeButton);

		return optionsPane;

	}

	

	private ComboBox<ConsistencyComputeMethod> createConsistencyMethodsComboBox() {

		ObservableList<ConsistencyComputeMethod> options = FXCollections
				.observableArrayList(ConsistencyComputeMethod.maximumEigenvalueMethod());
		ComboBox<ConsistencyComputeMethod> cbMethods = new ComboBox<ConsistencyComputeMethod>(options);

		cbMethods.setValue(ccm);
		cbMethods.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ccm = cbMethods.getValue();
			}
		});
		GridPane.setConstraints(cbMethods, 1, 2);
		return cbMethods;
	}



	private Label createConsistencyMethodsLabel() {
		Label lComputingMethod = new Label("consistency method ");
		GridPane.setConstraints(lComputingMethod, 0, 2);
		return lComputingMethod;
	}

	private Label createMaxConsistencyLabel() {
		Label lSourceFolder = new Label("max consistency ");
		GridPane.setConstraints(lSourceFolder, 0, 1);
		return lSourceFolder;
	}

	private Label createSourceFolderLabel() {
		Label lSourceFolder = new Label(sourceFolder);
		GridPane.setConstraints(lSourceFolder, 0, 4);
		return lSourceFolder;
	}

	private Button setCreateXMLButton() {
		Button bCompute = new Button();
		bCompute.setText("create XML");
		bCompute.setMinWidth(DEFAULT_BUTTON_WIDTH);
		GridPane.setConstraints(bCompute, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
		bCompute.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				try {
					String filePath = sourceFolder + fileName;
					String regex = "^(\\/{0,1}[a-zA-Z0-9-_]+)+\\.xml$";
					if (filePath.matches(regex)) {
						XMLCreatorLogic2 xml;
						xml = new XMLCreatorLogic2();
						xml.execute(filePath, tree);
						System.out.println("XML createrd");
					} else {
						throw new WrongFileNameException("File path is invalid.");
					}

				} catch (ParserConfigurationException e) {
					showAlert(e);
				} catch (FileAlreadyExistsException e) {
					showAlert(e);
				} catch (IOException e) {
					showAlert(e);
				} catch (TransformerException e) {
					showAlert(e);
				} catch (MalformedTreeException e) {
					showAlert(e);
				} catch (WrongFileNameException e) {
					showAlert(e);
				}

			}
		});
		return bCompute;
	}

	private TextField createMaxConsistencyValueTextField() {
		TextField tf = new TextField();
		tf.setText(fileName);
		tf.setPrefColumnCount(10);

		tf.setText(tree.getMaxConsistencyValue().toString());
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String regex = "[0-9]{0,3}\\.{0,1}\\d{0,10}";
				if (newValue.matches(regex)) {
					tree.setMaxConsistencyValue(Double.parseDouble(newValue));
					pane.fireEvent(new ChangeConsistencyEvent(ChangeConsistencyEvent.CHANGED_MAX_CONSISTENCY));
				} else {
					tf.setText(oldValue);
				}
			}

		});

		GridPane.setConstraints(tf, 1, 1);
		return tf;
	}

	private TextField createFileTextField() {
		TextField tf = new TextField();
		tf.setText(fileName);
		tf.setPrefColumnCount(10);

		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				fileName = tf.getText();
			}
		});

		GridPane.setConstraints(tf, 1, 4);
		return tf;
	}

	private Label createOptionsLabel() {
		Label label = new Label("options");
		GridPane.setConstraints(label, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);
		label.setFont(Font.font("Verdena", FontWeight.BOLD, 18));
		return label;
	}

	private Pane getAlternativesPane() {

		GridPane alternativesPane = createGridPane();

		Label lAlts = createAlternativesLabel();
		alternativesPane.getChildren().add(lAlts);

		ListView<Alternative> list = createAlternativesList();
		alternativesPane.getChildren().addAll(list);

		TextField tfalternativeName = createAlternativeNameTextFile();
		alternativesPane.getChildren().add(tfalternativeName);

		Button addAlternativeButton = createAddAlternativeButton();
		alternativesPane.getChildren().add(addAlternativeButton);

		Button removeAlternativeButton = createRemoveAlternativeButton();
		alternativesPane.getChildren().add(removeAlternativeButton);

		return alternativesPane;
	}

	private ListView<Alternative> createAlternativesList() {
		list = new ListView<Alternative>();
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
							setText(item.getName());
						}
						if (empty) {
							setText(null);
						}
					}
				};
			}
		});
		GridPane.setConstraints(list, 0, 2, 2, 1);
		// GridPane.setConstraints(list, 0, 2);
		VBox.setVgrow(list, Priority.ALWAYS);

		// list.getSelectionModel().selectedItemProperty().addListener(new
		// ChangeListener<Alternative>() {
		//
		// public void changed(ObservableValue<? extends Alternative> ov,
		// Alternative old_val, Alternative new_val) {
		// System.out.println(old_val+" "+new_val);
		//// list.getItems().remove(index);
		//// tree.deleteAlternative(new_val);
		//// list.refresh();
		// // TODO delete alternative
		// // TODO pane.fireEvent(new
		// //
		// ChangeConsistencyEvent(ChangeConsistencyEvent.CHANGED_NUBER_OF_CRITERIA));
		// }
		// });
		return list;
	}

	private Label createAlternativesLabel() {
		Label lAlts = new Label("alternatives");
		GridPane.setConstraints(lAlts, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);
		lAlts.setFont(Font.font("Verdena", FontWeight.BOLD, 18));
		return lAlts;
	}

	private Button createAddAlternativeButton() {
		Button addAlternativeButton = new Button("add");
		addAlternativeButton.setMinWidth(DEFAULT_BUTTON_WIDTH);
		addAlternativeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				addAlternativeHandle();
			}
		});
		GridPane.setConstraints(addAlternativeButton, 0, 4);
		return addAlternativeButton;
	}

	private Button createRemoveAlternativeButton() {
		Button addAlternativeButton = new Button("remove selected");

		addAlternativeButton.setMinWidth(DEFAULT_BUTTON_WIDTH);
		addAlternativeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Alternative selected = list.getSelectionModel().getSelectedItem();
				int cs = list.getSelectionModel().getSelectedIndex();
				System.out.println("selected: " + selected);
				if (selected != null) {
					tree.deleteAlternative(selected);
					list.getItems().remove(selected);
					final int newSelectedIdx = (cs == list.getItems().size() - 1) ? cs - 1 : cs;

					list.getSelectionModel().select(newSelectedIdx);
					// System.out.println(list.getItems());
					// list.refresh();
					pane.fireEvent(new ChangeConsistencyEvent(ChangeConsistencyEvent.CHANGED_NUBER_OF_CRITERIA));
				}

			}
		});
		GridPane.setConstraints(addAlternativeButton, 1, 4);
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
				addAlternativeHandle();
			}
		});

		GridPane.setConstraints(tfalternativeName, 0, 3, 2, 1);
		return tfalternativeName;
	}

	private Alternative createNewAlternative()
			throws AlreadyExistsException, UnspecyfiedParameterException, MalformedTreeException {
		String name = tfalternativeName.getText();
		if (name.equals("")) {
			throw new UnspecyfiedParameterException();
		}
		for (Alternative alt : tree.getAlternatives()) {
			if (alt.getName().equals(name)) {
				throw new AlreadyExistsException("This alternative already exists");
			}
		}
		Alternative a = new Alternative(name);
		tree.addNewAlternative(a);
		pane.fireEvent(new ChangeConsistencyEvent(ChangeConsistencyEvent.CHANGED_NUBER_OF_CRITERIA));
		return a;
	}

	private void addAlternativeHandle() {
		try {
			createNewAlternative();
			tfalternativeName.setText("");
		} catch (AlreadyExistsException e) {
			showAlert(e);
		} catch (UnspecyfiedParameterException e) {
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			showAlert(e);
		}
	}
}
