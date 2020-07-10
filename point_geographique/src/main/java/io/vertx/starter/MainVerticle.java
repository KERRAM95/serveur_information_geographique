package io.vertx.starter;



import java.io.IOException;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {
	 List<GeoPoint> ListPoint=null;
	 @Override
	  public void start() {
		 
		//Charegemet des points 
		
		 try {
			 String path ="src/main/resources/Fr.txt"; 
			 ListPoint =GeoPoint.loadGeoNamesPoints(path); 
			 
		 } catch (IOException e) {
		  
		  e.printStackTrace(); 
		  
		 }
		 
		 HttpServer server = vertx.createHttpServer();
			Router router = Router.router(vertx);
			router.route("/nearestPoints/:latitude/:longitude").method(HttpMethod.GET).handler(req -> {
				String lat = req.pathParam("latitude");
				String lon = req.pathParam("longitude");
				double latD = convertToDouble(lat);
				double lonD = convertToDouble(lon);
				
			    req.response().putHeader("content-type","application/json").end(GeoPoint.conevtToJsonArray(GeoPoint.getNearestPoints(latD, lonD, ListPoint)).toString());

			});
			server.requestHandler(router).listen(8080);
		 
		 
	 }
		 

  
 public static double convertToDouble(String s) { 
	 try{ return Double.parseDouble(s); }
	      catch(NumberFormatException e) { 
			  
			  return 100000; 
			  }
	  
}
 
	 }
	 


