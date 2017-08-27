package diploma;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;

import diploma.Cluster;
import diploma.DBSCANClusterer;


public class JsonMain {

	 static List<Coordinate> coordinates = new ArrayList<>();
	
	private static final String ITEMS_NAME = "items";
	private static final String LATITUDE_PROPERTY = "latitude";
	private static final String LONGITUDE_PROPERTY = "longitude";
	private static final String CRASH_NAME = "em_type_name";
	
	static void parseCrashCoordinates(final JsonReader jsonReader, final ICoordinatesListener listener)
	        throws IOException {	
	    // Read { like the object begins.
		// If we don't read it or rread it wrong then exception will be thrown - that is the point of pull-method
	    jsonReader.beginObject();
	    
	    // Looking for the object's next name of property and compare it with expected one
	    final String itemsName = jsonReader.nextName();
	    
	    if ( !itemsName.equals(ITEMS_NAME) ) {
	        // Not 'items'? No idea how to work with it, then better throw an exception
	        throw new MalformedJsonException(ITEMS_NAME + " expected but was " + itemsName);
	    }
	    
	    // Looking for [
	    jsonReader.beginArray();
	    
	    // And read every element of array
	    while ( jsonReader.hasNext() ) {
	    	
	        // Judging by scheme every element of array is object
	        jsonReader.beginObject();
	        double latitude = 0;
	        double longitude = 0;
	        String name = null;
	        
	        // Run through all properties of object
	        while ( jsonReader.hasNext() ) {
	        	// Now just look if they are what we know
	            final String property = jsonReader.nextName();
	           	           
	            	switch ( property ) {	            	
		            // latitude? Save it.
		            case LATITUDE_PROPERTY:
		                latitude = jsonReader.nextDouble();
		                break;
		            // longitude? Save it.
		            case LONGITUDE_PROPERTY:
		                longitude = jsonReader.nextDouble();
		                break;
		            // Save the type of crash    
		            case CRASH_NAME:
		            	name = jsonReader.nextString();
		            	break;
		            // Otherwise skip any values of property
		            default:
		                jsonReader.skipValue();
		                break;
		            }
	            }
	            
	        // Check if the object contains the exactly coordinates for crash with pedestrian and bicycle drivers
	        if (name.contains("Наезд на пешехода") || name.contains("Наезд на велосипедиста")) {
	        	// Just delegate our coordinates in handler
		        listener.onCoordinates(latitude, longitude);
	        }
	        
	        // And say, that we are done with current object 
	        jsonReader.endObject();
	    }
	    // Also close last ] и }
	    jsonReader.endArray();
	    jsonReader.endObject();
	}
	
	private static void readAndParse(final ICoordinatesListener listener)
	        throws IOException {
	    try ( final JsonReader jsonReader = new JsonReader(new BufferedReader(
	    		new InputStreamReader(
	    				new FileInputStream("json/2016-crash.json")))) ) {
	        parseCrashCoordinates(jsonReader, listener);
	    }
	}
	
	// Output testing in console
	private static void testOutput()
	        throws IOException {
	    readAndParse((lat, lng) -> System.out.println("(" + lat + "; " + lng + ")"));
	}

     // Collecting all coordinates in ArrayList.
	// Will JVM support increase? Perhaps, but not fact.
	private static void testCollecting()
	        throws IOException {
	  //  List<Coordinate> coordinates = new ArrayList<>();
	    readAndParse((lat, lng) -> coordinates.add(new Coordinate(lat, lng)));
	    System.out.println(coordinates.size());   
	}
	
	public static void main(String[] args) throws IOException {
		//testOutput();
	    testCollecting();  
	    
	    System.out.print(coordinates.isEmpty());
	    
	 // Initialize our clustering class with locations, minimum points in cluster and max Distance
	    DBSCANClusterer clusterer = new DBSCANClusterer(coordinates, 2, 2);
	    
	    ArrayList<ArrayList<Coordinate>> cluster_raw = clusterer.performClustering();
	//    testClusterOutput(cluster_raw);
	    
	    ArrayList<Cluster> clusters = new ArrayList<>();
	    for(int i=0; i < cluster_raw.size(); i++) {
	    	Cluster c = new Cluster(cluster_raw.get(i));
	    	clusters.add(c);
	    }
	    
	    
	  //Start building the HTML for display in browser
	    	    String html = "<!DOCTYPE html>\n" + 
	            		"<html>\n" + 
	            		"  <head>\n" + 
	            		"    <title>Simple Map</title>\n" + 
	            		"    <meta name=\"viewport\" content=\"initial-scale=1.0\">\n" + 
	            		"    <meta charset=\"utf-8\">\n" + 
	            		"    <style>\n" + 
	            		"      /* Always set the map height explicitly to define the size of the div\n" + 
	            		"       * element that contains the map. */\n" + 
	            		"      #map {\n" + 
	            		"        height: 100%;\n" + 
	            		"      }\n" + 
	            		"      /* Optional: Makes the sample page fill the window. */\n" + 
	            		"      html, body {\n" + 
	            		"        height: 100%;\n" + 
	            		"        margin: 0;\n" + 
	            		"        padding: 0;\n" + 
	            		"      }\n" + 
	            		"    </style>\n" + 
	            		"	<script src='https://cdn.rawgit.com/googlemaps/js-marker-clusterer/gh-pages/src/markerclusterer.js'></script>"+
	            		"  </head>\n" + 
	            		"  <body>\n" + 
	            		"    <div id=\"map\"></div>\n" + 
	            		"    <script>\n" + 
	            		"      var map;\n" + 
	            		"      function initMap() {\n" + 
	            		"        map = new google.maps.Map(document.getElementById('map'), {\n" + 
	            		"          zoom: 3,\n" +
	            		"			center: new google.maps.LatLng(70.503758, 88.513333)" +
	            		"        });\n" +
	            		"		var markers=[];var bounds = new google.maps.LatLngBounds(); ";
	    	    
	    	    // Iterate through the clusters and generate javascript code for adding markers with numbers
	            	for(int i=0;i<clusters.size();i++) {
	            		html += clusters.get(i).getMarkerString() + "\n" ;
	            	}
	            
	            html += "      };"+ 
	            		"    </script>\n" + 
	            		"    <script src=\"https://maps.googleapis.com/maps/api/js?callback=initMap\"\n" + 
	            		"    async defer></script>\n" + 
	            		"  </body>\n" + 
	            		"</html>";
	    	    
	            
	            // Instantiate our class for opening a browser and rendering the map
	            MapRenderer mr = new MapRenderer();
	    	    mr.setHtml(html);
	    	    mr.showMap();
	    
	}
	
	// Test console output of clusterer_raw
	private static void testClusterOutput(ArrayList<ArrayList<Coordinate>> cl) throws IOException {
		for (int i = 0; i < cl.size(); i++) {
			System.out.println(cl.get(i));
		}
	}
	
}
