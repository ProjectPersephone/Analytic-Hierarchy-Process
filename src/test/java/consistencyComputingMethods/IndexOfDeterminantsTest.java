package consistencyComputingMethods;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import Jama.Matrix;

public class IndexOfDeterminantsTest {

	@Test
	public void indexOfDeterminantsTest() {
		Matrix m = new Matrix(4, 4);
		
		//1		2	9	1
		//1/2	1	1/3	1/6
		//1/9	3	1	2
		//1		6	1/2	1
		m.set(0, 0, 1.);
		m.set(0, 1, 2.);
		m.set(0, 2, 9.);
		m.set(0, 3, 1.);
		
		m.set(1, 0, 1./2.);
		m.set(1, 1, 1.);
		m.set(1, 2, 1./3.);
		m.set(1, 3, 1./6.);
		
		m.set(2, 0, 1./9.);
		m.set(2, 1, 3.);
		m.set(2, 2, 1.);
		m.set(2, 3, 2.);
		
		m.set(3, 0, 1.);
		m.set(3, 1, 6.);
		m.set(3, 2, 1./2.);
		m.set(3, 3, 1.);
		
		double ci = ConsistencyComputeMethod.getIndexOfdeterminantsMethod().compute(m);
		assertEquals(ci, 7.80324074, 0.0000001);
		
	}

}
