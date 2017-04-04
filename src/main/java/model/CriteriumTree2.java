package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import exceptions.FirstStageMustBeGoalException;
import exceptions.MalformedTreeException;
import exceptions.notFoundException;
import priorityVecorComputingMethods.GeometricMeanPriorityVectorComputingMethod;

public class CriteriumTree2 {
	private Map<Goal, List<Criterium>> criteriaTree;
	private Map<Goal, List<Criterium>> alternativesTree;
	private Goal goal;
	private List<Criterium> criteriumAlternativesList;
	private List<Alternative> alternatives;

	private Double maxConsistencyValue;

	public CriteriumTree2(Goal goal, List<Alternative> alternatives)
			throws FirstStageMustBeGoalException, MalformedTreeException {
		if (goal instanceof Goal) {
			this.goal = goal;
			this.criteriaTree = new HashMap<Goal, List<Criterium>>();
			this.criteriaTree.put(goal, new ArrayList<Criterium>());
			this.alternatives = alternatives;
			prepareNewAlternatives();
			this.maxConsistencyValue = 0.1;
		} else {
			throw new FirstStageMustBeGoalException();
		}
	}

	private void prepareNewAlternatives() throws MalformedTreeException {
		alternativesTree = new HashMap<Goal, List<Criterium>>();
		Goal g = new Goal("tmp");
		for (Alternative a : alternatives) {
			Criterium newAlt = new Criterium(g.getId(), a.getName());
			addNewCriteriumToList(criteriumAlternativesList, newAlt);
		}

		// TODO refresh comparations

	}

	public void addNewAlternative(Alternative a) throws MalformedTreeException {
		alternatives.add(a);
		Goal g = new Goal("tmp");
		addNewCriteriumToList(criteriumAlternativesList, new Criterium(g.getId(), a.getName()));
		// TODO refresh comparations
	}

	public List<Criterium> getCriteriumAlternatives(Goal g) throws MalformedTreeException {
		// if (getChildren(g).size()>0) {
		// throw new MalformedTreeException();
		// }
		// List<Criterium> children = new ArrayList<Criterium>();
		// Criterium c = new Criterium(g.getId(), a.getName());
		// children.add(c);
		// }
		// System.out.println(children);
		// criteriaTree.put(g, children);
		// return children;
		return null;
	}

	public void addCriteriumTo(Goal parent, Criterium newCriterium) throws MalformedTreeException {
		if (newCriterium.getParentId() == parent.getId()) {
			List<Criterium> children = criteriaTree.get(parent);
			addNewCriteriumToList(children, newCriterium);
			criteriaTree.put(newCriterium, new ArrayList<Criterium>());
		} else {
			throw new MalformedTreeException();
		}
	}

	public Goal getParent(Criterium c) throws notFoundException {
		for (Entry<Goal, List<Criterium>> entry : criteriaTree.entrySet()) {
			List<Criterium> value = entry.getValue();
			if (value.contains(c)) {
				return entry.getKey();
			}
		}
		throw new notFoundException();
	}

	public List<Criterium> getChildren(Goal g) throws MalformedTreeException {
		List<Criterium> children = criteriaTree.get(g);
		if (children == null) {
			throw new MalformedTreeException();
		}
		return children;
	}

	public void deleteCriterium(Criterium c) throws MalformedTreeException {
		try {
			Goal parent = getParent(c);
			List<Criterium> children = criteriaTree.get(parent);
			for (Criterium cr : children) {
				Map<String, Double> values = cr.getValues();
				values.remove(c.getName());
			}
			criteriaTree.remove(c);

			children.remove(c);
		} catch (notFoundException e) {
			throw new MalformedTreeException();
		}
	}

	public void renameCriterium(Goal criterium, String newName) throws notFoundException, MalformedTreeException {
		if (criterium instanceof Criterium) {
			Goal parent = getParent((Criterium) criterium);
			for (Criterium c : getChildren(parent)) {
				Map<String, Double> values = c.getValues();
				Double v = values.get(criterium.getName());
				values.remove(criterium.getName());
				c.addValuesOf(newName, v);
			}
		}
		criterium.setName(newName);
	}

	public void changeValue(Criterium c, String name, Double value) {

		if (c.getValues().containsKey(name)) {
			c.getValues().put(name, value);
		} else {
			// TODO refresh
		}
	}

	private void addNewCriteriumToList(List<Criterium> criteriumList, Criterium newCriterium) {
		criteriumList.add(newCriterium);
		for (Criterium alt : criteriumList) {
			alt.addValuesOf(newCriterium.getName(), 1);
			newCriterium.addValuesOf(alt.getName(), 1);
		}
	}

	public Double getMaxConsistencyValue() {
		return maxConsistencyValue;
	}

	public void setMaxConsistencyValue(Double maxConsistencyValue) {
		this.maxConsistencyValue = maxConsistencyValue;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public Map<Goal, List<Criterium>> getCriteriaTree() {
		return criteriaTree;
	}

	public List<Criterium> getCriteriumAlternativesList() {
		return criteriumAlternativesList;
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}

	@Override
	public String toString() {
		return "tree: " + criteriaTree;
	}

	public boolean isConsistent(Goal goal) {
		if (maxConsistencyValue >= goal.getConsistencyValue()) {
			return true;
		}
		return false;
	}

}
