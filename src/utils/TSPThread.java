package utils;

import java.util.Random;

import static utils.Helper.*;

public class TSPThread extends Thread {

    private final TourData tourData;
    private final ExecutionSettings settings;

    public TSPThread(TourData tourData, ExecutionSettings settings) {
        this.tourData = tourData;
        this.settings = settings;
    }

    @Override
    public void run() {
        Random rand = new Random();
        int[][] possibleTours = new int[settings.getPopulationSize()][tourData.getCityMapSize()];
        int cityMapSize = tourData.getCityMapSize();

        /* Initialize possible tours with random values */
        for (int i = 0; i < settings.getPopulationSize(); ++i) {
            StartRandomTour(possibleTours[i], cityMapSize);
        }
        /* -------------------------------------------- */

        // Run loop for the entire population AND only for the specified amount of time
        // The reason that we place the time constrain on this for loop (instead of another)
        // is because it will end safely, waiting for the current results being calculated
        long startTime = System.currentTimeMillis();
        for (int i = 0; (i < settings.getPopulationSize()) &&
                (System.currentTimeMillis() - startTime < settings.getMaxExecutionTime()*1000); ++i) {
            // Select three indexes for the crossover process
            int targetIndex, alterIndex1, alterIndex2;
            do {
                targetIndex = rand.nextInt(settings.getPopulationSize());
                alterIndex1 = rand.nextInt(settings.getPopulationSize());
                alterIndex2 = rand.nextInt(settings.getPopulationSize());
            } while (alterIndex1 == targetIndex || alterIndex2 == targetIndex || alterIndex1 == alterIndex2);

            // Create an altered individual by combining the other two into this one
            for (int j = 0; j < cityMapSize; ++j) {
                if (rand.nextDouble() < settings.getCrossoverRate() || j == cityMapSize - 1) {
                    possibleTours[i][j] = (int) (possibleTours[alterIndex1][j] + settings.getDifEvolutionFactor() * (possibleTours[alterIndex2][j] - possibleTours[alterIndex1][j]));
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
                if (rand.nextDouble() < settings.getMutationRate()) {
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
            int mutantDistance = CalcTourDistance(possibleTours[i], tourData.getCityMap(), cityMapSize);

            // Update the distance
            // The class method will check if the value is better or not and update it
            tourData.setBestTour(possibleTours[i], mutantDistance);
        }
    }
}
