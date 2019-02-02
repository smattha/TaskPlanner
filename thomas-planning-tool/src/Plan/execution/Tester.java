package execution;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Vector;

import org.hibernate.Session;
import org.w3c.dom.Document;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import Plan.Process.Task.Operations.Operations;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import hibernate.HibernateUtil;
import hibernate.Processdb;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;
import planning.model.AssignmentDataModel;
import planning.model.io.AbstractInputSource;
import planning.model.io.FileInputSource;
import planning.model.io.HttpInputSource;
import planning.scheduler.algorithms.impact.IMPACT;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import testingDemo.MainPlanningTool;
import testingDemo.criteria.Utilization;
import xmlParser.CreateXmlFileDemo;

import java.io.FileWriter;
import java.io.IOException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
 


public class Tester {
	
	static executor d1=new executor();
	
	static PLANNINGINPUT layoutPlanningInput;
	
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8013), 0);
        server.createContext("/test2", new MyHandler());
        server.setExecutor(null); // creates a default executor
        
        server.createContext("/test3", new MyHandler2());
        server.setExecutor(null); //
        
        server.createContext("/test4", new MyHandler3());
        server.setExecutor(null); //
        
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {    	
        	
            String response = "{\"name\":\"Chuck\"}";
            layoutPlanningInput = d1.loadPlanningInput();
           
            JSONObject obj = new JSONObject();
            
            
            JSONObject obj1 = new JSONObject();
           
    		//obj.put("Name", "crunchify.com");
    		obj.put("id", "AppShah");
     

    		obj.put("name", "w");  		   		
    		//obj.put( "data",obj);
    		t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
    		t.getResponseHeaders().set("Content-Type", "application/json, charset=UTF-8");
            t.sendResponseHeaders(200, obj.toString().length());


            
            OutputStream os = t.getResponseBody();
            os.write(obj.toString().getBytes());
            
            os.close();
        }
    }
        
        static class MyHandler2 implements HttpHandler {
            @Override
            public void handle(HttpExchange t) throws IOException {    	
            	
                String response = "{\"name\":\"Chuck \"}";
    			d1.generateTemplates();
                
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            
        }
            static class MyHandler3 implements HttpHandler{
            	
            	public void handle(HttpExchange t) throws IOException { 
           
            		    Document document = null;
            			try {
            				document = LayoutPlanningInputGenerator.getPlanningInputXMLDocumentFromJaxb(layoutPlanningInput);
            			} catch (Exception e) {

            			}
            	        
            			MainPlanningTool tool = new MainPlanningTool(document);
            			tool.initializeSimulator();

            			IMPACT mptIMPACT = (IMPACT) tool.getAlgorithmFactoryforConfiguration()
            					.getAlgorithmInstance(IMPACT.MULTICRITERIA);
            			
            			mptIMPACT.setCriteria(new AbstractCriterion[] { new Utilization("test") });
            			            			
            			
            			int dh = 2;
            			int mna = 100;
            				
            			int sr = 2;

            			mptIMPACT.setDH(dh);
            			mptIMPACT.setMNA(mna);
            			mptIMPACT.setSR(sr);

            			tool.simulate();

            			Vector<AssignmentDataModel> assignments = tool.getAssignmentDataModelVector();
            	 		 CreateXmlFileDemo doc= new CreateXmlFileDemo();
            	   		 
            			System.out.println("");System.out.println("");System.out.println("");
            			for (AssignmentDataModel ass:assignments) {

            				//d1.storeXML(d1.planningInput);
            				
            				String taskID=ass.getTaskDataModel().getTaskId();
            				Operations op1=Operations.mapOperationsThomas2Id.get(taskID);
            				
            				System.out.println(ass.getTaskDataModel().getTaskName() + "   "
            						+ ass.getResourceDataModel().getResourceName()  +" "+op1.name  );//.getProperty("WorkingArea" ));
            				op1.assigned=ass.getResourceDataModel().getResourceName();
            				
            				op1.convert2XmlElement(doc);
            			                				
            			
            			}
            			
            			doc.store("C:\\Users\\smatt\\Desktop\\xml\\program.xml");

            			System.out.println("Finished");
            			
            			System.exit(0);
            			
            			System.out.println("Finished 2");
            			
                        String response = "{\"name\":\"Chuck \"}";
            			//d1.generateTemplates();
                        
                        t.sendResponseHeaders(200, response.length());
                        OutputStream os = t.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
            			
            			return ;
            			
            		}

            	 
            }
            
            	
            }
        
       	
       
  

        

	 
		




			
			


			
		
