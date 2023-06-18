package main.java.model.stats;

public class MultiTrialRulesComboStats {

    MultiTrialRulesComboStats(RuleComboStats[][] stats){

        this.stats = stats;

    }

    RuleComboStats[][] stats;

    public int getNumOfTrials(){
        return stats[3].length;
    }

    public double getAverageFrequency(int numRules, int numSolutions){

        double total = 0;
        for(int i = 0; i < getNumOfTrials(); i++){
            total += stats[numRules][i].getFrequency(numSolutions);
        }
        return total / getNumOfTrials();

    }

    public double getAverageFrequencyNormalized(int numRules, int numSolutions){
//        return getAverageFrequency(numRules, numSolutions) / RuleComboStats.getTotalComboCount(numRules);
        return 0;
    }

    public int getAverageValidSolutionCoverage(int numRules){

        int totalCoverage = 0;
        for(int i = 0; i < getNumOfTrials(); i++){
            totalCoverage += stats[numRules][i].getBoardCoverage();
        }
        return totalCoverage / getNumOfTrials();

    }


}
