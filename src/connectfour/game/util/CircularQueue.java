package connectfour.game.util;

public class CircularQueue {

    /** Minimal size fo the underlying attay */
    private static final int DEFAULT_CAPACITY = 4;
    private int head;
    private int tail;
    private int size;
    private int capacity;
    private int[] queue;

    /**
     * Construct a new, empty queue.
     */
    public CircularQueue() {
        this(DEFAULT_CAPACITY);
    }

    public CircularQueue(int initialCapacity) {
        capacity = initialCapacity;
        queue = new int[capacity];
        tail = 0;
        head = 0;
        size = 0;
    }

    /**
     * Returns the capacity of this queue.
     */
    public int getCapacity() {
        return capacity;
    }

    public void clear() {
        tail = 0;
        head = 0;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public boolean add(int value) {

        queue[head] = value;
        head = (head + 1) % capacity;

        if (size < capacity) {
            size++;
        }
        else {
            tail = (tail + 1) % capacity;
        }

        return true;
    }

    public int remove() {
        int retValue = 0;

        if (size > 0) {
            retValue = queue[tail];
            tail = (tail + 1) % capacity;
            size--;
        }

        return retValue;
    }

    public int get(int idx) {
        if (idx<size) {
            return queue[(tail+idx) % getCapacity()];
        }

        return Integer.MIN_VALUE;
    }

    static public void main(String[] argv) {
        
        int n = 10;
        CircularQueue q = new CircularQueue(4);

        for (int i = 0; i < n; i++) {
            q.add(i);
        }

        for (int j = 0; j < q.getCapacity(); j++) {
            for (int i = 0; i < q.size(); i++) {
//                System.out.println("head="+ head + " tail="+tail+ " q[" + i + "] = " + q.get(i));
            }

            System.out.println("\nDECREMENTO\n");
            q.remove();

            for (int i = 0; i < q.size(); i++) {
 //               System.out.println("head="+ head + " tail="+tail+ " q[" + i + "] = " + q.get(i));
            }

            System.out.println("\nDECREMENTO\n");
            q.remove();

            for (int i = 0; i < q.size(); i++) {
 //               System.out.println("head="+ head + " tail="+tail+ " q[" + i + "] = " + q.get(i));
            }

            q.add(69);
            System.out.println("\n69\n");
            
        }
    }

}
