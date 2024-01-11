package utils;

public class ExecutionSettings {
    private double maxExecTime;
    private int populationSize;
    private int threadCount;
    private String inputFilePath;
    private double crossoverRate;
    private double mutationRate;
    private double deFactor;

    public ExecutionSettings(int threadCount, double maxExecTime, int populationSize, double mutationRate,
                             double crossoverRate, double deFactor) {
        this.maxExecTime = maxExecTime;
        this.populationSize = populationSize;
        this.threadCount = threadCount;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.deFactor = deFactor;
    }

    public ExecutionSettings() {

    }

    public void loadFromArgs(String[] args) {
        if(args.length < 5) {
            System.out.println("Please make sure all arguments are present!\n");
            System.out.println("java -jar tsp.jar <inputFile> <threadCount> <maxExecutionTime> " +
                    "<populationSize> <mutationRate> <crossoverRate (OPTIONAL)> <difEvolutionFactor (OPTIONAL)");
            System.exit(0);
        }

        if(args[0].isEmpty()) {
            System.out.println("Please insert a valid path for the input file!");
            System.exit(0);
        }
        this.inputFilePath = args[0];


        this.threadCount = Integer.parseInt(args[1]);
        if(this.threadCount < 1) { // Doesn't matter if we validate after, the exec is stopped
            System.out.println("Please insert a valid thread count!");
            System.exit(0);
        }

        this.maxExecTime = Integer.parseInt(args[2]);
        if(this.maxExecTime < 1) {
            System.out.println("Please insert a valid thread execution time!");
            System.exit(0);
        }

        this.populationSize = Integer.parseInt(args[3]);
        if(this.populationSize < 1) {
            System.out.println("Please insert a valid population size!");
            System.exit(0);
        }

        this.mutationRate = Double.parseDouble(args[4]);

        if(args.length > 5) this.crossoverRate = Double.parseDouble(args[5]);
        else this.crossoverRate = 0.7;

        if(args.length > 6) this.deFactor = Double.parseDouble(args[6]);
        else this.deFactor = 0.5;

        if(this.mutationRate < 0 || this.mutationRate > 1 ||
                this.crossoverRate < 0 || this.crossoverRate > 1 ||
                this.deFactor < 0 || this.deFactor > 1) {
            System.out.println("All the application rates should be a value between 0 and 1!");
            System.exit(0);
        }
    }

    /*
    * The max amount of time a thread can run for
    * */
    public double getMaxExecutionTime() {
        return this.maxExecTime;
    }

    /*
    * Number of possible tours in each thread
    * Essentially this is the max amount of candidates in a thread
    * */
    public int getPopulationSize() {
        return this.populationSize;
    }

    /*
    * Number of threads that can run simultaneously
    * */
    public int getThreadCount() {
        return this.threadCount;
    }

    /*
    * Gets the path for the input file with the cities data
    * */
    public String getInputFilePath() {
        return this.inputFilePath;
    }

    /*
    * The chance at which crossover happens (between 0 and 1)
    * */
    public double getCrossoverRate() {
        return this.crossoverRate;
    }

    /*
    * The chance at which mutation happens (between 0 and 1)
    * */
    public double getMutationRate() {
        return this.mutationRate;
    }

    /*
    * Differential Evolution Factor, AKA DE Factor
    * This is the change at which the differential evolution happens
    * (value between 0 and 1)
    * */
    public double getDifEvolutionFactor() {
        return this.deFactor;
    }
}
