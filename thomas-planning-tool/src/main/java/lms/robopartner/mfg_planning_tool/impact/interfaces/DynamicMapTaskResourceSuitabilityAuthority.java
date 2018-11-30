/**
 * 
 */
package lms.robopartner.mfg_planning_tool.impact.interfaces;

import java.util.Calendar;
import java.util.Vector;

import lms.robopartner.datamodel.map.controller.LayoutEvaluator;
import lms.robopartner.datamodel.map.controller.MapParameters;

import org.slf4j.LoggerFactory;

import planning.model.AssignmentDataModel;
import planning.scheduler.algorithms.impact.IMPACT;
import planning.scheduler.algorithms.impact.interfaces.DynamicResourceTaskSuitabilityAuthorityInterface;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * An instance of this class is responsible for identifying which resources are
 * available for assignments on tasks. This class makes the following
 * assumptions: + Objects that are modeled by Tasks of given size are place into
 * a floor which is modeled as a grid. Grid squares are the resources. + All
 * objects and floor space are orthogonal (width*height) + Location are always
 * modeled as the upper-left corner of objects and map. + (0,0) is the
 * upper-left corner of the grid. + Objects cannot overlap. For this reason this
 * class checks assignments and considers the resources (grid squares) that are
 * occupied by an object as unavailable.
 * 
 * @author Spyros
 */
// TODO now is in 2D it should be in 3D.
public class DynamicMapTaskResourceSuitabilityAuthority implements DynamicResourceTaskSuitabilityAuthorityInterface {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DynamicMapTaskResourceSuitabilityAuthority.class);

    /**
     * Initialize, works only with DH=1. This means {@link IMPACT#getDH() should
     * be equals to 1} Also utilizes the {@link MapParameters}
     */
    public DynamicMapTaskResourceSuitabilityAuthority() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see lms.robopartner.mfg_planning_tool.impact.interfaces.
     * DynamicResourceTaskSuitabilityAuthorityInterface
     * #isSuitabilityValid(lms.robopartner
     * .planning.scheduler.simulation.ResourceSimulator,
     * lms.robopartner.planning.scheduler.simulation.TaskSimulator,
     * java.util.Calendar,
     * lms.robopartner.planning.scheduler.simulation.interfaces
     * .PlanHelperInterface)
     */

    // TODO MAIN function that checks suitability of
    public boolean isSuitabilityValid(ResourceSimulator resource, TaskSimulator task, Calendar timeNow, PlanHelperInterface planHelperInterface) {
        String msg = ".isSuitabilityValid(): ";
        // Vector containing all existing assignments.
        Vector<AssignmentDataModel> existingAssignements = planHelperInterface.getAssignments();
        logger.debug("existing assignments: {}", existingAssignements.size());
        // // TODO enable orientation - uncomment and finalize code below
        // Rectangle theZeroDegreeRectangle = DataModelToAWTHelper.createRectangle(resource, task);
        // Dimension theNinetyDegreeDimension = new Dimension(theZeroDegreeRectangle.height, theZeroDegreeRectangle.width); //translated dimensions.
        // Rectangle theNinetyDegreeRectangle = new Rectangle(theZeroDegreeRectangle.getLocation(), theNinetyDegreeDimension);
        // isSuitable = LayoutEvaluator.isSuitabilityValid(theZeroDegreeRectangle, existingAssignements);
        // LayoutEvaluator.isSuitabilityValid(theNinetyDegreeRectangle,existingAssignements);
        // // TODO enable orientation - uncomment and finalize code above

        boolean isSuitable = LayoutEvaluator.isSuitabilityValid(resource, task, existingAssignements);
        logger.trace(msg + "resource=" + resource + " task=" + task + " isSuitable=" + isSuitable);
        return isSuitable;
    }

}
