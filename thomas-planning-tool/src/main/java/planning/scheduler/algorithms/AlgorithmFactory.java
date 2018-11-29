/**
 * 
 */
package planning.scheduler.algorithms;

import java.util.HashMap;
import java.util.Map;

import planning.scheduler.algorithms.impact.IMPACT;

/**
 * @author Spyros This class generates instances deriving from class
 *         {@link AbstractAlgorithm}. Each instance of this class will return
 *         the same instance of each of the provided implementations of
 *         {@link AbstractAlgorithm}. Different instances of this class will
 *         return different instances of the provided implementations of
 *         {@link AbstractAlgorithm}.
 */
public class AlgorithmFactory {

	private Map<String, AbstractAlgorithm> algorithNameToImplementationMap = new HashMap<String, AbstractAlgorithm>();

	/**
	* 
	*/
	private boolean supportedAlgorithmsInited = false;

	/**
	* 
	*/
	private void initSupportedAlgorithms() {
		AbstractAlgorithm edd = new EDD();
		AbstractAlgorithm fifo = new FIFO();
		AbstractAlgorithm spt = new SPT();
		AbstractAlgorithm multicriteria = new IMPACT();

		this.algorithNameToImplementationMap.put(AbstractAlgorithm.EDD, edd);
		this.algorithNameToImplementationMap.put(AbstractAlgorithm.FIFO, fifo);
		this.algorithNameToImplementationMap.put(AbstractAlgorithm.SPT, spt);
		this.algorithNameToImplementationMap.put(AbstractAlgorithm.MULTICRITERIA, multicriteria);

		this.supportedAlgorithmsInited = true;
	}

	/**
	 * @param name
	 * @param implementorInstance
	 */
	public void addAlgorithm(String name, AbstractAlgorithm implementorInstance) {
		if (!this.supportedAlgorithmsInited) {
			this.initSupportedAlgorithms();
		}
		this.algorithNameToImplementationMap.put(name, implementorInstance);
	}

	/**
	 * @param algorithmName
	 * @return
	 */
	public AbstractAlgorithm getAlgorithmInstance(String algorithmName) {
		if (!this.supportedAlgorithmsInited) {
			this.initSupportedAlgorithms();
		}
		AbstractAlgorithm algorithm = this.algorithNameToImplementationMap.get(algorithmName);
		if (algorithm == null) {
			throw new UnsupportedOperationException("Algorithm " + algorithmName + " has not been found.");
		}
		return algorithm;
	}

	/**
	 * Creator
	 */
	public AlgorithmFactory() {
	}

}
