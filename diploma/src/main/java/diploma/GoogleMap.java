package diploma;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class GoogleMap extends Application {

	String html;
	
	public GoogleMap() {
		
	}
	public void setHtml (String html) {
		this.html = html;
	}
	

/*	@Override 
	public void start(Stage stage) {
		 URL url = this.getClass().getResource(("/html/map.html"));
		 WebView webView = new WebView();
         final WebEngine webEngine = webView.getEngine();
         //   webEngine.load(html);
         webEngine.load(getClass().getResource("/diploma/html/map.html").toString()); 
        
        // create scene
        stage.setTitle("Web Map");
        Scene scene = new Scene(webView,1000,700, Color.web("#666970"));
        stage.setScene(scene);
        // show stage
        stage.show();

    } 
 */
	
	 private Scene scene;
	  MyBrowser myBrowser;
	
	  @Override
	  public void start(Stage primaryStage) {
	      primaryStage.setTitle("WebMap");
	     
	      myBrowser = new MyBrowser();
	      scene = new Scene(myBrowser, 640, 480);
	     
	      primaryStage.setScene(scene);
	      primaryStage.show();
	  }
	 
	  class MyBrowser extends Region{
	     
	      HBox toolbar;
	     
	      WebView webView = new WebView();
	      WebEngine webEngine = webView.getEngine();
	     
	      public MyBrowser(){
	         
	          final URL urlGoogleMaps = getClass().getResource("/html/map.html");
	          webEngine.load(urlGoogleMaps.toExternalForm());
	 
	          getChildren().add(webView);
	         
	      }
	     
	  }
 
 //   static { // use system proxy settings when standalone application
//        System.setProperty("java.net.useSystemProxies", "true");
//    } 

}
