package planning.scheduler.simulation;

import java.util.Calendar;
import java.util.Vector;

import planning.model.AssignmentDataModel;
import planning.model.DockingStationDataModel;
import planning.model.MobileResourceDataModel;
import planning.model.ResourceDataModel;
import planning.model.ToolDataModel;
import planning.model.WorkcenterDataModel;

public class PoolOfResourcesSimulator {
    private final Vector<ResourceSimulator> resourceSimulatorVector = new Vector<ResourceSimulator>();

//    private Vector<ResourceSimulator> toolSimulatorVector = new Vector<ResourceSimulator>();
//    private Vector<ResourceSimulator> mobileResourceSimulatorVector = new Vector<ResourceSimulator>();
//
//    private Vector<WorkcenterDataModel> workcenterDataModelVector = null; // For future use ?

    public PoolOfResourcesSimulator(Vector<WorkcenterDataModel> workcenterDataModelVector) {
        // this.workcenterDataModelVector = workcenterDataModelVector;
        // Make the resources
        for (int i = 0; i < workcenterDataModelVector.size(); i++) {
            WorkcenterDataModel workcenterDataModel = workcenterDataModelVector.get(i);
            // Make the resources
            Vector<ResourceDataModel> resources = workcenterDataModel.getResourceDataModelVector();
            for (int j = 0; j < resources.size(); j++) {
                resourceSimulatorVector.add(new ResourceSimulator(resources.get(j)));
                // Add also the held tools
                if (resources.get(j).getEndEffectorsDataModel() != null) {
                    Vector<ToolDataModel> tools = resources.get(j).getEndEffectorsDataModel().getTools();
                    for (ToolDataModel toolDataModel : tools) {
                        resourceSimulatorVector.add(new ResourceSimulator(toolDataModel));
                        // toolSimulatorVector.add(new ResourceSimulator(toolDataModel));
                    }
                }
            }
            // Make the tools
            if (workcenterDataModel.getInventoryDataModel() != null) {
                Vector<ToolDataModel> tools = workcenterDataModel.getInventoryDataModel().getTools();
                for (ToolDataModel toolDataModel : tools) {
                    resourceSimulatorVector.add(new ResourceSimulator(toolDataModel));
                    // toolSimulatorVector.add(new ResourceSimulator(toolDataModel));
                }
            }
            // Make the mobile resources
            if (workcenterDataModel.getDockingStationDataModelVector() != null) {
                for (DockingStationDataModel dockingStation : workcenterDataModel.getDockingStationDataModelVector()) {
                    if (dockingStation.getCurrentLoad() != null) {
                        resourceSimulatorVector.add(new ResourceSimulator(dockingStation.getCurrentLoad()));
                        // mobileResourceSimulatorVector.add(new ResourceSimulator(dockingStation.getCurrentLoad()));
                        // Add also the held tools
                        if (dockingStation.getCurrentLoad().getEndEffectorsDataModel() != null) {
                            Vector<ToolDataModel> tools = dockingStation.getCurrentLoad().getEndEffectorsDataModel().getTools();
                            for (ToolDataModel toolDataModel : tools) {
                                resourceSimulatorVector.add(new ResourceSimulator(toolDataModel));
                                // toolSimulatorVector.add(new ResourceSimulator(toolDataModel));
                            }
                        }
                    }
                }
            }
        }
    }

    // Check the down resources when they will be available again
    protected Interupt getNextInterupt(Calendar timeNow) {
        Calendar interuptDate = null;
        // HashMap<Calendar, Vector<ResourceSimulator>>
        Vector<ResourceSimulator> sources = new Vector<ResourceSimulator>();
        // ResourceSimulator source = null;
        for (int i = 0; i < resourceSimulatorVector.size(); i++) {
            ResourceSimulator resource = resourceSimulatorVector.get(i);
            if (resource.getResourceState().equals(ResourceSimulator.DOWN)) {
                Calendar upDate = resource.getNextWorkingDate(timeNow);
                if (interuptDate == null) {
                    interuptDate = upDate;
                    sources.add(resource);
                } else {
                    if (interuptDate.getTimeInMillis() > upDate.getTimeInMillis()) {
                        interuptDate = upDate;
                        // Remove previous sources since new source found
                        sources.removeAllElements();
                        sources.add(resource);
                    } else if (interuptDate.getTimeInMillis() == upDate.getTimeInMillis()) {
                        interuptDate = upDate;
                        sources.add(resource);
                    }
                }
            }
        }

        if (interuptDate == null) {
            return null;
        }

        return new Interupt(interuptDate, sources, Interupt.RESOURCES_IDLE_FROM_RESOURCE_DOWN);
    }

    protected void init(Calendar timeNow) {
        for (int i = 0; i < resourceSimulatorVector.size(); i++) {
            ResourceSimulator resource = resourceSimulatorVector.get(i);
            if ((resource.getResourceState() != null) && (resource.getResourceState().equals(ResourceSimulator.BUSY))) {
                // Do nothing these resources will be initialized when they will finish their assignment
            } else {
                if (resource.isResourceDown(timeNow)) {
                    resource.setResourceState(ResourceSimulator.DOWN);
                } else {
                    resource.setResourceState(ResourceSimulator.IDLE);
                }
            }
        }
    }

    public Vector<ResourceSimulator> getIdleResourceSimulatorVector(WorkcenterDataModel workcenterDataModel) {
        Vector<ResourceSimulator> idleResources = new Vector<ResourceSimulator>();
        for (int i = 0; i < resourceSimulatorVector.size(); i++) {
            if (resourceSimulatorVector.get(i).getResourceState().equals(ResourceSimulator.IDLE) && workcenterDataModel.containsResourceDataModel(resourceSimulatorVector.get(i).getResourceDataModel())) {
                idleResources.add(resourceSimulatorVector.get(i));
            } else if (resourceSimulatorVector.get(i).getResourceState().equals(ResourceSimulator.IDLE) && (resourceSimulatorVector.get(i).getResourceDataModel() instanceof ToolDataModel || resourceSimulatorVector.get(i).getResourceDataModel() instanceof MobileResourceDataModel)) {
                // Add idle common resources
                idleResources.add(resourceSimulatorVector.get(i));
            }
        }
        return idleResources;
    }

    public Vector<ResourceSimulator> getIdleResourceSimulatorVector() {
        Vector<ResourceSimulator> idleResources = new Vector<ResourceSimulator>();
        for (int i = 0; i < resourceSimulatorVector.size(); i++) {
            if (resourceSimulatorVector.get(i).getResourceState().equals(ResourceSimulator.IDLE)) {
                idleResources.add(resourceSimulatorVector.get(i));
            }
        }
        return idleResources;
    }

    public ResourceSimulator getResourceSimulator(ResourceDataModel resource) {
        for (int i = 0; i < resourceSimulatorVector.size(); i++) {
            ResourceSimulator resourceSimulator = resourceSimulatorVector.get(i);
            if (resourceSimulator.getResourceDataModel().getResourceId().equals(resource.getResourceId())) {
                return resourceSimulator;
            }
        }
        return null;
    }

    public Vector<ResourceSimulator> getWorkcentersCommonResourceSimulatorVector() {
        Vector<ResourceSimulator> commonResources = new Vector<ResourceSimulator>();
        for (ResourceSimulator resource : resourceSimulatorVector) {
            if (resource.getResourceDataModel() instanceof ToolDataModel || resource.getResourceDataModel() instanceof MobileResourceDataModel) {
                commonResources.add(resource);
            }
        }
        return commonResources;
    }

    public void notifyForNewAssignment(AssignmentDataModel assignment) {
        ResourceDataModel resource = assignment.getResourceDataModel();
        getResourceSimulator(resource).setResourceState(ResourceSimulator.BUSY);
    }

    public void notifyForAssignmentFinished(AssignmentDataModel assignmentDataModel) {
        ResourceDataModel resource = assignmentDataModel.getResourceDataModel();
        getResourceSimulator(resource).setResourceState(ResourceSimulator.IDLE);
    }
}
