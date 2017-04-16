package consistencyComputingMethods;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class MaximumEigenvalue extends ConsistencyComputeMethod {

	@Override
	public double compute(Matrix matrix) {
		double consistencyIndex;
		EigenvalueDecomposition eig = matrix.eig();
		double[] eigenvalues = eig.getRealEigenvalues();
		int n = eigenvalues.length;
		if (n < 2) {
			return 0;
		}
		double maxEig = eigenvalues[0];
		for (int i = 1; i < n; i++) {
			if (maxEig < eigenvalues[i]) {
				maxEig = eigenvalues[i];
			}
		}
		consistencyIndex = (maxEig - n) / (n - 1);
		return getConsistencyRatio(consistencyIndex, n);
	}

	@Override
	public String toString() {
		return "maximum eigenvalue";
	}

}
