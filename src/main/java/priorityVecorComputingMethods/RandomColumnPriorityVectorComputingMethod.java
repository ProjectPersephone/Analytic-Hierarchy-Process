package priorityVecorComputingMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Jama.Matrix;
import model.PriorityVector;

public class RandomColumnPriorityVectorComputingMethod extends PriorityVectorComputeMethod {

	@Override
	public PriorityVector Compute(Matrix matrix, String criterium) {
		List<Double> weightVector = new ArrayList<Double>();
		double sum=0;
		Random r = new Random();
		int randomInt = r.nextInt(matrix.getColumnDimension());
		
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			sum = sum + matrix.get(i, randomInt);
		}
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			double w = matrix.get(i, randomInt)/sum;
			weightVector.add(w);
		}
		return new PriorityVector(weightVector, criterium);
	}

	@Override
	public String toString() {
		return "random column";
	}
}
