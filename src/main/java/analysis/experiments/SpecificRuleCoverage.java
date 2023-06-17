package analysis.experiments;

import analysis.superclasses.AnalyserHistogram;
import model.game.board.Board;
import model.model.RuleManager;

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
    protected double computeDatum() {
        return (double) RuleManager.getNormalRules()[ruleIndex].getPossibleLocations().size() / Board.SIZE;
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
