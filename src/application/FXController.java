package application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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
	void startCamera(ActionEvent event) throws SocketException {
		if (cameraOn) { // user wants to stop recording
			cameraOn = false;
			startButton.setText("Start Camera");
			timer.shutdown();
			this.capture.release();
		}
		else { // user wants to start recording
			cameraOn = true;
			capture = new VideoCapture(0);

			// this executes on a thread to grab frames, display them, send them
			Runnable frameGrabber;
				frameGrabber = new Runnable() {
					DatagramSocket socket = new DatagramSocket(1234);
					public void run() { // default method when runnable executes
						Mat frame = new Mat(); // matrix that holds image information
						capture.read(frame); // read in from framegrabber to matrix

						MatOfByte frameByteMat = new MatOfByte(); // create specific byte mat for conversions
						Imgcodecs.imencode(".bmp", frame, frameByteMat); // encode from original matrix to byte matrix as bmp
						byte[] byteArray = frameByteMat.toArray(); // array of bytes
						Image frameImage = new Image(new ByteArrayInputStream(byteArray)); // convert byte array to Image

						Platform.runLater(() -> {
							/*try {
								sendFrame(byteArray);
							} catch (SocketException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
							frameView.imageProperty().set(frameImage); // update current image in frame
						});
					}
					public void sendFrame(byte[] byteArray) throws SocketException { // TODO
						System.out.println("Made it here!");
						DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length);
						try {
							socket.receive(packet);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("Made it here! 2");
						InetAddress addr = packet.getAddress();
						int port = packet.getPort();
						System.out.println("Made it here! 3");
						packet = new DatagramPacket(byteArray, byteArray.length, addr, port);
					}
				};

			timer = Executors.newSingleThreadScheduledExecutor(); // create new thread for image-capturing
			timer.scheduleAtFixedRate(frameGrabber, 0, 20, TimeUnit.MILLISECONDS); // run frameGrabber every 20ms

			startButton.setText("Stop Camera");

		}
	}
}
