package connectfour.game;

//~--- non-JDK imports --------------------------------------------------------

import connectfour.game.util.CircularQueue;
import connectfour.game.util.XYLocation;

//~--- JDK imports ------------------------------------------------------------


/**
 *
 * @author Giovanni
 */
public class ConnectFourBoard {
    public static final int COLUMNS       = 7;
    public static final int LINE_COMPLETE = 4;
    public static final int R             = 1;
    public static final int ROWS          = 8;
    public static final int Y             = -1;
    private int[][]         board         = new int[ROWS][COLUMNS];
    private float           increment     = 999
                                            / ((ROWS - 3) * COLUMNS + (COLUMNS - 3) * ROWS
                                               + 2 * (Math.max(ROWS, COLUMNS) * (ROWS + COLUMNS - 7)));
    private int freeCells = ROWS * COLUMNS;

    public ConnectFourBoard() {
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLUMNS; ++j) {
                board[i][j] = 0;
            }
        }
    }

    public boolean isEmpty(int row, int col) {
        return (board[row][col] == 0);
    }

    public void markR(int row, int col) {
        mark(row, col, R);
    }

    public void markY(int row, int col) {
        mark(row, col, Y);
    }

    public void mark(int row, int col, int symbol) {
        board[row][col] = symbol;
        freeCells--;
    }

    public boolean lineThroughBoard(XYLocation lastMove) {
        int j      = 0;
        int count  = 0;
        int startX = 0;
        int endX   = 0;
        int startY = 0;
        int endY   = 0;

        if (lastMove != null) {
            int row = lastMove.getXCoOrdinate();
            int col = lastMove.getYCoOrdinate();

            // controllo riga
            count  = 1;
            startX = (col >= 3)
                     ? col - 2
                     : 1;
            endX   = (col + 3 <= COLUMNS - 1)
                     ? col + 3
                     : COLUMNS - 1;

            for (j = startX; (j <= endX) && (count < LINE_COMPLETE); ++j) {
                if ((board[row][j] != 0) && (board[row][j] == board[row][j - 1])) {
                    count++;
                } else {
                    count = 1;
                }
            }

            if (count == LINE_COMPLETE) {
                return true;
            }

            // controllo colonna
            count  = 1;
            startY = (row >= 3)
                     ? row - 2
                     : 1;
            endY   = (row + 3 <= ROWS - 1)
                     ? row + 3
                     : ROWS - 1;

            for (j = startY; (j <= endY) && (count < LINE_COMPLETE); ++j) {
                if ((board[j][col] != 0) && (board[j][col] == board[j - 1][col])) {
                    count++;
                } else {
                    count = 1;
                }
            }

            if (count == LINE_COMPLETE) {
                return true;
            }

            // controllo prima diagonale
            j      = 1;
            startX = row;
            startY = col;
            count  = 1;

            while ((startX > 0) && (startY > 0)) {
                startX--;
                startY--;
            }

            while ((startX + j < ROWS) && (startY + j < COLUMNS) && (count < LINE_COMPLETE)) {
                if ((board[startX + j][startY + j] != 0)
                        && (board[startX + j][startY + j] == board[startX + j - 1][startY + j - 1])) {
                    count++;
                } else {
                    count = 1;
                }

                j++;
            }

            if (count == LINE_COMPLETE) {
                return true;
            }

            // controllo seconda diagonale
            j      = 1;
            startX = row;
            startY = col;
            count  = 1;

            while ((startX < ROWS - 1) && (startY > 0)) {
                startX++;
                startY--;
            }

            while ((startX - j >= 0) && (startY + j < COLUMNS) && (count < LINE_COMPLETE)) {
                if ((board[startX - j][startY + j] != 0)
                        && (board[startX - j][startY + j] == board[startX - j + 1][startY + j - 1])) {
                    count++;
                } else {
                    count = 1;
                }

                j++;
            }

            if (count == LINE_COMPLETE) {
                return true;
            }
        }

        return false;
    }

    public int getEuristicValue() {
        int                    retVal = 0;
        int                    row    = 0,
                               col    = 0,
                               j      = 0,
                               diag   = 0;
        CircularQueue rQueue = new CircularQueue(LINE_COMPLETE);
        CircularQueue yQueue = new CircularQueue(LINE_COMPLETE);

        // euristica su righe
        for (row = 0; row < ROWS; ++row) {
            for (col = 0; col < COLUMNS; ++col) {
                retVal += (increment * checkPin(rQueue, yQueue, board[row][col]));
            }

            // svuotamento code
            rQueue.clear();
            yQueue.clear();
        }

        // euristica su colonne
        for (col = 0; col < COLUMNS; ++col) {
            for (row = 0; row < ROWS; ++row) {
                retVal += (increment * checkPin(rQueue, yQueue, board[row][col]));
            }

            // svuotamento code
            rQueue.clear();
            yQueue.clear();
        }

        // euristica su diagonali nw->se
        for (diag = ROWS - 1; diag > 0; diag--) {
            row = diag;
            col = 0;

            do {
                retVal += (increment * checkPin(rQueue, yQueue, board[row][col]));
                row++;
                col++;
            } while ((row < ROWS) && (col < COLUMNS));

            // svuotamento code
            rQueue.clear();
            yQueue.clear();
        }

        for (diag = 0; diag < COLUMNS; diag++) {
            row = 0;
            col = diag;

            do {
                retVal += (increment * checkPin(rQueue, yQueue, board[row][col]));
                row++;
                col++;
            } while ((row < ROWS) && (col < COLUMNS));

            // svuotamento code
            rQueue.clear();
            yQueue.clear();
        }

        // euristica su diagonali ne->so
        for (diag = 0; diag < ROWS; diag++) {
            row = diag;
            col = 0;

            do {
                retVal += (increment * checkPin(rQueue, yQueue, board[row][col]));
                row--;
                col++;
            } while ((row > -1) && (col < COLUMNS));

            // svuotamento code
            rQueue.clear();
            yQueue.clear();
        }

        for (diag = 0; diag < COLUMNS; diag++) {
            row = ROWS - 1;
            col = diag;

            do {
                retVal += (increment * checkPin(rQueue, yQueue, board[row][col]));
                row--;
                col++;
            } while ((row > -1) && (col < COLUMNS));

            // svuotamento code
            rQueue.clear();
            yQueue.clear();
        }

        return retVal;
    }

    private float checkPin(CircularQueue rQueue, CircularQueue yQueue, int pin) {
        float weigth = 0;

        switch (pin) {
        case (R) :
            rQueue.add(pin);
            yQueue.clear();
            weigth = checkQueue(rQueue);

            break;

        case (Y) :
            yQueue.add(pin);
            rQueue.clear();
            weigth = -1 * checkQueue(yQueue);

            break;

        default :
            rQueue.add(pin);
            yQueue.add(pin);
            weigth = checkQueue(rQueue) + (-1 * checkQueue(yQueue));

            break;
        }

        return weigth;
    }

    private float checkQueue(CircularQueue queue) {
        float weigth = 0;
        int   count  = 0;

        if (queue.size() >= LINE_COMPLETE) {
            count = Math.abs(queue.get(0) + queue.get(1) + queue.get(2) + queue.get(3));

            if (count == 3) {
                weigth = 3;
            } else if (count == 2) {
                weigth = (float) 0.3;
            }

            queue.remove();
        }

        return weigth;
    }

    public int getValue(int row, int col) {
        return board[row][col];
    }

    public void print() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                int    value = getValue(i, j);
                String printValue;

                if (value == 0) {
                    printValue = "-";
                } else {
                    printValue = (value == R)
                                 ? "R"
                                 : "Y";
                }

                System.out.print(printValue + " ");
            }

            System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int    value = getValue(i, j);
                String printValue;

                if (value == 0) {
                    printValue = "-";
                } else {
                    printValue = (value == R)
                                 ? "R"
                                 : "Y";
                }

                buf.append(printValue + " ");
            }

            buf.append("\n");
        }

        return buf.toString();
    }

    public ConnectFourBoard cloneBoard() {
        return (ConnectFourBoard) clone();
    }

    @Override
    public Object clone() {
        ConnectFourBoard newBoard = new ConnectFourBoard();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                int s = getValue(i, j);

                newBoard.board[i][j] = s;
            }
        }

        newBoard.freeCells = this.freeCells;

        return newBoard;
    }

    public boolean isMarked(int val, int i, int j) {
        return board[i][j] == val;
    }

    public boolean isFull() {
        return freeCells == 0;
    }
}
