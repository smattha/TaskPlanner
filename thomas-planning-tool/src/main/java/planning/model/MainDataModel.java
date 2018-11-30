package planning.model;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import planning.ViewToPlanningInputDataModelInterface;
import planning.ViewToPlanningOutputDataModelInterface;
import planning.model.io.AbstractInputSource;
import planning.model.io.XMLToPlanningInputModel;
import planning.model.io.XMLToPlanningOutputModel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class MainDataModel extends AbstractDataModel
{
    private PlanningInputDataModel planningInputDataModel = null;
    private PlanningOutputDataModel planningOutputDataModel = null;

    AbstractInputSource inputSource = null;

    public MainDataModel(AbstractInputSource inputSource)
    {
        this.inputSource = inputSource;

        //Initialize the models
        // Todo :
        // 1) check persistency ammong data (the tasks\resources that exist in the output model to allready exist in the input model
        try
        {
            Document planningInputXMLDoument = inputSource.fetchPlanningInputXMLDocument();
            XMLToPlanningInputModel xmlToPlanningInputModel = new XMLToPlanningInputModel(planningInputXMLDoument);
            this.planningInputDataModel = xmlToPlanningInputModel.getPlanningInputDataModel();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            this.planningInputDataModel = new PlanningInputDataModel();
        }

        try
        {
            Document planningOutputXMLDoument = inputSource.fetchPlanningOutputXMLDocument();
            XMLToPlanningOutputModel xmlToPlanningOutputModel = new XMLToPlanningOutputModel(planningOutputXMLDoument, planningInputDataModel);
            this.planningOutputDataModel = xmlToPlanningOutputModel.getPlanningOutputDataModel();
        }
        catch(Exception ex)
        {
//            ex.printStackTrace();
            System.err.println("MainDataModel : "+ex.getMessage());
            this.planningOutputDataModel = new PlanningOutputDataModel();
        }

        this.planningOutputDataModel.setPlanningInputDataModel(this.planningInputDataModel);
    }

    public MainDataModel(PlanningInputDataModel planningInputDataModel, PlanningOutputDataModel planningOutputDataModel)
    {
        if(planningInputDataModel!=null)
        {
            this.planningInputDataModel = planningInputDataModel;
        }
        else
        {
            this.planningInputDataModel = new PlanningInputDataModel();
        }
        if(planningOutputDataModel!=null)
        {
            this.planningOutputDataModel = planningOutputDataModel;
        }
        else
        {
            this.planningOutputDataModel = new PlanningOutputDataModel();
        }
        this.planningOutputDataModel.setPlanningInputDataModel(this.planningInputDataModel);
    }

    public Node toXMLNode(Document document)
    {
        return null;
    }

    public ViewToPlanningInputDataModelInterface getViewToPlanningInputModelInterface()
    {
        return this.planningInputDataModel;
    }

    public ViewToPlanningOutputDataModelInterface getViewToPlanningOutputModelInterface()
    {
        return this.planningOutputDataModel;
    }

    public PlanningInputDataModel getPlanningInputDataModel()
    {
        return this.planningInputDataModel;
    }

    public PlanningOutputDataModel getPlanningOutputDataModel()
    {
        return this.planningOutputDataModel;
    }
}