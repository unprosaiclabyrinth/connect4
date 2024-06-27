import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;

import javafx.application.Platform;

public class CFourLogicTest{
	
	GameButton[] board;
	CFourLogic cfl;
	
	@BeforeAll
	static void initJfxRuntime() {
		Platform.startup(()->{});
	}
	
	@BeforeEach
	void boardInit() {
		board = new GameButton[42];
		for (int i = 0; i < 42; ++i) {
			board[i] = new GameButton();
		}
		cfl = new CFourLogic(board);
	}
	
	//
	// 1
	//
	@Test
	void verticalCheckTest1() {
		board[18].pressWithValue(1);
		board[25].pressWithValue(1);
		board[32].pressWithValue(1);
		assertEquals(true, cfl.isWinningMove("1", 39), "Case 1 failed");
		
		board[21].pressWithValue(2);
		board[14].pressWithValue(2);
		board[7].pressWithValue(2);
		assertEquals(true, cfl.isWinningMove("2", 0), "Case 2 failed");
	}
	
	//
	// 2
	//
	@Test
	void verticalCheckTest2() {
		board[15].pressWithValue(1);
		board[16].pressWithValue(1);
		board[17].pressWithValue(1);
		assertEquals(true, cfl.isWinningMove("1", 18), "Case 1 failed");
		
		board[15].pressWithValue(1);
		board[16].pressWithValue(2);
		board[17].pressWithValue(1);
		assertEquals(false, cfl.isWinningMove("2", 18), "Case 2 failed");
	}
	
	//
	// 3
	//
	@Test
	void horizaontalCheckTest1() {
		board[20].pressWithValue(1);
		board[22].pressWithValue(1);
		board[23].pressWithValue(1);
		assertEquals(false, cfl.isWinningMove("1", 21), "Case 1 failed");
		
		board[38].pressWithValue(2);
		board[39].pressWithValue(2);
		board[41].pressWithValue(2);
		assertEquals(true, cfl.isWinningMove("2", 40), "Case 2 failed");
	}
	
	//
	// 4
	//
	@Test
	void horizaontalCheckTest2() {
		board[37].pressWithValue(1);
		board[36].pressWithValue(1);
		board[38].pressWithValue(1);
		assertEquals(true, cfl.isWinningMove("1", 35), "Case 1 failed");
		
		board[40].pressWithValue(2);
		board[41].pressWithValue(2);
		board[38].pressWithValue(2);
		assertEquals(true, cfl.isWinningMove("2", 39), "Case 2 failed");
	}
	
	//
	// 5
	//
	@Test
	void nwseCheckTest1() {
		board[38].pressWithValue(1);
		board[30].pressWithValue(1);
		board[22].pressWithValue(1);
		assertEquals(true, cfl.isWinningMove("1", 14), "Case 1 failed");
		
		board[33].pressWithValue(2);
		board[41].pressWithValue(2);
		board[25].pressWithValue(2);
		assertEquals(true, cfl.isWinningMove("2", 17), "Case 2 failed");
	}
	
	//
	// 6
	//
	@Test
	void nwseCheckTest2() {
		board[32].pressWithValue(1);
		board[24].pressWithValue(1);
		board[16].pressWithValue(1);
		assertEquals(false, cfl.isWinningMove("2", 8), "Case 1 failed");
		
		board[0].pressWithValue(2);
		board[24].pressWithValue(2);
		board[16].pressWithValue(1);
		assertEquals(false, cfl.isWinningMove("2",8), "Case 2 failed");
	}
	
	//
	// 7
	//
	@Test
	void neswCheckTest1() {
		board[5].pressWithValue(1);
		board[23].pressWithValue(1);
		board[17].pressWithValue(1);
		assertEquals(true, cfl.isWinningMove("1", 11), "Case 1 failed");
		
		board[28].pressWithValue(2);
		board[34].pressWithValue(2);
		board[22].pressWithValue(2);
		assertEquals(false, cfl.isWinningMove("2", 16), "Case 2 failed");
	}
	
	//
	// 8
	//
	@Test
	void neswCheckTest2() {
		board[35].pressWithValue(1);
		board[41].pressWithValue(1);
		board[29].pressWithValue(1);
		assertEquals(false, cfl.isWinningMove("2", 23), "Case 1 failed");
		
		board[13].pressWithValue(2);
		board[7].pressWithValue(2);
		board[25].pressWithValue(2);
		assertEquals(false, cfl.isWinningMove("2", 19), "Case 2 failed");
	}
	
	//
	// 9
	//
	@Test
	void randomCheckTest() {
		board[38].pressWithValue(1);
		board[0].pressWithValue(1);
		board[9].pressWithValue(1);
		assertEquals(false, cfl.isWinningMove("1", 14), "Case 1 failed");
		
		board[26].pressWithValue(2);
		board[3].pressWithValue(2);
		board[6].pressWithValue(2);
		assertEquals(false, cfl.isWinningMove("2", 41), "Case 2 failed");
	}
	
	//
	// 10
	//
	@Test
	void legalizeTest() {
		assertEquals(35, cfl.legalizedMove(0), "Case 1 failed");
		
		board[36].pressWithValue(1);
		assertEquals(29, cfl.legalizedMove(1), "Case 2 failed");
		
		assertEquals(37, cfl.legalizedMove(23), "Case 3 failed");
		
		assertEquals(38, cfl.legalizedMove(38), "Case 4 failed");
		
		assertEquals(39, cfl.legalizedMove(39), "Case 5 failed");
		
		board[40].pressWithValue(2);
		assertEquals(33, cfl.legalizedMove(33), "Case 6 failed");
		
		board[34].pressWithValue(2);
		board[41].pressWithValue(1);
		assertEquals(27, cfl.legalizedMove(6), "Case 7 failed");
	}
	
	//
	// 11
	//
	@Test
	void tieCheckTest() {
		board[0].pressWithValue(1);
		board[1].pressWithValue(2);
		board[2].pressWithValue(1);
		board[3].pressWithValue(2);
		board[4].pressWithValue(1);
		//board[5].pressWithValue(1);
		board[6].pressWithValue(2);
		board[7].pressWithValue(1);
		board[8].pressWithValue(2);
		board[9].pressWithValue(1);
		board[10].pressWithValue(2);
		board[11].pressWithValue(1);
		board[12].pressWithValue(2);
		board[13].pressWithValue(1);
		board[14].pressWithValue(2);
		board[15].pressWithValue(1);
		board[16].pressWithValue(1);
		board[17].pressWithValue(2);
		board[18].pressWithValue(1);
		board[19].pressWithValue(1);
		board[20].pressWithValue(2);
		board[21].pressWithValue(1);
		board[22].pressWithValue(2);
		board[23].pressWithValue(2);
		board[24].pressWithValue(1);
		board[25].pressWithValue(2);
		board[26].pressWithValue(1);
		board[27].pressWithValue(2);
		board[28].pressWithValue(2);
		board[29].pressWithValue(1);
		board[30].pressWithValue(2);
		board[31].pressWithValue(1);
		board[32].pressWithValue(2);
		board[33].pressWithValue(2);
		board[34].pressWithValue(1);
		board[35].pressWithValue(2);
		board[36].pressWithValue(2);
		board[37].pressWithValue(1);
		board[38].pressWithValue(1);
		board[39].pressWithValue(1);
		board[40].pressWithValue(2);
		board[41].pressWithValue(1);
		assertEquals(true, cfl.isTie());
	}
}