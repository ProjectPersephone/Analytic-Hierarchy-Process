package consistencyComputingMethods;

import Jama.Matrix;

public class IndexOfDeterminants extends ConsistencyComputeMethod {

	@Override
	public double compute(Matrix a) {
		int n = a.getColumnDimension();
		if (n < 3)
			return 0;

		double sum = 0;
		for (int i = 0; i < n - 2; i++) {
			for (int j = i + 1; j < n - 1; j++) {
				for (int k = j + 1; k < n; k++) {
					// System.out.println("a" + i + k + ": " + a.get(i, k));
					// System.out.println("a" + i + j + ": " + a.get(i, j));
					// System.out.println("a" + j + k + ": " + a.get(j, k));
					double part = a.get(k, i) / (a.get(j, i) * a.get(k, j)) + (a.get(j, i) * a.get(k, j)) / a.get(k, i)
							- 2;
					sum = sum + part;
					System.out.println("sum: " + sum);
				}

			}

		}
		double bc = binomialCoefficient(n, 3);
		System.out.println("bc: " + bc + " sum: " + sum);

		double consistencyIndex = sum / bc;
		return getConsistencyRatio(consistencyIndex, n);
	}

	public int factorial(int n) {
		int fact = 1;
		for (int i = 1; i <= n; i++) {
			fact *= i;
		}
		return fact;
	}

	public int binomialCoefficient(int n, int k) {
		return factorial(n) / (factorial(k) * factorial(n - k));
	}

	@Override
	public String toString() {
		return "index of determinants";
	}
}
