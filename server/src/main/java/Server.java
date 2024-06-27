import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import java.util.ArrayList;

public class Server {
	
	int playerCount = 0;
	PlayerThread player1, player2;
	GameServer gameServer;
	private Consumer<Serializable> callback;
	GameButton[] board;
	CFourLogic cfl;
	
	public Server(Consumer<Serializable> call) {
		callback = call;
		board = new GameButton[42];
		for (int r = 0; r < 6; ++r) {
			for (int c = 0; c < 7; ++c) {
				GameButton gb = new GameButton();
				gb.setPrefSize(65, 65);
				gb.setDisable(true);
				gb.setStyle("-fx-border-color:Blue");
				board[(7 * r) + c] = gb;
			}
		}
		cfl = new CFourLogic(board);
		gameServer = new GameServer();
		gameServer.start();
	}
	
	
	public class GameServer extends Thread {
		
		public void run() {
			try(ServerSocket ssocket = new ServerSocket(5555);) {
				while(true) {
					PlayerThread player = new PlayerThread(ssocket.accept(), ++playerCount);
					if (playerCount == 1) {
						player1 = player;
						callback.accept(new CFourInfo(1,0,-1,-1));
						player1.start();
					} else if (playerCount == 2) {
						if (player1.isAlive()) {
							player2 = player;
							player2.start();
						} else {
							player1 = player;
							player1.start();
						}
						callback.accept(new CFourInfo(2,1,-1,-1));
					}
				}
			}
			catch (Exception e) {
				System.out.println("Encountered unexpected exception at Server:45; shutting down!!!!!!!");
				System.exit(1);
			}
		}
	}
	
	class PlayerThread extends Thread {
		
		Socket connection;
		int number;
		ObjectInputStream in;
		ObjectOutputStream out;
		
		public PlayerThread(Socket s, int number) {
			this.connection = s;
			this.number = number;
		}
		
		public void run() {
			try {
				out = new ObjectOutputStream(connection.getOutputStream());
				in = new ObjectInputStream(connection.getInputStream());
				connection.setTcpNoDelay(true);
			}
			catch (Exception e) {
				System.out.println("Encountered unexpected exception at Server:84; shutting down!!!!!!!");
				System.exit(1);
			}
			
			if (playerCount == 1) {
				try {
					player1.out.writeObject(new CFourInfo(1,46,-1,-1));
					player1.out.writeObject(new CFourInfo(1,0,-1,-1));
				}
				catch (Exception e) {
					System.out.println("Encountered unexpected exception at Server:95; shutting down!!!!!!!");
					System.exit(1);
				}
			} else if (playerCount == 2) {
				try {
					this.out.writeObject(new CFourInfo(2,46,-1,-1));
					player1.out.writeObject(new CFourInfo(2,1,-1,-1));
					player2.out.writeObject(new CFourInfo(2,1,-1,-1));
				}
				catch (Exception e) {
					System.out.println("Encountered unexpected exception at Server:104; shutting down!!!!!!!");
					System.exit(1);
				}
			}
			
			while(true) {
				try {
					CFourInfo cfi = (CFourInfo)(in.readObject());
					callback.accept(cfi);
					if (cfi.playerCount == 2) {
						int cfiMove = cfl.legalizedMove(cfi.move);
						int nextTurn = cfi.turn == 1 ? 2 : 1;
						if (cfl.isWinningMove(Integer.toString(cfi.turn), cfiMove)) {
							try {
								ArrayList<Integer> winningCombination = cfl.getWinningCombination();
								CFourInfo winningCombinationInfo = new CFourInfo(69, winningCombination.get(0), winningCombination.get(1), winningCombination.get(2), winningCombination.get(3));
								player1.out.writeObject(winningCombinationInfo);
								player2.out.writeObject(winningCombinationInfo);
								callback.accept(new CFourInfo(69 + cfi.turn, winningCombination.get(0), winningCombination.get(1), winningCombination.get(2), winningCombination.get(3)));
								CFourInfo winInfo = new CFourInfo(playerCount, nextTurn, cfi.move, cfiMove, cfi.turn);
								player1.out.writeObject(winInfo);
								player2.out.writeObject(winInfo);
								callback.accept(winInfo);
							} catch (IOException e1) {
								System.out.println("Encountered unexpected exception at Server:116; shutting down!!!!!!!");
								System.exit(1);
							}
						} else if (cfl.isTie()) {
							try {
								CFourInfo tieInfo = new CFourInfo(playerCount, nextTurn, cfi.move, cfiMove, 0);
								player1.out.writeObject(tieInfo);
								player2.out.writeObject(tieInfo);
								callback.accept(tieInfo);
							} catch (IOException e1) {
								System.out.println("Encountered unexpected exception at Server:138; shutting down!!!!!!!");
								System.exit(1);
							}
						} else {
							try {
								CFourInfo info = new CFourInfo(playerCount, nextTurn, cfi.move, cfiMove, -1);
								player1.out.writeObject(info);
								player2.out.writeObject(info);
							} catch (IOException e1) {
								System.out.println("Encountered unexpected exception at Server:148; shutting down!!!!!!!");
								System.exit(1);
							}
						}
					} else if (cfi.playerCount == 3) {
						if (playerCount == 2) {
							this.out.writeObject(new CFourInfo(1,46,-1,-1));
							this.out.writeObject(new CFourInfo(1,0,-1,-1));
							callback.accept(new CFourInfo(3,0,-1,-1));
							++playerCount;
						} else if (playerCount == 3) {
							this.out.writeObject(new CFourInfo(2,46,-1,-1));
							player1.out.writeObject(new CFourInfo(3,1,-1,-1));
							player2.out.writeObject(new CFourInfo(3,1,-1,-1));
							callback.accept(new CFourInfo(2,0,-1,-1));
							--playerCount;
						}
						
					} else if (cfi.playerCount == 4) {
						if (playerCount == 3) {
							playerCount = 1;
						} else {
							--playerCount;
						}
						if (playerCount == 1) {
							if (this == player1) {
								player2.out.writeObject(new CFourInfo(1,46,-1,-1));
								player2.out.writeObject(new CFourInfo(1,0,-1,-1));
							} else {
								player2.out.writeObject(new CFourInfo(1,46,-1,-1));
								player1.out.writeObject(new CFourInfo(1,0,-1,-1));
							}
						}
						callback.accept(new CFourInfo(playerCount,0,-1,-1));
						break;
					}
				} catch (Exception e) {
					--playerCount;
					callback.accept(new CFourInfo(playerCount,0,-1,-1));
					try {
						if (playerCount == 1) {
							if (this == player1) {
								player2.out.writeObject(new CFourInfo(1,46,-1,-1));
								player2.out.writeObject(new CFourInfo(1,0,-1,-1));
							} else if (this == player2) {
								player1.out.writeObject(new CFourInfo(1,46,-1,-1));
								player1.out.writeObject(new CFourInfo(1,0,-1,-1));
							}
						}
					} catch (IOException e1) {
						System.out.println("Encountered unexpected exception at Server:192; shutting down!!!!!!!");
						System.exit(1);
					}
					break;
				}
			}
		}
	}
}