package lms.robopartner.mfg_planning_tool.impact.interfaces;

import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.slf4j.LoggerFactory;

import planning.model.AssignmentDataModel;
import planning.scheduler.algorithms.impact.LayerNode.Assignment;
import planning.scheduler.algorithms.impact.interfaces.DecisionPointAssignmentsConsumerInterface;
import planning.scheduler.simulation.ResourceSimulator;
import planning.scheduler.simulation.TaskSimulator;
import planning.scheduler.simulation.interfaces.PlanHelperInterface;

/**
 * This class consume the assignments and evalutes the average criteria values.
 * 
 * @author Spyros
 *
 */
public class AverageRawCriteriaValuesDecisionPointAssignmentsConsumer implements DecisionPointAssignmentsConsumerInterface {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(AverageRawCriteriaValuesDecisionPointAssignmentsConsumer.class);
    private double[] cumulativeCriteriaValues;
    private Set<Calendar> differentTimes = new TreeSet<Calendar>();

    /*
     * (non-Javadoc)
     * 
     * @see planning.scheduler.algorithms.impact.interfaces.
     * DecisionPointAssignmentsConsumerInterface
     * #consumeDecisionPointAssignments(java.util.Calendar,
     * planning.scheduler.simulation.interfaces.PlanHelperInterface,
     * java.util.Vector, double[])
     */
    public Vector<AssignmentDataModel> consumeDecisionPointAssignments(Calendar timeNow, PlanHelperInterface helper, Vector<Assignment> decisionPointAssignments, double[] criteriaValues) {

        Vector<AssignmentDataModel> assignments = new Vector<AssignmentDataModel>();
        for (int i = 0; i < decisionPointAssignments.size(); i++) {
            Assignment layerAssignment = decisionPointAssignments.get(i);
            TaskSimulator taskSimulator = layerAssignment.getTask();
            ResourceSimulator resourceSimulator = layerAssignment.getResource();

            // Creating the assignment

            AssignmentDataModel assignment = new AssignmentDataModel(taskSimulator.getTaskDataModel(), resourceSimulator.getResourceDataModel(), timeNow,
                helper.getSetUpTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, timeNow, decisionPointAssignments)
                    + helper.getOperationTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, timeNow, decisionPointAssignments)
                    + helper.getResourceDownTimeInMillisecondsForTaskOnResource(taskSimulator, resourceSimulator, timeNow, decisionPointAssignments), false, null);
            assignments.add(assignment);
        }
        // add
        if (timeNow != null) {
            differentTimes.add(timeNow);
            this.addCriteria(criteriaValues);
        }
        return assignments;
    }

    /**
     * @param arrayForInitialization
     * @param size
     * @return
     */
    private static double[] initiliazeCriteriaVector(double[] arrayForInitialization, int size) {
        if (arrayForInitialization != null) {
            return arrayForInitialization;
        }
        else {
            arrayForInitialization = new double[size];
            for (int i = 0; i < arrayForInitialization.length; i++) {
                arrayForInitialization[i] = 0d;
            }
            return arrayForInitialization;
        }

    }

    /**
     * @param criteriaValues
     */
    private void addCriteria(double[] criteriaValues) {
        String msg=".addCriteria(): ";
        if(criteriaValues!=null&&criteriaValues.length>0){
            this.cumulativeCriteriaValues=AverageRawCriteriaValuesDecisionPointAssignmentsConsumer.initiliazeCriteriaVector(this.cumulativeCriteriaValues, criteriaValues.length);
        
        for (int i = 0; i < this.cumulativeCriteriaValues.length; i++) {
            logger.trace(msg+" Previous Value:"+this.cumulativeCriteriaValues[i]+" Next Value:"+(this.cumulativeCriteriaValues[i] + criteriaValues[i]));
            this.cumulativeCriteriaValues[i] += criteriaValues[i];
            
        }}
        
    }

    /**
     * @return null in the following cases:
     *         1. The
     *         {@link AverageRawCriteriaValuesDecisionPointAssignmentsConsumer#cumulativeCriteriaValues}
     *         has not be initialized - no values have been accumulated yet.
     *         2. The criteriaValues provided consumed are of zero length.
     * 
     */
    public double[] getAverageCriteriaValues() {
        double[] averageValues = null;
        if (this.cumulativeCriteriaValues != null && this.cumulativeCriteriaValues.length > 0) {
            averageValues= AverageRawCriteriaValuesDecisionPointAssignmentsConsumer.initiliazeCriteriaVector(averageValues, this.cumulativeCriteriaValues.length);
            for (int i = 0; i < averageValues.length; i++) {
                averageValues[i] = cumulativeCriteriaValues[i] / (double) this.getDecisionPoints();
            }
            return averageValues;
        }
        else
            return null;// return null
    }

    /** Returns the sum of the {@link AverageRawCriteriaValuesDecisionPointAssignmentsConsumer#getAverageCriteriaValues()}
     * or null if the {@link AverageRawCriteriaValuesDecisionPointAssignmentsConsumer#getAverageCriteriaValues()} is null
     * @return
     */
    public Double getAveragedSumCriteriaValues() {
        double[] averageValues = this.getAverageCriteriaValues();
        if (averageValues != null) {
            double sum = 0;
            for (int i = 0; i < averageValues.length; i++) {
                sum += averageValues[i];

            }
            return sum;
        }
        else
            return null;
    }

    /**
     * @return
     */
    public int getDecisionPoints() {
        return this.differentTimes.size();
    }
}
