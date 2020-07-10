package io.vertx.starter;

import java.util.Comparator;

public class PointDistanceComparator implements Comparator<GeoPoint> {

	private final GeoPoint pointRefrence;
	public  PointDistanceComparator(GeoPoint pointdereference) {
		this.pointRefrence=pointdereference;
	}

	@Override
	public int compare(GeoPoint o1, GeoPoint o2) {
		double d1=Haversine.haversine(o1.latitude,o1.longitude, pointRefrence.latitude,pointRefrence.longitude);
		double d2=Haversine.haversine(o2.latitude,o2.longitude, pointRefrence.latitude,pointRefrence.longitude);
		return Double.compare(d1, d2);
		
	} 
	

	
	
}
