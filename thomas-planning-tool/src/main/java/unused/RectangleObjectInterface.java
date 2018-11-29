/**
 * 
 */
package unused;

import java.awt.Rectangle;
import java.io.Serializable;

/**
 * @author Spyros
 * Represents the rectangle area occupied by an object on the map.
 * Also has orientation (0 or 90) degrees
 */
/**
 * @author Spyros
 *
 */
public interface RectangleObjectInterface extends Cloneable, Serializable {
	/**
	 * @param anotherRectangleObjectInterface
	 * @return
	 */
	public abstract boolean isWithinReach(RectangleObjectInterface anotherRectangleObjectInterface);

	/**
	 * @return
	 */
	public abstract boolean isOrientation0Degrees();

	/**
	 * @return
	 */
	public abstract boolean isOrientation90Degrees();

	/**
	 * @return
	 */
	public abstract RectangleObjectInterface setOrientation0Degrees();

	/**
	 * @return
	 */
	public abstract RectangleObjectInterface setOrientation90Degrees();

	/**
	 * @return
	 */
	public abstract Rectangle getRectangle();

	/**
	 * @param aRectangle
	 * @return self reference for chaining statements
	 */
	public abstract RectangleObjectInterface setRectangle(Rectangle aRectangle);

	/**
	 * @param anotherRectangleObjectInterface
	 * @return Returns true if intersection is not zero or null. See
	 *         {@link java.awt.Shape#intersects(java.awt.geom.Rectangle2D)}
	 */
	public abstract boolean intersects(RectangleObjectInterface anotherRectangleObjectInterface);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public RectangleObjectInterface clone();

	/**
	 * @return the id of the object that this {@link RectangleObjectInterface}
	 *         refers to.
	 */
	public abstract String getID();

	/**
	 * set the id of the object that this {@link RectangleObjectInterface} refers
	 * to.
	 * 
	 * @return the {@link RectangleObjectInterface} for chaining statements
	 */
	public abstract RectangleObjectInterface setID(String id);

	/**
	 * It is important to note that the reachability is stored as a double internaly
	 * but during the check whether something is reachable or not it is tested as
	 * integer
	 * 
	 * @param reachability
	 */
	public abstract RectangleObjectInterface setReachability(Double reachability);

	/**
	 * @return
	 */
	public abstract double getReachability();

	/**
	 * @return
	 */
	public abstract Rectangle getReachabilityRectangle();
}
