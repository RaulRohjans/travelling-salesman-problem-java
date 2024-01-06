import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import static utils.Helper.*;


public class Main {

    private static final int POSSIBLE_TOURS_SIZE = 10;
    private static final int MAX_GENERATIONS = 10; // Number of times the possible tours will be improved
    private static final double CROSSOVER_RATE = 0.7;
    private static final double MUTATION_RATE = 0.2;
    private static final double DE_FACTOR = 0.5;

    public static void main(String[] args) {
        // Start log
        long begin = System.currentTimeMillis();

        /* --- Init Program Data --- */
        /*if(args.length < 5) {
            System.out.println("Please make sure all arguments are present!\n");
            System.out.println("java -jar tsp.jar <inputFile> <threadsNumber> <maxExecutionTime> " +
                    "<populationSize> <mutationRate>");
            return;
        }

        String inputFilePath = args[0];
        int threadsNumber = Integer.parseInt(args[1]);
        int maxExecTime = Integer.parseInt(args[2]);
        int populationSize = Integer.parseInt(args[3]);
        double MUTATION_RATE = Double.parseDouble(args[4]);

        Map<Integer, int[][]> inputValues = ReadInputFileData(inputFilePath);
        int cityMapSize = inputValues.entrySet().stream().findFirst().get().getKey();
        int[][] cityMap = inputValues.entrySet().stream().findFirst().get().getValue();*/
        int cityMapSize = 5;
        int[][] cityMap = LoadTestCities();

        int[][] possibleTours = new int[POSSIBLE_TOURS_SIZE][cityMapSize];
        int[] distances = new int[POSSIBLE_TOURS_SIZE];
        Arrays.fill(distances, Integer.MAX_VALUE);

        Random rand = new Random();
        /* ---------------------- */

        /* Initialize possible tours with random values */
        for (int i = 0; i < POSSIBLE_TOURS_SIZE; ++i) {
            StartRandomTour(possibleTours[i], cityMapSize);
            distances[i] = CalcTourDistance(possibleTours[i], cityMap, cityMapSize);
        }
        /* -------------------------------------------- */

        /* --- Start Differential Evolution Algo --- */
        for (int generation = 0; generation < MAX_GENERATIONS; ++generation) {
            for (int i = 0; i < POSSIBLE_TOURS_SIZE; ++i) {
                // Select three indexes for the crossover process
                int targetIndex, alterIndex1, alterIndex2;
                do {
                    targetIndex = rand.nextInt(POSSIBLE_TOURS_SIZE);
                    alterIndex1 = rand.nextInt(POSSIBLE_TOURS_SIZE);
                    alterIndex2 = rand.nextInt(POSSIBLE_TOURS_SIZE);
                } while (alterIndex1 == targetIndex || alterIndex2 == targetIndex || alterIndex1 == alterIndex2);

                // Create an altered individual by combining the other two into this one
                for (int j = 0; j < cityMapSize; ++j) {
                    if (rand.nextDouble() < CROSSOVER_RATE || j == cityMapSize - 1) {
                        possibleTours[i][j] = (int) (possibleTours[alterIndex1][j] + DE_FACTOR * (possibleTours[alterIndex2][j] - possibleTours[alterIndex1][j]));
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
                    if (rand.nextDouble() < MUTATION_RATE) {
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
                }
            }
        }
        /* ----------------------------------------- */

        /* --- Get the best tour & show the results --- */
        int bestIndex = 0; // Find the best tour
        for (int i = 1; i < POSSIBLE_TOURS_SIZE; ++i) {
            if (distances[i] < distances[bestIndex]) {
                bestIndex = i;
            }
        }

        // Show it
        System.out.print("Best tour: ");
        for (int i = 0; i < cityMapSize; ++i) {
            System.out.print(possibleTours[bestIndex][i] + 1 + " ");
        }
        System.out.println();
        System.out.println("Distance: " + distances[bestIndex]);
        /* --------------------------------------------- */

        // End log timer
        long end = System.currentTimeMillis();
        double timeSpent = (end - begin) / 1000.0;
        System.out.println("\nProgram run in " + timeSpent + " seconds.");
    }
}