/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package connectfour.game.agent;

import connectfour.game.Game;

/**
 *
 * @author Giovanni
 *
 *  giocatore di gioco a somma zero che utiliza
 * l'algoritmo minimax con AlphaBeta pruning
 *
 * 
 */
public class MiniMaxAgent extends GameAgent {

    public MiniMaxAgent (Game aGame, int limit){
        super(aGame,limit);
    }

    @Override
    public void move() {
       game.makeMiniMaxMove(getLimit());
    }

    @Override
    public void move(Object o) {
        game.makeMiniMaxMove(getLimit());
    }

   
}
