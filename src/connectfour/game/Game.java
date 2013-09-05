package connectfour.game;

import java.util.ArrayList;

import connectfour.game.util.Util;

/**
 * @author Ravi Mohan
 * @author juen
 */

/* rappresentazione di un gioco generico a somma zero e a due giocatori
 * lo stato di un gioco vene rappresentato attraverso una hashtable (gestita dalla classe GameState)
 * vengono considerate comuni per tutti i giochi i seguenti elementi dello stato:
 *                         - livello dell'albero di gioco
 *                         - giocatore di turno
 *                         - utilità (rispetto al primo player) dello stato
 *                         - mosse disponibli nello stato
 *                         - ultima mossa effettuata
 *                         - prossimo stato del gioco
 *
 *
 */
public abstract class Game {

    protected GameState presentState = new GameState();
    protected long visitedNodes = 0;
    protected long createdNodes = 0;

    /*metodi astratti che dipendono dalla particolare implementazione del gioco */

    /*
     * restituisce tutti gli stati ottenibili applicando le azioni disponibili
     * allo stato passato in input
     */
    public abstract ArrayList getSuccessorStates(GameState state);

    /*
     * restituisce lo stato ottenuto applicando la mossa "move" allo stato passato in input
     * tale nuovo stato diventa lo stato attuale del gioco
     */
    public abstract GameState makeMove(GameState state, Object move);

    /*
     * applica l'algoritmo MiniMax fino ad una profodità massima "limit"
     * allo stato passato in input settando il prossimo stato e restituendo il valore
     * di minimax corrispondente
     *
     */
    public abstract int getMiniMaxValue(GameState state, int limit);


    /*
     * applica l'algoritmo MiniMax con AlphaBeta pruning fino ad una profodità massima "limit"
     * allo stato passato in input settando il prossimo stato e restituendo il valore
     * di minimax corrispondente
     */
    public abstract int getAlphaBetaValue(GameState state, int limit);

    /*
     * restitusce l'utilità (rispetto al primo player) calcolata sullo stato
     * passato in input
     */
    protected abstract int computeUtility(GameState state);

    /*
     * restitusce l'utilità (rispetto al primo player) calcolata sullo stato
     * passato in input
     */
    protected abstract int computeEuristicUtility(GameState state);

    /*
     * verifica se lo statopassato in input è terminale
     */
    protected abstract boolean terminalTest(GameState state);

    //verifica la terminazione del gioco
    public boolean isEndOfGame() {
        return (terminalTest(getState()));
    }

    //metodi accessori
    public int getLevel(GameState g) {
        return (((Integer) g.get("level")).intValue());
    }

    public int[] getMoves(GameState state) {
        return (int[]) state.get("moves");
    }

    public String getPlayerToMove(GameState state) {
        return (String) state.get("player");
    }

    public int getUtility(GameState h) {
        return ((Integer) h.get("utility")).intValue();
    }

    public GameState getState() {
        return presentState;
    }


    /*
     * MiniMax : effettua il calcolo del maxValue di uno stato
     */
    public int maxValue(GameState state, int limit) {

        int v = Integer.MIN_VALUE;
        //se lo stato e' terminale il valore da ritornare è l'utilita'
        if (terminalTest(state)) {
            return computeUtility(state);
        } else //se si e' raggiunto il limite di profondità di ricerca
        //e' restituita una stima dell'utilita' delllo stato
        if (((Integer) state.get("level")) >= ((Integer) presentState.get("level") + limit)) {
            return computeEuristicUtility(state);
        } else {
            //genera gli stati successori dello stato
            ArrayList successorList = getSuccessorStates(state);
            for (int i = 0; i < successorList.size(); i++) {
                GameState successor = (GameState) successorList.get(i);
                //calcola il MinValue di ogni successore applicando ricorsivamente l'algoritmo
                int minimumValueOfSuccessor = minValue(successor, limit);

                //cerca il massimo dei MinValue e fissa prossimo stato
                if (minimumValueOfSuccessor > v) {
                    v = minimumValueOfSuccessor;
                    state.put("next", successor);
                }
            }

            return v;
        }
    }

    /*
     * MiniMax-AlphaBeta : effettua il calcolo del maxValue di uno stato
    utlizzando l'alphbeta pruning
     */
    protected int maxValue(GameState state, AlphaBeta ab, int limit) {

        int v = Integer.MIN_VALUE;
        //se lo stato e' terminale il valore da ritornare è l'utilita'
        if (terminalTest(state)) {
            return computeUtility(state);
        } else //se si e' raggiunto il limite di profondità di ricerca
        //e' restituita una stima dell'utilita' delllo stato
        if (((Integer) state.get("level")) >= ((Integer) presentState.get("level") + limit)) {
            return computeEuristicUtility(state);
        } else {       //genera gli stati successori dello stato
            ArrayList successorList = getSuccessorStates(state);
            for (int i = 0; i < successorList.size(); i++) {
                GameState successor = (GameState) successorList.get(i);
                //calcola il MinValue di ogni successore applicando ricorsivamente l'algoritmo
                int minimumValueOfSuccessor = minValue(successor, ab.copy(), limit);

                //cerca il massimo dei MinValue e fissa prossimo stato
                if (minimumValueOfSuccessor > v) {
                    v = minimumValueOfSuccessor;
                    state.put("next", successor);
                }
                //se il valore trovato e' maggiore o uguale della
                // migliore alternativa per Min applica il pruning e ritorna
                if (v >= ab.beta()) {
                    // System.out.println("pruning from max");
                    return v;
                }
                //aggiorna il vaolre della miglior alternativa per max
                ab.setAlpha(Util.max(ab.alpha(), v));
            }

            return v;
        }

    }

    public int minValue(GameState state, int limit) {

        int v = Integer.MAX_VALUE;

        if (terminalTest(state)) {
            return computeUtility(state);

        } else if (((Integer) state.get("level")) >= ((Integer) presentState.get("level") + limit)) {
            return computeEuristicUtility(state);
        } else {
            ArrayList successorList = getSuccessorStates(state);
            for (int i = 0; i < successorList.size(); i++) {
                GameState successor = (GameState) successorList.get(i);
                int maximumValueOfSuccessors = maxValue(successor, limit);

                if (maximumValueOfSuccessors < v) {
                    v = maximumValueOfSuccessors;
                    state.put("next", successor);
                }
            }

            return v;
        }

    }

    public int minValue(GameState state, AlphaBeta ab, int limit) {
        int v = Integer.MAX_VALUE;

        if (terminalTest(state)) {
            return (computeUtility(state));

        } else if (((Integer) state.get("level")) >= ((Integer) presentState.get("level") + limit)) {
            return computeEuristicUtility(state);
        } else {
            ArrayList successorList = getSuccessorStates(state);
            for (int i = 0; i < successorList.size(); i++) {
                GameState successor = (GameState) successorList.get(i);
                int maximumValueOfSuccessors = maxValue(successor, ab.copy(), limit);

                if (maximumValueOfSuccessors < v) {
                    v = maximumValueOfSuccessors;
                    state.put("next", successor);
                }
                if (v <= ab.alpha()) {
                    return v;
                }
                ab.setBeta(Util.min(ab.beta(), v));

            }

            return v;
        }

    }

    /*
     * effettua un iterazione del gioco scegliendo una mossa attraverso l'algoritmo MiniMax
     */
    public void makeMiniMaxMove(int limit) {
        this.createdNodes = 0;
        this.visitedNodes = 0;
        //applica l'algoritmo minimax e setta il prossimo stato
        getMiniMaxValue(presentState, limit);
        GameState nextState = (GameState) presentState.get("next");
        if (nextState == null) {
            throw new RuntimeException("Mini Max Move failed");
        }
        //effettua la mossa che porta al prossimo stato e aggiorno lo stato corrente
        makeMove(presentState, nextState.get("moveMade"));

    }
    /*
     * effettua un iterazione del gioco scegliendo una mossa attraverso l'algoritmo MiniMax
     * con alphaBeta pruning
     */

    public void makeAlphaBetaMove(int limit) {
        this.createdNodes = 0;
        this.visitedNodes = 0;
        //applica l'algoritmo minimax con alphaBeta pruning e setta il prossimo stato
        getAlphaBetaValue(presentState, limit);

        GameState nextState = (GameState) presentState.get("next");
        if (nextState == null) {
            throw new RuntimeException("Alpha Beta Move failed");
        }
        makeMove(presentState, nextState.get("moveMade"));

    }

    /**
     * @return the visitedNodes
     */
    public long getVisitedNodes() {
        return visitedNodes;
    }

    /**
     * @return the createdNodes
     */
    public long getCreatedNodes() {
        return createdNodes;
    }
}
