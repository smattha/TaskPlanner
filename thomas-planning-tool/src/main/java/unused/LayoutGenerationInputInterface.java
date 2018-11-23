/**
 * 
 */
package unused;

import java.awt.Dimension;
import java.util.Collection;

/**
 * @author Spyros
 *
 */
public interface LayoutGenerationInputInterface {

    /**
     * @return the step dimensions
     */
    public Dimension getResolutionDimension();

    /**
     * @return self reference for chaining statements
     */
    public LayoutGenerationInputInterface setResolutionDimension(
	    Dimension aResolutionDimension);

    /**
     * @return the maps dimensions
     */
    public Dimension getMapDimension();

    /**
     * @return self reference for chaining statements
     */
    public LayoutGenerationInputInterface setMapDimension(Dimension aDimension);

    /**
     * @param resourceID
     * @return
     */
    public BasicResourceInterface getResource(String resourceID);

    /**
     * @param taskID
     * @return
     */
    public BasicTaskInterface getTask(String taskID);

    /**
     * @param taskID
     * @return
     */
    public Collection<BasicResourceInterface> getResourcesForTask(String taskID);

    /**
     * @param resourceID
     * @return
     */
    public Collection<BasicResourceInterface> getTasksForResourves(
	    String resourceID);

    /**
     * @param aNewTask
     */
    public void addTask(BasicTaskInterface aNewTask);

    /**
     * @param aNewResource
     */
    public void removeResource(BasicResourceInterface aNewTask);

    /**
     * @param aNewTask
     */
    public void removeTask(BasicTaskInterface aNewTask);

    /**
     * @param aNewResource
     */
    public void addResource(BasicResourceInterface aNewTask);

    /**
     * @param resourceId
     * @param taskID
     */
    public void setResourceSuitableForTask(String resourceId, String taskID);

}
