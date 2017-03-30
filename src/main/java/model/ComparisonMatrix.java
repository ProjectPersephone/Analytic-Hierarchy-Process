package model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import priorityVecorComputingMethods.PriorityVectorComputeMethod;

public class ComparisonMatrix {
	private Matrix matrix;
	private String criterium;

	public ComparisonMatrix(Double[] weightVector, String criterium) {
		this.criterium = criterium;
		Matrix m = new Matrix(weightVector.length, weightVector.length);
		for (int i = 0; i < m.getRowDimension(); i++) {
			for (int j = 0; j < m.getColumnDimension(); j++) {
				m.set(i, j, weightVector[i] / weightVector[j]);
			}
		}
		matrix = m;

	}

	public ComparisonMatrix(List<Double> weightVector, String criterium) {
		this(weightVector.toArray(new Double[weightVector.size()]), criterium);
	}

	public ComparisonMatrix(Matrix matrix, String criteriumName) {
		this.matrix = matrix;
		this.criterium = criteriumName;
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix m) {
		this.matrix = m;
	}

	public String getCriterium() {
		return criterium;
	}

	public void setCriterium(String criterium) {
		this.criterium = criterium;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder(criterium + ":\n");
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getColumnDimension(); j++) {
				sBuilder.append(String.format("%.3f", matrix.get(i, j)) + " ");
			}
			sBuilder.append("\n");
		}
		return sBuilder.toString();
	}
	
	public PriorityVector getProrityVector(PriorityVectorComputeMethod method){
		return method.Compute(matrix, criterium);
	}

}
