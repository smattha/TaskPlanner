package planning.scheduler.simulation;

import java.util.Calendar;
import java.util.Vector;

import planning.scheduler.simulation.interfaces.PlanHelperInterface;

public class WorkloadAllocationUntilDateEndRule implements PlanEndRule
{
    private Calendar planEndDate = null;

    public WorkloadAllocationUntilDateEndRule(Calendar planEndDate)
    {
        this.planEndDate = planEndDate;
    }

    public boolean continuePlan(Vector<Interupt> interupts, PlanHelperInterface planHelperInterface)
    {
        Calendar interuptDate =null;
        if(interupts.size()>0)
            interuptDate = interupts.get(0).getDate();
        if(planEndDate!=null && planEndDate.getTimeInMillis() <= interuptDate.getTimeInMillis())
        {
            return false;
        }
        return true;
    }

    public String getHumanReadableDescription()
    {
        return "Plan End Date "+planEndDate.getTime().toString()+", is reached and the plan should stop assignment allocation.";
    }
}
