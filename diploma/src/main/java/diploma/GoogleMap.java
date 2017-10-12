package diploma;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
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
import netscape.javascript.JSObject;

public class GoogleMap {
	
	public String hhh;
	
	public void setHtml(String html) {
		this.hhh = html;
	}

	private DBSCANClusterer clusterer ;

    private final WebView webView ;
	
	
	 public GoogleMap(DBSCANClusterer c) throws MalformedURLException {
	        this.clusterer = c;

//	        File file = new File("C:/Users/Evgeny/git/Diploma_MSU/diploma/html/map.html");
//	        URL url222 = file.toURI().toURL();
	        
	        File filewrite = new File("C:/Users/Evgeny/git/Diploma_MSU/diploma/html/writer.html");
	        URL urlwriter = filewrite.toURI().toURL();
	        
	        webView = new WebView();
	        final WebEngine webEngine = webView.getEngine();

	        JSObject jsobj = (JSObject) webView.getEngine()
	                 .executeScript("window");
	        jsobj.setMember("BrowserJavaObject", new BrowserJavaObject(clusterer));

//	        webEngine.load(url222.toString()); 
	        
	        webEngine.load(urlwriter.toString());
	        
//	        webEngine.loadContent(hhh, "text/html");

	    } 

	    public Node getView() {
	        return webView ;
	    }
	
	
/*
	@Override 
	public void start(Stage stage) throws MalformedURLException {
		
		File file = new File("C:/Users/Evgeny/git/Diploma_MSU/diploma/html/map.html");
		URL url222 = file.toURI().toURL();
		
//		 URL url = this.getClass().getResource(("/html/map.html"));
		 WebView webView = new WebView();
         final WebEngine webEngine = webView.getEngine();
//            webEngine.load(html);
   //      webEngine.load(getClass().getResource("/diploma/html/map.html").toExternalForm()); 
        
         JSObject jsobj = (JSObject) webView.getEngine()
                 .executeScript("window");
         jsobj.setMember("BrowserJavaObject", new BrowserJavaObject(clusterer));
         
        webEngine.load(url222.toString()); 
         
        // create scene
        stage.setTitle("Web Map");
        Scene scene = new Scene(webView,1000,700, Color.web("#666970"));
        stage.setScene(scene);
        // show stage
        stage.show();

    } 
 
*/
	

/*	
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
	         
	          final URL urlGoogleMaps = this.getClass().getResource("/html/map.html");
	          webEngine.load(urlGoogleMaps.toExternalForm());
	 
	          getChildren().add(webView);
	         
	      }
	     
	  }
	  
*/	  
 
 //   static { // use system proxy settings when standalone application
//        System.setProperty("java.net.useSystemProxies", "true");
//    } 

}
