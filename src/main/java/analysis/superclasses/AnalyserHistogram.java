package analysis.superclasses;

import java.util.ArrayList;
import java.util.List;

public abstract class AnalyserHistogram extends Analyser {

    protected AnalyserHistogram(String trialName, double binSize) {
        super(trialName);
        this.binSize = binSize;
    }

    protected double binSize;

    @Override
    protected double[] computeData(){return null;}

    protected abstract double computeDatum();

    @Override
    public void runAnalyser(int numTrials){

        List<Integer> counts = new ArrayList<>();

        //run the trials
        for(int i = 0; i < numTrials; i++){

            model.initializeRandomBoard();

            double datum = computeDatum();

            //increment the correct bin
            int index = (int) (datum/binSize);
            while(index >= counts.size())
                counts.add(0);

            counts.set(index, counts.get(index)+1);

        }

        //convert to double array
        double[][] data = new double[1][getIVCount()];
        for(int i = 0; i < counts.size(); i++){
            data[0][i] = counts.get(i);
        }
        writeResults("data/" + trialName + ".csv", data);

    }

}
