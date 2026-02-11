package com.matrix;

public class MultiplierThread extends Thread {
    private Matrix m1;
    private Matrix m2;
    private int row;
    private int col;
    private int result;

    public MultiplierThread(Matrix m1, Matrix m2, int row, int col) {
        this.m1 = m1;
        this.m2 = m2;
        this.row = row;
        this.col = col;
    }

    @Override
    public void run() {
        result = 0;
        // The number of columns of m1 is equal to the number of rows of m2
        for (int k = 0; k < m1.getCols(); k++) {
            result += m1.getValue(row, k) * m2.getValue(k, col);
        }
        System.out.println("Hilo [" + row + "][" + col + "] termin. Resultado: " + result);
    }

    public int getResult() {
        return result;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
