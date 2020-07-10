package io.vertx.starter;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


public class GeoPoint {

	public final String name;
	public final double latitude;
	public final double longitude;
	public final double altitude;

	public GeoPoint(String name, double latitude, double longitude, double altitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}
	public String toString() {

		return "GeoPoint{"+"name "+name+" latitude "+latitude+" longitude "+longitude+" altitude "+altitude+"}";

	}
	public static GeoPoint fromGeoNamesLine(String line) {
		String[] parts = line.split("\t");
		double altitude= (parts[15].length()>0)?Double.parseDouble(parts[15]):Double.NaN; 
		return new GeoPoint(parts[1],Double.parseDouble(parts[4]),
				Double.parseDouble(parts[5]), altitude);
	}

	public static List<GeoPoint> loadGeoNamesPoints(String filepath)throws IOException{

		List<GeoPoint> l= new ArrayList<>();
		try ( BufferedReader bf = Files.newBufferedReader(Paths.get(filepath))){; 

		for(String line=bf.readLine(); line!=null; line =bf.readLine()) {
			line=line.trim();
			if(line.length()>0) {
				GeoPoint points = fromGeoNamesLine(line);					
				l.add(points); 

			}
		}
		
		}

		return l;

	}
	
	
	public static List<GeoPoint> getNearestPoints(double lat, double lon,List<GeoPoint> ListPoint) {
		List<GeoPoint> clonedPoints = new ArrayList<>(ListPoint);
		clonedPoints.sort(new  PointDistanceComparator(new GeoPoint("",lat,lon,Float.NaN))); // sort with the comparator
		return new ArrayList<GeoPoint>(clonedPoints.subList(0, 10));
	};
	
	
	public JsonObject toJSON() {

        JsonObject jo = new JsonObject();
        jo.put("name", name);
        jo.put("latitude", latitude);
        jo.put("longitude", longitude);
        jo.put("altitude", altitude);

        return jo;
    }
	
	
	
	public static JsonObject conevtToJsonArray(List<GeoPoint> jsArray) {
		JsonObject jo = new JsonObject();
		JsonObject mainObj = new JsonObject();
		JsonArray ja = new JsonArray();
	
		for(GeoPoint j:jsArray) {
		 
			ja.add(new JsonObject().put("fields",j.toJSON()));
			
		  }
		
		mainObj.put("resultat",ja);

		return mainObj;
} 






}
