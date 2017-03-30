package model;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class PriorityVector {
	Matrix priorityVector;
	String criterium;

	public PriorityVector(Double[] weightVector, String criterium) {
		this.criterium = criterium;
		priorityVector = new Matrix(weightVector.length, 1);
		for (int i = 0; i < priorityVector.getRowDimension(); i++) {
			priorityVector.set(i, 0, weightVector[i]);
		}
	}

	public PriorityVector(List<Double> weightVector, String criterium) {
		this(weightVector.toArray(new Double[weightVector.size()]), criterium);
	}

	public PriorityVector() {
		this.criterium = "scalar";
		priorityVector = new Matrix(1, 1);
		priorityVector.set(0, 0, 0);
	}

	public PriorityVector(String criteriumName, List<PriorityVector> vectorsToSum) {
		this.criterium = criteriumName;
//		List<Double> sum = new ArrayList<Double>();
		Double [] sum;
		sum = new Double[vectorsToSum.get(0).getLength()];
		for(int j=0;j<sum.length;j++){
			sum[j]=0.;
		}
		for (int v = 0; v < vectorsToSum.size(); v++) {
			for (int i = 0; i < vectorsToSum.get(v).getLength(); i++) {
				sum[i] = sum[i] + vectorsToSum.get(v).get(i);
			}
		}
		
		this.priorityVector = new Matrix(sum.length, 1);
		for(int it =0;it<sum.length;it++){
			priorityVector.set(it, 0, sum[it]);
		}
		
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder(criterium + ":\n");
		for (int i = 0; i < priorityVector.getRowDimension(); i++) {
			sBuilder.append(String.format("%.3f", priorityVector.get(i, 0)) + "\n");
		}
		return sBuilder.toString();
	}

	public double get(int index) {
		return priorityVector.get(index, 0);
	}

	public int getLength() {
		return priorityVector.getRowDimension();
	}

	public PriorityVector multiplyBy(double d) {
		Double[] v = new Double[priorityVector.getRowDimension()];
		for (int i = 0; i < priorityVector.getRowDimension(); i++) {
			v[i] = priorityVector.get(i, 0) * d;
		}
		return new PriorityVector(v, criterium);
	}

	public void normalize() {
		double sum = sumVectorItems(priorityVector);
		for(int i=0;i<priorityVector.getRowDimension();i++){
			priorityVector.set(i, 0, priorityVector.get(i, 0)/sum);
		}
	}

	private double sumVectorItems(Matrix m) {
		double sum = 0;
		for(int i=0;i<m.getRowDimension();i++){
			sum = sum+m.get(i, 0);
		}
		return sum;
	}
}
