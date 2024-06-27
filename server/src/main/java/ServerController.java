import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ServerController implements Initializable {
	
	@FXML
	private TextField noOfPlayers, gameState;
	
	@FXML
	private GridPane gameBoard;
	
	// Updating gameState text-field with the message
	public void updateGameState(String message) {
		gameState.setText(message);
	}
	
	// Updating noOfPlayers text-field with no. of players
	public void updateNoOfPlayers(int count) {
		noOfPlayers.setText("Number of players currently connected: " + count);
	}
	
	// Initializing the gameBoard
	public void boardInit(GameButton[] board) {
		for (int r = 0; r < 6; ++r) {
			for (int c = 0; c < 7; ++c) {
				gameBoard.add(board[(7 * r) + c], c, r);
			}
		}
	}
	
	// Resetting the gameBoard to default
	public void resetBoard(GameButton[] board) {
		for (int i = 0; i < 42; ++i) {
			board[i].unpress();
		}
	}
	
	// Returning the text from the gameState text-field
	public String getGameState() {
		return gameState.getText();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Auto-generated method stub
		
	}
}