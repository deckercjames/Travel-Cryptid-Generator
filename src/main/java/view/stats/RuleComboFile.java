package view.stats;

import model.model.Model;
import model.stats.MultiTrialRulesComboStats;
import model.stats.RuleComboStats;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class RuleComboFile {

//
//
//    public static void outputStatistics(RuleComboStats stats){
//
//        //calculate the number of combinations
//        int comboCount = Model.getTotalComboCount(stats.getNumRules());
//        System.out.println("\nnum allRules: "+48);
//        System.out.println("total number of combos: "+comboCount);
//
//        //print frequency
//        System.out.println("\nSolution Count Freq");
//        for(int i = 0; i < 60; i++){
//            double proportion = (stats.getFrequency(i) / (double) comboCount) * 100;
//            System.out.println(String.format("%3d: %4d %2.2f", i, stats.getFrequency(i), proportion));
//        }
//
//        System.out.println("Found "+stats.getFrequencyOfValidCombos()+" Valid solutions");
//        System.out.println("covering "+stats.getBoardCoverage()+" locations");
//
//    }
//
//
//
//    public static void outputAllStats(MultiTrialRulesComboStats stats){
//        System.out.println();
//        System.out.println("Average valid solution coverage");
//        for(int i = 3; i <= 5; i++){
//            System.out.println(i+" "+stats.getAverageValidSolutionCoverage(i));
//        }
//
//        try {
//            File outputFile = new File("output/statistics.csv");
//            BufferedWriter output = new BufferedWriter(new FileWriter(outputFile));
//            for(int i = 0; i < stats.getNumOfTrials(); i++){
//                output.write(Integer.toString(i));
//                for (int k = 3; k <= 5; k++){
//                    output.write(",");
//                    output.write( Double.toString(stats.getAverageFrequencyNormalized(k, i)) );
//                }
//                output.write("\n");
//            }
//            output.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

}
