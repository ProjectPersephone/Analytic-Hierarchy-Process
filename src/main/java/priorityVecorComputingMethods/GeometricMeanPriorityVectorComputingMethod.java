package priorityVecorComputingMethods;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import model.PriorityVector;

public class GeometricMeanPriorityVectorComputingMethod extends PriorityVectorComputeMethod {

	@Override
	public PriorityVector Compute(Matrix matrix, String criterium) {
		List<Double> weightVector = new ArrayList<Double>();
		List<Double> ratioVector = new ArrayList<Double>();
		double norm = 0.;
		double power = 1. / matrix.getRowDimension();
		for (int s = 0; s < matrix.getRowDimension(); s++) {
			double ratio = 1;
			for (int r = 0; r < matrix.getColumnDimension(); r++) {
				ratio = ratio * matrix.get(s, r);
			}
			ratioVector.add(ratio);
			norm = norm + Math.pow(ratio, power);
		}
		
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			double w = Math.pow(ratioVector.get(i), power) / norm;
			weightVector.add(w);
		}
		return new PriorityVector(weightVector, criterium);
	}

	@Override
	public String toString() {
		return "geometric mean";
	}
	
	

}
