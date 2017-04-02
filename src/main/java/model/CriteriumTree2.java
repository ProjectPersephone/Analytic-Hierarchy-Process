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
	private Goal goal;
	private List<Alternative> alternatives;
	private Double maxConsistencyValue;

	public CriteriumTree2(Goal goal, List<Alternative> alternatives) throws FirstStageMustBeGoalException {
		if (goal instanceof Goal) {
			this.goal = goal;
			this.criteriaTree = new HashMap<Goal, List<Criterium>>();
			this.criteriaTree.put(goal, new ArrayList<Criterium>());
			this.alternatives = alternatives;
			this.maxConsistencyValue = 0.1;
		} else {
			throw new FirstStageMustBeGoalException();
		}
	}

	public void addCriteriumTo(Goal parent, Criterium newCriterium) throws MalformedTreeException {
		if (newCriterium.getParentId() == parent.getId()) {
			List<Criterium> children = criteriaTree.get(parent);
			children.add(newCriterium);
			for (Criterium c : children) {

				c.addValuesOf(newCriterium.getName(), 1);
				newCriterium.addValuesOf(c.getName(), 1);
			}
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
	public boolean hasChildren(Goal g){
		if(criteriaTree.containsKey(g)){
			return true;
		}
		return false;
		
	}

	public List<Criterium> getChildren(Goal g) throws MalformedTreeException {
//		System.out.println("g: "+g);
//		System.out.println(this);
		List<Criterium> children = criteriaTree.get(g);
//		System.out.println(this);
		if(children == null){
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
		// System.out.println(criterium.getName()+" "+newName);
		if (criterium instanceof Criterium) {
			// System.out.println(criterium.getName()+" "+newName);
			Goal parent = getParent((Criterium) criterium);
			for (Criterium c : getChildren(parent)) {
				Map<String, Double> values = c.getValues();
				Double v = values.get(criterium.getName());
				// System.out.println("bef1: "+values);
				values.remove(criterium.getName());
				// System.out.println("bef2: "+values);
				c.addValuesOf(newName, v);
				// values.put(newName, v);
				// System.out.println("af: "+values);
				// System.out.println("tree: "+this);
			}
		}
		criterium.setName(newName);
//		System.out.println("tree: " + this);
		return;

	}

	public void changeValue(Criterium c, String name, Double value) {

		if(c.getValues().containsKey(name)){
			c.getValues().put(name, value);
		}
		else{
			//TODO refresh
		}
	}
	
	public Double getMaxConsistencyValue() {
		return maxConsistencyValue;
	}

	public void setMaxConsistencyValue(Double maxConsistencyValue) {
		this.maxConsistencyValue = maxConsistencyValue;
	}

	public void addAlternative(Alternative a) {
		alternatives.add(a);
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(List<Alternative> alternatives) {
		this.alternatives = alternatives;
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

	@Override
	public String toString() {
		return "tree: " + criteriaTree;
	}

	public boolean isConsistent(Goal goal) {
		if(maxConsistencyValue>=goal.getConsistencyValue()){
			return true;
		}
		return false;
	}

//	public boolean isConsistent() {
//		if(maxConsistencyValue>=consistencyValue){
//			return true;
//		}
//		return false;
//		
//	}

}
