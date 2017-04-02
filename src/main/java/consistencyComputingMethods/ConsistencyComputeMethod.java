package consistencyComputingMethods;

import Jama.Matrix;

public abstract class ConsistencyComputeMethod {
	protected double consistencyRatio;
	protected double consistencyIndex;
	protected final double[] randomIndex = new double[] 
			//		0				1					2				3		4		5		6		7		8		9		10
			{Double.MIN_VALUE, Double.MIN_NORMAL, Double.MIN_NORMAL, 0.5247, 0.8816, 1.1086, 1.2479, 1.3417, 1.4057, 1.4499, 1.4854 };

	public abstract double compute(Matrix matrix);

	public static MaximumEigenvalueConsistencyComputingMethod maximumEigenvalueMethod() {
		return new MaximumEigenvalueConsistencyComputingMethod();
	}
}
