import java.util.ArrayList;

public class CFourLogic {
	
	private GameButton[] board;
	private ArrayList<Integer> winningCombination;
	
	public CFourLogic(GameButton[] board) {
		this.board = board;
		this.winningCombination = new ArrayList<>();
	}
	
	// Legalizing the move and returning the legal index
	public int legalizedMove(int index) {
		int c = index % 7;
		for (int r = 0; r < 6; ++r) {
			if (r > 0 && board[I(r, c)].getText() != "") {
				return I(r - 1, c);
			}
		}
		return I(5, c);
	}
	
	// Checking whether a move is a winning move
	public boolean isWinningMove(String playerNo, int index) {
		int r = index / 7;
		int c = index % 7;
		// Horizontal check
		int connect = 0;
		for (int i = -3; i <= 3; ++i) {
			int C = c + i;
			if (C >= 0 && C < 7) {
				if (board[I(r, C)].getText().equals(playerNo) || C == c) {
					winningCombination.add(I(r, C));
					++connect;
				} else {
					winningCombination.clear();
					connect = 0;
				}
			}
			if (connect == 4) {
				return true;
			}
		}
		// Vertical check
		connect = 0;
		for (int i = -3; i <= 3; ++i) {
			int R = r + i;
			if (R >= 0 && R < 6) {
				if (board[I(R, c)].getText().equals(playerNo) || R == r) {
					winningCombination.add(I(R, c));
					++connect;
				} else {
					winningCombination.clear();
					connect = 0;
				}
			}
			if (connect == 4) {
				return true;
			}
		}
		// NW-SE diagonal check
		connect = 0;
		for (int i = -3; i <= 3; ++i) {
			int R = r + i, C = c + i;
			if (R >= 0 && R < 6 && C >= 0 && C < 7) {
				if (board[I(R, C)].getText().equals(playerNo) || (C == c && R == r)) {
					winningCombination.add(I(R, C));
					++connect;
				} else {
					winningCombination.clear();
					connect = 0;
				}
			}
			if (connect == 4) {
				return true;
			}
		}
		// NE-SW diagonal check
		connect = 0;
		for (int i = -3; i <= 3; ++i) {
			int R = r - i, C = c + i;
			if (R >= 0 && R < 6 && C >= 0 && C < 7) {
				if (board[I(R, C)].getText().equals(playerNo) || (C == c && R == r)) {
					winningCombination.add(I(R, C));
					++connect;
				} else {
					winningCombination.clear();
					connect = 0;
				}
			}
			if (connect == 4) {
				return true;
			}
		}
		return false;
	}
	
	// Returning the "connect-4" indices
	public ArrayList<Integer> getWinningCombination() {
		return winningCombination;
	}
	
	// Checking whether the game is a tie at every move
	// by checking if all sqaures have been clicked
	public boolean isTie() {
		int count = 0;
		for (int r = 0; r < 6; ++r) {
			for (int c = 0; c < 7; ++c) {
				if (board[I(r, c)].getText() != "1" && board[I(r, c)].getText() != "2") {
					++count;
				}
			}
		}
		if (count == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	// Setter (for testing purposes)
	public void setBoard(GameButton[] board) {
		this.board = board;
	}
	
	// Returning the 1D index for 2D coordinates
	private int I(int r, int c) {
		return (7 * r) + c;
	}
}
