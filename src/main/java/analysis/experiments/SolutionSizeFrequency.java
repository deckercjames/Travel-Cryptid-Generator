package main.java.analysis.experiments;

import main.java.analysis.superclasses.AnalyserMultiPlayer;
import main.java.model.game.board.Board;
import main.java.model.model.RuleCombo;
import main.java.model.model.RuleManager;

public class SolutionSizeFrequency extends AnalyserMultiPlayer {

    public SolutionSizeFrequency() {
        super("Solution_Sizes");
    }

    @Override
    protected double[] computeData(int numPlayers) {

        RuleCombo[] allCombos = RuleManager.getAllRuleCombos(numPlayers, true);

        //make a list to store the frequencies
        double[] solutionCountFrequencies = new double[Board.SIZE+1];

        //iterate through every rule combo
        for(RuleCombo combo : allCombos){

            //increment the frequency of combos with that number os solutions
            solutionCountFrequencies[model.getSolutions(combo).size()]++;

        }
        return solutionCountFrequencies;
    }

    @Override
    protected String[] getIVValues() {
        String[] values = new String[109];
        for(int i = 0; i < values.length; i++){
            values[i] = ""+i;
        }
        return values;
    }
}
