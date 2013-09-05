/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package connectfour.game.util;

/**
 *
 * @author Giovanni
 */
public class MoveStatistics {
    private long examinedNodes=0;
    private long createdNodes = 0;
    private long timeToMove = 0;


    public MoveStatistics (long visited,long created,long time){
        examinedNodes=visited;
        createdNodes=created;
        timeToMove=time;
    }

    /**
     * @return the examinedNodes
     */
    public long getExaminedNodes() {
        return examinedNodes;
    }

    /**
     * @param examinedNodes the examinedNodes to set
     */
    public void setExaminedNodes(long examinedNodes) {
        this.examinedNodes = examinedNodes;
    }

    /**
     * @return the createdNodes
     */
    public long getCreatedNodes() {
        return createdNodes;
    }

    /**
     * @param createdNodes the createdNodes to set
     */
    public void setCreatedNodes(long createdNodes) {
        this.createdNodes = createdNodes;
    }

    /**
     * @return the timeToMove
     */
    public long getTimeToMove() {
        return timeToMove;
    }

    /**
     * @param timeToMove the timeToMove to set
     */
    public void setTimeToMove(long timeToMove) {
        this.timeToMove = timeToMove;
    }

    @Override
    public String toString() {
        return "Esaminati " + examinedNodes +
                " nodi (di cui " + createdNodes +
                " creati) in " + timeToMove +
                " millisecondi";
    }

}
