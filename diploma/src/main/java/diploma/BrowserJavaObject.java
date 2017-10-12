package diploma;

import java.util.ArrayList;

public class BrowserJavaObject {
	
	private DBSCANClusterer clusterer ;
	
	public BrowserJavaObject(DBSCANClusterer c) {
		this.clusterer = c;
	}

	public String showMarkers() {
		ArrayList<ArrayList<Coordinate>> clusters_raw= this.clusterer.performClustering();
		ArrayList<Cluster> clusters = new ArrayList<>();
		
		String pointsArray = " [ ";
	    for(int i=0; i<clusters_raw.size(); i++) {
	    		Cluster c = new Cluster(clusters_raw.get(i));
	    		clusters.add(c);
	    		
	    		pointsArray += c.getLocationAsArray() + " , ";
	    }
	    pointsArray += "]";
	    
	    System.out.println("Number Of Clusters Created: "+clusters.size());
		return pointsArray;
		
	}
	
}
