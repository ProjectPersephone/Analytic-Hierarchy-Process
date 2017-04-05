package AHPSolver;

import Jama.Matrix;
import consistencyComputingMethods.ConsistencyComputeMethod;

public class ConsistencyCalculator {
	
	public double compute(Matrix m, ConsistencyComputeMethod ccm){
		if(m.getRowDimension() < 1){
			return 0.0;
		}
		return ccm.compute(m);
	}

}
