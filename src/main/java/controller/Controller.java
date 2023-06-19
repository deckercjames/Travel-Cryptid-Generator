package main.java.controller;

import main.java.model.model.Model;
import main.java.model.model.Clue;
import main.java.view.boardview.ViewBoard;
import main.java.view.boardview.ViewBoardImageVectors;
import main.java.view.clueview.ClueBookletView;
import main.java.view.clueview.ClueBookletViewPDF;
import main.java.view.keyview.KeyView;

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
        ClueBookletView clueView = new ClueBookletViewPDF(outputFolder);

        // player index -> number of players -> game number
        List<Map<Integer, List<Clue>>> clueTable = new ArrayList<>(5);

        //add all player maps
        for(int i = 0; i < 5; i++)
        {
            clueTable.add(new TreeMap<>());
            for(int j = (i<3)?3:(i+1); j <= 5; j++)
            {
                clueTable.get(i).put(j, new ArrayList<>(numGames));
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

            // make clues set for each number of players
            for(int numPlayers = 3; numPlayers <= 5; numPlayers++)
            {
                System.out.println("For "+numPlayers+" players");
                
                //get the clues combo for a game with k players
                Clue[] clues = model.getRandomValidFairClueCombo(hardMode, numPlayers).getCluessInRandomOrder();

                for(Clue clue : clues)
                    System.out.println(clue);

                //add the clue to the table
                //player j, game i, k players
                for(int j = 0; j < numPlayers; j++)
                {
                    clueTable.get(j).get(numPlayers).add(clues[j]);
                }

                System.out.println("\n");
            }
        }

        if(progressMonitor != null)
        {
            progressMonitor.setNote("Making Clue Booklet");
            progressMonitor.setProgress(numGames+2);
        }

        // output all the clues to a pdf
        try
        {
            clueView.outputClues(gameSetName, numEasyGames, clueTable);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        if(progressMonitor != null)
        {
            progressMonitor.setNote("Making Game Key");
            progressMonitor.setProgress(numGames+2);
        }

        KeyView.outputKey(outputFolder);
        
        // Close the Progress monitor
        if(progressMonitor != null)
        {
            progressMonitor.close();
        }

    }

}