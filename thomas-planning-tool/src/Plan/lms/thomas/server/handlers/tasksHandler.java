package lms.thomas.server.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gr.upatras.lms.util.Convert;
import hibernate.HibernateUtil;
import hibernate.Processdb;
import hibernate.Resourcesdb;
import hibernate.Tasksdb;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class tasksHandler implements HttpHandler {
	
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

		Session sessionH = HibernateUtil.getSessionFactory().openSession();
		sessionH.beginTransaction();

		// second load() method example

		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		System.out.println("param A=" + params.get("Process"));
		
		int id= Convert.getInteger( params.get("Process"));
		
		Processdb processData = (Processdb) sessionH.load("hibernate.Processdb", id);


		JSONArray processArray = new JSONArray();

		for (Iterator iter = processData.getTasksdbs().iterator(); iter.hasNext();) {
			Tasksdb taskdb = (Tasksdb) iter.next();

			JSONObject newEntry = new JSONObject();
			newEntry.put("id", Convert.getString(taskdb.getId()));
			newEntry.put("name", taskdb.getName());

			processArray.add(newEntry);
		}

		JSONObject mainObj = new JSONObject();
		mainObj.put("Task", processArray);

		t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
		t.getResponseHeaders().set("Content-Type", "application/json, charset=UTF-8");
		t.sendResponseHeaders(200, processArray.toString().length());

		OutputStream os = t.getResponseBody();
		os.write(processArray.toString().getBytes());
		os.close();
	}
}