package main.java.controller;

import main.java.model.model.Model;
import main.java.model.model.Rule;
import main.java.view.boardview.ViewBoard;
import main.java.view.boardview.ViewBoardImageVectors;
import main.java.view.ruleview.RuleBookletView;
import main.java.view.ruleview.RuleBookletViewPDF;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class Controller{

    public Controller()
    {
        model = new Model();
    }

    private Model model;

    /**
     * makes games and saves them to the output folder
     * @param outputFolder
     * @param gameSetName
     * @param numGames the total number of games to generate
     * @param numEasyGames the number of games that should be easy mode. Must be less than or equal to numGames
     * @param boardImageSize
     * @param progressMonitor
     */
    public void makeCompleteGames(File outputFolder, String gameSetName, int numGames, int numEasyGames,
                                  int boardImageSize, ProgressMonitor progressMonitor)
    {
        if(progressMonitor != null)
        {
            progressMonitor.setNote("Making Cryptid ");
            progressMonitor.setProgress(1);
        }

        ViewBoard boardViewBoard = new ViewBoardImageVectors();
        RuleBookletView ruleView = new RuleBookletViewPDF(outputFolder);

        // player index -> number of players -> game number
        List<Map<Integer, List<Rule>>> ruleTable = new ArrayList<>(5);

        //add all player maps
        for(int i = 0; i < 5; i++)
        {
            ruleTable.add(new TreeMap<>());
            for(int j = (i<3)?3:(i+1); j <= 5; j++)
            {
                ruleTable.get(i).put(j, new ArrayList<>(numGames));
            }
        }

        //make i games
        for(int i = 0; i < numGames; i++)
        {
            if(progressMonitor != null)
            {
                progressMonitor.setNote("Making Game "+(i+1));
                progressMonitor.setProgress(i+2);
            }

            System.out.println("Making Game "+i);

            // after making all easy games, make hard games
            boolean hardMode = i >= numEasyGames;
            
            // Intilize the board
            model.initializeRandomBoard();

            // Output game board to a file
            boardViewBoard.outputBoard(model.getBoard(), boardImageSize, outputFolder, i, gameSetName, hardMode);

            //make rules set for each number of players
            for(int numPlayers = 3; numPlayers <= 5; numPlayers++)
            {
                System.out.println("For "+numPlayers+" players");
                
                //get the rule combo for a game with k players
                Rule[] rules = model.getRandomValidFairRuleCombo(hardMode, numPlayers).getRulesInRandomOrder();

                for(Rule rule : rules)
                    System.out.println(rule);

                //add the rule to the table
                //player j, game i, k players
                for(int j = 0; j < numPlayers; j++)
                {
                    ruleTable.get(j).get(numPlayers).add(rules[j]);
                }

                System.out.println("\n");
            }
        }

        if(progressMonitor != null)
        {
            progressMonitor.setNote("Making Rules");
            progressMonitor.setProgress(numGames+2);
        }

        //output all the rules to a pdf
        try
        {
            ruleView.outputRules(gameSetName, numEasyGames, ruleTable);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        // Close the Progress monitor
        if(progressMonitor != null)
        {
            progressMonitor.close();
        }

    }

}