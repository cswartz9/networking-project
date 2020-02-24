package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("vidStreamFX.fxml"));
		BorderPane root = (BorderPane) loader.load();
		FXController controller = loader.getController();
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}
