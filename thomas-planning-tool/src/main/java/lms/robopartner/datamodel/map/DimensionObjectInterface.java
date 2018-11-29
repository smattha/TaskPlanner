/**
 * 
 */
package lms.robopartner.datamodel.map;

import java.awt.Dimension;
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
public interface DimensionObjectInterface extends Cloneable, Serializable {

	public abstract void setProperty(String key, String value);

	public abstract String getProperty(String key);

	/**
	 * @return
	 */
	public abstract Dimension getDimension();

	/**
	 * @param aRectangle
	 * @return self reference for chaining statements
	 */
	public abstract DimensionObjectInterface setDimension(Dimension aDimension);

	/**
	 * Set the height of this object
	 * 
	 * @param height
	 */
	public abstract void setHeight(double height);

	/**
	 * Set the height of this object
	 * 
	 * @return
	 */
	public abstract double getHeight();

	/**
	 * @return the id of the object that this {@link DimensionObjectInterface}
	 *         refers to.
	 */
	public abstract String getID();

	/**
	 * set the id of the object that this {@link DimensionObjectInterface} refers
	 * to.
	 * 
	 * @return the {@link DimensionObjectInterface} for chaining statements
	 */
	public abstract DimensionObjectInterface setID(String id);

	/**
	 * It is important to note that the reachability is stored as a double
	 * internally but during the check whether something is reachable or not it is
	 * tested as integer
	 * 
	 * @param reachability
	 */
	public abstract DimensionObjectInterface setReachability(Double reachability);

	/**
	 * @return the reachability
	 */
	public abstract double getReachability();

	/**
	 * @return the weight
	 */
	public abstract double getWeight();

	/**
	 * @return
	 */

	// public abstract String getName();

	/**
	 * @return
	 */
	public abstract int getVirtualIMAU();

	/**
	 * 
	 * @return shape
	 */
	public abstract String getShape();

	public abstract void setShape(String shape);

}
