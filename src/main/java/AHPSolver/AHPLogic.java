package AHPSolver;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Jama.*;
import exceptions.InvalidXMLStructureException;
import model.ComparisonMatrix;
import model.PriorityVector;
import priorityVecorComputingMethods.PriorityVectorComputeMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AHPLogic {

	public Document getXMLDocument(String filePath) throws SAXException, IOException, ParserConfigurationException {

		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		return doc;

		// for (int temp = 0; temp < nList.getLength(); temp++) {
		//
		// Node nNode = nList.item(temp);
		//
		// System.out.println("\nCurrent Element :" + nNode.getNodeName());
		//
		// if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		//
		// Element eElement = (Element) nNode;
		// NodeList childrens = eElement.getElementsByTagName("criteria");
		// System.out.println("value: "+eElement.getAttribute("name")+" | id: "
		// + eElement.getAttribute("value"));
		// if(childrens.getLength()>0){
		// System.out.println(":
		// "+((Element)eElement.getElementsByTagName("criteria").item(0)).getAttribute("name"));
		//
		// }
		//// System.out.println(
		//// "First Name : " +
		// eElement.getElementsByTagName("firstname").item(0).getTextContent());
		//// System.out.println(
		//// "Last Name : " +
		// eElement.getElementsByTagName("lastname").item(0).getTextContent());
		//// System.out.println(
		//// "Nick Name : " +
		// eElement.getElementsByTagName("nickname").item(0).getTextContent());
		//// System.out.println("Salary : " +
		// eElement.getElementsByTagName("salary").item(0).getTextContent());
		//
		// }
		// }
		// System.out.println("-----------END-----------");
	}

	public Node getAlternatives(Document doc) throws InvalidXMLStructureException {
		NodeList altList = doc.getElementsByTagName("alternatives");
		if (altList.getLength() == 1) {
			return altList.item(0);
		} else {
			throw new InvalidXMLStructureException();
		}
	}

	public Node getGoal(Document doc) throws InvalidXMLStructureException {
		NodeList goalList = doc.getElementsByTagName("goal");
		if (goalList.getLength() == 1) {
			return goalList.item(0);
		} else {
			throw new InvalidXMLStructureException();
		}
	}

	public PriorityVector execute(Node goal, PriorityVectorComputeMethod method) {
		return recursive(goal, method);
	}

	private PriorityVector recursive(Node n, PriorityVectorComputeMethod method) {
		NodeList nList = n.getChildNodes();

		// List<Double> weightVector = new ArrayList<Double>();
		List<PriorityVector> priorityVectors = new ArrayList<PriorityVector>();

		List<String> nNames = getCriteriaNames(nList);
		Matrix matrix = new Matrix(nNames.size(), nNames.size());

		int row = 0;
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				PriorityVector pv = recursive(nNode, method);
				// System.out.println("pv: "+pv);
				priorityVectors.add(pv);
				Element criteria = (Element) nNode;

				// double value =
				// Double.parseDouble(criteria.getAttribute("value"));
				// weightVector.add(value);
				double criteriumValue = Double.parseDouble(criteria.getAttribute(nNames.get(row)));
				for (int col = 0; col < nNames.size(); col++) {
					double value = Double.parseDouble(criteria.getAttribute(nNames.get(col)));
					matrix.set(row, col, value / criteriumValue);

				}
				row++;
			}

		}
		if (nNames.size() > 0) {
			String criteriumName = ((Element) n).getAttribute("name");
			// ComparisonMatrix cMatrix = new ComparisonMatrix(weightVector,
			// criteriumName);
			ComparisonMatrix cMatrix = new ComparisonMatrix(matrix, criteriumName);
			// System.out.println("MATRIX: " + cMatrix);
			PriorityVector currentPrirityVector = cMatrix.getProrityVector(method);
			if (priorityVectors.get(0).get(0) == 0) {
				return currentPrirityVector;
			}
			// System.out.println("CURRENT PRIORITY VECTOR:
			// "+currentPrirityVector);
			List<PriorityVector> vectorsToSum = new ArrayList<PriorityVector>();

			for (int it = 0; it < priorityVectors.size(); it++) {
				// System.out.println("VECTORS TO MULTIPLE:
				// "+priorityVectors.get(it));
				vectorsToSum.add(priorityVectors.get(it).multiplyBy(currentPrirityVector.get(it)));
			}
			// System.out.println("VECTORS TO SUM: "+vectorsToSum);
			PriorityVector newVector = new PriorityVector(criteriumName, vectorsToSum);
			// System.out.println("NEW VECTOR: "+newVector);
			return newVector;
		} else {
			return new PriorityVector();
		}
	}

	private List<String> getCriteriaNames(NodeList nList) {
		List<String> nNames = new ArrayList<String>();
		for (int it = 0; it < nList.getLength(); it++) {
			Node nNode = nList.item(it);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nList.item(it);
				nNames.add(e.getAttribute("name"));
			}
		}
		return nNames;
	}

	public void printResults(Node alternatives, PriorityVector spv) throws InvalidXMLStructureException {
//		NodeList nList = alternatives.getChildNodes();
//		List<String> alts = new ArrayList<String>();
//		for (int i = 0; i < nList.getLength(); i++) {
//			Node nNode = nList.item(i);
//			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//				alts.add(((Element) nNode).getAttribute("name"));
//			}
//		}
		List<String> alts = getAlternativesNames(alternatives);
		for (int j = 0; j < alts.size(); j++) {
			System.out.println(String.format("%4s", alts.get(j)) + ": " + String.format("%.5f", spv.get(j)));
		}

	}

	public List<String> getAlternativesNames(Node alternatives) throws InvalidXMLStructureException {

		NodeList nList = alternatives.getChildNodes();
		List<String> alts = new ArrayList<String>();
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				alts.add(((Element) nNode).getAttribute("name"));
			}
		}
		return alts;
	}

}
