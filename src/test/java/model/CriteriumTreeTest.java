package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import exceptions.FirstStageMustBeGoalException;
import exceptions.InvalidParentIdInTreeException;
import exceptions.MalformedTreeException;

public class CriteriumTreeTest {

	@Test
	public void test() throws MalformedTreeException, FirstStageMustBeGoalException, InvalidParentIdInTreeException {
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
		Criterium c1 = new Criterium(goal.getId(), "c1");
		c1.setValue(values1);
		Criterium c2 = new Criterium(goal.getId(), "c2");
		c2.setValue(values1);
		Criterium c3 = new Criterium(c1.getId(), "c3");
		c3.setValue(values2);
		Criterium c4 = new Criterium(c2.getId(), "c4");
		c4.setValue(values2);

		List<Criterium> criteria = new ArrayList<Criterium>();
		criteria.add(c1);
		criteria.add(c2);
		ct.addStage(criteria);
		ct.addCriteriumToNewStage(c3);
		ct.addCriteriumToStage(2, c4);

		// System.out.println("n: "+ct.getNumberOfStages());
		// System.out.println("s0 criteria: "+ct.getStageCriteria(0));
		// System.out.println("s1 criteria: "+ct.getStageCriteria(1));
		// System.out.println("s2 criteria: "+ct.getStageCriteria(2));

		assertEquals(ct.getStageCriteria(0).get(0), goal);
		assertEquals(ct.getStageCriteria(1).get(0), c1);
		assertEquals(ct.getStageCriteria(1).get(1), c2);
		assertEquals(ct.getStageCriteria(2).get(0), c4);
		assertEquals(ct.getStageCriteria(2).get(1), c3);
		assertEquals(ct.getNumberOfStages(), 3);

	}

}
