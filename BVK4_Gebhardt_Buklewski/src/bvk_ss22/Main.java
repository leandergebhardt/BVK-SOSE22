// BVK Ue1 SS2022 Vorgabe
//
// Copyright (C) 2022 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bvk_ss22;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = (BorderPane)loadFXML("AriAppView.fxml");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Arithmetic Encoding - SS2022 - <Leander Gebhardt & Karl Buklewski>");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private Object loadFXML(String resourceName) throws Exception {
		String path = System.getProperty("user.home") + File.separator + "src" + File.separator + "Java" + File.separator + "PixelatedImageView.jar";
		try {
			URL url = new File(path).toURI().toURL();
			URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
			Method addMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			addMethod.setAccessible(true);
			addMethod.invoke(classLoader, url);
			Method method = classLoader.loadClass("de.htw.lcs.fx.FXMLLoaderEx").getMethod("load", InputStream.class);
			Object object = method.invoke(null, getClass().getResourceAsStream(resourceName));
			return object;
		} catch (Exception e) {
			//System.out.println("Cannot load methodes from " + path);
    		return FXMLLoader.load(getClass().getResource(resourceName));
	    }		
	}
}
