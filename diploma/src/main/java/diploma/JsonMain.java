package diploma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;

import diploma.Cluster;
import diploma.DBSCANClusterer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class JsonMain extends Application {

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
	    	
	    	/* JSON META:
	         * 	em_type_name; Type of crash; Вид ДТП; string
	         *	em_moment_date; Crash date; Дата ДТП; string
			 *	em_moment_time; Crash time; Время ДТП; string
			 *	subject; Region of country; Субъект РФ; string
			 *	longitude; Longitude; Долгота; number
			 *	latitude;Latitude; Широта; number
			 *	okato_code;OKATO; Код места ДТП по ОКАТО (конечного значения по иерархии территориальных единиц); string
			 *	place_path;Place; Место возникновния ДТП; string
			 *	road_significance_name; Type of road(federal,regional,etc..); Тип дороги(региональная,федеральная...); string
			 *	road_name;Road name; Название дороги; string
			 *	road_loc;Kilometer; Километр; string
			 *	region_name;Area name; Название субъекта РФ; string
			 *	light_type_name;Level of lighting; Условия освещения; string
			 *	tr_area_state_name;Road condition; Состояние проезжей части; string
			 *	transp_amount;Quantity of cars; Количество траспорта участвующего в ДТП; string
			 *	suffer_amount;Quantity suffers; Количество пострадавших; string
			 *	loss_amount;Total loss people; Общее количество погибших; string
			 *	suffer_child_amount;Quantity suffer childs; Количество пострадавших детей; string
			 *	loss_child_amount;Quantity loss childs; Количество погибших детей; string
			 *	mt_rate_name;Change on road; Изменения в режиме движения; string
	         */
	    	
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
	        
	        /* That's what I need to refactor code:
	         *  	em_moment_time - show time of crash
	         *  	em_moment_date - show data of crash
	         *   	subject - show where it happens ( Moscow or wherever )
	         *   	transp_amount - amount of cars in crash
	         *   	suffer_amount - amount of people who got suffered there
	         *   	loss_amount - amount of killed people
	         *   	suffer_child_amount - amount of suffered children in crash
	         *   	loss_child_amount - amount of dead children
	         */        
	            
	        // Check if the object contains the exactly coordinates for crash with pedestrian and bicycle drivers
	        if (name.contains("Наезд на пешехода") || name.contains("Наезд на велосипедиста")) {
	        	// Allow accidents only for Moscow
	        	 if((latitude <= 56.0 & longitude >= 37.0)) {
	        		 if((latitude >= 55.0 & longitude <= 38.0) ) {
	        			// Just delegate our coordinates in handler
	     		        listener.onCoordinates(latitude, longitude);
	        		 }
	        	}	        	        	
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
	    				new FileInputStream("json/rus-crash.json")))) ) {
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
	
    public static void main(String[] args) {
        launch(args);
    }
	
	public void start(Stage stage) throws IOException, URISyntaxException {
		//testOutput();
	    testCollecting();  
	    
	    
	 // Initialize our clustering class with locations, minimum points in cluster and max Distance
	    DBSCANClusterer clusterer = new DBSCANClusterer(coordinates, 2, 2);

	    
	    
	    //Perform the clustering and save the returned clusters as a list
	    ArrayList<ArrayList<Coordinate>> clusters_raw= clusterer.performClustering();
	    
	    // Change the cluster list into array of object of our cluster class
	    // The clusters class is responsible for finding the center of the cluster and also the number of points inside the cluster
	    // It also exposes a method which returns javascript code for plotting the cluster as markers with numbers
	    ArrayList<Cluster> clusters = new ArrayList<>();
	    for(int i=0; i<clusters_raw.size(); i++) {
	    		Cluster c = new Cluster(clusters_raw.get(i));
	    		clusters.add(c);
	    }
	    System.out.println(clusters.size());
//	    testClusterOutput(clusters_raw);

	    
	    
	    //Start building the HTML for display in browser
	    String html = "<!DOCTYPE html>\n" + 
        		"<html>\n" + 
        		"  <head>\n" + 
        		"    <title>Simple Map</title>\n" + 
        		"    <meta name=\"viewport\" content=\"initial-scale=1.0\">\n" + 
        		"    <meta charset=\"utf-8\">\n" + 
        		"      <style type=\"text/css\">\n" +
        		"           html { height: 100% }\n" +
        		"           body { height: 100%; margin: 0; padding: 0 }\n" +
        		"           #map { height: 100% }\n" +
        		"    </style>\n" + 
//        		"	<script src='https://cdn.rawgit.com/googlemaps/js-marker-clusterer/gh-pages/src/markerclusterer.js'></script>"+
				"    <script async defer type=\"text/javascript\" src=\"https://maps.googleapis.com/maps/api/js?callback=initMap\"> </script>\n" +
        		"  </head>\n" + 
        		
        		"  <body onload=\"initMap()\" style=\"width:100%; height:100%\">\n" + 
        		"    <div id=\"map\"></div>\n" + 
        		
        		"    <script>\n" + 
        		"      var map;\n" + 
        		"      function initMap() {\n" + 
        		
        		"        var options = {\n" +
        		"            zoom: 9,\n" +
        		"            center: new google.maps.LatLng(55.7558, 37.6173),\n" +
        		"            mapTypeId: google.maps.MapTypeId.ROADMAP\n" +
        		"        }\n" +
        		
        		"        map = new google.maps.Map(document.getElementById(\"map\"), options);\n" + 
        		
        		"		var markers=[];var bounds = new google.maps.LatLngBounds();\n ";
//	    System.out.println(html);   
	    // Iterate through the clusters and generate javascript code for adding markers with numbers
        	for(int i=0;i<clusters.size();i++) {
        		html += clusters.get(i).getMarkerString() + "\n" ;
        	}
        
        html += "      }\n"+ 
        		"    </script>\n" + 
        		"  </body>\n" + 
        		"</html>";
		 
        FileOutputStream fos = new FileOutputStream("C:/Users/Evgeny/git/Diploma_MSU/diploma/html/writer.html");
 //       bw = new BufferedWriter(new FileWriter("C:/Users/Evgeny/git/Diploma_MSU/diploma/html/writer.html"));
        PrintStream ps = new PrintStream(fos);
        ps.println(html);
        
        // Instantiate our class for opening a browser and rendering the map
//        MapRenderer mr = new MapRenderer();
//	    mr.setHtml("map.html");
//	    mr.showMap();
	    
	    GoogleMap gm = new GoogleMap(clusterer);
	    gm.setHtml(html);
//	    System.out.println(gm.hhh);
//	    gm.launch(GoogleMap.class);
	    
	    stage.setTitle("Web Map");
        Scene scene = new Scene((Parent) gm.getView(), 1000, 700, Color.web("#666970"));
        stage.setScene(scene);
        // show stage
        stage.show();
	}
	
	// Test console output of clusterer_raw
	private static void testClusterOutput(ArrayList<ArrayList<Coordinate>> cl) throws IOException {
		for (int i = 0; i < cl.size(); i++) {
			System.out.println(cl.get(i));
		}
	}	
	
}
