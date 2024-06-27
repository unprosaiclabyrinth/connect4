import java.io.Serializable;

/* Client version */

//-----------------------------------------------------------------------------------------
// Info codes:-
//-----------------------------------------------------------------------------------------
// playerCount = -1:
//	no server (server is not turned on or has been shut down)
// playerCount = 0, 1, 2:
//	no of players on the server
// playerCount = 3:
//	player wants to start a new game after completing a game
// playerCount = 4:
// 	player wants to exit after completing a game
// playerCount = 69:
// 	the other 4 fields are the winning combination
// playerCount = 69 + x:
//	x is the number of the player who has won, the other 4 fields are the winning combination
// turn = 46:
//	code for player number update; playerCount is the updated player number
//-------------------------------------------------------------------------------------------

public class CFourInfo implements Serializable{

	private static final long serialVersionUID = -1714012956479529889L;
	
	int playerCount;
	int turn;
	int prevMove;
	int move;
	int result;
	
	public CFourInfo(int count, int turn, int move, int result) {
		this.playerCount = count;
		this.turn = turn;
		this.prevMove = -1;
		this.move = move;
		this.result = result;
	}
	
	public CFourInfo(int count, int turn, int prevMove, int move, int result) {
		this.playerCount = count;
		this.turn = turn;
		this.prevMove = prevMove;
		this.move = move;
		this.result = result;
	}
}