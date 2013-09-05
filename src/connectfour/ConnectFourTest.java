package connectfour;

import connectfour.game.ConnectFourGameManager;

public class ConnectFourTest {

	public static void main(String[] args) {

		ConnectFourGameManager gm = new ConnectFourGameManager();
		
		System.out.println("computing...");
		gm.setFirstPlayerType(ConnectFourGameManager.MINIMAX);
		gm.setFirstPlayerLimint(6);
		
		gm.setSecondPlayerType(ConnectFourGameManager.MINIMAX);
		gm.setSecondPlayerLimit(9);
		
		gm.performMove();
		System.out.println("ok!");

	}

}
