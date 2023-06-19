package main.java.view.keyview;

import main.java.fileio.Settings;
import main.java.model.game.hexs.*;
import main.java.model.model.RuleManager;
import main.java.view.boardview.ViewBoardImageVectors;
import main.java.view.ruleview.RuleView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class KeyView
{
    
    final static int sizeXInPixels = 800;
    final static int sizeYInPixels = 1100;
    
    final static int textSizePixelsH1 = 50;
    final static int textSizePixelsH2 = 35;
    final static int textSizePixelsP = 19;
    final static int textSizePixelsP2 = 16;
    
    final static int hexSizeXInPixels = 80;
    
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
        g.drawString("Cryptid Key", 250, 70);
        
        // Terrain Header
        setFont(g, textSizePixelsH2);
        g.drawString("Terrain", 100, 120);
        // Draw one of each terrain
        final int terrainHexesPosX = 60;
        final int terrainHexesPosY = 140;
        final int terrainHexesSpacing = 90;
        drawTerrainHex(viewBoardImageVectors, g, Terrain.WATER,     terrainHexesPosX, terrainHexesPosY+(0*terrainHexesSpacing));
        drawTerrainHex(viewBoardImageVectors, g, Terrain.DESERT,    terrainHexesPosX, terrainHexesPosY+(1*terrainHexesSpacing));
        drawTerrainHex(viewBoardImageVectors, g, Terrain.FOREST,    terrainHexesPosX, terrainHexesPosY+(2*terrainHexesSpacing));
        drawTerrainHex(viewBoardImageVectors, g, Terrain.MOUNTAINS, terrainHexesPosX, terrainHexesPosY+(3*terrainHexesSpacing));
        drawTerrainHex(viewBoardImageVectors, g, Terrain.SWAMP,     terrainHexesPosX, terrainHexesPosY+(4*terrainHexesSpacing));

        // Animal Header
        setFont(g, textSizePixelsH2);
        g.drawString("Animal Teritory", 420, 120);
        // Draw Each animal territory
        final int animalHexesPosX = 400;
        final int animalHexesPosY = 140;
        final int animalHexesSpacing = 85;
        drawAnimalHex(viewBoardImageVectors, g, Animal.BEAR,        animalHexesPosX, animalHexesPosY+(0*animalHexesSpacing));
        drawAnimalHex(viewBoardImageVectors, g, Animal.COUGAR,      animalHexesPosX, animalHexesPosY+(1*animalHexesSpacing));
        
        // Structure Header
        final int structHexesPosX = 400;
        final int structHexesPosY = 360;
        final int structHexesSpacing = 47;
        setFont(g, textSizePixelsH2);
        g.drawString("Structures", 420, structHexesPosY-10);
        // Draw Each animal territory
        drawStructure(viewBoardImageVectors, g, StructureType.STANDING_STONE,  StructureColor.BLACK, "Black Standing Stone",  structHexesPosX, structHexesPosY+(0*structHexesSpacing));
        drawStructure(viewBoardImageVectors, g, StructureType.STANDING_STONE,  StructureColor.GREEN, "Green Standing Stone",  structHexesPosX, structHexesPosY+(1*structHexesSpacing));
        drawStructure(viewBoardImageVectors, g, StructureType.ABANDONED_SHACK, StructureColor.WHITE, "White Abandoned Shack", structHexesPosX, structHexesPosY+(2*structHexesSpacing));
        drawStructure(viewBoardImageVectors, g, StructureType.ABANDONED_SHACK, StructureColor.BLUE,  "Blue Abandoned Shack",  structHexesPosX, structHexesPosY+(3*structHexesSpacing));
        // Noe on structures
        setFont(g, textSizePixelsP2);
        g.drawString("Note: Not all structures are listed here", structHexesPosX+20, structHexesPosY+(4*structHexesSpacing)+22);
        
        // Clues Header
        final int cluesPosX = 50;
        final int cluesPosY = 660;
        final int cluesSpacing = 25;
        setFont(g, textSizePixelsH2);
        g.drawString("All Clues", 100, cluesPosY-30);
        setFont(g, textSizePixelsP);
        String[] clues = {
            "On <Terrain_1> or <Terrain_2>",
            "Within 1 of <Terrain>",
            RuleView.getRuleAsString(RuleManager.getNormalRules()[15]),
            RuleView.getRuleAsString(RuleManager.getNormalRules()[16]),
            RuleView.getRuleAsString(RuleManager.getNormalRules()[17]),
            RuleView.getRuleAsString(RuleManager.getNormalRules()[18]),
            RuleView.getRuleAsString(RuleManager.getNormalRules()[19]),
            RuleView.getRuleAsString(RuleManager.getNormalRules()[20]),
            RuleView.getRuleAsString(RuleManager.getNormalRules()[21]),
            RuleView.getRuleAsString(RuleManager.getNormalRules()[22]),
            RuleView.getRuleAsString(RuleManager.getNormalRules()[23]),
        };
        for (int i = 0; i < clues.length; i++)
        {
            g.drawString(clues[i], cluesPosX, cluesPosY+(i*cluesSpacing));
        }
        g.drawString("For Hard Mode, all these clues", cluesPosX, cluesPosY+((clues.length+1)*cluesSpacing));
        g.drawString("can be used in the negative", cluesPosX, cluesPosY+((clues.length+2)*cluesSpacing));
        g.drawString("e.g. \"Not within 2 of Cougar Territory\"", cluesPosX, cluesPosY+((clues.length+3)*cluesSpacing));
        
        // How to Play Header
        final int howToPlayPosX = 392;
        final int howToPlayPosY = 660;
        final int howToPlaySpacing = 19;
        setFont(g, textSizePixelsH2);
        g.drawString("Game Rules", 420, howToPlayPosY-30);
        setFont(g, textSizePixelsP2);
        String[] gameRules = {
            "Each player takes a turn going clockwise.",
            "On your turn, take one of the following actions:",
            " 1. Quesion",
            "     - Choose another player and ask them if",
            "       the Cryptid can be on a certain hex",
            "     - They mark that hex with a YES/NO",
            "       according to their rule",
            "     - If they mark NO, you mark a hex NO some-",
            "       where on the board according to your rule",
            " 2. Search",
            "     - Select a hex the Cryptid could be on",
            "       according to your rule and mark it YES",
            "     - Players continue around clockwise marking",
            "       that hex YES/NO according to their rules",
            "     - Once the first player marks it NO, the",
            "       search ends. You mark a hex as",
            "       NO somewhere according to your rule",
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
