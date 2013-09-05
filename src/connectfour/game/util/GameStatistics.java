/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package connectfour.game.util;

/**
 *
 * @author Giovanni
 */
public class GameStatistics {
    private String winner=null;
    private long redMoves=0;
    private long totalRedTime=0;
    private long redExaminedNodes=0;
    private long redCreatedNodes = 0;
    private long yellowExaminedNodes = 0;
    private long yellowCreatedNodes = 0;
    private long yellowMoves=0;
    private long totalYellowTime=0;


    public long getRedTimeAverage (){
        if (redMoves != 0) {
            return totalRedTime/redMoves;
        }

        return 0;
    }

     public long getYellowTimeAverage (){
         if (yellowMoves != 0) {
            return totalYellowTime/yellowMoves;
         }

         return 0;
    }
   
    /**
     * @return the redMoves
     */
    public long getRedMoves() {
        return redMoves;
    }

    /**
     * @param redMoves the redMoves to increment
     */
    public void incrementRedMoves(long redMoves) {
        this.redMoves += redMoves;
    }

    /**
     * @return the totalRedTime
     */
    public long getTotalRedTime() {
        return totalRedTime;
    }

    /**
     * @param totalRedTime the totalRedTime to increment
     */
    public void incrementTotalRedTime(long totalRedTime) {
        this.totalRedTime += totalRedTime;
    }

    /**
     * @return the yellowMoves
     */
    public long getYellowMoves() {
        return yellowMoves;
    }

    /**
     * @param yellowMoves the yellowMoves to increment
     */
    public void incrementYellowMoves(long yellowMoves) {
        this.yellowMoves += yellowMoves;
    }

    /**
     * @return the totalYellowTime
     */
    public long getTotalYellowTime() {
        return totalYellowTime;
    }

    /**
     * @param totalYellowTime the totalYellowTime to increment
     */
    public void incrementTotalYellowTime(long totalYellowTime) {
        this.totalYellowTime += totalYellowTime;
    }

    /**
     * @return the redExaminedNodes
     */
    public long getRedExaminedNodes() {
        return redExaminedNodes;
    }

    /**
     * @param redExaminedNodes the redExaminedNodes to increment
     */
    public void incrementRedExaminedNodes(long redExaminedNodes) {
        this.redExaminedNodes += redExaminedNodes;
    }

    /**
     * @return the redCreatedNodes
     */
    public long getRedCreatedNodes() {
        return redCreatedNodes;
    }

    /**
     * @param redCreatedNodes the redCreatedNodes to increment
     */
    public void incrementRedCreatedNodes(long redCreatedNodes) {
        this.redCreatedNodes += redCreatedNodes;
    }

    /**
     * @return the yellowExaminedNodes
     */
    public long getYellowExaminedNodes() {
        return yellowExaminedNodes;
    }

    /**
     * @param yellowExaminedNodes the yellowExaminedNodes to increment
     */
    public void incrementYellowExaminedNodes(long yellowExaminedNodes) {
        this.yellowExaminedNodes += yellowExaminedNodes;
    }

    /**
     * @return the yellowCreatedNodes
     */
    public long getYellowCreatedNodes() {
        return yellowCreatedNodes;
    }

    /**
     * @param yellowCreatedNodes the yellowCreatedNodes to increment
     */
    public void incrementYellowCreatedNodes(long yellowCreatedNodes) {
        this.yellowCreatedNodes += yellowCreatedNodes;
    }

    /**
     * @return the winner
     */
    public String getWinner() {
        return winner;
    }

    /**
     * @param winner the winner to set
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return " \n\n  " + winner.toUpperCase() + " WINS!!!\n\n" +
                "Red Player Statistics: \n" +
                "\t Time Average = " + getRedTimeAverage() + " ms\n" +
                "\t Total Red Time = " + totalRedTime + " ms\n" +
                "\t Examined Nodes = " + redExaminedNodes + " \n" +
                "\t Created Nodes = " + redCreatedNodes + " \n" +
                "Yellow Player Statistics: \n" +
                "\t Time Average = " + getYellowTimeAverage() + " ms\n" +
                "\t Total Red Time = " + totalYellowTime + " ms\n" +
                "\t Examined Nodes = " + yellowExaminedNodes + " \n" +
                "\t Created Nodes = " + yellowCreatedNodes + " \n";
    }
}
