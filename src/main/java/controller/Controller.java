package main.java.controller;

import main.java.model.game.hexs.HexLocation;
import main.java.model.model.Model;
import main.java.model.model.Rule;
import main.java.model.model.RuleCombo;
import main.java.view.boardview.ViewBoard;
import main.java.view.boardview.ViewBoardImageVectors;
import main.java.view.gui.ViewGUI;
import main.java.view.ruleview.RuleView;
import main.java.view.ruleview.RuleViewPDF;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class Controller{

    public Controller(){
        model = new Model();
    }

    private Model model;
    private Map<Integer, RuleCombo> completeRuleCombos;

    public void makeGUI(){
        new ViewGUI();
    }

    public void printPossibleSolutionsBoard(int numPlayers, boolean hardMode){

        model.initializeRandomBoard();

        new ViewBoardImageVectors();

        //get the valid solutions from the rules combos
        Set<HexLocation> validSolutions = new HashSet<>(300);
        Set<HexLocation> validFairSolutions = new HashSet<>(300);
        int validComboCount = 0;
        int validFairComboCount = 0;

        List<RuleCombo> allCombos = model.getAllRuleCombos(numPlayers, hardMode);

        for(RuleCombo combo : allCombos){

            HexLocation validSolution = combo.getValidSolution();

            if(validSolution != null) {

                validSolutions.add(validSolution);
                validComboCount++;
                if(combo.isFair()){
                    validFairSolutions.add(validSolution);
                    validFairComboCount++;
                }
            }
        }

        System.out.println("Found "+validComboCount+" valid combos covering "+validSolutions.size()+" hexes");
        System.out.println("Found "+validFairComboCount+" valid fair combos covering "+validFairSolutions.size()+" hexes");


//        viewBoard.outputBoard(model.getBoard(), 1000, "output/validSolutions_"+numPlayers+"_players", "Board", "Pemi");
//        viewBoard.outputBoard(model.getBoard(), 1000, "output/validFairSolutions_"+numPlayers+"_players", "Board", "Pemi");

    }

    public void testBoardImage(String outputFolder){
        new ViewBoardImageVectors();

        model.initializeRandomBoard();

        //output game boards
//        boardViewBoard.outputBoard(model.getBoard(), 1000, outputFolder+"BoardTest", "Board", "Pemi");

    }


    private void makeCompleteGame(boolean hardMode){

        model.initializeRandomBoard();

        completeRuleCombos = model.getCompleteRandomRuleCombos(hardMode);

    }

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
                                  int boardImageSize, ProgressMonitor progressMonitor){

        if(progressMonitor != null){
            progressMonitor.setNote("Making Cryptid ");
            progressMonitor.setProgress(1);
        }

        ViewBoard boardViewBoard = new ViewBoardImageVectors();
        RuleView ruleView = new RuleViewPDF(outputFolder);

        //player index -> number of players -> game number
        List<Map<Integer, List<String>>> ruleTable = new ArrayList<>(5);

        //add all player maps
        for(int i = 0; i < 5; i++){
            ruleTable.add(new TreeMap<>());
            for(int j = (i<3)?3:(i+1); j <= 5; j++){
                ruleTable.get(i).put(j, new ArrayList<>(numGames));
            }
        }

        //make i games
        for(int i = 0; i < numGames; i++){

            if(progressMonitor != null){
                progressMonitor.setNote("Making Game "+(i+1));
                progressMonitor.setProgress(i+2);
            }

            System.out.println("Making Game "+i);

            //after making all easy games, make hard games
            boolean hardMode = i >= numEasyGames;
            makeCompleteGame(hardMode);

            //output game board
            boardViewBoard.outputBoard(model.getBoard(), boardImageSize, outputFolder, i, gameSetName, hardMode);

            //make rules set for each number of players
            for(int k = 3; k <= 5; k++){

                System.out.println("For "+k+" players");

                //get the rule combo for a game with k players
                List<Rule> combo = completeRuleCombos.get(k).getRulesInRandomOrder();

                for(Rule rule : combo)
                    System.out.println(rule);

                //add the rule to the table
                //player j, game i, k players
                for(int j = 0; j < k; j++){
                    ruleTable.get(j).get(k).add(combo.get(j).toString());
                }

                System.out.println();
                System.out.println();

            }

        }

        if(progressMonitor != null){
            progressMonitor.setNote("Making Rules");
            progressMonitor.setProgress(numGames+2);
        }

        //output all the rules
        try {
            ruleView.outputRules(gameSetName, numEasyGames, ruleTable);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(progressMonitor != null) {
            progressMonitor.close();
        }

    }

}