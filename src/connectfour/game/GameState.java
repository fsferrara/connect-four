package connectfour.game;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Ravi Mohan
 * 
 */

/* struttura dati per la memorizzazione di un generico stato
 * gli elementi dello stato sono memorizzati nella forma nome=valore
 * in una hashtable
 */
public class GameState {

    public static final int MAX_CHILDS = 7;
    private Hashtable<String, Object> state;
    private GameState[] childs = null;

    public GameState() {
        state = new Hashtable<String, Object>();
    }

    @Override
    public boolean equals(Object anotherState) {

        if (this == anotherState) {
            return true;
        }
        if ((anotherState == null)
                || (this.getClass() != anotherState.getClass())) {
            return false;
        }
        GameState another = (GameState) anotherState;
        Set keySet1 = state.keySet();
        Iterator i = keySet1.iterator();
        Iterator j = another.state.keySet().iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            boolean keymatched = false;
            boolean valueMatched = false;
            while (j.hasNext()) {
                String key2 = (String) j.next();
                if (key.equals(key2)) {
                    keymatched = true;
                    if (state.get(key).equals(another.state.get(key2))) {
                        valueMatched = true;
                    }
                    break;
                }
            }
            if (!((keymatched) && valueMatched)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        for (String s : state.keySet()) {
            result = 37 * result + s.hashCode();
            result = 37 * result + state.get(s).hashCode();
        }

        return result;
    }

    public Object get(String key) {
        return state.get(key);
    }

    public void put(String key, Object value) {
        state.put(key, value);

    }

    /**
     * @return the childs
     */
    public GameState[] getChilds() {
        return childs;
    }

    /**
     * @param childs the childs to set
     */
    public void setChilds(GameState[] childs) {
        this.childs = childs;
    }

    public void addChild(GameState childState, int pos) {
        if (childs == null) {
            childs = new GameState[MAX_CHILDS];
        }
        if (pos > 0 && pos < childs.length) {
            childs[pos] = childState;
        }
    }

    public GameState getChild(int pos) {
        if (childs != null) {
            return childs[pos];
        }
        return null;
    }
}
