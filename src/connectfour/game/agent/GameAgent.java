package connectfour.game.agent;

import connectfour.game.Game;

/**
 * @author Ravi Mohan
 *
 * Generico giocatore di un gioco a somma zero
 */
public abstract class GameAgent extends AbstractAgent {
        //gioco sul quale l'agente è impegnato
	protected  Game game;
        //liomite di ricerca per l'agente
        protected int limit=0;

	public GameAgent(Game g, int limit) {
                this.limit=limit;
		this.game = g;
	}

        //metodi per effettuare una mossa
	public abstract void move () ;
        public abstract void move(Object o);

    /**
     * @param game the game to set
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

}