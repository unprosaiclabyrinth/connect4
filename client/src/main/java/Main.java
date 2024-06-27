import java.io.IOException;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

/* Client version */

public class Main extends Application {
	
	Player player;
	boolean changedToBoard = false;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Welcome Player");
			
			FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("FXML/WelcomeView.fxml"));
			Parent welcomeRoot = welcomeLoader.load();
			WelcomeController welcomeCtrl = welcomeLoader.getController();
			Scene welcome = new Scene(welcomeRoot, 500, 450);
			welcome.getStylesheets().add("CSS/welcome.css");
			
			FXMLLoader boardLoader = new FXMLLoader(getClass().getResource("FXML/BoardView.fxml"));
			Parent boardRoot = boardLoader.load();
			BoardController boardCtrl = boardLoader.getController();
			Scene board = new Scene(boardRoot, 700, 700);
			board.getStylesheets().add("CSS/board.css");
			
			FXMLLoader winLoader = new FXMLLoader(getClass().getResource("FXML/WinView.fxml"));
			Parent winRoot = winLoader.load();
			WinController winCtrl = winLoader.getController();
			Scene win = new Scene(winRoot, 700, 700);
			win.getStylesheets().add("CSS/result.css");
			
			FXMLLoader lossLoader = new FXMLLoader(getClass().getResource("FXML/LossView.fxml"));
			Parent lossRoot = lossLoader.load();
			LossController lossCtrl = lossLoader.getController();
			Scene loss = new Scene(lossRoot, 700, 700);
			loss.getStylesheets().add("CSS/result.css");
			
			FXMLLoader tieLoader = new FXMLLoader(getClass().getResource("FXML/TieView.fxml"));
			Parent tieRoot = tieLoader.load();
			TieController tieCtrl = tieLoader.getController();
			Scene tie = new Scene(tieRoot, 700, 700);
			tie.getStylesheets().add("CSS/result.css");
			
			welcomeCtrl.playButton.setOnAction(e->{
				player = new Player(data->{
					Platform.runLater(()->{
						try {
							CFourInfo cfi = (CFourInfo)data;
							if (cfi.playerCount == -1) {
								welcomeCtrl.stopAnimation();
								welcomeCtrl.playButton.setDisable(true);
								welcomeCtrl.updateLobby("Unable to read from server!!!");
								welcomeCtrl.lowlightLobby();
								welcomeCtrl.updateWaiting("Try restarting the application!");
								primaryStage.setScene(welcome);
								primaryStage.setTitle("Welcome Player");
							} else if (cfi.playerCount == 1) {
								welcomeCtrl.playButton.setDisable(true);
								PauseTransition pt = new PauseTransition(Duration.seconds(0.5));
								pt.setOnFinished(el->{
									welcomeCtrl.updateLobby("But wait! You are the only player in the game!");
									welcomeCtrl.lowlightLobby();
									welcomeCtrl.startAnimation();
								});
								pt.play();
								primaryStage.setScene(welcome);
								primaryStage.setTitle("Welcome Player");
								changedToBoard = false;
							} else if (cfi.playerCount == 2) {
								welcomeCtrl.playButton.setDisable(true);
								welcomeCtrl.stopAnimation();
								if (!changedToBoard) {
									prepareForTransition(welcomeCtrl, boardCtrl);
									PauseTransition pt = new PauseTransition(Duration.seconds(2));
									pt.setOnFinished(el->{
										primaryStage.setScene(board);
										primaryStage.setTitle("The Board");
										welcomeCtrl.updateLobby("");
									});
									pt.play();
									changedToBoard = true;
								}
								if (cfi.result == player.number) {
									makeMove(boardCtrl, cfi, true);
									player.board[cfi.move].highlight();
									boardCtrl.updateInstruction(":-D");
									PauseTransition pt1 = new PauseTransition(Duration.seconds(0.5));
									pt1.setOnFinished(el->{
										boardCtrl.updateTurn("Wow! You have won!");
									});
									pt1.play();
									PauseTransition pt = new PauseTransition(Duration.seconds(2));
									pt.setOnFinished(el->{
										primaryStage.setScene(win);
										primaryStage.setTitle("Victory");
									});
									pt.play();
								} else if (cfi.result == 0) {
									makeMove(boardCtrl, cfi, true);
									boardCtrl.updateInstruction(":-|");
									PauseTransition pt1 = new PauseTransition(Duration.seconds(0.5));
									pt1.setOnFinished(el->{
										boardCtrl.updateTurn("Well, it's a tie.");
									});
									pt1.play();
									PauseTransition pt = new PauseTransition(Duration.seconds(2));
									pt.setOnFinished(el->{
										primaryStage.setScene(tie);
										primaryStage.setTitle("Tie");
									});
									pt.play();
								} else if (cfi.result == -1) {
									makeMove(boardCtrl, cfi, false);
								} else {
									makeMove(boardCtrl, cfi, true);
									player.board[cfi.move].highlight();
									boardCtrl.updateInstruction(":-(");
									PauseTransition pt1 = new PauseTransition(Duration.seconds(0.5));
									pt1.setOnFinished(el->{
										boardCtrl.updateTurn("Oh no! You have lost!");
									});
									pt1.play();
									PauseTransition pt = new PauseTransition(Duration.seconds(2));
									pt.setOnFinished(el->{
										primaryStage.setScene(loss);
										primaryStage.setTitle("Defeat");
									});
									pt.play();
								}
							} else if (cfi.playerCount == 3) {
								welcomeCtrl.stopAnimation();
								welcomeCtrl.updateLobby("");
								prepareForTransition(welcomeCtrl, boardCtrl);
								primaryStage.setScene(welcome);
								primaryStage.setTitle("Welcome Player");
								PauseTransition pt = new PauseTransition(Duration.seconds(2));
								pt.setOnFinished(el->{
									primaryStage.setScene(board);
									primaryStage.setTitle("The Board");
									welcomeCtrl.updateLobby("");
								});
								pt.play();
							} else if (cfi.playerCount == 69) {
								// Highlighting winning squares on the board
								player.board[cfi.turn].highlight();
								player.board[cfi.prevMove].highlight();
								player.board[cfi.move].highlight();
								player.board[cfi.result].highlight();
							}
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}
					});
				});
				boardCtrl.boardInit(player.board);
				player.start();
			});
			
			winCtrl.newGame.setOnAction(e->{
				player.send(new CFourInfo(3,player.number,-1,-1,-1));
			});
			
			winCtrl.exit.setOnAction(e->{
				player.send(new CFourInfo(4,player.number,-1,-1,-1));
				primaryStage.close();
				System.exit(0);
			});
			
			lossCtrl.newGame.setOnAction(e->{
				player.send(new CFourInfo(3,player.number,-1,-1,-1));
			});
			
			lossCtrl.exit.setOnAction(e->{
				player.send(new CFourInfo(4,player.number,-1,-1,-1));
				primaryStage.close();
				System.exit(0);
			});
			
			tieCtrl.newGame.setOnAction(e->{
				player.send(new CFourInfo(3,player.number,-1,-1,-1));
			});
			
			tieCtrl.exit.setOnAction(e->{
				player.send(new CFourInfo(4,player.number,-1,-1,-1));
				primaryStage.close();
				System.exit(0);
			});
			
			primaryStage.setScene(welcome);
			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	// Making the move on the board
	private void makeMove(BoardController boardCtrl, CFourInfo cfi, boolean gameOver) {
		if (cfi.turn != player.number) {
			boardCtrl.setTurn(false, player.number);
			makeOnTurnMove(cfi);
		} else {
			boardCtrl.setTurn(true, player.number);
			makeOffTurnMove(gameOver, cfi);
		}
	}
	
	// Changing the GUI when it is the player's turn
	private void makeOnTurnMove(CFourInfo cfi) {
		if (cfi.prevMove != -1) {
			player.board[cfi.prevMove].unpress();
		}
		if (cfi.move != -1) {
			player.board[cfi.move].pressWithValue(player.number);
		}
		player.disableBoard();
	}
	
	// Changing the GUI when it is not the player's turn
	private void makeOffTurnMove(boolean gameOver, CFourInfo cfi) {
		int prevTurn = cfi.turn == 1 ? 2 : 1;
		if (cfi.move != -1) {
			player.board[cfi.move].pressWithValue(prevTurn);
		}
		if (gameOver) {
			player.disableBoard();
		} else {
			player.enableBoard();
		}
	}
	
	// Preparing GUI for welcome scene to board transition
	private void prepareForTransition(WelcomeController welcomeCtrl, BoardController boardCtrl) {
		if (player.number == 1) {
			welcomeCtrl.updateLobby("Another player has joined!");
			welcomeCtrl.highlightLobby();
			boardCtrl.addPlayerTheme(1);
			player.resetBoard(false);
		} else {
			welcomeCtrl.updateLobby("Taking you to the game board...");
			welcomeCtrl.highlightLobby();
			boardCtrl.addPlayerTheme(2);
			player.resetBoard(true);
		}
	}
}