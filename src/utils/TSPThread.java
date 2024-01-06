package utils;

import java.util.Random;

import static utils.Helper.CalcTourDistance;
import static utils.Helper.GenerateNewUniqueCity;

public class TSPThread extends Thread {
    private int[][] cityMap;
    private int cityMapSize;
    private int[][] possibleTours;
    private int[][] bestTours;
    private int[] distances;
    private int startGeneration;
    private int endGeneration;

    private double crossoverRate;
    private double mutationRate;
    private double deFactor;


    public TSPThread(int startGeneration, int endGeneration, int[][] possibleTours, int[] distances,
                     int[][] cityMap, double crossoverRate, double mutationRate, double deFactor) {
        this.startGeneration = startGeneration;
        this.endGeneration = endGeneration;
        this.possibleTours = possibleTours.clone();
        this.bestTours = possibleTours;
        this.distances = distances;
        this.cityMap = cityMap;
        this.cityMapSize = cityMap.length;

        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.deFactor = deFactor;
    }

    @Override
    public void run() {
        Random rand = new Random();
        int possibleTourSize = possibleTours.length;

        for (int generation = startGeneration; generation < endGeneration; ++generation) {
            for (int i = 0; i < possibleTourSize; ++i) {
                // Select three indexes for the crossover process
                int targetIndex, alterIndex1, alterIndex2;
                do {
                    targetIndex = rand.nextInt(possibleTourSize);
                    alterIndex1 = rand.nextInt(possibleTourSize);
                    alterIndex2 = rand.nextInt(possibleTourSize);
                } while (alterIndex1 == targetIndex || alterIndex2 == targetIndex || alterIndex1 == alterIndex2);

                // Create an altered individual by combining the other two into this one
                for (int j = 0; j < cityMapSize; ++j) {
                    if (rand.nextDouble() < crossoverRate || j == cityMapSize - 1) {
                        possibleTours[i][j] = (int) (possibleTours[alterIndex1][j] + deFactor * (possibleTours[alterIndex2][j] - possibleTours[alterIndex1][j]));
                    }

                    // Repair process
                    for (int k = 0; k < j; ++k) {
                        if (possibleTours[i][j] == possibleTours[i][k]) {
                            possibleTours[i][j] = GenerateNewUniqueCity(possibleTours[i], cityMapSize, j);
                            k = -1;
                        }
                    }
                }

                // Apply the altering of the values
                for (int j = 0; j < cityMapSize; ++j) {
                    /*
                     * Use a similar logic to the crossover process where we generate a random number between 0 and 1.
                     * If this is lower than the altering rate (mutation rate), than we apply it to the city.
                     * */
                    if (rand.nextDouble() < mutationRate) {
                        int mutationPoint = rand.nextInt(cityMapSize); //Select another city in the tour (random) to swap with current
                        int temp = possibleTours[i][j];
                        possibleTours[i][j] = possibleTours[i][mutationPoint]; //Alter the tour with, perform swap
                        possibleTours[i][mutationPoint] = temp; //Assign the current city to the mutation point
                    }
                }

                // Enforce value check
                for (int j = 0; j < cityMapSize; ++j) {
                    if (possibleTours[i][j] < 0) {
                        possibleTours[i][j] = 0;
                    } else if (possibleTours[i][j] >= cityMapSize) {
                        possibleTours[i][j] = cityMapSize - 1;
                    }
                }

                // Calculate Distance
                int mutantDistance = CalcTourDistance(possibleTours[i], cityMap, cityMapSize);

                // Update the distance if the altered is better
                if (mutantDistance < distances[i]) {
                    distances[i] = mutantDistance;
                    bestTours[i] = possibleTours[i].clone();
                }
            }
        }
    }

}
