import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;

/* Server main */

public class Main extends Application {
	
	Server server;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Game Server");
			
			FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("FXML/welcomeView.fxml"));
			Parent welcomeRoot = welcomeLoader.load();
			WelcomeController welcomeCtrl = welcomeLoader.getController();
			Scene welcome = new Scene(welcomeRoot, 400, 350);
			welcome.getStylesheets().add("CSS/welcome.css");
			
			FXMLLoader serverLoader = new FXMLLoader(getClass().getResource("FXML/ServerView.fxml"));
			Parent serverRoot = serverLoader.load();
			ServerController serverCtrl = serverLoader.getController();
			Scene serverScene = new Scene(serverRoot, 700, 700);
			serverScene.getStylesheets().add("CSS/server.css");
			
			Button on = welcomeCtrl.on;
			
			on.setOnAction(e->{
				server = new Server(data->{
					Platform.runLater(()->{
						CFourInfo cfi = (CFourInfo)data;
						if (cfi.playerCount == 0) {
							serverCtrl.updateGameState("Whoa! All players have left. Waiting for 2 players...");
							serverCtrl.updateNoOfPlayers(0);
						} else if (cfi.playerCount == 1) {
							if (!serverCtrl.getGameState().equals("1 player wants to play again. Waiting for 1 player...")) {
								serverCtrl.updateGameState("1 player is connected. Waiting for 1 player...");
							}
							serverCtrl.updateNoOfPlayers(1);
							serverCtrl.resetBoard(server.board);
						} else if (cfi.playerCount == 2) {
							serverCtrl.updateGameState("2 players are connected. Game in progress...");
							serverCtrl.updateNoOfPlayers(2);
							if (cfi.result == 1) {
								serverCtrl.updateGameState("Player 1 (Red) has won the game!");
							} else if (cfi.result == 2) {
								serverCtrl.updateGameState("Player 2 (Yellow) has won the game!");
							} else if (cfi.result == 0) {
								serverCtrl.updateGameState("It is a tie!");
							} else if (cfi.move != -1) {
								int cfiMove = server.cfl.legalizedMove(cfi.move);
								server.board[cfiMove].pressWithValue(cfi.turn);
							}
						} else if (cfi.playerCount == 3) {
							serverCtrl.updateGameState("1 player wants to play again. Waiting for 1 player...");
							serverCtrl.resetBoard(server.board);
						} else if (cfi.playerCount > 69) {
							int playerNo = cfi.playerCount - 69;
							server.board[cfi.turn].highlight();
							server.board[cfi.prevMove].highlight();
							server.board[cfi.move].highlight();
							server.board[cfi.result].highlight();
							PauseTransition pt = new PauseTransition(Duration.seconds(2));
							pt.setOnFinished(el->{
								server.board[cfi.turn].pressWithValue(playerNo);
								server.board[cfi.prevMove].pressWithValue(playerNo);
								server.board[cfi.move].pressWithValue(playerNo);
								server.board[cfi.result].pressWithValue(playerNo);
							});
							pt.play();
						}

					});
				});
				serverCtrl.boardInit(server.board);
				primaryStage.setScene(serverScene);
			});
			
			primaryStage.setScene(welcome);
			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}