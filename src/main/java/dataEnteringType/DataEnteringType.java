package dataEnteringType;

import consistencyComputingMethods.MaximumEigenvalueConsistencyComputingMethod;

public abstract class DataEnteringType {

	public abstract void create();

	public static DataEnteringType getHalfConsistencyType() {
		return new HalfConsistecyDataEnteringType();
	}
}
