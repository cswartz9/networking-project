package application;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FXController {
	@FXML
	private Button startButton;
	@FXML
	private ImageView frameView;

	private ScheduledExecutorService timer;
	private VideoCapture capture;

	private boolean cameraOn;


	public FXController() {
		cameraOn = false;
	}

	@FXML
	void startCamera(ActionEvent event) {
		if (cameraOn) {
			cameraOn = false;
			startButton.setText("Start Camera");
			timer.shutdown();
			this.capture.release();
		}
		else {
			cameraOn = true;
			capture = new VideoCapture(0);
			Runnable frameGrabber = new Runnable() {
				public void run() {
					Mat frame = new Mat();
					capture.read(frame);

					MatOfByte frameByteMat = new MatOfByte();
					Imgcodecs.imencode(".bmp", frame, frameByteMat);
					Image frameImage = new Image(new ByteArrayInputStream(frameByteMat.toArray()));

					Platform.runLater(() -> {
						frameView.imageProperty().set(frameImage);
					});
				}
			};

			timer = Executors.newSingleThreadScheduledExecutor();
			timer.scheduleAtFixedRate(frameGrabber, 0, 20, TimeUnit.MILLISECONDS);

			startButton.setText("Stop Camera");

		}
	}
}
