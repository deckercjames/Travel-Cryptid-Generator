package main.java.view.keyview;

import main.java.fileio.Settings;
import main.java.model.game.hexs.*;
import main.java.model.model.ClueManager;
import main.java.view.boardview.ViewBoardImageVectors;
import main.java.view.clueview.ClueView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * This is a class with a single public static function to write a key image to a specified png file
 */
public class KeyView
{
    private KeyView() {}
    
    final static int sizeXInPixels = 1200;
    final static int sizeYInPixels = 1650;
    
    final static int textSizePixelsH1 = 75;
    final static int textSizePixelsH2 = 53;
    final static int textSizePixelsP = 29;
    final static int textSizePixelsP2 = 24;
    
    final static int hexSizeXInPixels = 120;
    
    private static void setFont(Graphics2D g, int size)
    {
        g.setFont(new Font("Helvetica", Font.PLAIN, size));
    }

    private static void drawTerrainHex(ViewBoardImageVectors viewBoardImageVectors, Graphics2D g, Terrain terrain, int x, int y)
    {
        final int hexSizeY = (int) (hexSizeXInPixels * 0.865);
        
        viewBoardImageVectors.drawTerrain(g, x, y, terrain, hexSizeXInPixels, hexSizeY, Settings.loadColors());
        
        g.setColor(Color.BLACK);
        setFont(g, textSizePixelsP);
        g.drawString(" = "+terrain.toString(), x + hexSizeXInPixels, (int) (y + ((hexSizeY + textSizePixelsP) / 2.25)));
    }
    
    private static void drawAnimalHex(ViewBoardImageVectors viewBoardImageVectors, Graphics2D g, Animal animal, int x, int y)
    {
        final int hexSizeY = (int) (hexSizeXInPixels * 0.865);
        
        viewBoardImageVectors.drawAnimalTerritory(g, x, y, animal, hexSizeXInPixels, hexSizeY, Settings.loadColors());
        
        g.setColor(Color.BLACK);
        setFont(g, textSizePixelsP);
        g.drawString(" = "+animal.toString(), x + hexSizeXInPixels, (int) (y + ((hexSizeY + textSizePixelsP) / 2.25)));
    }
    
    private static void drawStructure(ViewBoardImageVectors viewBoardImageVectors, Graphics2D g, StructureType type, StructureColor color, String label, int x, int y)
    {
        final int hexSizeY = (int) (hexSizeXInPixels * 0.865);
        
        viewBoardImageVectors.drawStructure(g, x, y, type, color, hexSizeXInPixels, hexSizeY, Settings.loadColors());
        
        g.setColor(Color.BLACK);
        setFont(g, textSizePixelsP);
        g.drawString("=  "+label, x + hexSizeXInPixels, (int) (y + ((hexSizeY + textSizePixelsP) / 2.25)));
    }
    
    public static void outputKey(File outputFolder)
    {
        System.out.println("Making Key...");
        
        //create the image and graphics
        BufferedImage bi = new BufferedImage(
                sizeXInPixels,
                sizeYInPixels,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = bi.createGraphics();
        
        //draw white background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, sizeXInPixels, sizeYInPixels);
        
        ViewBoardImageVectors viewBoardImageVectors = new ViewBoardImageVectors();
        
        
        // set text settings
        g.setFont(g.getFont().deriveFont(textSizePixelsP));
        setFont(g, textSizePixelsH1);
        g.setColor(Color.BLACK);
        g.drawString("Cryptid Key", 375, 105);
        
        // Terrain Header
        setFont(g, textSizePixelsH2);
        g.drawString("Terrain", 150, 180);
        // Draw one of each terrain
        final int terrainHexesPosX = 90;
        final int terrainHexesPosY = 210;
        final int terrainHexesSpacing = 135;
        drawTerrainHex(viewBoardImageVectors, g, Terrain.WATER,     terrainHexesPosX, terrainHexesPosY+(0*terrainHexesSpacing));
        drawTerrainHex(viewBoardImageVectors, g, Terrain.DESERT,    terrainHexesPosX, terrainHexesPosY+(1*terrainHexesSpacing));
        drawTerrainHex(viewBoardImageVectors, g, Terrain.FOREST,    terrainHexesPosX, terrainHexesPosY+(2*terrainHexesSpacing));
        drawTerrainHex(viewBoardImageVectors, g, Terrain.MOUNTAINS, terrainHexesPosX, terrainHexesPosY+(3*terrainHexesSpacing));
        drawTerrainHex(viewBoardImageVectors, g, Terrain.SWAMP,     terrainHexesPosX, terrainHexesPosY+(4*terrainHexesSpacing));

        // Animal Header
        setFont(g, textSizePixelsH2);
        g.drawString("Animal Teritory", 630, 180);
        // Draw Each animal territory
        final int animalHexesPosX = 600;
        final int animalHexesPosY = 210;
        final int animalHexesSpacing = 128;
        drawAnimalHex(viewBoardImageVectors, g, Animal.BEAR,        animalHexesPosX, animalHexesPosY+(0*animalHexesSpacing));
        drawAnimalHex(viewBoardImageVectors, g, Animal.COUGAR,      animalHexesPosX, animalHexesPosY+(1*animalHexesSpacing));
        
        // Structure Header
        final int structHexesPosX = 600;
        final int structHexesPosY = 540;
        final int structHexesSpacing = 71;
        setFont(g, textSizePixelsH2);
        g.drawString("Structures", 630, structHexesPosY-15);
        // Draw Each animal territory
        drawStructure(viewBoardImageVectors, g, StructureType.STANDING_STONE,  StructureColor.BLACK, "Black Standing Stone",  structHexesPosX, structHexesPosY+(0*structHexesSpacing));
        drawStructure(viewBoardImageVectors, g, StructureType.STANDING_STONE,  StructureColor.GREEN, "Green Standing Stone",  structHexesPosX, structHexesPosY+(1*structHexesSpacing));
        drawStructure(viewBoardImageVectors, g, StructureType.ABANDONED_SHACK, StructureColor.WHITE, "White Abandoned Shack", structHexesPosX, structHexesPosY+(2*structHexesSpacing));
        drawStructure(viewBoardImageVectors, g, StructureType.ABANDONED_SHACK, StructureColor.BLUE,  "Blue Abandoned Shack",  structHexesPosX, structHexesPosY+(3*structHexesSpacing));
        // Noe on structures
        setFont(g, textSizePixelsP2);
        g.drawString("Note: Not all structures are listed here", structHexesPosX+30, structHexesPosY+(4*structHexesSpacing)+33);
        
        // Clues Header
        final int cluesPosX = 75;
        final int cluesPosY = 990;
        final int cluesSpacing = 38;
        setFont(g, textSizePixelsH2);
        g.drawString("All Clues", 150, cluesPosY-45);
        setFont(g, textSizePixelsP);
        String[] clues = {
            "On <Terrain_1> or <Terrain_2>",
            "Within 1 of <Terrain>",
            ClueView.getClueAsString(ClueManager.getNormalClues()[15]),
            ClueView.getClueAsString(ClueManager.getNormalClues()[16]),
            ClueView.getClueAsString(ClueManager.getNormalClues()[17]),
            ClueView.getClueAsString(ClueManager.getNormalClues()[18]),
            ClueView.getClueAsString(ClueManager.getNormalClues()[19]),
            ClueView.getClueAsString(ClueManager.getNormalClues()[20]),
            ClueView.getClueAsString(ClueManager.getNormalClues()[21]),
            ClueView.getClueAsString(ClueManager.getNormalClues()[22]),
            ClueView.getClueAsString(ClueManager.getNormalClues()[23]),
        };
        for (int i = 0; i < clues.length; i++)
        {
            g.drawString(clues[i], cluesPosX, cluesPosY+(i*cluesSpacing));
        }
        g.drawString("For Hard Mode, all these clues", cluesPosX, cluesPosY+((clues.length+1)*cluesSpacing));
        g.drawString("can be used in the negative", cluesPosX, cluesPosY+((clues.length+2)*cluesSpacing));
        g.drawString("e.g. \"Not within 2 of Cougar Territory\"", cluesPosX, cluesPosY+((clues.length+3)*cluesSpacing));
        
        // How to Play Header
        final int howToPlayPosX = 588;
        final int howToPlayPosY = 990;
        final int howToPlaySpacing = 29;
        setFont(g, textSizePixelsH2);
        g.drawString("Game Rules", 630, howToPlayPosY-45);
        setFont(g, textSizePixelsP2);
        String[] gameRules = {
            "Each player takes a turn going clockwise.",
            "On your turn, take one of the following actions:",
            " 1. Quesion",
            "     - Choose another player and ask them if",
            "       the Cryptid can be on a certain hex",
            "     - They mark that hex with a YES/NO",
            "       according to their clue",
            "     - If they mark NO, you mark a hex NO some-",
            "       where on the board according to your clue",
            " 2. Search",
            "     - Select a hex the Cryptid could be on",
            "       according to your clue and mark it YES",
            "     - Players continue around clockwise marking",
            "       that hex YES/NO according to their clues",
            "     - Once the first player marks it NO, the",
            "       search ends. You mark a hex as",
            "       NO somewhere according to your clue",
            "",
            "Win Condition",
            " - You win when you perform a Search action",
            "   and all players mark that hex as YES"
        };
        for (int i = 0; i < gameRules.length; i++)
        {
            g.drawString(gameRules[i], howToPlayPosX, howToPlayPosY+(i*howToPlaySpacing));
        }
        
        g.dispose();
        
        String outputFileName = Paths.get(outputFolder.toString(), "Key.png").toString();

        System.out.println("Writing key image to file ("+outputFileName+") ...");

        //print image to output
        try
        {
            File outputFile = new File(outputFileName);
            ImageIO.write(bi, "png", outputFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
