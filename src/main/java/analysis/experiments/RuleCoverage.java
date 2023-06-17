package analysis.experiments;

import analysis.superclasses.Analyser;
import model.game.board.Board;
import model.model.RuleManager;

public class RuleCoverage extends Analyser {


    public RuleCoverage() {
        super("Rule_Coverage");
    }

    @Override
    protected double[] computeData() {
        double[] data = new double[RuleManager.getNormalRules().length];
        for(int i = 0; i < data.length; i++){
            data[i] = (double) RuleManager.getNormalRules()[i].getPossibleLocations().size() / Board.SIZE;
        }
        return data;
    }

    @Override
    protected String[] getIVValues() {
        String[] values = new String[RuleManager.getNormalRules().length];
        for(int i = 0; i < values.length; i++){
            values[i] = RuleManager.getNormalRules()[i].toString();
        }
        return values;
    }
}