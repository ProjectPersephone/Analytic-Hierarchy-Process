package AHPSolver;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exceptions.FileAlreadyExistsException;
import exceptions.FirstStageMustBeGoalException;
import exceptions.InvalidParentIdInTreeException;
import exceptions.MalformedTreeException;
import model.Alternative;
import model.Criterium;
import model.CriteriumTree;
import model.Goal;

public class XMLCreatorLogicTest {

	@Before
	public void before() {
		String filePath = "src/XML/testXML.xml";

		File file = new File(filePath);
		file.delete();

	}

	@Test
	public void createXMLTest()
			throws FirstStageMustBeGoalException, MalformedTreeException, ParserConfigurationException,
			FileAlreadyExistsException, IOException, TransformerException, InvalidParentIdInTreeException {
		Goal goal = new Goal("goal");
		Alternative alt1 = new Alternative("a1");
		Alternative alt2 = new Alternative("a2");
		Alternative[] aArray = new Alternative[] { alt1, alt2 };
		List<Alternative> alts = Arrays.asList(aArray);
		CriteriumTree ct = new CriteriumTree(goal, alts);

		Map<String, Double> values1 = new HashMap<String, Double>();
		values1.put("c1", 1.);
		values1.put("c2", 2.);

		Map<String, Double> values2 = new HashMap<String, Double>();
		values2.put("c3", 2.);
		values2.put("c4", 4.);
		values2.put("c5", 6.);

		Map<String, Double> valuesA = new HashMap<String, Double>();
		valuesA.put("a1", 1.);
		valuesA.put("a2", 2.);

		Criterium c1 = new Criterium(goal.getId(), "c1");
		c1.setValue(values1);
		Criterium c2 = new Criterium(goal.getId(), "c2");
		c2.setValue(values1);
		
		Criterium c3 = new Criterium(c1.getId(), "c3");
		c3.setValue(values2);
		Criterium c4 = new Criterium(c2.getId(), "c4");
		c4.setValue(values2);
		Criterium c5 = new Criterium(c2.getId(), "c5");
		c5.setValue(values2);
		

		Criterium a1_3 = new Criterium(c3.getId(), "a1");
		a1_3.setValue(valuesA);
		Criterium a2_3 = new Criterium(c3.getId(), "a2");
		a2_3.setValue(valuesA);
		
		Criterium a1_4 = new Criterium(c4.getId(), "a1");
		a1_4.setValue(valuesA);
		Criterium a2_4 = new Criterium(c4.getId(), "a2");
		a2_4.setValue(valuesA);
		
		Criterium a1_5 = new Criterium(c5.getId(), "a1");
		a1_5.setValue(valuesA);
		Criterium a2_5 = new Criterium(c5.getId(), "a2");
		a2_5.setValue(valuesA);

		List<Criterium> criteria = new ArrayList<Criterium>();
		criteria.add(c1);
		criteria.add(c2);
		ct.addStage(criteria);
		ct.addCriteriumToNewStage(c3);
		ct.addCriteriumToStage(2, c4);
		ct.addCriteriumToStage(2, c5);

		ct.addCriteriumToStage(3, a1_3);
		ct.addCriteriumToStage(3, a2_3);
		ct.addCriteriumToStage(3, a1_4);
		ct.addCriteriumToStage(3, a2_4);
		ct.addCriteriumToStage(3, a1_5);
		ct.addCriteriumToStage(3, a2_5);
		
//		System.out.println("n: " + ct.getNumberOfStages());
//		System.out.println("s0 criteria: " + ct.getStageCriteria(0));
//		System.out.println("s1 criteria: " + ct.getStageCriteria(1));
//		System.out.println("s2 criteria: " + ct.getStageCriteria(2));
//		System.out.println("s3 criteria: " + ct.getStageCriteria(3));

		String filePath = "testXML.xml";
		XMLCreatorLogic xml = new XMLCreatorLogic();
		xml.execute(filePath, ct);

	}

}
