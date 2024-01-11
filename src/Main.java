import utils.ExecutionSettings;
import utils.TSPThread;
import utils.TourData;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        long begin = System.currentTimeMillis(); // Start log

        /* --- Init Program Data --- */
        /* Uncomment this and comment the rest of the section for debugging

        ExecutionSettings settings = new ExecutionSettings(1, 50, 10,
                0.2, 0.7, 0.5);
        TourData tourData = new TourData(LoadTestCities());
        //*/

        ExecutionSettings settings = new ExecutionSettings();
        settings.loadFromArgs(args);

        TourData tourData = new TourData(settings.getInputFilePath());
        /* ---------------------- */

        /* --- Start Differential Evolution Algo --- */
        List<TSPThread> threads = new ArrayList<>();
        for (int i = 0; i < settings.getThreadCount(); ++i)
            threads.add(new TSPThread(tourData, settings));

        for (TSPThread thread : threads) thread.start(); // Start threads

        try {
            for (TSPThread thread : threads) thread.join(); // Wait for threads to finish
        } catch (InterruptedException e) {
            // If interrupted, just ignore since the results are
            // stored in the TourData class object
        }
        /* ----------------------------------------- */

        /* --- Show the best tour --- */
        Map<Integer, int[]> bestTour = tourData.getBestTour();
        int bestDist = bestTour.keySet().iterator().next();

        System.out.print("Best tour: ");
        for (int i = 0; i < tourData.getCityMapSize(); ++i) {
            System.out.print(bestTour.get(bestDist)[i] + 1 + " ");
        }
        System.out.println();
        System.out.println("Distance: " + bestDist);
        /* --------------------------------------------- */

        // End log timer
        long end = System.currentTimeMillis();
        double timeSpent = (end - begin) / 1000.0;
        System.out.println("\nProgram run in " + timeSpent + " seconds.");
    }
}