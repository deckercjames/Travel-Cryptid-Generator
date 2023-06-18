package main.java.model.stats;

import main.java.model.game.board.Board;
import main.java.model.model.Rule;
import main.java.model.game.hexs.HexLocation;

import java.util.Set;

public class RuleComboStats {

    public RuleComboStats(int numRules){
        this.numRules = numRules;
        frequencyCount = new int[Board.SIZE+1];
    }

    private final int numRules;



    //after running a brute force recursion, these wil be reset
    //sets of valid rules combos
    private Set<Set<Rule>> validCombos;
    //all the locations that the valid combos limit to
    private Set<HexLocation> validSolutionLocations;

    //statistics
    private int[] frequencyCount;

    public void incrementFrequency(int solutionsToRulesCombo){
        frequencyCount[solutionsToRulesCombo]++;
    }

    public int getFrequency(int solutionsToRulesCombo){
        return frequencyCount[solutionsToRulesCombo];
    }

    public int getNumRules(){
        return numRules;
    }

    public int getBoardCoverage(){
        return validSolutionLocations.size();
    }


    public int getFrequencyOfValidCombos(){
        return frequencyCount[1];
    }

    public void addValidRuleCombo(Set<Rule> rulesCombo, HexLocation solution){
        validCombos.add(rulesCombo);
        validSolutionLocations.add(solution);
    }



}
