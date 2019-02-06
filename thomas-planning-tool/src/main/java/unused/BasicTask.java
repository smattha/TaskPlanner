/**
 * 
 */
package unused;

import java.awt.Rectangle;

/**
 * @author Spyros
 *
 */
public class BasicTask extends RectangleObject implements RectangleObjectInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8966161572245462001L;

	/**
	 * @param aRectangle
	 * @param id
	 * @param reachability
	 */
	private BasicTask(Rectangle aRectangle, String id, double reachability) {
		super(aRectangle, id, reachability);
	}

	/**
	 * @param aRectangle
	 * @param id
	 */
	public BasicTask(Rectangle aRectangle, String id) {
		this(aRectangle, id, aRectangle.getHeight());
		this.myReachabilityRectangle.setBounds(this.getRectangle());
	}

}
