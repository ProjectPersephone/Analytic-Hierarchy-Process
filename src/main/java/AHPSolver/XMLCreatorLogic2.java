package AHPSolver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import exceptions.FileAlreadyExistsException;
import exceptions.MalformedTreeException;
import model.Alternative;
import model.Criterium;
import model.CriteriumTree;
import model.CriteriumTree2;
import model.Goal;

public class XMLCreatorLogic2 {

	private Document doc;
	private Element rootElement;

	public XMLCreatorLogic2() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		rootElement = doc.createElement("ahp");
	}

	public String execute(String path, CriteriumTree2 cTree) throws FileAlreadyExistsException, IOException,
			ParserConfigurationException, TransformerException, MalformedTreeException {

		createAlternatives(cTree.getAlternatives());
		createCriteria(cTree);
		createXMLFile(path);
		return path;

	}

	private void createCriteria(CriteriumTree2 cTree) throws MalformedTreeException {

		System.out.println(cTree);
		// Goal g = cTree.getGoal();
		// Element goalElement = createGoal(g);

		for (Entry<Goal, List<Criterium>> entry : cTree.getCriteriaTree().entrySet()) {
			// List<Criterium> value = entry.getValue();
			Goal mainC = entry.getKey();
//			List<Criterium> children = cTree.getChildren(mainC);
//
//			Element newElement = createElement(mainC);
//			// goalElement.appendChild(newElement);
//			for (Criterium c : children) {
//				Element e = createCriterium(c);
//				System.out.println(newElement + " " + mainC + " appendChild  " + e + " " + c);
//				newElement.appendChild(e);
//			}
			Element newElement = createElement(mainC);
			createRecursiveElements(cTree, mainC, newElement);

		}

		// Map<Criterium, Element> ceMap = createCriteriumElementMap(cTree);
		// for (Criterium key1 : ceMap.keySet()) {
		// for (Criterium key2 : ceMap.keySet()) {
		// if (key1.getId() == key2.getParentId()) {
		// ceMap.get(key1).appendChild(ceMap.get(key2));
		// } else if (key2.getParentId() == 0) {
		// eGoal.appendChild(ceMap.get(key2));
		// }
		// }
		// }

	}
	
	private void createRecursiveElements(CriteriumTree2 cTree, Goal criterium, Element element) throws MalformedTreeException{
		
		List<Criterium> children = cTree.getChildren(criterium);
		for (Criterium c : children) {
			Element e = createCriterium(c);
			element.appendChild(e);
			createRecursiveElements(cTree, c, e);
		}
		
	}

//	private Map<Criterium, Element> createCriteriumElementMap(CriteriumTree cTree) {
//		Map<Criterium, Element> ceMap = new HashMap<Criterium, Element>();
//		int i = 1;
//		while (!cTree.getStageCriteria(i).isEmpty()) {
//			List<Goal> cList = cTree.getStageCriteria(i);
//			for (Goal g : cList) {
//				Criterium c = (Criterium) g;
//				Element eCriterium = createCriterium(c);
//				ceMap.put(c, eCriterium);
//			}
//			i++;
//		}
//		return ceMap;
//	}

	private Element createElement(Goal c) {
		if (c instanceof Criterium) {
			System.out.println("crit: " + c);
			return createCriterium((Criterium) c);
		} else {
			System.out.println("goal: " + c);
			return createGoal(c);
		}
	}

	private Element createCriterium(Criterium c) {
		Element eCriterium = doc.createElement("criteria");
		eCriterium.setAttribute("name", c.getName().replace(" ", "_"));

		for (Entry<String, Double> entry : c.getValues().entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue();
			
			eCriterium.setAttribute(key.replace(" ", "_"), String.valueOf(value).replace(" ", "_"));
		}
		return eCriterium;
	}

	private Element createGoal(Goal goal) {
		Element eGoal = doc.createElement("goal");
		eGoal.setAttribute("name", goal.getName().replace(" ", "_"));
		eGoal.appendChild(doc.createTextNode("\n"));
		rootElement.appendChild(eGoal);
		return eGoal;
	}

	private void createXMLFile(String path)
			throws FileAlreadyExistsException, IOException, ParserConfigurationException, TransformerException {
		if (checkFileExists(path)) {
			throw new FileAlreadyExistsException("File already exists.");
		}

		rootElement.appendChild(doc.createTextNode("\n"));
		doc.appendChild(rootElement);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(path));

		transformer.transform(source, result);
	}

	private void createAlternatives(List<Alternative> list) throws ParserConfigurationException {

		Element alts = doc.createElement("alternatives");
		alts.appendChild(doc.createTextNode("\n"));
		rootElement.appendChild(alts);

		for (int i = 0; i < list.size(); i++) {
			Element alt = doc.createElement("alternative");
			Alternative a = list.get(i);
			alt.setAttribute("name", a.getName().replace(" ", "_"));
			alts.appendChild(alt);
		}
	}

	private boolean checkFileExists(String path) {
		File f = new File(path);
		if (f.exists()) {
			return true;
		}
		return false;
	}
}
