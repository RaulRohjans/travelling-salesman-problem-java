package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Helper {
    public static void StartRandomTour(int[] tour, int maxCitySize) {
        Random rand = new Random();

        // Fill the array with sequential values
        for (int i = 0; i < maxCitySize; ++i) {
            tour[i] = i;
        }

        // Shuffle the array values
        for (int i = maxCitySize - 1; i > 0; --i) {
            int j = rand.nextInt(i + 1);
            int temp = tour[i];
            tour[i] = tour[j];
            tour[j] = temp;
        }
    }

    public static int CalcTourDistance(int[] tour, int[][] graph, int maxCitySize) {
        int distance = 0;
        for (int i = 0; i < maxCitySize - 1; ++i) {
            distance += graph[tour[i]][tour[i + 1]];
        }
        distance += graph[tour[maxCitySize - 1]][tour[0]]; // Return to starting city
        return distance;
    }

    public static int GenerateNewUniqueCity(int[] currentPossibleTours, int maxCitySize, int currentCityIndex) {
        Random rand = new Random();

        int newCity;
        boolean cityAlreadyUsed;

        do {
            // Generate a new candidate city
            newCity = rand.nextInt(maxCitySize);

            // Check if the candidate city has already been used in the current tour
            cityAlreadyUsed = false;
            for (int k = 0; k < currentCityIndex; ++k) {
                if (newCity == currentPossibleTours[k]) {
                    cityAlreadyUsed = true;
                    break;
                }
            }
        } while (cityAlreadyUsed);

        return newCity;
    }

    public static int[][] LoadTestCities() {
        return new int[][] {
                {0, 23, 10, 4, 1},
                {23, 0, 9, 5, 4},
                {10, 9, 0, 8, 2},
                {4, 5, 8, 0, 11},
                {1, 4, 2, 11, 0}
        };
    }

    public static Map<Integer, int[][]> ReadInputFileData(String path) {
        Map<Integer, int[][]> returnValues = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int cityCount = Integer.parseInt(br.readLine()); // Get city count

            int[][] matrix = new int[cityCount][cityCount];
            for (int i = 0; i < cityCount; i++) {
                String[] values = br.readLine().split(" ");
                int matrixCounter = 0;
                for (int j = 0; matrixCounter < cityCount; j++) {
                    if(!values[j].isEmpty()) {
                        matrix[i][matrixCounter] = Integer.parseInt(values[j]);
                        matrixCounter++;
                    }
                }
            }

            // This is used as a way to return multiple values
            returnValues.put(cityCount, matrix);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the specified file.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("An error has occurred reading the file: " + e.getMessage());
            System.exit(0);
        }
        return returnValues;
    }
}
