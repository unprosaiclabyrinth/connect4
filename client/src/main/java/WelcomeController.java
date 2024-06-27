import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;

public class WelcomeController implements Initializable {
	
	@FXML
	public Button playButton;
	
	@FXML
	private TextField lobby, waiting;
	
	@FXML
	public Circle dot1, dot2, dot3;
	
	GameButton[] board;
	
    PauseTransition pt1 = new PauseTransition(Duration.seconds(0.5));
    PauseTransition pt2 = new PauseTransition(Duration.seconds(0.5));
    PauseTransition pt3 = new PauseTransition(Duration.seconds(0.5));

    // Playing the animation while waiting for a second player
	public void startAnimation() {
		updateWaiting("Waiting for another player...");
		
		double r = 10;
		Color c1 = Color.RED;
		Color c2 = Color.YELLOW;
		
		 dot1.setRadius(r);
		 dot1.setFill(c1);
		 dot2.setRadius(r);
		 dot2.setFill(c1);
		 dot3.setRadius(r);
		 dot3.setFill(c1);
	     
	     pt1.setOnFinished(e->{
	    	 dot3.setFill(c1);
	    	 dot1.setFill(c2);
	    	 pt2.play();
	     });
	     pt2.setOnFinished(e->{
	    	 dot1.setFill(c1);
	    	 dot2.setFill(c2);
	    	 pt3.play();
	     });
	     pt3.setOnFinished(e->{
	    	 dot2.setFill(c1);
	    	 dot3.setFill(c2);
	    	 pt1.play();
	 
	     });
		 
	     pt1.play();
	}
	
	// Stopping the animation
	public void stopAnimation() {
		updateWaiting("");
		
		pt1.stop();
		pt2.stop();
		pt3.stop();
		
		Color c0 = Color.BLACK;
		dot1.setFill(c0);
		dot2.setFill(c0);
		dot3.setFill(c0);
	}
	
	// Updating the waiting text-field with the message
	public void updateWaiting(String message) {
		waiting.setText(message);
	}
	
	// Updating the lobby text-field with the message
	public void updateLobby(String message) {
		lobby.setText(message);
	}
	
	// Highlighting the lobby text-field (by changing the color) to emphasize the message
	public void highlightLobby() {
		lobby.setStyle("-fx-text-fill: Chartreuse");
	}
	
	// Changing the color of the lobby text-field back to default
	public void lowlightLobby() {
		lobby.setStyle("-fx-text-fill: LightSalmon");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}