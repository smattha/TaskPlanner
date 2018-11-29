package unused;

import java.awt.Dimension;
import java.util.Collection;

/**
 * Defines behaviour of a shopfloor map
 * 
 * @author Spyros
 *
 */
public interface ShopFloorMapInterface extends Cloneable {

	/**
	 * Does not check if object can be added. Allows overlapping objects. Check with
	 * {@link ShopFloorMapInterface#canAddRectangleObjectToMapWithoutOverlap(RectangleObjectInterface)}
	 * 
	 * @param aRectangle
	 * @return
	 */
	public abstract ShopFloorMapInterface addRectangleObjectToMap(RectangleObjectInterface aRectangle);

	/**
	 * @param aRectangle
	 * @return true if the {@link RectangleObjectInterface} can be added to the map
	 *         without intersecting any other existing rectangle.
	 */
	public abstract Boolean canAddRectangleObjectToMapWithoutOverlap(RectangleObjectInterface aRectangle);

	/**
	 * @param aRectangle
	 * @return shelf reference for chaining
	 */
	public abstract ShopFloorMapInterface removeRectangleObjectFromMap(RectangleObjectInterface aRectangle);

	/**
	 * @return a {@link Collection} with all included
	 *         {@link RectangleObjectInterface}
	 */
	public abstract Collection<RectangleObjectInterface> getRectangleObjectsFromMap();

	/**
	 * @return the requested object, or null if not found
	 */
	public abstract RectangleObjectInterface getRectangleObjectFromId(String id);

	/**
	 * @return true if the map already contains an object with this id
	 */
	public abstract Boolean containsRectangleObjectWithId(String id);

	/**
	 * @return map dimension
	 */
	public abstract Dimension getMapDimension();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public ShopFloorMapInterface clone();

	/**
	 * @return a shallow copy of this object
	 */
	public ShopFloorMapInterface shallowCopy();

	/**
	 * @return the id of the object that this {@link ShopFloorMapInterface} refers
	 *         to.
	 */
	public abstract String getID();

	/**
	 * set the id of the object that this {@link ShopFloorMapInterface} refers to.
	 * 
	 * @return the {@link ShopFloorMapInterface} for chaining statements
	 */
	public abstract ShopFloorMapInterface setID(String id);

}