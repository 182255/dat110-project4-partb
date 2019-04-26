package no.hvl.dat110.ac.rest;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;
import static spark.Spark.path;

import com.google.gson.Gson;

import spark.Request;

/**
 * Hello world!
 *
 */
public class App {

	static AccessLog accesslog = null;
	static AccessCode accesscode = null;

	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service

		accesslog = new AccessLog();
		accesscode = new AccessCode();

//		after((req, res) -> {
//			res.type("application/json");
//		});

		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {

			Gson gson = new Gson();

			return gson.toJson("IoT Access Control Device");
		});

		// TODO: implement the routes required for the access control service
//		after((req, res) -> {
//			res.type("application/json");
//		});

		// PATH
		path("/accessdevice", () -> {			
			post("/log/", (req, res) -> {
				String message = req.body();
				Gson gson = new Gson();
				accesslog.add(message);
				
				return req.body();
				
			});
			
			get("/log/", (req, res) -> {
				res.type("application/json");
				Gson gson = new Gson();
				
//				for(int i )

				return gson.toJsonTree(accesslog.log);
			});
						
			get("/log/{id}", (req, res) -> {
				res.type("application/json");
				int id = -1;
				Gson gson = new Gson();
				try {
					id = Integer.parseInt(req.params("{id}"));
					return gson.toJsonTree(accesslog.get(id));
				} catch(Exception e) {
					return gson.toJsonTree("FAIL");
					
				} 
				
				
				
			});

			get("/code", (req, res) -> {
				Gson gson = new Gson();
				String a = String.valueOf(accesscode.getAccesscode()[0]);
				String b = String.valueOf(accesscode.getAccesscode()[1]);

				// TODO: fix this shit
				String json = "accesscode : ["  + a + ", " + b + "]";

				return gson.toJson(json);
			});

			delete("/log/", (req, res) -> {
				accesslog.clear();
				Gson gson = new Gson();
				return gson.toJson(accesslog.toJson());
			});

			put("/code", (req, res) -> {
				Gson gson = new Gson();
				
				AccessCode ac = gson.fromJson(req.body(), AccessCode.class);
				accesscode.setAccesscode(ac.getAccesscode());
				
				return req.body();
			
			});

		}); // end path  

	}
}
