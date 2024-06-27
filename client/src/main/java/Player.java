import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Player extends Thread {
	
	Socket playerSocket;
	ObjectInputStream in;
	ObjectOutputStream out;
	private Consumer<Serializable> callback;
	int number = 0;
	public GameButton[] board;
	
	public Player(Consumer<Serializable> call) {
		callback = call;
		board = new GameButton[42];
		for (int r = 0; r < 6; ++r) {
			for (int c = 0; c < 7; ++c) {
				GameButton gb = new GameButton();
				gb.setPrefSize(65, 65);
				gb.setStyle("-fx-border-color:Blue");
				int r0 = r, c0 = c;
				gb.setOnAction(e->{
					gb.pressWithValue(number);
					sendMove(r0, c0);
				});
				board[(7 * r) + c] = gb;
			}
		}
	}
	
	public void run() {
		try {
			playerSocket = new Socket("127.0.0.1", 5555);
			out = new ObjectOutputStream(playerSocket.getOutputStream());
			in = new ObjectInputStream(playerSocket.getInputStream());
			playerSocket.setTcpNoDelay(true);
		}
		catch (Exception e) {
			System.out.println("*** Encountered unexpected exception from Player:36; Shutting down!!!!!");
			System.exit(1);
		}
		
		while(true) {
			try {
				CFourInfo cfi = (CFourInfo)(in.readObject());
				if (cfi.turn == 46) {
					number = cfi.playerCount;
				} else {
					callback.accept(cfi);
				}
			} catch (ClassNotFoundException e) {
				System.out.println("*** CFourInfo class not found");
				break;
			} catch (Exception eof) {
				callback.accept(new CFourInfo(-1,-1,-1,-1));
				break;
			}
		}
			
	}
	
	public void send(CFourInfo cfi) {
		try {
			out.writeObject(cfi);
		}
		catch (Exception e) {
			System.out.println("*** Encountered unexpected exception from Player:67; Shutting down!!!!!");
			System.exit(1);
		}
	}
	
	void disableBoard() {
		for (int i = 0; i < 42; ++i) {
			board[i].setDisable(true);
		}
	}
	
	void enableBoard() {
		for (int i = 0; i < 42; ++i) {
			if (board[i].getText() == "") {
				board[i].setDisable(false);
			}
		}
	}
	
	public void resetBoard(boolean disabled) {
		for (int i = 0; i < 42; ++i) {
			if (disabled) {
				board[i].unpressAndDisable();
			} else {
				board[i].unpress();
			}
		}
	}
	
	private void sendMove(int r, int c) {
		send(new CFourInfo(2, number, (7 * r) + c,  -1));
	}
}