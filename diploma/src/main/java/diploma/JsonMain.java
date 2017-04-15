package diploma;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;

import diploma2.ICoordinatesListener;

public class JsonMain {

	private static final class Coordinate {

	    private final double latitude;
	    private final double longitude;

	    private Coordinate(final double latitude, final double longitude) {
	        this.latitude = latitude;
	        this.longitude = longitude;
	    }

	}
	
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
	            
	        // Check if the object contains the exactly coordinates for crash with pedestrian
	        if (name.contains("Наезд на пешехода")) {
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
	
}
