package priorityVecorComputingMethods;

import java.util.ArrayList;
import java.util.List;

import Jama.*;
import model.PriorityVector;

public class EigenvectorPriorityVectorComputingMethod extends PriorityVectorComputeMethod {

	@Override
	public PriorityVector Compute(Matrix matrix, String criterium) {
		List<Double> weightVector = new ArrayList<Double>();

		EigenvalueDecomposition ed = new EigenvalueDecomposition(matrix);
		Matrix eigVector = ed.getV();
		double[] eigenValuesR = ed.getRealEigenvalues();

		int index = getMaximumEigenValueIndex(eigenValuesR);
		for (int j = 0; j < eigVector.getColumnDimension(); j++) {
			weightVector.add(eigVector.get(j, index));
		}
		PriorityVector pv = new PriorityVector(weightVector, criterium);
		pv.normalize();
		return pv;
	}

	private int getMaximumEigenValueIndex(double[] eigenValuesR) {
		double max = eigenValuesR[0];
		int index = 0;
		for (int it = 1; it < eigenValuesR.length; it++) {
			if (max < eigenValuesR[it]) {
				max = eigenValuesR[it];
				index = it;
			}
		}
		return index;
	}
	

	@Override
	public String toString() {
		return "eigenvector";
	}

}
