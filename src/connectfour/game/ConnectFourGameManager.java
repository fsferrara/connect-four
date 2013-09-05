
package connectfour.game;

import connectfour.game.agent.AlphaBetaAgent;
import connectfour.game.agent.GameAgent;
import connectfour.game.agent.MiniMaxAgent;
import connectfour.game.agent.HumanAgent;
import connectfour.game.util.GameStatistics;
import connectfour.game.util.MoveStatistics;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author Giovanni
 *
 * Gestore del gioco forza quattro
 * Sono gestiti tre tipi di giocatori : Minimax Player, AlphaBeta Player, Human Player
 */

public class ConnectFourGameManager {

    //tipi di giocatori codificati con interi
    public static final int MINIMAX=0;
    public static final int ALPHABETA=1;
    public static final int HUMAN=2;

    //limite di default per le ricerche
    public static final int DEFAULT_LIMIT=5;
    //istanza del gioco
    private ConnectFour game=null;

    private GameStatistics gameStatistics= null;

    //agenti partecipanti al gioco
    private GameAgent firstPlayer=null;
    private GameAgent secondPlayer=null;
    //giocatore di turno
    private boolean firstPlayerTurn=true;

    //inizializzazione del gestore
    public ConnectFourGameManager(){
        this.game=new ConnectFour();
        //di default il primo giocatore gioca con MIniMax ed il secondo con AlphaBeta pruning
        firstPlayer=new MiniMaxAgent(game,DEFAULT_LIMIT);
        secondPlayer=new AlphaBetaAgent(game,DEFAULT_LIMIT);
        //si fissa il primo giocatore
        firstPlayerTurn=true;
        gameStatistics = new GameStatistics();
    }

    //crea un nuovo gioco
    public void newGame() {
        this.game=new ConnectFour();
        //si setta il nuovo gioco nei due giocatori
        firstPlayer.setGame(game);
        secondPlayer.setGame(game);
        //si fissa il primo giocatore
        firstPlayerTurn=true;
        gameStatistics = new GameStatistics();
    }

    //cancella il gioco corrente
    public void destroyGame() {
        this.game=null;
    }
    
    //restituisce le staistiche della partita
    public String getStatistic() {
        return "xxxxxxxx";
    }

    //setta il tipo del primo giocatore
    public boolean setFirstPlayerType(int type) {
        if(type == MINIMAX && !firstPlayer.getClass().equals(MiniMaxAgent.class)){
            firstPlayer=new MiniMaxAgent(game,getFirstPlayerLimit());
            return true;
        }
        if (type == ALPHABETA  && !firstPlayer.getClass().equals(AlphaBetaAgent.class)){
            firstPlayer=new AlphaBetaAgent(game,getFirstPlayerLimit());
            return true;
        }
        else if (type == HUMAN && !firstPlayer.getClass().equals(HumanAgent.class)){
            firstPlayer=new HumanAgent(game,DEFAULT_LIMIT);
            return true;
        }
        return false;
    }

    //restituisce il tipo del primo giocatore
    public int getFirstPlayerType() {
        if(firstPlayer != null){
            if (firstPlayer.getClass().equals(MiniMaxAgent.class))
                return MINIMAX;
            else if (firstPlayer.getClass().equals(AlphaBetaAgent.class))
                return ALPHABETA;
            else if (firstPlayer.getClass().equals(HumanAgent.class))
                return HUMAN;
        }
        return -1;
    }

   //setta il tipo del secondo giocatore
   public boolean setSecondPlayerType(int type) {
        if(type == MINIMAX && !secondPlayer.getClass().equals(MiniMaxAgent.class)){
            secondPlayer=new MiniMaxAgent(game,getSecondPlayerLimit());
            return true;
        }
        if (type == ALPHABETA && !secondPlayer.getClass().equals(AlphaBetaAgent.class)){
            secondPlayer=new AlphaBetaAgent(game,getSecondPlayerLimit());
            return true;
        }
        else if (type == HUMAN && !secondPlayer.getClass().equals(HumanAgent.class)){
            secondPlayer=new HumanAgent(game,DEFAULT_LIMIT);
            return true;
        }
        return false;
    }

   //restituisce il tipo del secondo giocatore
    public int getSecondPlayerType() {
        if(secondPlayer != null){
            if (secondPlayer.getClass().equals(MiniMaxAgent.class))
                return MINIMAX;
            else if (secondPlayer.getClass().equals(AlphaBetaAgent.class))
                return ALPHABETA;
            else if (secondPlayer.getClass().equals(HumanAgent.class))
                return HUMAN;
        }
        return -1;
    }

    //setta il limite di ricerca per il primo giocatore
    public boolean setFirstPlayerLimint(int limit) {
        if(limit > 0 ){
            firstPlayer.setLimit(limit);
            return true;
        } else return false;
    }

    //restituisce il limite di ricerca per il primo giocatore
    public int getFirstPlayerLimit() {
        return firstPlayer.getLimit();
    }

     //setta il limite di ricerca per il primo giocatore
    public boolean setSecondPlayerLimit(int limit) {
        if(limit > 0 ){
            secondPlayer.setLimit(limit);
            return true;
        } else return false;
    }
    //restituisce il limite di ricerca per il secondo giocatore
    public int getSecondPlayerLimit() {
        return secondPlayer.getLimit();
    }

    // effettua una iterazione del gioco facendo giocare il giocatore di turno
    public MoveStatistics performMove() {

        MoveStatistics statistic=null;
        long start = 0;
        long end=0;

       if (game.terminalTest(game.getState())){
           System.out.println("end game");
           ((ConnectFourBoard)game.getState().get("board")).print();
       }

        start= System.currentTimeMillis();

         if(isFirstPlayerTurn()){
             firstPlayerTurn=false;
             firstPlayer.move();
             end=System.currentTimeMillis();
                     statistic=new MoveStatistics(game.getVisitedNodes(),
                                                        game.getCreatedNodes(),
                                                                        end-start);
                     gameStatistics.incrementRedMoves(1);
                     gameStatistics.incrementRedCreatedNodes(game.getCreatedNodes());
                     gameStatistics.incrementRedExaminedNodes(game.getVisitedNodes());
                     gameStatistics.incrementTotalRedTime(end-start);
         } else {
             secondPlayer.move();
             firstPlayerTurn=true;
              end=System.currentTimeMillis();
                     statistic=new MoveStatistics(game.getVisitedNodes(),
                                                        game.getCreatedNodes(),
                                                                        end-start);
            gameStatistics.incrementYellowMoves(1);
                     gameStatistics.incrementYellowCreatedNodes(game.getCreatedNodes());
                     gameStatistics.incrementYellowExaminedNodes(game.getVisitedNodes());
                     gameStatistics.incrementTotalYellowTime(end-start);
         }
         return statistic;////////////////////////////////////////////////////////////////////
    }

    //metodo per il giocatore umano
    //effettua sul gioco la mossa indicata dall'indice di colonna passato in input
    //la mossa e' effettuata dal giocatore di turno

    public MoveStatistics performMove(int column) {

        MoveStatistics statistic=null;

         if (game.terminalTest(game.getState())){
           System.out.println("end game");
           ((ConnectFourBoard)game.getState().get("board")).print();
       }

       //controlla che la mossa sia fattibile
        int [] moves=game.getMoves(game.getState());
        //effettua un iterazione del gioco con la mossa passata
        if (moves[column]!=ConnectFour.NO_MORE){
            statistic=new MoveStatistics(0,0,0);
                if(isFirstPlayerTurn()){
                     firstPlayerTurn=false;
                     firstPlayer.move(column);
                 } else {
                     secondPlayer.move(column);
                     firstPlayerTurn=true;
                 }
        }

        return statistic;
    }

    //restituisce la board del gioco
    public ConnectFourBoard getGameBoard() {
        return game.getBoard(game.getState());
    }

    /**
     * @return the firstPlayerTurn
     */
    public boolean isFirstPlayerTurn() {
        return firstPlayerTurn;
    }

    //verifica se il gioco e' terminato
    public GameStatistics endOfGame() {

        if(game.isEndOfGame()){
            int finalUtility =(Integer)game.getState().get("utility");
                if(finalUtility==ConnectFour.MAX_UTIL)
                    gameStatistics.setWinner("Red");
                else if (finalUtility==ConnectFour.MIN_UTIL)
                    gameStatistics.setWinner("Yellow");
                else gameStatistics.setWinner("Parita'");
                return gameStatistics;
        }else return null;
    }

    public void setBoost(boolean boost) {
        game.setBoost(boost);
    }

}
