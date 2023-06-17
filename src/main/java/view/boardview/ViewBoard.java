package view.boardview;

import model.game.board.Board;

import java.io.File;

public abstract class ViewBoard {

    public static int getHexWidth(int imageWidth, int horizontalHexCount){
        return (int) ((imageWidth / (horizontalHexCount * 3.0 + 1.0)) * 4.0);
    }

    public static int getHexHeight(int hexWidth){
        return (int) ((hexWidth / 4.0) * Math.sqrt(3));
    }

    public abstract void outputBoard(Board board, int sizeXInPixels, File outputFolder, int boardIndex, String title, boolean hardMode);

}
