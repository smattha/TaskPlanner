package lms.thomas.actionsGenerations;

import java.util.Iterator;

import Plan.Process.Task.Operations.Operations;
import Plan.Process.Task.Operations.Actions.Actions;
import Plan.Process.Task.Operations.Actions.Detect;
import Plan.Process.Task.Operations.Actions.Move;
import Plan.Process.Task.Operations.Actions.Navigate;
import gr.upatras.lms.util.Convert;
import hibernate.Actionsdb;
import hibernate.Operationsdb;
import hibernate.Parametersdb;
import hibernate.Part;
import xmlParser.Constants;
import xmlParser.CreateXmlFileDemo;

public class actionsGenerator {

	
    public static boolean  templateFill(Operationsdb opdb,Operations operation)
    {
   	 

         //if (opdb.getType().equals(Constants.OPERATIONS_PICK)){
       	 
       	 //operation= new Place(opdb.getName(), getTool(tool),w1,tasks,theTaskprecedenceconstraints,null,opdb.getDescription());
       	 
       	 for (Iterator iter=opdb.getActionsdbs().iterator();iter.hasNext();) {
   	          
       		  Actionsdb curAction=(Actionsdb) iter.next();	 
       		  Actions command=null;
       		  
       			if (curAction.getType().equals(Constants.ACTIONS_LOCALIZE)){
       				
       				//new 
       			}
       			else if (curAction.getType().equals(Constants.ACTIONS_NAVIGATE)){
       				Part p1=(Part)opdb.getParts().iterator().next();
       				command= new Navigate(curAction.getName(),curAction.getDescription(),p1.getWorkingareadb().getName());
       			}
       			else if (curAction.getType().equals(Constants.ACTIONS_APPROACH)){
       				
       			}
       			else if (curAction.getType().equals(Constants.ACTIONS_DETECT)){
       				
       						Part p1=(Part)opdb.getParts().iterator().next();
       				       
           				command=new Detect(curAction.getName(),curAction.getDescription(),"3","4", p1.getWorkingareadb().getName());
       			}
       			else if (curAction.getType().equals(Constants.ACTIONS_ALIGN)){
       				
       			}
       		    else if (curAction.getType().equals(Constants.ACTIONS_ATTACH)){
       		    	
       		    }			
       		    else if (curAction.getType().equals(Constants.ACTIONS_GRASP)){
       		    	
       		    }
        		    else if (curAction.getType().equals(Constants.ACTIONS_PRE_REACT)){
       		    	
       		    }
       		    else if (curAction.getType().equals(Constants.ACTIONS_RETRACT)){
       		    	
       		    }
       		    else if (curAction.getType().equals(Constants.ACTIONS_MOVE)){
       		    
       		    //for (Iterator iter=op.getParametersdbs().iterator();iter.hasNext();) {                		    		 
       		    	    Iterator iterParams=curAction.getParametersdbs().iterator();
       		      	   	  Parametersdb curOp=(Parametersdb) iterParams.next();	
       		      	   	   
       		      	   	  command=new Move(curOp.getValue());
       		      			      	  
       		      //}
       		    }
       		    else if (curAction.getType().equals(Constants.ACTIONS_POST_ATTACH)){
       		    	
       		    }
       		    else if (curAction.getType().equals(Constants.ACTIONS_RELEASE)){
       		    	
       		    }    
       		    else if (curAction.getType().equals(Constants.ACTIONS_DETECT_BARCODE)){
       		    	
       		    }    
       		    else if (curAction.getType().equals(Constants.ACTIONS_DETECT_SCREWING_POSE)){
       		    	
       		    }   
       		    else if (curAction.getType().equals(Constants.ACTIONS_SCREW)){
       		    	
       		    } 
       		    else if (curAction.getType().equals(Constants.ACTIONS_READ_BARCODE)){
       		    	
       		    }   		
       		    else {
   		    	  Iterator iterParams=curAction.getParametersdbs().iterator();
 		      	   	  Parametersdb curOp=(Parametersdb) iterParams.next();	
 		      	   	   
 		      	   	  command=new Move(curOp.getValue());
 		      			      	  
       		    }
if(operation.equals(null))
{
	}
else {
       			operation.setAction(command);
   	      }
       	 }  	 
       	
       	 
       	// }
         
   	 //System.out.println("It worked");
   	 
   	 //storeXML(operation);
   	 
   	 return true;
    }
    static private int i=1;
   static public  boolean storeXML(Operations op,String pathRoot)
    {
  
   		 CreateXmlFileDemo doc= new CreateXmlFileDemo();
   		 op.convert2XmlElement(doc);
   		 doc.store(pathRoot+Convert.getString(i++)+".xml");
   		
   return true;
    }
    
	


}