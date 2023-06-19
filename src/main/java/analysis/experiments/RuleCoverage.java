package main.java.analysis.experiments;

import main.java.analysis.superclasses.Analyser;
import main.java.model.model.ClueManager;
import main.java.model.model.Clue;

public class RuleCoverage extends Analyser {


    public RuleCoverage() {
        super("Rule_Coverage");
    }

    @Override
    protected double[] computeData() {
        double[] data = new double[ClueManager.getNormalClues().length];
        for(int i = 0; i < data.length; i++)
        {
            Clue rule = ClueManager.getNormalClues()[i];
            data[i] = (double) model.getPossibleLocations(rule).size() / 108;
        }
        return data;
    }

    @Override
    protected String[] getIVValues() {
        String[] values = new String[ClueManager.getNormalClues().length];
        for(int i = 0; i < values.length; i++){
            values[i] = ClueManager.getNormalClues()[i].toString();
        }
        return values;
    }
}
