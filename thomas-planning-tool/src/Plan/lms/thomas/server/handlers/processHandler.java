package lms.thomas.server.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gr.upatras.lms.util.Convert;
import hibernate.HibernateUtil;
import hibernate.Processdb;
import hibernate.Resourcesdb;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class processHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange t) throws IOException {

		Session sessionH = HibernateUtil.getSessionFactory().openSession();
		sessionH.beginTransaction();

		// second load() method example

		List<Resourcesdb> res = (List<Resourcesdb>) sessionH.createCriteria(Processdb.class).list();

		JSONArray processArray = new JSONArray();

		for (Iterator iter = res.iterator(); iter.hasNext();) {
			Processdb processdb = (Processdb) iter.next();

			JSONObject newProcess = new JSONObject();
			newProcess.put("id", Convert.getString(processdb.getId()));
			newProcess.put("name", processdb.getName());

			processArray.add(newProcess);
		}

		JSONObject mainObj = new JSONObject();
		mainObj.put("Process", processArray);

		t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
		t.getResponseHeaders().set("Content-Type", "application/json, charset=UTF-8");
		t.sendResponseHeaders(200, processArray.toString().length());

		OutputStream os = t.getResponseBody();
		os.write(processArray.toString().getBytes());
		os.close();
	}
}