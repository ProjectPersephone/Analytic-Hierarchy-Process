package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import exceptions.FirstStageMustBeGoalException;
import exceptions.InvalidParentIdInTreeException;
import exceptions.MalformedTreeException;

public class CriteriumTree {

	private Map<Goal, Integer> criteria;
	private int numberOfStages;
	private List<Alternative> alternatives;

	public CriteriumTree(Goal goal, List<Alternative> alternatives) throws FirstStageMustBeGoalException {
		if (goal instanceof Goal) {
			// System.out.println(goal);
			this.criteria = new HashMap<Goal, Integer>();
			this.criteria.put(goal, 0);
			this.alternatives = alternatives;
			this.numberOfStages = 1;
		} else {
			throw new FirstStageMustBeGoalException();
		}
	}

	public void addStage(List<Criterium> criteriaList) throws InvalidParentIdInTreeException, MalformedTreeException {
		for (Criterium c : criteriaList) {
			validParentId(c, numberOfStages);
			criteria.put(c, numberOfStages);
		}
		numberOfStages++;
	}

	public void addCriteriumToNewStage(Criterium c) throws InvalidParentIdInTreeException, MalformedTreeException {
		validParentId(c, numberOfStages);
		criteria.put(c, numberOfStages);
		numberOfStages++;
	}

	public void addCriteriumToStage(int stage, Criterium c)
			throws MalformedTreeException, InvalidParentIdInTreeException {
		if ((stage < 1) || (stage > numberOfStages)) {
			throw new MalformedTreeException();
		} else {
			validParentId(c, stage);
			criteria.put(c, stage);
		}

	}

	public int getStage(Goal g) {
		return criteria.get(g);
	}

	public List<Goal> getChildren(Goal g) {
		List<Goal> lGoal = new ArrayList<Goal>();
		for(Goal criterium : getStageCriteria(getStage(g)+1)){
			if(((Criterium)criterium).getParentId() == g.getId()){
				lGoal.add(criterium);
			}
		}
		return lGoal;
	}

	public List<Goal> getStageCriteria(Integer stage) {
		List<Goal> lGoal = new ArrayList<Goal>();
		for (Entry<Goal, Integer> entry : criteria.entrySet()) {
			Goal key = entry.getKey();
			Integer value = entry.getValue();
			// System.out.println("key: "+key+", value: "+value);
			if (value.equals(stage)) {
				lGoal.add(key);
			}
		}
		return lGoal;
	}

	public int getNumberOfStages() {
		return numberOfStages;
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}

	private void validParentId(Criterium c, int stage) throws InvalidParentIdInTreeException {

		for (Goal crit : getStageCriteria(stage - 1)) {
			if (crit.getId() == c.getParentId()) {
				return;
			}
		}
		throw new InvalidParentIdInTreeException();
	}
}
