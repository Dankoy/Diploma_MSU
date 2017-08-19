package diploma;

import java.util.ArrayList;
import java.util.HashSet;

public class DBSCANClusterer {
	
	private double epsilon = 1f;
	private int minimumNumbersOfClusterMembers = 2;
	
	private ArrayList<Coordinate> inputValues = null;
	
	private HashSet<Coordinate> visitedPoints = new HashSet<>();
	
	public static final double R = 6372.8; // Earth Radius In kilometers
	
	// Haversine Distance Formula
	// Take two points(Coordinates) to calculate distance between them
	public double calculateDistance(Coordinate val1, Coordinate val2) {
		
		// Take lattitude and longitude of each point
		double lat1 = val1.getLatitude();
		double lat2 = val2.getLatitude();
		double lon1 = val1.getLongitude();
		double lon2 = val2.getLongitude();
		
		// Find the delta between points
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		
		// Actual Haversine calculation
		double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
	}
	
}
