
package connectfour.game.agent;

import connectfour.game.Game;

/**
 *
 * @author Giovanni
 *
 * giocatore di gioco a somma zero che utiliza
 * l'algoritmo minimax con AlphaBeta pruning
 *
 */

public class AlphaBetaAgent extends GameAgent {

    public AlphaBetaAgent (Game aGame, int limit){
        super(aGame,limit);
    }

    @Override
    public void move() {
       game.makeAlphaBetaMove(getLimit());
    }

    @Override
    public void move(Object o) {
       game.makeAlphaBetaMove(getLimit());
    }
    
}
