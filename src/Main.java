import utils.TSPThread;

import java.util.*;

import static utils.Helper.*;


public class Main {

    private static final int POSSIBLE_TOURS_SIZE = 10;
    private static final int MAX_GENERATIONS = 10; // Number of times the possible tours will be improved
    private static final int THREAD_NUMBER = 5;
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

        int[][] cityMap = LoadTestCities();
        int cityMapSize = cityMap.length;

        int[][] possibleTours = new int[POSSIBLE_TOURS_SIZE][cityMapSize];
        int[] distances = new int[POSSIBLE_TOURS_SIZE];

        // Split the workload among threads
        int generationsPerThread = MAX_GENERATIONS / THREAD_NUMBER;
        List<TSPThread> threads = new ArrayList<>();
        /* ---------------------- */

        /* Initialize possible tours with random values */
        for (int i = 0; i < POSSIBLE_TOURS_SIZE; ++i) {
            StartRandomTour(possibleTours[i], cityMapSize);
            distances[i] = CalcTourDistance(possibleTours[i], cityMap, cityMapSize);
        }
        /* -------------------------------------------- */

        /* --- Start Differential Evolution Algo --- */
        for (int i = 0; i < THREAD_NUMBER; ++i) {
            int startGeneration = i * generationsPerThread;
            int endGeneration = (i + 1) * generationsPerThread;

            TSPThread thread = new TSPThread(startGeneration, endGeneration, possibleTours, distances,
                    cityMap, CROSSOVER_RATE, MUTATION_RATE, DE_FACTOR);
            threads.add(thread);
        }

        for (TSPThread thread : threads) { // Start threads
            thread.start();
        }

        try {
            for (TSPThread thread : threads) { // Wait for threads to finish
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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