/**
 * 
 */
package unused;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Spyros
 *
 */
public class ShopFloorMap implements ShopFloorMapInterface {

	/**
	 * Defines the area enclosed by the map. Cannot be changed.
	 */
	private Rectangle theShopfloorBounds = null;
	/**
	 * Stores a bit more information (The id is also stored inside the object) to
	 * provide faster search. However JVM specs internally specify that identical
	 * strings are grouped together in memory, so that the memory overhead is
	 * relatively small.
	 */
	private Map<String, RectangleObjectInterface> objectsMap = new HashMap<String, RectangleObjectInterface>();
	private String id;

	/**
	 * 
	 */
	public ShopFloorMap(Dimension mapDimensions, String id) {
		String msg = ".ShopfloorMap() ";
		if (mapDimensions == null) {
			throw new RuntimeException(msg + "mapDimensions should never be null.");
		}
		this.theShopfloorBounds = new Rectangle(mapDimensions);
		this.setID(id);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.ShopFloorMapInterface#
	 * addRectangleObjectToMap(lms.robopartner.planning
	 * .model.map.RectangleObjectInterface)
	 */
	public ShopFloorMapInterface addRectangleObjectToMap(RectangleObjectInterface aRectangle) {
		String msg = ".addRectangleObjectToMap() ";
		if (aRectangle == null) {
			throw new RuntimeException(msg + "aRectangle should never be null.");
		}

		this.objectsMap.put(aRectangle.getID(), aRectangle);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.ShopFloorMapInterface#
	 * canAddRectangleObjectToMapWithoutOverlap
	 * (lms.robopartner.datamodel.map.RectangleObjectInterface)
	 */
	public Boolean canAddRectangleObjectToMapWithoutOverlap(RectangleObjectInterface aRectangle) {
		String msg = ".canAddRectangleObjectToMapWithoutOverlap() ";
		if (aRectangle == null) {
			throw new RuntimeException(msg + "aRectangle should never be null.");
		}

		// check if it fits inside map
		if (!this.theShopfloorBounds.contains(aRectangle.getRectangle())) {
			return false;
		}

		// You should not just add another object with the same id. Safely
		// remove the old object first.
		if (this.containsRectangleObjectWithId(id)) {
			return false;
		}

		boolean canAdd = true;
		// check if it colides with other objects
		for (RectangleObjectInterface anExistingRectangle : this.objectsMap.values()) {
			if (anExistingRectangle.intersects(aRectangle)) {
				canAdd = false;// cannot add safely since it intersects an
				// existing object.
				break;// no need to check again
			}
		}

		return canAdd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.ShopFloorMapInterface#
	 * removeRectangleObjectFromMap
	 * (lms.robopartner.datamodel.map.RectangleObjectInterface)
	 */
	public ShopFloorMapInterface removeRectangleObjectFromMap(RectangleObjectInterface aRectangle) {
		String msg = ".removeRectangleObjectFromMap() ";
		if (aRectangle == null) {
			throw new RuntimeException(msg + "aRectangle should never be null.");
		}
		this.objectsMap.remove(aRectangle);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.ShopFloorMapInterface#
	 * getRectangleObjectsFromMap()
	 */
	public Collection<RectangleObjectInterface> getRectangleObjectsFromMap() {

		return this.objectsMap.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.ShopFloorMapInterface#getMapDimension ()
	 */
	public Dimension getMapDimension() {
		Dimension aDimension = new Dimension(this.theShopfloorBounds.width, this.theShopfloorBounds.height);
		return aDimension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public ShopFloorMap clone() {

		Dimension aNewDimension = new Dimension(this.getMapDimension().width, this.getMapDimension().height);
		ShopFloorMap aNewShopfloorMap = new ShopFloorMap(aNewDimension, this.getID());

		for (RectangleObjectInterface anExistingRectangle : this.objectsMap.values()) {
			aNewShopfloorMap.addRectangleObjectToMap(anExistingRectangle.clone());
		}
		return aNewShopfloorMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.ShopFloorMapInterface#getID()
	 */
	public String getID() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.ShopFloorMapInterface#setID(java.lang
	 * .String)
	 */
	public ShopFloorMapInterface setID(String id) {
		String msg = ".setID() ";
		if (id == null || id.isEmpty()) {
			throw new RuntimeException(msg + "id should never be null.");
		}
		this.id = id;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.ShopFloorMapInterface#
	 * getRectangleObjectsWithId(java.lang.String)
	 */
	public RectangleObjectInterface getRectangleObjectFromId(String id) {
		return this.objectsMap.get(id);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.ShopFloorMapInterface#
	 * containsRectangleObjectWithId(java.lang.String)
	 */
	public Boolean containsRectangleObjectWithId(String id) {

		return this.objectsMap.containsKey(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lms.robopartner.datamodel.map.ShopFloorMapInterface#shallowCopy()
	 */
	public ShopFloorMapInterface shallowCopy() {

		ShopFloorMap aNewShopfloorMap = new ShopFloorMap(this.getMapDimension(), this.getID());

		for (RectangleObjectInterface anExistingRectangle : this.objectsMap.values()) {
			aNewShopfloorMap.addRectangleObjectToMap(anExistingRectangle);
		}
		return aNewShopfloorMap;
	}

}
