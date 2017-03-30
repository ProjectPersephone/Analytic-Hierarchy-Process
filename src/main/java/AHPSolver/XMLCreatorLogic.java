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

import exceptions.FIleAlreadyExistsException;
import model.Alternative;
import model.Criterium;
import model.CriteriumTree;
import model.Goal;

public class XMLCreatorLogic {

	private Document doc;
	private Element rootElement;

	public XMLCreatorLogic() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		rootElement = doc.createElement("ahp");
	}

	public String execute(String path, CriteriumTree cTree)
			throws FIleAlreadyExistsException, IOException, ParserConfigurationException, TransformerException {

		createAlternatives(cTree.getAlternatives());
		createCriteria(cTree);
		createXMLFile(path);
		return path;

	}

	private void createCriteria(CriteriumTree cTree) {

		Goal goal = cTree.getStageCriteria(0).get(0);
		Element eGoal = createGoal(goal);
		Map<Criterium, Element> ceMap = createCriteriumElementMap(cTree);
		for (Criterium key1 : ceMap.keySet()) {
			for (Criterium key2 : ceMap.keySet()) {
				if (key1.getId() == key2.getParentId()) {
					ceMap.get(key1).appendChild(ceMap.get(key2));
				} else if (key2.getParentId() == 0) {
					eGoal.appendChild(ceMap.get(key2));
				}
			}
		}

	}

	private Map<Criterium, Element> createCriteriumElementMap(CriteriumTree cTree) {
		Map<Criterium, Element> ceMap = new HashMap<Criterium, Element>();
		int i = 1;
		while (!cTree.getStageCriteria(i).isEmpty()) {
			List<Goal> cList = cTree.getStageCriteria(i);
			for (Goal g : cList) {
				Criterium c = (Criterium) g;
				Element eCriterium = createCriterium(c);
				ceMap.put(c, eCriterium);
			}
			i++;
		}
		return ceMap;
	}

	private Element createCriterium(Criterium c) {
		Element eCriterium = doc.createElement("criteria");
		eCriterium.setAttribute("name", c.getName());
		
		for (Entry<String, Integer> entry : c.getValues().entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			eCriterium.setAttribute(key, String.valueOf(value));
		}
		return eCriterium;
	}

	private Element createGoal(Goal goal) {
		Element eGoal = doc.createElement("goal");
		eGoal.setAttribute("name", goal.getName());
		eGoal.appendChild(doc.createTextNode("\n"));
		rootElement.appendChild(eGoal);
		return eGoal;
	}

	private void createXMLFile(String path)
			throws FIleAlreadyExistsException, IOException, ParserConfigurationException, TransformerException {
		if (checkFileExists(path)) {
			throw new FIleAlreadyExistsException();
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
			alt.setAttribute("name", a.getName());
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
