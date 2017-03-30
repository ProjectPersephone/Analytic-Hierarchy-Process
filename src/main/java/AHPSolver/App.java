package AHPSolver;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import model.Criterium;
import model.PriorityVector;
import priorityVecorComputingMethods.PriorityVectorComputeMethod;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {

	public static void main(String argv[]) {

		try {

			String filePath = "src/XML/testXML.xml";
			AHPLogic logic = new AHPLogic();
			Document doc = logic.getXMLDocument(filePath);
			Node goal = logic.getGoal(doc);
			Node alternatives = logic.getAlternatives(doc);

			PriorityVector spv = logic.execute(goal, PriorityVectorComputeMethod.simpleMethod());
			PriorityVector gpv = logic.execute(goal, PriorityVectorComputeMethod.geometricMethod());
			PriorityVector epv = logic.execute(goal, PriorityVectorComputeMethod.eigenvectorMethod());

			System.out.println("---Simple---");
			logic.printResults(alternatives, spv);
			System.out.println();

			System.out.println("---Geometric---");
			logic.printResults(alternatives, gpv);
			System.out.println();

			System.out.println("---Eigenvector---");
			logic.printResults(alternatives, epv);
			System.out.println();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}