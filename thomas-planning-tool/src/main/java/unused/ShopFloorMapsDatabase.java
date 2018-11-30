package unused;

import java.util.Map;
import java.util.TreeMap;

public class ShopFloorMapsDatabase {

    /**
     * Where all maps are stored
     */
    private Map<String, ShopFloorMapInterface> mapsDatabase = new TreeMap<String, ShopFloorMapInterface>();

    /**
     * @param id
     * @return
     */
    public boolean containsID(String id) {
	return this.mapsDatabase.containsKey(id);
    }

    /**
     * @param id
     * @return the map with the given id
     */
    public ShopFloorMapInterface getMap(String id) {
	return this.mapsDatabase.get(id);
    }

    /**
     * @param id
     * @return this to allow statement chain
     */
    public ShopFloorMapsDatabase putMap(
	    ShopFloorMapInterface aNewShopFloorMapInterface) {

	String msg = ".putMap() ";
	if (aNewShopFloorMapInterface == null
		|| aNewShopFloorMapInterface.getID() == null
		|| aNewShopFloorMapInterface.getID().isEmpty()) {
	    throw new RuntimeException(
		    msg
			    + "aNewShopFloorMapInterface or its id should never be null.");
	}
	this.mapsDatabase.put(aNewShopFloorMapInterface.getID(),
		aNewShopFloorMapInterface);
	return this;
    }
}
