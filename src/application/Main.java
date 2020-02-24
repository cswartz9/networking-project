package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import org.opencv.core.Core;


public class Main extends Application {
	@Override
	public void start(Stage mainWindow) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("vidStreamFX.fxml"));
		BorderPane root = (BorderPane) loader.load();
		FXController controller = loader.getController();

		Scene videoScene = new Scene(root, 960, 540);
		mainWindow.setTitle("OpenCV + JavaFX Video");
		mainWindow.setScene(videoScene);
		mainWindow.show();
	}


	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}
}
