package utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static utils.Helper.ReadInputFileData;

public class TourData {
    private final int[][] cityMap;
    private final int cityMapSize; // We no longer need this since, but let just keep it for legacy
    private int[] bestTour;
    private int bestDistance;

    public TourData(int[][] cityMap) {
        this.cityMap = cityMap;
        this.cityMapSize = cityMap.length;

        this.bestDistance = Integer.MAX_VALUE;
    }

    public TourData(String inputFilePath) {
        Map<Integer, int[][]> inputValues = ReadInputFileData(inputFilePath);

        this.cityMapSize = inputValues.keySet().iterator().next();
        this.cityMap = inputValues.get(this.cityMapSize);

        this.bestDistance = Integer.MAX_VALUE;
    }

    public synchronized void setBestTour(int[] tour, int distance) {
        /*
        * If the distance is not shorted than the current one, ignore it
        * */
        if(distance < this.bestDistance) {
            this.bestTour = tour.clone();
            this.bestDistance = distance;
        }
    }

    public synchronized Map<Integer, int[]> getBestTour() {
        return Collections.unmodifiableMap(new HashMap<>(Map.of(this.bestDistance, this.bestTour)));
    }

    /* --- Getters and Setters --- */
    public int[][] getCityMap() {
        return cityMap;
    }

    public int getCityMapSize() {
        return this.getCityMap().length;
    }
    /* --------------------------- */
}
