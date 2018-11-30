/**
 * 
 */
package unused;

import java.awt.Point;
import java.util.Map;

/**
 * @author Spyros Interface that defines the output of the layouts generation
 */
public interface LayoutAlternativeInterface {

    /**
     * Returns the
     * 
     * @param id
     * @return
     */
    public Point getLocationForID(String id);

    /**
     * @return all available locations
     */
    public Map<String, Point> getLocations();

}
