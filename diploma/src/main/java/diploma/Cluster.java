package diploma;

import java.util.ArrayList;

public class Cluster {
	
	private int numOfPoints;
	private Coordinate center;
	
	public Cluster( ArrayList<Coordinate> cluster) {
		this.numOfPoints = cluster.size();
		this.center = getCenterOfCluster(cluster);
	}
	
	// Get the cluster position
	private Coordinate getCenterOfCluster(ArrayList<Coordinate> cluster) {
		Coordinate firstPoint = cluster.get(0);
	    double minLat = firstPoint.getLatitude(), maxLat = firstPoint.getLatitude(), minLong = firstPoint.getLongitude(), maxLong = firstPoint.getLongitude();
	    for(int i=1; i<cluster.size(); i++) {
	    		Coordinate c = cluster.get(i);
	    		
	    		if( c.getLatitude() < minLat ) {
	    			minLat = c.getLatitude();
	    		}
	    		if( c.getLatitude() > maxLat ) {
	    			maxLat = c.getLatitude();
	    		}
	    		if( c.getLongitude() < minLong ) {
	    			minLong = c.getLongitude();
	    		}
	    		if( c.getLongitude() > maxLong ) {
	    			maxLong = c.getLongitude();
	    		}
	    }
	    
	    double centreLat = (minLat + maxLat) / 2;
	    double centreLong = (minLong + maxLong) / 2;
	    
	    return new Coordinate(centreLat, centreLong);

	}
	
}
