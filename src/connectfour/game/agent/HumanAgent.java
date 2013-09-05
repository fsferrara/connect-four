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
 *  giocatore umano di gioco a somma zero
 *
 *
 */
public class HumanAgent extends GameAgent {

    public HumanAgent (Game aGame,int limit){
        super(aGame,limit);
    }

    //se non viene passtata in input una mossa particolare si utilizza
    // il minimax con alpha beta pruning.
    @Override
    public void move() {
        game.makeAlphaBetaMove(getLimit());
    }

    //viene effettuata la mossa passata in input e decisa dall'utente
    @Override
    public void move(Object move) {
         game.makeMove(game.getState(), move);
    }
}
