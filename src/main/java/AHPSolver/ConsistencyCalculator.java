package AHPSolver;

import Jama.Matrix;
import consistencyComputingMethods.ConsistencyComputeMethod;

public class ConsistencyCalculator {
	
	public double compute(Matrix m, ConsistencyComputeMethod ccm){
		return ccm.compute(m);
	}

}
