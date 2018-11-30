package planning.model;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import planning.ViewToPlanningInputDataModelInterface;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class PlanningInputDataModel extends AbstractDataModel implements ViewToPlanningInputDataModelInterface {
    private Vector<ResourceDataModel> resources = null;
    private Vector<TaskDataModel> tasks = null;
    private Vector<TaskPrecedenceConstraintDataModel> precedenceConstraints = null;
    private Vector<TaskSuitableResourceDataModel> tasksSuitableResources = null;
    private Vector<WorkcenterDataModel> workcenters = null;
    private Vector<JobDataModel> jobs = null;
    private Calendar planStartDate = null;
    private Calendar planEndDate = null;
    private boolean continueAssignmentsAfterPlanEndDate = true;
    private String planningInputDataModelId = null;
    private HashMap<String, SetUpMatrixDataModel> setUpMatricesDataModelHashMap = null;
    private Vector<ToolTypeDataModel> toolTypes = null;
    private Vector<ToolDataModel> tools = null;
    private Vector<MobileResourceTypeDataModel> mobileResourceTypes = null;
    private Vector<MobileResourceDataModel> mobileResources = null;

    public PlanningInputDataModel() {
        resources = new Vector<ResourceDataModel>();
        tasks = new Vector<TaskDataModel>();
        precedenceConstraints = new Vector<TaskPrecedenceConstraintDataModel>();
        tasksSuitableResources = new Vector<TaskSuitableResourceDataModel>();
        workcenters = new Vector<WorkcenterDataModel>();
        jobs = new Vector<JobDataModel>();
        toolTypes = new Vector<ToolTypeDataModel>();
        tools = new Vector<ToolDataModel>();
        mobileResourceTypes = new Vector<MobileResourceTypeDataModel>();
        mobileResources = new Vector<MobileResourceDataModel>();
        this.planningInputDataModelId = "UNSPECIFIED";
    }

    public PlanningInputDataModel(Vector<ResourceDataModel> resources, Vector<TaskDataModel> tasks, Vector<TaskPrecedenceConstraintDataModel> precedenceConstraints, Vector<TaskSuitableResourceDataModel> tasksSuitableResources, Vector<WorkcenterDataModel> workcenters, Vector<JobDataModel> jobs, Vector<ToolTypeDataModel> toolTypes, Vector<ToolDataModel> tools, Vector<MobileResourceTypeDataModel> mobileResourceTypes, Vector<MobileResourceDataModel> mobileResources) {
        this.resources =  resources;
        this.tasks = tasks;
        this.precedenceConstraints = precedenceConstraints;
        this.tasksSuitableResources = tasksSuitableResources;
        this.workcenters = workcenters;
        this.jobs = jobs;
        this.toolTypes = toolTypes;
        this.tools = tools;
        this.mobileResourceTypes = mobileResourceTypes;
        this.mobileResources = mobileResources;
        this.planningInputDataModelId = "UNSPECIFIED";
    }

    @Override
    public Node toXMLNode(Document document) {
        Element root = document.getDocumentElement();
        root.setAttribute("id", this.planningInputDataModelId);
        root.setAttribute("planStartDate_day", "" + this.planStartDate.get(Calendar.DAY_OF_MONTH));
        root.setAttribute("planStartDate_month", "" + (this.planStartDate.get(Calendar.MONTH) + 1));
        root.setAttribute("planStartDate_year", "" + this.planStartDate.get(Calendar.YEAR));
        root.setAttribute("planEndDate_day", "" + this.planEndDate.get(Calendar.DAY_OF_MONTH));
        root.setAttribute("planEndDate_month", "" + (this.planEndDate.get(Calendar.MONTH) + 1));
        root.setAttribute("planEndDate_year", "" + this.planEndDate.get(Calendar.YEAR));

        Node planningInputNode = document.createDocumentFragment();

        // making the work centers
        Element workcentersElement = document.createElement("WORKCENTERS");
        planningInputNode.appendChild(workcentersElement);
        for (int i = 0; i < this.workcenters.size(); i++) {
            WorkcenterDataModel workcenter = this.workcenters.get(i);
            workcentersElement.appendChild(workcenter.toXMLNode(document));
        }

        // making the resources
        Element resourcesElement = document.createElement("RESOURCES");
        planningInputNode.appendChild(resourcesElement);
        for (int i = 0; i < this.resources.size(); i++) {
            ResourceDataModel resource = this.resources.get(i);
            resourcesElement.appendChild(resource.toXMLNode(document));
        }

        // making the jobs
        Element jobsElement = document.createElement("JOBS");
        planningInputNode.appendChild(jobsElement);
        for (int i = 0; i < this.jobs.size(); i++) {
            JobDataModel job = this.jobs.get(i);
            jobsElement.appendChild(job.toXMLNode(document));
        }

        // making the tasks
        Element tasksElement = document.createElement("TASKS");
        planningInputNode.appendChild(tasksElement);
        for (int i = 0; i < this.tasks.size(); i++) {
            TaskDataModel task = this.tasks.get(i);
            tasksElement.appendChild(task.toXMLNode(document));
        }

        // making the task suitable resources
        Element taskSuitableResourcesElement = document.createElement("TASK_SUITABLE_RESOURCES");
        planningInputNode.appendChild(taskSuitableResourcesElement);
        for (int i = 0; i < this.tasksSuitableResources.size(); i++) {
            TaskSuitableResourceDataModel taskSuitableResource = this.tasksSuitableResources.get(i);
            taskSuitableResourcesElement.appendChild(taskSuitableResource.toXMLNode(document));
        }

        // making the task precedence constraints
        Element taskPrecedenceConstraintsElement = document.createElement("TASK_PRECEDENCE_CONSTRAINTS");
        planningInputNode.appendChild(taskPrecedenceConstraintsElement);
        for (int i = 0; i < this.precedenceConstraints.size(); i++) {
            TaskPrecedenceConstraintDataModel taskPrecedenceConstraint = this.precedenceConstraints.get(i);
            taskPrecedenceConstraintsElement.appendChild(taskPrecedenceConstraint.toXMLNode(document));
        }

        // making the set up matrices
        Element setUpMatricesElement = document.createElement("SET_UP_MATRICES");
        planningInputNode.appendChild(setUpMatricesElement);
        Collection<SetUpMatrixDataModel> collection = setUpMatricesDataModelHashMap.values();
        Iterator<SetUpMatrixDataModel> iterator = collection.iterator();
        while (iterator.hasNext()) {
            SetUpMatrixDataModel setUpMatrixDataModel = iterator.next();
            setUpMatricesElement.appendChild(setUpMatrixDataModel.toXMLNode(document));
        }

        return planningInputNode;
    }

    public TaskDataModel getTaskDataModel(String taskid) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskId().equals(taskid)) {
                return tasks.get(i);
            }
        }
        return null;
    }

    public ResourceDataModel getResourceDataModel(String resourceId) {
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i).getResourceId().equals(resourceId)) {
                return resources.get(i);
            }
        }
        return null;
    }

    public WorkcenterDataModel getWorkcenterDataModel(String workcenterId) {
        for (int i = 0; i < workcenters.size(); i++) {
            if (workcenters.get(i).getWorkcenterId().equals(workcenterId)) {
                return workcenters.get(i);
            }
        }
        return null;
    }

    public void setResourceDataModelVector(Vector<ResourceDataModel> resources) {
        // TODO : implementation and check of pre-initialised values
        // i.e. : if the attribute is null (not initialised) then set this attribute
        // else throw exception that the attribute is already initialised.
        this.resources = resources;
        notifyObservers();
    }

    public void setWorkcenterDataModelVector(Vector<WorkcenterDataModel> workcenters) {
        // TODO : implementation and check of pre-initialised values
        // i.e. : if the attribute is null (not initialised) then set this attribute
        // else throw exception that the attribute is already initialised.
        // TODO : Check data consistency
        this.workcenters = workcenters;
        notifyObservers();
    }

    public void setJobDataModelVector(Vector<JobDataModel> jobs) {
        // TODO : implementation and check of pre-initialised values
        // i.e. : if the attribute is null (not initialised) then set this attribute
        // else throw exception that the attribute is already initialised.
        // TODO : Check data consistency
        this.jobs = jobs;
        notifyObservers();
    }

    public void setTaskDataModelVector(Vector<TaskDataModel> tasks) {
        // Todo :
        // 1) implementation and check of pre initialized values
        // i.e. : if the attribute is null (not initialized) then set this
        // attribute
        // else throw exception that the attribute is allready initialized.
        this.tasks = tasks;
        notifyObservers();
    }

    public void setTaskPrecedenceConstraintsDataModelVector(Vector<TaskPrecedenceConstraintDataModel> precedenceConstraints) {
        // Todo :
        // 1) implementation and check of pre initialized values
        // i.e. : if the attribute is null (not initialized) then set this
        // attribute
        // else throw exception that the attribute is allready initialized.
        // 2) check persistency ammong data (the tasks\resources that exist in
        // the vector to allready exist in the model
        this.precedenceConstraints = precedenceConstraints;
        notifyObservers();
    }

    public void setTaskSuitableResourceDataModelVector(Vector<TaskSuitableResourceDataModel> tasksSuitableResources) {
        // Todo :
        // 1) implementation and check of pre initialized values
        // i.e. : if the attribute is null (not initialized) then set this
        // attribute
        // else throw exception that the attribute is allready initialized.
        // 2) check persistency ammong data (the tasks\resources that exist in
        // the vector to allready exist in the model
        this.tasksSuitableResources = tasksSuitableResources;
        notifyObservers();
    }

    public void setPlanningInputDataModelId(String planningInputDataModelId) {
        this.planningInputDataModelId = planningInputDataModelId;
    }

    public String getPlanningInputDataModelId() {
        return planningInputDataModelId;
    }

    public void setSetUpMatrices(HashMap<String, SetUpMatrixDataModel> setUpMatrices) {
        this.setUpMatricesDataModelHashMap = setUpMatrices;
    }

    public SetUpMatrixDataModel getSetUpMatrixDataModel(String id) {
        SetUpMatrixDataModel returnedSetUpMatrixDataModle = null;
        if (setUpMatricesDataModelHashMap != null) {
            returnedSetUpMatrixDataModle = setUpMatricesDataModelHashMap.get(id);
        }

        return returnedSetUpMatrixDataModle;
    }

    @SuppressWarnings("unchecked")
    private Vector<JobDataModel> getJobs() {
        return (Vector<JobDataModel>) this.jobs.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<WorkcenterDataModel> getWorkcenterDataModelVector() {
        return (Vector<WorkcenterDataModel>) this.workcenters.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<ResourceDataModel> getResourceDataModelVector() {
        return (Vector<ResourceDataModel>) this.resources.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<TaskDataModel> getTaskDataModelVector() {
        return (Vector<TaskDataModel>) this.tasks.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<TaskSuitableResourceDataModel> getTaskSuitableResourceDataModelVector() {
        return (Vector<TaskSuitableResourceDataModel>) this.tasksSuitableResources.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<TaskPrecedenceConstraintDataModel> getTaskPrecedenceConstraintDataModelVector() {
        return (Vector<TaskPrecedenceConstraintDataModel>) this.precedenceConstraints.clone();
    }

    @Override
    public Calendar getPlanStartDate() {
        return (Calendar) this.planStartDate.clone();
    }

    @Override
    public Calendar getPlanEndDate() {
        return (Calendar) this.planEndDate.clone();
    }

    public boolean continueAssignmentsAfterPlanEndDate() {
        return this.continueAssignmentsAfterPlanEndDate;
    }

    @Override
    public JobDataModel getJobDataModelForTaskDataModel(TaskDataModel taskDataModel) {
        for (int i = 0; i < jobs.size(); i++) {
            JobDataModel job = jobs.get(i);
            if (job.contains(taskDataModel)) {
                return job;
            }
        }
        return null;
    }

    /********************************************************************************/
    /*************************** SETTERS PUBLIC FUNCTIONS ***************************/
    /********************************************************************************/

    public void setPlanStartDate(Calendar newPlanStartDate) {
        this.planStartDate = newPlanStartDate;
        notifyObservers();
    }

    public void setPlanEndDate(Calendar newPlanEndDate) {
        this.planEndDate = newPlanEndDate;
        notifyObservers();
    }

    public void setNameToWorkcenterDataModel(String newName, WorkcenterDataModel workcenter) {
        workcenter.setName(newName);
        notifyObservers();
    }

    public void setAlgorithmToWorkcenterDataModel(String algorithm, WorkcenterDataModel workcenter) {
        workcenter.setAlgorithm(algorithm);
        notifyObservers();
    }

    public void addResourceDataModelToWorkcenter(ResourceDataModel newResource, WorkcenterDataModel workcenter) {
        workcenter.addResourceDataModel(newResource);
        this.resources.add(newResource);
        notifyObservers();
    }

    public void setNameToResourceDataModel(String newName, ResourceDataModel resource) {
        resource.setResourceName(newName);
        notifyObservers();
    }

    public void setResourceAvailabilityDataModelToResourceDataModelName(ResourceAvailabilityDataModel newResourceAvailabilityDataModel, ResourceDataModel resource) {
        resource.setResourceAvailabilityDataModel(newResourceAvailabilityDataModel);
        notifyObservers();
    }

    public void setSetUpCodeToTaskSuitableResourceDataModel(String newSetUpCode, TaskSuitableResourceDataModel taskSuitableResourceDataModel) {
        taskSuitableResourceDataModel.setSetUpCode(newSetUpCode);
        notifyObservers();
    }

    public void setOperationTimeInMillisecondsToTaskSuitableResourceDataModel(long operationTimeInMilliseconds, TaskSuitableResourceDataModel taskSuitableResourceDataModel) {
        taskSuitableResourceDataModel.setOperationTimeInMilliseconds(operationTimeInMilliseconds);
        notifyObservers();
    }

    public void setContinueAssignmentsAfterPlanEndDate(boolean continueAssignmentsAfterPlanEndDate) {
        this.continueAssignmentsAfterPlanEndDate = continueAssignmentsAfterPlanEndDate;
        notifyObservers();
    }

    public void setNameToJobDataModel(String newName, JobDataModel jobDataModel) {
        jobDataModel.setName(newName);
        notifyObservers();
    }

    public void setTaskDataModelVectorToJobDataModel(Vector<TaskDataModel> newTasks, JobDataModel jobDataModel) {
        jobDataModel.setTasks(newTasks);
        notifyObservers();
    }

    public void setArrivalDateToJobDataModel(Calendar newArrivalCalendarDate, JobDataModel jobDataModel) {
        jobDataModel.setArrivalDate(newArrivalCalendarDate);
        notifyObservers();
    }

    public void setDueDateToJobDataModel(Calendar newDueCalendarDate, JobDataModel jobDataModel) {
        jobDataModel.setDueDate(newDueCalendarDate);
        notifyObservers();
    }

    public void addTaskDataModelToJob(TaskDataModel newTaskDataModel, JobDataModel jobDataModel) {
        jobDataModel.addTask(newTaskDataModel);
        this.tasks.add(newTaskDataModel);
        this.calculateTaskArrivalAndDueDates(jobDataModel);
        notifyObservers();
    }

    public void setNameToTaskDataModel(String newName, TaskDataModel taskDataModel) {
        taskDataModel.setTaskName(newName);
        notifyObservers();
    }

    /**
     * Initialises the tasks due dates depending on job arrival and due dates
     */
    public void calculateTaskArrivalAndDueDates() {
        for (int i = 0; i < jobs.size(); i++) {
            JobDataModel jobDataModel = jobs.get(i);
            calculateTaskArrivalAndDueDates(jobDataModel);
        }
    }

    public void calculateTaskArrivalAndDueDates(JobDataModel jobDataModel) {
        // Because the precedence constraints draws a tree we have to model them
        // into trees.
        // The tree nodes are the tasks, and the hole tree is the job.
        // The algorithm must:
        // 1) Search all possible paths till reaching the leafs.
        // 2) For each path and taking into consideration the mean operation
        // time of each task\node (without the setup times and down times of
        // resource assuming that they are evenly distributed among the tasks)
        // calculating a weight for each task\node that represent the mean
        // operation
        // of the task divided by the path's overall duration (summing of each
        // tasks
        // mean operation time). With this weight calculate each task due date
        // by multiplying it with the job's duration (arrivalDate - dueDate)
        // and adding it to the previous in the path task/node due date.
        // 3) For each task/node take the average of their due dates (since one
        // task can be found in more than one paths.

        TaskNodeVector allTaskNodes = new TaskNodeVector();
        Vector<TaskDataModel> taskDataModelVector = jobDataModel.getTasks();
        for (int j = 0; j < taskDataModelVector.size(); j++) {
            TaskDataModel currentTaskDataModel = taskDataModelVector.get(j);

            Vector<TaskPrecedenceConstraintDataModel> currentTaskDataModelPrecedenceConstraints = getPrecedenceConstraintsForTaskDataModel(currentTaskDataModel, precedenceConstraints);
            if (currentTaskDataModelPrecedenceConstraints.size() == 0) // No
                                                                       // constraints
                                                                       // so
                                                                       // arrival
                                                                       // and
                                                                       // due
                                                                       // date
                                                                       // are
                                                                       // the
                                                                       // dates
                                                                       // of the
                                                                       // job
            {
                currentTaskDataModel.setTaskArrrivalDate(jobDataModel.getArrivalDate());
                currentTaskDataModel.setTaskDueDate(jobDataModel.getDueDate());
                System.out.println("task : " + currentTaskDataModel.getTaskName() + " initialized with arrival : " + currentTaskDataModel.getTaskArrivalDate().getTime() + " , due : " + currentTaskDataModel.getTaskDueDate().getTime());
            } else // Find if there are constraints between tasks of the current
                   // job
            {
                boolean foundConstrainInsideTheJob = false;
                for (int k = 0; k < currentTaskDataModelPrecedenceConstraints.size(); k++) {
                    TaskPrecedenceConstraintDataModel currentTaskDataModelPrecedenceConstraint = currentTaskDataModelPrecedenceConstraints.get(k);
                    if (jobDataModel.contains(currentTaskDataModelPrecedenceConstraint.getPreconditionTask()) && jobDataModel.contains(currentTaskDataModelPrecedenceConstraint.getPostconditionTask())) {
                        foundConstrainInsideTheJob = true;
                        break;
                    }
                }

                if (!foundConstrainInsideTheJob) // No constraints inside the
                                                 // job so arrival and due date
                                                 // are the dates of the job
                {
                    currentTaskDataModel.setTaskArrrivalDate(jobDataModel.getArrivalDate());
                    currentTaskDataModel.setTaskDueDate(jobDataModel.getDueDate());
                    System.out.println("task : " + currentTaskDataModel.getTaskName() + " initialized with arrival : " + currentTaskDataModel.getTaskArrivalDate().getTime() + " , due : " + currentTaskDataModel.getTaskDueDate().getTime());
                } else // task with constraints inside the job. Wrap this task
                       // into a node to keep path data and add it into the
                       // allNodes vector
                {
                    allTaskNodes.add(new TaskNode(currentTaskDataModel));
                }
            }
        }

        // At this point all the tasks of the job that are not inside a path of
        // the tree (have no precedence constraints inside the job)
        // have been initialized. Now its time to construct the paths
        for (int j = 0; j < allTaskNodes.size(); j++) {
            TaskNode taskNode = allTaskNodes.get(j);
            TaskDataModel task = taskNode.getTaskDataModel();
            // Try to find the roots
            if (getPreconditionsConstraintsForTaskDataModelInsideTheSameJob(jobDataModel, task, precedenceConstraints).size() == 0) {
                // Root Node found
                TaskNode rootNode = taskNode;

                // Building paths
                Vector<Vector<TaskNode>> rootPaths = getAllPathsForTheRoot(jobDataModel, rootNode, precedenceConstraints, allTaskNodes);

                // Loop for each path
                for (int k = 0; k < rootPaths.size(); k++) {
                    // The path finally
                    Vector<TaskNode> singlePath = rootPaths.get(k);
                    // Now for each node of the path we must make a weight in
                    // order to be used for the due date calculation
                    // Loop to make the overall operation time of the path
                    long pathOperationTimeInMilliseconds = 0;
                    for (int l = 0; l < singlePath.size(); l++) {
                        TaskNode pathTaskNode = singlePath.get(l);
                        // Loop to find task/node meanOperational time
                        long sumOperationalTimeInMilliseconds = 0;
                        long meanOperationalTimeInMilliseconds = 0;
                        int suitableResourcesSize = 0;
                        for (int m = 0; m < tasksSuitableResources.size(); m++) {
                            TaskSuitableResourceDataModel suitable = tasksSuitableResources.get(m);
                            if (suitable.getTaskDataModel().equals(pathTaskNode.getTaskDataModel())) {
                                sumOperationalTimeInMilliseconds += suitable.getOperationTimeInMilliseconds();
                                suitableResourcesSize++;
                            }
                        }
                        if (suitableResourcesSize != 0)
                            meanOperationalTimeInMilliseconds = sumOperationalTimeInMilliseconds / suitableResourcesSize;
                        else
                            meanOperationalTimeInMilliseconds = 0;

                        pathTaskNode.setMeanOperationalTimeInMilliseconds(meanOperationalTimeInMilliseconds);
                        pathOperationTimeInMilliseconds += meanOperationalTimeInMilliseconds;
                    }

                    // Having the overall pathOperationaTime calculating the due
                    // dates
                    // Second loop to find due dates
                    // Keeping reference of the previous
                    Calendar previousCalculatedDueDate = null;
                    long jobDurationInMilliseconds = jobDataModel.getDueDate().getTimeInMillis() - jobDataModel.getArrivalDate().getTimeInMillis();
                    for (int l = 0; l < singlePath.size(); l++) {
                        // Getting the Node
                        TaskNode pathTaskNode = singlePath.get(l);
                        // Calculating the weight
                        double weight = (double) pathTaskNode.getMeanOperationalTimeInMilliseconds() / (double) pathOperationTimeInMilliseconds;
                        // Converting the weight to task duration time reference
                        // tor the job
                        long taskDurationInMilliseconds = (long) (jobDurationInMilliseconds * weight);
                        // Converting the task duration into due date
                        if (previousCalculatedDueDate == null) {
                            // First task node so add the task duration to the
                            // job arrival date
                            long calendarDueDateInMilliseconds = jobDataModel.getArrivalDate().getTimeInMillis() + taskDurationInMilliseconds;
                            Calendar taskDueDate = Calendar.getInstance();
                            taskDueDate.setTimeInMillis(calendarDueDateInMilliseconds);
                            // Setting the just calculated due date to the task
                            // node
                            pathTaskNode.addDueDate(taskDueDate);
                            // Setting the new due date
                            previousCalculatedDueDate = taskDueDate;
                        } else {
                            // Add the task duration to the previous calculated
                            // due date
                            long calendarDueDateInMilliseconds = previousCalculatedDueDate.getTimeInMillis() + taskDurationInMilliseconds;
                            Calendar taskDueDate = Calendar.getInstance();
                            taskDueDate.setTimeInMillis(calendarDueDateInMilliseconds);
                            // Setting the just calculated due date to the task
                            // node
                            pathTaskNode.addDueDate(taskDueDate);
                            // Setting the new due date
                            previousCalculatedDueDate = taskDueDate;
                        }
                    }
                }
            }
        }

        // And now finally to initialize the tasks (construct task simulators
        // with due dates)
        for (int j = 0; j < allTaskNodes.size(); j++) {
            TaskNode taskNode = allTaskNodes.get(j);
            TaskDataModel task = taskNode.getTaskDataModel();
            task.setTaskArrrivalDate(jobDataModel.getArrivalDate());
            task.setTaskDueDate(taskNode.getMeanDueDate());
            System.out.println("task : " + task.getTaskName() + " initialized with arrival : " + task.getTaskArrivalDate().getTime() + " , due : " + task.getTaskDueDate().getTime());
        }
    }

    private Vector<Vector<TaskNode>> getAllPathsForTheRoot(JobDataModel job, TaskNode root, Vector<TaskPrecedenceConstraintDataModel> precedenceConstraints, TaskNodeVector allTaskNodes) {
        Vector<Vector<TaskNode>> paths = new Vector<Vector<TaskNode>>();

        Vector<TaskPrecedenceConstraintDataModel> nextNodesConstraints = getPostconditionsConstraintsForTaskDataModelInsideTheSameJob(job, root.getTaskDataModel(), precedenceConstraints);
        if (nextNodesConstraints.size() == 0) {
            Vector<TaskNode> finalPath = new Vector<TaskNode>();
            finalPath.add(root);
            paths.add(finalPath);
        } else {
            // there are paths
            for (int i = 0; i < nextNodesConstraints.size(); i++) {
                TaskNode taskNode = allTaskNodes.getTaskNodeForTaskDataModel(nextNodesConstraints.get(i).getPostconditionTask());
                Vector<Vector<TaskNode>> subPaths = getAllPathsForTheRoot(job, taskNode, precedenceConstraints, allTaskNodes);
                for (int j = 0; j < subPaths.size(); j++) {
                    Vector<TaskNode> subPath = subPaths.get(j);
                    subPath.add(0, root);
                    paths.add(subPath);
                }
            }
        }
        return paths;
    }

    // Helper function for the initialization of task arrival/due dates
    private Vector<TaskPrecedenceConstraintDataModel> getPrecedenceConstraintsForTaskDataModel(TaskDataModel taskDataModel, Vector<TaskPrecedenceConstraintDataModel> precedenceConstraints) {
        Vector<TaskPrecedenceConstraintDataModel> precedenceConstraintsForTaskDataModel = new Vector<TaskPrecedenceConstraintDataModel>();
        for (int i = 0; i < precedenceConstraints.size(); i++) {
            TaskPrecedenceConstraintDataModel constraint = precedenceConstraints.get(i);
            // Check if task has preconditions
            if (constraint.getPostconditionTask().equals(taskDataModel) || constraint.getPreconditionTask().equals(taskDataModel)) {
                precedenceConstraintsForTaskDataModel.add(constraint);
            }
        }
        return precedenceConstraintsForTaskDataModel;
    }

    // Helper function for the initialization of task arrival/due dates
    private Vector<TaskPrecedenceConstraintDataModel> getPostconditionsConstraintsForTaskDataModelInsideTheSameJob(JobDataModel jobDataModel, TaskDataModel taskDataModel, Vector<TaskPrecedenceConstraintDataModel> precedenceConstraints) {
        Vector<TaskPrecedenceConstraintDataModel> precedenceConstraintsForTaskDataModel = new Vector<TaskPrecedenceConstraintDataModel>();
        for (int i = 0; i < precedenceConstraints.size(); i++) {
            TaskPrecedenceConstraintDataModel constraint = precedenceConstraints.get(i);
            // Check if task has preconditions
            if (constraint.getPreconditionTask().equals(taskDataModel) && jobDataModel.contains(constraint.getPostconditionTask())) {
                precedenceConstraintsForTaskDataModel.add(constraint);
            }
        }
        return precedenceConstraintsForTaskDataModel;
    }

    // Helper function for the initilization of task arrival/due dates
    private Vector<TaskPrecedenceConstraintDataModel> getPreconditionsConstraintsForTaskDataModelInsideTheSameJob(JobDataModel jobDataModel, TaskDataModel taskDataModel, Vector<TaskPrecedenceConstraintDataModel> precedenceConstraints) {
        Vector<TaskPrecedenceConstraintDataModel> precedenceConstraintsForTaskDataModel = new Vector<TaskPrecedenceConstraintDataModel>();
        for (int i = 0; i < precedenceConstraints.size(); i++) {
            TaskPrecedenceConstraintDataModel constraint = precedenceConstraints.get(i);
            // Check if task has preconditions
            if (constraint.getPostconditionTask().equals(taskDataModel) && jobDataModel.contains(constraint.getPreconditionTask())) {
                precedenceConstraintsForTaskDataModel.add(constraint);
            }
        }
        return precedenceConstraintsForTaskDataModel;
    }

    // Used for holding path traversal data for calculating task's due dates
    private class TaskNodeVector extends Vector<TaskNode> {
        /**
         * Serial version id
         */
        private static final long serialVersionUID = 297342351795305374L;

        public TaskNode getTaskNodeForTaskDataModel(TaskDataModel task) {
            try {
                for (int i = 0; i < this.size(); i++) {
                    TaskNode taskNode = this.get(i);
                    if (taskNode.getTaskDataModel().equals(task))
                        return taskNode;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    // Used to wrap the TaskDataModel in order to hold detailed informations
    // for calculating the Due Date
    private class TaskNode {
        private TaskDataModel task = null;
        private final Vector<Calendar> dueDates = new Vector<Calendar>();
        private long meanOperationalTimeInMilliseconds = 0;

        private TaskNode(TaskDataModel task) {
            this.task = task;
        }

        public TaskDataModel getTaskDataModel() {
            return this.task;
        }

        public Calendar getMeanDueDate() {
            long meanTimeInMilliseconds = 0;
            long sumTimeInMilliseconds = 0;
            if (dueDates.size() == 0) {
                // Means that the due date cannot be calculated maybe due to
                // cycling in precedence constrains
                // so make as due date the due date of the job ...
                JobDataModel job = getJobDataModelForTaskDataModel(this.task);
                Calendar taskWrongDueDate = Calendar.getInstance();
                taskWrongDueDate.setTimeInMillis(job.getDueDate().getTimeInMillis());
                return taskWrongDueDate;
            }
            for (int i = 0; i < dueDates.size(); i++) {
                sumTimeInMilliseconds += dueDates.get(i).getTimeInMillis();
            }
            meanTimeInMilliseconds = sumTimeInMilliseconds / dueDates.size();
            Calendar meanDueDate = Calendar.getInstance();
            meanDueDate.setTimeInMillis(meanTimeInMilliseconds);
            return meanDueDate;
        }

        public void addDueDate(Calendar newDueDate) {
            this.dueDates.add(newDueDate);
        }

        public void setMeanOperationalTimeInMilliseconds(long newTime) {
            this.meanOperationalTimeInMilliseconds = newTime;
        }

        public long getMeanOperationalTimeInMilliseconds() {
            return this.meanOperationalTimeInMilliseconds;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TaskNode) {
                return this.task.equals(((TaskNode) obj).getTaskDataModel());
            }
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see planning.ViewToPlanningInputDataModelInterface#getJobDataModelVector()
     */
    @Override
    public Vector<JobDataModel> getJobDataModelVector() {
        return getJobs();
    }

    /*
     * NEW IMPLEMENTATION OF TOOLS
     */

    public ToolTypeDataModel getToolTypeDataModel(String toolTypeId) {
        for (int i = 0; i < toolTypes.size(); i++) {
            if (toolTypes.get(i).getId().equals(toolTypeId)) {
                return toolTypes.get(i);
            }
        }
        return null;
    }

    public void setToolTypes(Vector<ToolTypeDataModel> toolTypes) {
        // TODO : implementation and check of pre-initialised values
        // i.e. : if the attribute is null (not initialised) then set this attribute
        // else throw exception that the attribute is already initialised.
        this.toolTypes = toolTypes;
        notifyObservers();
    }

    public ToolDataModel getToolDataModel(String toolId) {
        for (int i = 0; i < tools.size(); i++) {
            if (tools.get(i).getResourceId().equals(toolId)) {
                return tools.get(i);
            }
        }
        return null;
    }

    public void setTools(Vector<ToolDataModel> tools) {
        // TODO : implementation and check of pre-initialised values
        // i.e. : if the attribute is null (not initialised) then set this attribute
        // else throw exception that the attribute is already initialised.
        this.tools = tools;
        notifyObservers();
    }

    public MobileResourceTypeDataModel getMobileResourceTypeDataModel(String mobileResourceTypeId) {
        for (int i = 0; i < mobileResourceTypes.size(); i++) {
            if (mobileResourceTypes.get(i).getId().equals(mobileResourceTypeId)) {
                return mobileResourceTypes.get(i);
            }
        }
        return null;
    }

    public void setMobileResourceTypes(Vector<MobileResourceTypeDataModel> mobileResourceTypesDataModel) {
        // TODO : implementation and check of pre-initialised values
        // i.e. : if the attribute is null (not initialised) then set this attribute
        // else throw exception that the attribute is already initialised.
        this.mobileResourceTypes = mobileResourceTypesDataModel;
        notifyObservers();
    }

    public MobileResourceDataModel getMobileResourceDataModel(String mobileResourcesId) {
        for (int i = 0; i < mobileResources.size(); i++) {
            if (mobileResources.get(i).getResourceId().equals(mobileResourcesId)) {
                return mobileResources.get(i);
            }
        }
        return null;
    }

    public void setMobileResources(Vector<MobileResourceDataModel> mobileResourcesDataModel) {
        // TODO : implementation and check of pre-initialised values
        // i.e. : if the attribute is null (not initialised) then set this attribute
        // else throw exception that the attribute is already initialised.
        this.mobileResources = mobileResourcesDataModel;
        notifyObservers();
    }

}
