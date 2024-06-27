import javafx.scene.control.Button;

/* Server version */

public class GameButton extends Button{
	
	// Changing text and color of button according to player no.
	public void pressWithValue(int playerNo) {
		if (playerNo == 1) {
			this.setText("1");
			this.setStyle("-fx-text-fill:Red; -fx-background-color:Red");
		} else {
			this.setText("2");
			this.setStyle("-fx-text-fill:Yellow; -fx-background-color:Yellow");
		}
		this.setDisable(true);
	}
	
	// Resetting button to default
	public void unpress() {
		this.setStyle("-fx-border-color: Blue");
		this.setText("");
	}
	
	// Highlighting game button (used to show winning squares)
	public void highlight() {
		this.setStyle("-fx-background-color: Chartreuse; -fx-text-fill: Chartreuse");
	}
}