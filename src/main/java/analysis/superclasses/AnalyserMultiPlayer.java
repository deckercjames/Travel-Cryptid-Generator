package main.java.analysis.superclasses;

public abstract class AnalyserMultiPlayer extends Analyser {

    protected AnalyserMultiPlayer(String trialName) {
        super(trialName);
    }

    @Override
    protected double[] computeData(){return null;}

    protected abstract double[] computeData(int numPlayers);

    @Override
    public void runAnalyser(int numTrials){

        double[][][] allData = new double[6][numTrials][getIVCount()];


        for(int i = 0; i < numTrials; i++){

            System.out.println("\nTRIAL " + (i+1) + " / " + numTrials);

            model.initializeRandomBoard();

            System.out.println("Gathering Data...");

            for(int k = 3; k <= 5; k++) {
                System.out.print("  "+k+" players...");
                allData[k][i] = computeData(k);
                System.out.println("Complete");
            }

        }

        for(int k = 3; k <= 5; k++) {
            writeResults("data/" + trialName + "/" + k + ".csv", allData[k]);
        }

    }

}
