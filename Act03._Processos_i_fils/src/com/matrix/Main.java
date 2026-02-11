package com.matrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce los tamaños de las matrices (ancho1 alto1 ancho2 alto2), separados por espacios:");
        int w1, h1, w2, h2;
        w1 = h1 = w2 = h2 = 0;
        boolean valid = false;
        while (!valid) {
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine();
            String[] parts = line.trim().split("\\s+");
            if (parts.length != 4) {
                System.out.println("Error: Debes introducir exactamente 4 números separados por espacios. Intenta de nuevo:");
                continue;
            }
            try {
                w1 = Integer.parseInt(parts[0]);
                h1 = Integer.parseInt(parts[1]);
                w2 = Integer.parseInt(parts[2]);
                h2 = Integer.parseInt(parts[3]);
            } catch (NumberFormatException e) {
                System.out.println("Error: Todos los valores deben ser números enteros. Intenta de nuevo:");
                continue;
            }
            if (w1 < 1 || w1 > 20 || h1 < 1 || h1 > 20 || w2 < 1 || w2 > 20 || h2 < 1 || h2 > 20) {
                System.out.println("Error: Los tamaños de las matrices deben estar entre 1 y 20. Intenta de nuevo:");
                continue;
            }
            if (w1 != h2) {
                System.out.println("Error: El número de columnas de la primera matriz (" + w1 + ") debe ser igual al número de filas de la segunda (" + h2 + "). Intenta de nuevo:");
                continue;
            }
            valid = true;
        }

        boolean continueMultiplication = true;
        while (continueMultiplication) {
            Matrix m1 = new Matrix(h1, w1);
            Matrix m2 = new Matrix(h2, w2);
            Matrix resultMatrix = new Matrix(h1, w2);

            fillMatrix(scanner, m1, "Matriz 1");
            fillMatrix(scanner, m2, "Matriz 2");

            System.out.println("Calculando matriz resultante...");
            List<MultiplierThread> threads = new ArrayList<>();
            for (int i = 0; i < h1; i++) {
                for (int j = 0; j < w2; j++) {
                    MultiplierThread t = new MultiplierThread(m1, m2, i, j);
                    threads.add(t);
                    t.start();
                }
            }

            for (MultiplierThread t : threads) {
                try {
                    t.join();
                    resultMatrix.setValue(t.getRow(), t.getCol(), t.getResult());
                } catch (InterruptedException e) {
                    System.err.println("Error esperando al hilo: " + e.getMessage());
                }
            }

            System.out.println("\nMatriz Resultante:");
            resultMatrix.display();

            System.out.print("\n¿Deseas guardar la matriz resultante en un archivo? (s/n): ");
            String saveOption = scanner.next();
            if (saveOption.equalsIgnoreCase("s")) {
                System.out.print("Introduce el nombre del archivo: ");
                String filename = scanner.next();
                try {
                    resultMatrix.saveToFile(filename);
                    System.out.println("Matriz guardada con éxito.");
                } catch (IOException e) {
                    System.err.println("Error al guardar el archivo: " + e.getMessage());
                }
            }

            System.out.print("\n¿Deseas realizar otra multiplicación con los mismos tamaños? (s/n): ");
            String again = scanner.next();
            continueMultiplication = again.equalsIgnoreCase("s");
            if (continueMultiplication) {
                scanner.nextLine(); // Consumir nueva línea restante
            }
        }

        System.out.println("Programa finalizado.");
        scanner.close();
    }

    private static void fillMatrix(Scanner scanner, Matrix m, String name) {
        System.out.println("\n--- Configuración de " + name + " ---");
        System.out.println("1. Introducir valores por consola");
        System.out.println("2. Leer valores desde archivo");
        System.out.print("Elige una opción: ");
        
        int option = 0;
        while (option != 1 && option != 2) {
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
            } else {
                scanner.next();
            }
            if (option != 1 && option != 2) {
                System.out.print("Opción no válida. Elige 1 o 2: ");
            }
        }

        if (option == 1) {
            m.loadFromConsole(scanner, name);
        } else {
            System.out.print("Introduce el nombre del archivo para " + name + ": ");
            String filename = scanner.next();
            try {
                m.loadFromFile(filename);
            } catch (IOException e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
                System.out.println("Se procederá a la introducción manual por consola.");
                m.loadFromConsole(scanner, name);
            }
        }
    }
}
