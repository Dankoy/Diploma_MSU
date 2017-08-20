package diploma;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class DBSCANClusterer {
	
	private double epsilon = 1f;
	private int minimumNumbersOfClusterMembers = 2;
	
	private ArrayList<Coordinate> inputValues = null;
	
	private HashSet<Coordinate> visitedPoints = new HashSet<>();
	
	public static final double R = 6372.8; // Earth Radius In kilometers
	
	
	// Create object with set values of maximum Distance and minimum elements in cluster. This object will be performed on clustering method
	public DBSCANClusterer(final Collection<Coordinate> inputValues, int minNumElem, int maxDistance) {
		setInputValues(inputValues);
		setMinimalNumbersOfMembersInCluster(minNumElem);
		setMaxDistance(maxDistance);
	}
	
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
	
	// Method to take coordinates to new ArrayList
	public void setInputValues(final Collection<Coordinate> collection) {
		this.inputValues = new ArrayList<Coordinate>(collection);
	}
	
	// Sets the minimal number of points in cluster.
	public void setMinimalNumbersOfMembersInCluster(final int num) {
		this.minimumNumbersOfClusterMembers = num;
	}
	
	// Sets the maximum distance between points 
	public void setMaxDistance(final double maxDistance) {
		this.epsilon = maxDistance;
	}
	
	// Method is looking for points which are situated close to each other (less than maxDistance value)
	// and if it's true then add them in neighbour array
	private ArrayList<Coordinate> getNeighbours(final Coordinate inputValue) {
		ArrayList<Coordinate> neighbours = new ArrayList<Coordinate>();
		for(int i=0; i<inputValues.size(); i++) {
			Coordinate candidate = inputValues.get(i);
			
			if(calculateDistance(inputValue, candidate) <= epsilon) {
				neighbours.add(candidate);
			}
		}
		return neighbours;
	}
	
}
