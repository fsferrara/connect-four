package connectfour.game;



import connectfour.game.util.XYLocation;
import java.util.ArrayList;


/*
* implementazione di gioco a somma zero a due player
* Forza Quattro
*
* Due giocatori: Red e Yellow
*
* Red --> Max
* Yellow --> Min
*
 */
public class ConnectFour extends Game {
    public static final int MAX_UTIL = 1000;
    public static final int MIN_UTIL = -1000;
    public static int       NO_MORE  = ConnectFourBoard.ROWS;

    private boolean boost = false;

    public ConnectFour() {

        // mosse disponibli sono codificate come coppie <riga,colonna>
        // e memorizzate in oggetti XYLovation
        int[] moves = new int[ConnectFourBoard.COLUMNS];

        // all'inizio del gioco le celle della prima riga
        for (int j = 0; j < ConnectFourBoard.COLUMNS; j++) {
            moves[j] = 0;
        }

        // rispetto alle voci previste da Game in Connect four sono previste le ulteriori:
        // - la cofigurazione corrente delle pedine sulla board
        // - una flag di stato terminale
        presentState.put("moves", moves);

        // il primo a giocare e' red
        presentState.put("player", "R");
        presentState.put("utility", new Integer(0));
        presentState.put("board", new ConnectFourBoard());
        presentState.put("level", new Integer(0));
    }

    public ConnectFourBoard getBoard(GameState state) {
        return (ConnectFourBoard) state.get("board");
    }

    /*
     *     restituisce tutti gli stati ottenibili applicando le azioni disponibili
     *     allo stato passato in input
     */
    @Override
    public ArrayList getSuccessorStates(GameState state) {
        GameState         temp        = presentState;
        ArrayList<Object> retVal      = new ArrayList<Object>();
        int               parentLevel = getLevel(state);

        // per ogni mossa disponibile
        for (int col = 0; col < getMoves(state).length; col++) {
            if (getMoves(state)[col] != NO_MORE) {

                // calcola lo stato che si ottiene effettuendo la mossa sullo
                // stato passato in input
                GameState aState = makeMove(state, col);

                // memorizza l'ultima mossa e il livello nel nuovo stato
                aState.put("moveMade", col);
                aState.put("level", new Integer(parentLevel + 1));
                retVal.add(aState);
            }
        }

        presentState = temp;

        return retVal;
    }

    /*
     * restituisce lo stato ottenuto applicando la mossa "move" allo stato passato in input
     * tale nuovo stato diventa lo stato attuale del gioco
     */
    @Override
    public GameState makeMove(GameState state, Object move) {
        int col = (Integer) move;

        return makeMove(state, col);
    }

    /*
     * restituisce lo stato ottenuto applicando (se possibile) la mossa
     * indicata dall'indice di colonna passato in input allo
     * stato passato in input
     *
     *  tale nuovo stato diventa lo stato attuale del gioco
     *
     */
    public GameState makeMove(GameState state, int col) {
        GameState temp = getMove(state, col);

        if (temp != null) {
            presentState = temp;
        }

        return presentState;
    }

    /*
     * restituisce lo stato ottenuto applicando (se possibile) la mossa
     * indicata dall'indice di colonna passato in input allo
     * stato attuale del gioco
     *
     *  tale nuovo stato diventa il nuovo stato attuale del gioco
     *
     */
    public GameState makeMove(int col) {
        GameState state = presentState;
        GameState temp  = getMove(state, col);

        if (temp != null) {
            presentState = temp;
        }

        return presentState;
    }


    /**
     * restituisce lo stato ottenuto applicando (se possibile) la mossa
     * indicata dagli indici di riga e colona passati in input allo
     * stato passato in input
     *
     * @param state
     * @param col
     * @return
     */
    public GameState getMove(GameState state, int col) {

        GameState retVal = null;
        int[]     moves  = getMoves(state);

        // se la mossa e' possibile nello stato
        if (moves[col] != NO_MORE) {
            this.visitedNodes++;
            
            // BOOST ottimizzazione new
            if (boost) {
                if (state.getChild(col) != null) {
                    return state.getChild(col);
                }
            }
          
         
            this.createdNodes++;
            int[] newMoves = moves.clone();

            // calcola il nuova stato dopo la mossa
            retVal = new GameState();

            
            retVal.put("moves", newMoves);

            // effettua una copia della tavola di gioco
            ConnectFourBoard newBoard = getBoard(state).cloneBoard();

            // segna la posizione indicata nella mossa con il colore del giocatore di turno
            if (getPlayerToMove(state).equals("R")) {
                newBoard.markR(moves[col], col);

                // cambia il giocatore di turno
                retVal.put("player", "Y");
            } else {
                newBoard.markY(moves[col], col);
                retVal.put("player", "R");
            }

            retVal.put("board", newBoard);
            retVal.put("level", new Integer(getLevel(state) + 1));
            retVal.put("moveMade", col);

            // controlla e memorizza nel nuovo stato se esso è terminale
            retVal.put("terminalState", terminalTest(retVal));

            // calcola e memorizza nello stesso l'utilita' del nuovo stato
            retVal.put("utility", new Integer(computeUtility(retVal)));
            // se c'e' ancora spazio sulla colonna per infilare pedine
            // allora aggiunge una nuova mossa su quella colonna
            newMoves[col]++;

            // BOOST ottimizzazione
            if (boost) {
            	state.addChild(retVal, col);
            }
            

        }

        return retVal;
    }

    /*
     * verifica se lo statopassato in input è terminale
     * nel foza quattro uno stato è terminale se una delle due e' verificata:
     *       - c'e' una fila (orizzontale,verticale o diagonale)
     *               di 4 pedine dello stesso colore adiacenti
     *       - non ci sono posizioni libere
     */
    @Override
    public boolean terminalTest(GameState state) {

        // se il controllo e' stato gia effettuato
        Boolean terminal = (Boolean) state.get("terminalState");

        if (terminal != null) {
            return terminal;
        }

        if (state.get("moveMade") == null) {
            return false;
        }

        int   col   = (Integer) state.get("moveMade");
        int[] moves = (int[]) state.get("moves");

        // effettua il controllo sulla board
        ConnectFourBoard board = (ConnectFourBoard) state.get("board");

        // controlla se c'e' una linea di quattro
        boolean line = board.lineThroughBoard(new XYLocation(moves[col], col));

        // controla se la board e piena <--> ci sono ancora mosse
        boolean filled = ((ConnectFourBoard) state.get("board")).isFull();

        return (line || filled);
    }

    /*
     * applica l'algoritmo MiniMax fino ad una profodità massima "limit"
     * allo stato passato in input settando il prossimo stato e restituendo il valore
     * di minimax corrispondente
     *
     */
    @Override
    public int getMiniMaxValue(GameState state, int limit) {

        if (getPlayerToMove(state).equalsIgnoreCase("R")) {
            return maxValue(state, limit);
        } else {
            return minValue(state, limit);
        }
    }

    /*
     * applica l'algoritmo MiniMax con AlphaBeta pruning fino ad una profodità massima "limit"
     * allo stato passato in input settando il prossimo stato e restituendo il valore
     * di minimax corrispondente
     */
    @Override
    public int getAlphaBetaValue(GameState state, int limit) {

//      System.out.println("In get Minimax Value");
//      System.out.println("Received state ");
//      ((ConnectFourBoard) state.get("board")).print();
//      if (terminalTest(state)) {
//          System.out.println("end game");
//      }
        // se e' il turno di Red allora calcola maxValue dello stato altrimenti calcola minValue
        if (getPlayerToMove(state).equalsIgnoreCase("R")) {

            // gernera due valori di alpha e di beta iniziali
            AlphaBeta initial = new AlphaBeta(Integer.MIN_VALUE, Integer.MAX_VALUE);
            int       max     = maxValue(state, initial, limit);

            return max;
        } else {
            AlphaBeta initial = new AlphaBeta(Integer.MIN_VALUE, Integer.MAX_VALUE);

            return minValue(state, initial, limit);
        }
    }

    /*
     * restitusce l'utilità (rispetto al primo player) calcolata sullo stato
     * passato in input
     */
    @Override
    public int computeUtility(GameState state) {
        int retVal = 0;

        // se l'utilità dello stato e' stata gia calcolata
        if (state.get("utility") != null) {
            return (Integer) state.get("utility");
        } else {
            ConnectFourBoard board = getBoard(state);
            int[]            moves = (int[]) state.get("moves");
            int              col   = (Integer) state.get("moveMade");

            // se c'e' una linea di quattro assegna i valori per la
            // vittoria o la sconfitta
            boolean line         = board.lineThroughBoard(new XYLocation(moves[col], col));
            String  playerToMove = getPlayerToMove(state);

            if (line) {
                if (playerToMove.equals("R")) {
                    retVal = MIN_UTIL;
                } else {
                    retVal = MAX_UTIL;
                }
            }

            return retVal;
        }
    }

    public int computeEuristicUtility(GameState game) {
        ConnectFourBoard board = (ConnectFourBoard) game.get("board");

        return board.getEuristicValue();
    }

    /**
     * @return the boost
     */
    public boolean isBoost() {
        return boost;
    }

    /**
     * @param boost the boost to set
     */
    public void setBoost(boolean boost) {
        this.boost = boost;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
