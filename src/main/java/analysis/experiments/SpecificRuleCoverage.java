package main.java.analysis.experiments;

import main.java.analysis.superclasses.AnalyserHistogram;
import main.java.model.game.board.Board;
import main.java.model.model.RuleManager;
import main.java.model.model.Rule;

import java.util.Arrays;

public class SpecificRuleCoverage extends AnalyserHistogram {

    public SpecificRuleCoverage(double binSize, int ruleIndex) {
        super(RuleManager.getNormalRules()[ruleIndex].toString(), binSize);
        System.out.println("bin size: "+binSize);
        this.binCount = (int) (1.0/binSize) + 1;
        this.ruleIndex = ruleIndex;
        System.out.println("bin size: "+binSize);
        System.out.println("bin count: "+binCount);
    }


    private int ruleIndex;

    private int binCount;

    @Override
    protected double computeDatum()
    {
        Rule rule = RuleManager.getNormalRules()[ruleIndex];
        return (double) model.getPossibleLocations(rule).size() / Board.SIZE;
    }

    @Override
    protected String[] getIVValues() {
        String[] values = new String[binCount];
        for(int i = 0; i < values.length; i++){
            values[i] = String.format("%.2f", binSize*i);
        }
        System.out.println(Arrays.toString(values));
        return values;
    }
}
