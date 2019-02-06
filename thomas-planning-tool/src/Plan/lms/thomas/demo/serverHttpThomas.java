package lms.thomas.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.hibernate.Session;
import org.w3c.dom.Document;

import Plan.Process.Task.Operations.Operations;
import eu.robopartner.ps.planner.planninginputmodel.PLANNINGINPUT;
import gr.upatras.lms.util.Convert;
import hibernate.HibernateUtil;
import hibernate.Processdb;
import hibernate.Resourcesdb;
import lms.robopartner.task_planner.LayoutPlanningInputGenerator;
import planning.model.AssignmentDataModel;
import planning.model.io.AbstractInputSource;
import planning.model.io.FileInputSource;
import planning.model.io.HttpInputSource;
import planning.scheduler.algorithms.impact.IMPACT;
import planning.scheduler.algorithms.impact.criteria.AbstractCriterion;
import lms.thomas.Constants;
import lms.thomas.planning.*;
import lms.thomas.planning.criteria.Utilization;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import lms.robopartner.datamodel.map.controller.MapToResourcesAndTasks;
import lms.thomas.util.initializeFromDb;
import lms.thomas.util.xml.createThomasProgram;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import lms.thomas.server.handlers.*;

public class serverHttpThomas {

	static initializeFromDb d1 = new initializeFromDb();

	 static planner thomasPlanner= new planner();

	public static void main(String[] args) throws Exception {

		HttpServer server = HttpServer.create(new InetSocketAddress(Constants.HTTP_SERVER_PORT), 0);

		server.createContext("/test2", new MyHandler());
		server.setExecutor(null);
		server.createContext("/test3", new MyHandler2());
		server.setExecutor(null);
		server.createContext("/test4", new MyHandler3());
		server.setExecutor(null); //

		server.createContext(Constants.HTTP_REQUEST_GET_PROCESS_PATH, new processHandler());
		server.setExecutor(null);

		server.start();
	}

	static class MyHandler implements HttpHandler {

		public Map<String, String> queryToMap(String query) {
			Map<String, String> result = new HashMap<>();
			for (String param : query.split("&")) {
				String[] entry = param.split("=");
				if (entry.length > 1) {
					result.put(entry[0], entry[1]);
				} else {
					result.put(entry[0], "");
				}
			}
			return result;
		}

		@Override
		public void handle(HttpExchange t) throws IOException {

			String response = "{\"name\":\"Chuck\"}";
			Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
			System.out.println("param A=" + params.get("Process"));

			// layoutPlanningInput = d1.loadPlanningInput();

			thomasPlanner.getCompResource(params.get("Process"));

			JSONObject obj = new JSONObject();
			JSONObject obj1 = new JSONObject();

			// obj.put("Name", "crunchify.com");
			obj.put("id", "AppShah");

			obj.put("name", "w");
			// obj.put( "data",obj);
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

			// d1.generateTemplates();
			thomasPlanner.generateTemplates();

			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

	}

	static class MyHandler3 implements HttpHandler {

		public void handle(HttpExchange t) throws IOException {

			thomasPlanner.executePlanner();
			thomasPlanner.generateThomasProgram();

			String response = "{\"name\":\"Chuck \"}";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();

			return;

		}

	}

}
