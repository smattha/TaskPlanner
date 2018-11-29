/**
 * 
 */
package unused;

import java.awt.Dimension;
import java.awt.Point;

/**
 * @author Spyros Static class that can be used to generate a the next
 *         alternatives for a {@link ShopFloorMapInterface}.
 */
@SuppressWarnings("unused")
public class MapGenerator {

	/**
	 * @param shopfloorDimension
	 * @param theLayoutInput
	 */
	public static void generateEvaluations(Dimension shopfloorDimension,
			LayoutGenerationInputInterface theLayoutInput) {

	}

	/**
	 * Returns a copy of the input with the new id.
	 */
	public static ShopFloorMapInterface getNextState(ShopFloorMapInterface theShopFloorMapInitialState,
			RectangleObjectInterface theNewRectangleObjectInterfaceToAdd, String id) {
		String msg = ".getNextState(): ";

		if (id == null || id.isEmpty() || theNewRectangleObjectInterfaceToAdd == null
				|| theShopFloorMapInitialState == null) {
			throw new RuntimeException(msg + "No input cannot be null. Input:{ id=" + id
					+ " theNewRectangleObjectInterfaceToAdd=" + theNewRectangleObjectInterfaceToAdd
					+ " theNewRectangleObjectInterfaceToAdd=" + theNewRectangleObjectInterfaceToAdd + " }.");
		}
		ShopFloorMapInterface theNextShopFloorMapNextState = theShopFloorMapInitialState.clone().setID(id);

		RectangleObjectInterface theObjectOfTheNewState = theNewRectangleObjectInterfaceToAdd.clone();

		// TODO ROBOPARTNER Write here code that puts the object in a new random
		// position

		return theNextShopFloorMapNextState;
	}

	/**
	 * @param theMap
	 * @param aRectangle the x and y of the rectangle are ignored.
	 * @return
	 */
	public static Point randomPlacement(ShopFloorMapInterface theMap, RectangleObjectInterface aRectangle) {
		//
		boolean check = theMap.canAddRectangleObjectToMapWithoutOverlap(aRectangle);
		return null;
	}

	public static void checkAllPlacements(LayoutGenerationInputInterface aLayoutGenerationInputInterface) {
		Dimension mapDimension = aLayoutGenerationInputInterface.getMapDimension();
		Dimension stepDimension = aLayoutGenerationInputInterface.getResolutionDimension();

		ShopFloorMapsDatabase theFeasibleSolutions = new ShopFloorMapsDatabase();
		// TODO
		// for(aLayoutGenerationInputInterface.getResourcesForTask(taskID))

	}

}
