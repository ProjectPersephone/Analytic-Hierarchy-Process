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
	private List<Criterium> alternativeAsCriteriumList;
	private List<Alternative> alternatives;

	private Double maxConsistencyValue;

	public CriteriumTree2(Goal goal, List<Alternative> alternatives)
			throws FirstStageMustBeGoalException, MalformedTreeException {
		if (goal instanceof Goal) {
			this.goal = goal;
			this.criteriaTree = new HashMap<Goal, List<Criterium>>();
			this.criteriaTree.put(goal, new ArrayList<Criterium>());
			this.alternatives = alternatives;
			alternativesTree = new HashMap<>();
			prepareNewAlternatives();
			this.maxConsistencyValue = 0.1;
		} else {
			throw new FirstStageMustBeGoalException();
		}
	}

	private void prepareNewAlternatives() throws MalformedTreeException {
		alternativeAsCriteriumList = new ArrayList<Criterium>();
		Goal g = new Goal("tmp");
		for (Alternative a : alternatives) {
			Criterium newAlt = new Criterium(g.getId(), a.getName());
			addNewCriteriumToListAndCriteriaValueList(alternativeAsCriteriumList, newAlt);
		}

		// TODO refresh comparations

	}

	public void addNewAlternative(Alternative a) throws MalformedTreeException {
		alternatives.add(a);
		Goal g = new Goal("tmp");
		Criterium c = new Criterium(g.getId(), a.getName());
		addNewCriteriumToListAndCriteriaValueList(alternativeAsCriteriumList, c);

		for(Entry<Goal, List<Criterium>> e : alternativesTree.entrySet()){
			Criterium newC = new Criterium(c);
			newC.setParentId(e.getKey().getId());
			addNewCriteriumToListAndCriteriaValueList(e.getValue(), newC);
		}

		// TODO refresh comparations
	}

	public List<Criterium> getChildren(Goal g) throws MalformedTreeException {
		if (criteriaTree.containsKey(g)) {
			List<Criterium> children = criteriaTree.get(g);
			if (!children.isEmpty()) {
				return children;
			} else {
				if (alternativesTree.containsKey(g)) {
					children = alternativesTree.get(g);
					if (!children.isEmpty()) {
						return children;
					}
				} else {
					return createAlternativesAsCriterium(g);
				}
			}
		}
		return new ArrayList<Criterium>(); // criterium have no children

	}

	private List<Criterium> createAlternativesAsCriterium(Goal g) throws MalformedTreeException {
		List<Criterium> children = new ArrayList<>();
		for (Criterium c : alternativeAsCriteriumList) {
			Criterium newC = new Criterium(c);
			newC.setParentId(g.getId());
			children.add(newC);
		}
		alternativesTree.put(g, children);
		return children;
	}

	public void addCriteriumTo(Goal parent, Criterium newCriterium) throws MalformedTreeException {
		if (newCriterium.getParentId() != parent.getId()) {
			throw new MalformedTreeException();
		}
		if (alternativesTree.containsKey(parent)) {
			alternativesTree.remove(parent);
			System.out.println("----- remove alternative list CT2 --- " + getCriteriumAlternativesList().size());
		}

		List<Criterium> children = criteriaTree.get(parent);
		addNewCriteriumToListAndCriteriaValueList(children, newCriterium);
		criteriaTree.put(newCriterium, new ArrayList<Criterium>());
	}

	public Goal getParent(Criterium c) throws notFoundException {
		Optional<Goal> goal = searchForParent(criteriaTree, c);
		if (goal.isPresent()) {
			return goal.get();
		}
		goal = searchForParent(alternativesTree, c);
		if (goal.isPresent()) {
			return goal.get();
		}
		throw new notFoundException();

	}

	private Optional<Goal> searchForParent(Map<Goal, List<Criterium>> map, Criterium c) {
		for (Entry<Goal, List<Criterium>> entry : map.entrySet()) {
			List<Criterium> value = entry.getValue();
			if (value.contains(c)) {
				return Optional.of(entry.getKey());
			}
		}
		return Optional.empty();
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

	private void addNewCriteriumToListAndCriteriaValueList(List<Criterium> criteriumList, Criterium newCriterium) {
		criteriumList.add(newCriterium);
		addNewCriteriumToCriteriaValueList(criteriumList, newCriterium);
	}
	private void addNewCriteriumToCriteriaValueList(List<Criterium> criteriumList, Criterium newCriterium) {
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
		return alternativeAsCriteriumList;
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
