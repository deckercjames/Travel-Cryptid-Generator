package controller;

import javax.swing.*;
import java.io.File;

public class ControlThread extends Controller implements Runnable {

    public ControlThread(File outputFolder, String gameSetName, int numGames, int numEasyGames,
                         int boardImageSize, ProgressMonitor progressMonitor){
        this.outputFolder = outputFolder;
        this.gameSetName = gameSetName;
        this.numGames = numGames;
        this.numEasyGames = numEasyGames;
        this.boardSizeImage = boardImageSize;
        this.progressMonitor = progressMonitor;
    }

    private File outputFolder;
    private String gameSetName;
    private int numGames;
    private int numEasyGames;
    private int boardSizeImage;
    private ProgressMonitor progressMonitor;

    @Override
    public void run(){
        makeCompleteGames(outputFolder, gameSetName, numGames, numEasyGames, boardSizeImage, progressMonitor);
    }

}
