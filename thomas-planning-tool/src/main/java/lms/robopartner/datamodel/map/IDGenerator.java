/**
 * 
 */
package lms.robopartner.datamodel.map;

import java.math.BigInteger;

/**
 * @author Spyros provides a static method that creates unique ids every time
 *         called range from {@link BigInteger.ZERO}
 */
public class IDGenerator {

	/**
	 * Internal storage of last given id start from lowest possible
	 */
	private static BigInteger	lastId	= BigInteger.ZERO;

	/**
	 * @return a new ID
	 */
	public synchronized static final BigInteger getNewID() {
		lastId = lastId.add(BigInteger.ONE);
		return lastId;
	}

}
