package main.java.main;

import main.java.analysis.superclasses.Analyser;
import main.java.gui.CryptidGUI;

import main.java.analysis.experiments.RuleCoverage;
import main.java.analysis.experiments.SolutionSizeFrequency;
import main.java.analysis.experiments.SpecificRuleCoverage;

public class Cryptid
{
    public static void main(String[] args)
    {
        //if there are no arguments, create a gui application
        if (args.length == 0)
        {
            new CryptidGUI();
            return;
        }

        Analyser analyser = null;

        //if there are arguments, run analysis
        //numTrials, trial_type, additional args
        switch (args[1])
        {
            case "SolutionCountFrequency":
                analyser = new SolutionSizeFrequency();
                break;
            case "RuleCoverage":
                analyser = new RuleCoverage();
                break;
            case "SpecificRuleCoverage":
                analyser = new SpecificRuleCoverage(Double.parseDouble(args[2]), Integer.parseInt(args[3]));
                break;
        }

        if (analyser != null)
        {
            final long startTime = System.currentTimeMillis();
            analyser.runAnalyser(Integer.parseInt(args[0]));
            final long duration = System.currentTimeMillis() - startTime;
            final long minutes = duration / (1000 * 60);
            final double seconds = (duration / 1000.0) - minutes;
            System.out.printf("\nAnalysis Complete\nRuntime %dm %.2fs\n", minutes, seconds);
        }

    }
}