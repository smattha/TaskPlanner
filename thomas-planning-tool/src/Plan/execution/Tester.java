package execution;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import org.hibernate.Session;
import org.w3c.dom.Document;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import hibernate.HibernateUtil;
import hibernate.Processdb;
import planning.model.io.AbstractInputSource;
import planning.model.io.FileInputSource;
import planning.model.io.HttpInputSource;
import java.io.FileWriter;
import java.io.IOException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
 

public class Tester {
	
	static executor d1=new executor();
	

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8012), 0);
        server.createContext("/test2", new MyHandler());
        server.setExecutor(null); // creates a default executor
        
        server.createContext("/test3", new MyHandler2());
        server.setExecutor(null); //
        
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {    	
        	
            String response = "{\"name\":\"Chuck\"}";
            PLANNINGINPUT layoutPlanningInput = d1.loadPlanningInput();
           
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
            os.
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
        
        
        /* 
        static class loadProcessdb
        {
        	//void loadProcess(){
        		
        		static public List< Processdb > loadTask( ) {
        			
        		     Session sessionFactory = HibernateUtil.getSessionFactory().openSession();
        		     
        		     sessionFactory.beginTransaction();
        		     
        		     return sessionFactory.getSessionFactory().getCurrentSession().createCriteria(Processdb.class).list();

        		}
        	}
*/

        	
        }
  

        

	 
		




			
			


			
		
