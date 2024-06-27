import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class BoardController implements Initializable {
	
	@FXML
	private GridPane gameBoard;
	
	@FXML
	private TextField title, turn, instruction;
	
	// Initializing the game board
	public void boardInit(GameButton[] board) {
		for (int r = 0; r < 6; ++r) {
			for (int c = 0; c < 7; ++c) {
				gameBoard.add(board[(7 * r) + c], c, r);
			}
		}
	}
	
	// Changing GUI according to whether it is the player's turn
	public void setTurn(boolean isTurn, int playerNo) {
		if (isTurn) {
			instruction.setText("Click the square to play your move");
			turn.setText("It is your turn:-");
			if (playerNo == 1) {
				instruction.setStyle("-fx-text-fill: Red");
				turn.setStyle("-fx-text-fill: Red");
			} else {
				instruction.setStyle("-fx-text-fill: Yellow");
				turn.setStyle("-fx-text-fill: Yellow");
			}
		} else {
			instruction.setText("Please wait");
			turn.setText("Your opponent is playing...");
			if (playerNo == 1) {
				instruction.setStyle("-fx-text-fill: Yellow");
				turn.setStyle("-fx-text-fill: Yellow");
			} else {
				instruction.setStyle("-fx-text-fill: Red");
				turn.setStyle("-fx-text-fill: Red");
			}
		}
	}
	
	// Updating the turn text field with the message
	public void updateTurn(String message) {
		turn.setStyle("-fx-text-fill:Chartreuse");
		turn.setText(message);
	}
	
	// Updating the instruction text field with the message
	public void updateInstruction(String message) {
		instruction.setStyle("-fx-text-fill:Chartreuse");
		instruction.setText(message);
	}
	
	// Adding player theme (red or yellow) to the GUI
	public void addPlayerTheme(int playerNo) {
		if (playerNo == 1) {
			title.setStyle("-fx-background-color: indianred");
			setTurn(true, 1);
		} else if (playerNo == 2) {
			title.setStyle("-fx-background-color: yellow");
			setTurn(false, 2);
		}
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
}