package com.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Matrix {
    private int rows;
    private int cols;
    private int[][] data;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new int[rows][cols];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getValue(int row, int col) {
        return data[row][col];
    }

    public void setValue(int row, int col, int value) {
        data[row][col] = value;
    }

    public void loadFromConsole(Scanner scanner, String name) {
        System.out.println("Introduciendo valores para la matriz " + name + " (" + rows + "x" + cols + "):");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print("Elemento [" + i + "][" + j + "]: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Por favor, introduce un número entero.");
                    scanner.next();
                }
                data[i][j] = scanner.nextInt();
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            for (int i = 0; i < rows; i++) {
                String line = reader.readLine();
                if (line == null) throw new IOException("El archivo no tiene suficientes filas.");
                String[] parts = line.trim().split("\\s+");
                if (parts.length < cols) throw new IOException("La fila " + i + " no tiene suficientes columnas.");
                for (int j = 0; j < cols; j++) {
                    data[i][j] = Integer.parseInt(parts[j]);
                }
            }
        }
    }

    public void saveToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    writer.print(data[i][j] + (j == cols - 1 ? "" : " "));
                }
                writer.println();
            }
        }
    }

    public void display() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%4d ", data[i][j]);
            }
            System.out.println();
        }
    }
}
