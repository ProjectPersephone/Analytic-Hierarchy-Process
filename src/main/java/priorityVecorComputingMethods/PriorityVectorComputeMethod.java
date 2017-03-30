package priorityVecorComputingMethods;

import Jama.Matrix;
import model.PriorityVector;

public abstract class PriorityVectorComputeMethod {

	public abstract PriorityVector Compute(Matrix matrix, String criterium);
	
	public static PriorityVectorComputeMethod simpleMethod(){
		return new RandomColumnPriorityVectorComputingMethod();
	}
	public static PriorityVectorComputeMethod geometricMethod(){
		return new GeometricMeanPriorityVectorComputingMethod();
	}
	public static PriorityVectorComputeMethod eigenvectorMethod(){
		return new EigenvectorPriorityVectorComputingMethod();
	}
}
