package main.java.view.boardview;

import main.java.fileio.Settings;
import main.java.model.game.board.Board;
import main.java.model.game.hexs.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ViewBoardImageVectors extends ViewBoard
{
    private int[] getHexagonXVerts(int xPosition, int xLength, double scale)
    {
        double size = xLength * scale;
        double pos = xPosition + (xLength - size) / 2;

        return new int[] {
                (int) (pos + size / 4),
                (int) (pos +  3 * size / 4),
                (int) (pos + size),
                (int) (pos +  3 * size / 4),
                (int) (pos + size / 4),
                (int) pos
        };

    }
    private int[] getHexagonYVerts(int yPosition, int yLength, double scale)
    {
        double size = yLength * scale;
        double pos = yPosition + (yLength - size) / 2;

        return new int[]
        {
                (int) (pos),
                (int) (pos),
                (int) (pos + size / 2),
                (int) (pos +  size),
                (int) (pos + size),
                (int) (pos + size / 2)
        };

    }

    private int[] getOctagonXVerts(int xPosition, int xLength)
    {
        double pos = xPosition + (xLength - (double) xLength) / 2;
        double a =  (double) xLength / (1 + Math.sqrt(2));

        return new int[]
        {
                (int) (pos + (((double) xLength - a) / 2)),
                (int) (pos + (((double) xLength - a) / 2) + a),
                (int) (pos + (double) xLength),
                (int) (pos + (double) xLength),
                (int) (pos + (((double) xLength - a) / 2) + a),
                (int) (pos + (((double) xLength - a) / 2)),
                (int) pos,
                (int) pos
        };
    }
    private int[] getOctagonYVerts(int yPosition, int yLength)
    {
        double pos = yPosition + (yLength - (double) yLength) / 2;
        double a =  (double) yLength / (1 + Math.sqrt(2));

        return new int[]
        {
                (int) pos,
                (int) pos,
                (int) (pos + (((double) yLength - a) / 2)),
                (int) (pos + (((double) yLength - a) / 2) + a),
                (int) (pos + (double) yLength),
                (int) (pos + (double) yLength),
                (int) (pos + (((double) yLength - a) / 2) + a),
                (int) (pos + (((double) yLength - a) / 2)),
        };
    }

    private int[] getTriangleXVerts(int pos, int xLength){

        return new int[] {
                (int) (pos + ((double) xLength / 2)),
                (int) (pos + (double) xLength),
                pos,
        };

    }
    private int[] getTriangleYVerts(int pos, int xLength)
    {
        double size = (xLength / 2.0) * Math.sqrt(3);

        return new int[] {
                pos,
                (int) (pos + size),
                (int) (pos + size),
        };
    }
    
    
    
    public void drawTerrain(Graphics2D g, int x, int y, Terrain terrain, int hexSizeX, int hexSizeY, Map<HexTag, Color> tagColorMap)
    {
        if (terrain == null) return;
        
        g.setColor(tagColorMap.get(terrain));
        g.fillPolygon(
            getHexagonXVerts(x, hexSizeX, 0.95),
            getHexagonYVerts(y, hexSizeY, 0.95),
            6
        );
    }
    
    public void drawAnimalTerritory(Graphics2D g, int x, int y, Animal animal, int hexSizeX, int hexSizeY, Map<HexTag, Color> tagColorMap)
    {
        if (animal == null) return;
        
        g.setColor(tagColorMap.get(animal));
        g.setStroke(new BasicStroke(hexSizeX / 30.0f));
        g.drawPolygon(
            getHexagonXVerts(x, hexSizeX, 0.8),
            getHexagonYVerts(y, hexSizeY, 0.8),
            6
        );
    }
    
    public void drawStructure(Graphics2D g, int x, int y, StructureType type, StructureColor color, int hexSizeX, int hexSizeY, Map<HexTag, Color> tagColorMap)
    {
        if (type == null || color == null) return;
        
        final int halfStruct = (int) (hexSizeX * 0.2);
        
        int[] xVerts = null;
        int[] yVerts = null;
        
        switch (type)
        {
            case STANDING_STONE:
                xVerts = getOctagonXVerts(x + (hexSizeX/2)-halfStruct, halfStruct*2);
                yVerts = getOctagonYVerts(y + (hexSizeY/2)-halfStruct, halfStruct*2);
                break;
            case ABANDONED_SHACK:
                xVerts = getTriangleXVerts(x + (hexSizeX/2)-halfStruct, halfStruct*2);
                yVerts = getTriangleYVerts(y + (hexSizeY/2)-halfStruct, halfStruct*2);
                break;
        }
        
        g.setColor(tagColorMap.get(color));
        
        g.fillPolygon(
            xVerts,
            yVerts,
            xVerts.length
        );
        
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke((int)(hexSizeX / 100)));
        
        g.drawPolygon(
            xVerts,
            yVerts,
            xVerts.length
        );
    }
    
    
    public void drawHex(Graphics2D g, int x, int y, Hex hex, int hexSizeX, Map<HexTag, Color> tagColorMap)
    {
        final int hexSizeY = (int) (hexSizeX * 0.865);
        
        drawTerrain(g, x, y, hex.getTerrain(), hexSizeX, hexSizeY, tagColorMap);
        drawAnimalTerritory(g, x, y, hex.getAnimal(), hexSizeX, hexSizeY, tagColorMap);
        drawStructure(g, x, y, hex.getStructureType(), hex.getStructureColor(), hexSizeX, hexSizeY, tagColorMap);
    }

    /**
     * Draws all the hexes and their attributes (animal territories, structiors)
     * 
     * @param g Graphics to draw to
     * @param itr Iterator of HexLocation->Hex
     * @param hexSizeX The size each hex should be in pixels
     * @param offset The offset from the upper left to draw to in the graphics
     * @param tagColorMap Map of hex tags to their colors
     */
    public void drawHexes(Graphics2D g, Iterator<Map.Entry<HexLocation, Hex>> itr, int hexSizeX, Dimension offset,
                          Map<HexTag, Color> tagColorMap)
    {

        //draw all hexes
        while (itr.hasNext())
        {
            Map.Entry<HexLocation, Hex> entry = itr.next();
            HexLocation hexLocation = entry.getKey();
            Hex hex = entry.getValue();

            Dimension hexGraphicLocation = hexLocation.getGraphicPosition(hexSizeX);
            
            // Calculate the pixel location of the hex
            int hexGraphicLocationX = hexGraphicLocation.width + offset.width;
            int hexGraphicLocationY = hexGraphicLocation.height + offset.height;

            // Draw the hex
            drawHex(g, hexGraphicLocationX, hexGraphicLocationY, hex, hexSizeX, tagColorMap);
        }


    }

    @Override
    public void outputBoard(Board board, int sizeXInPixels, File outputFolder, int boardIndex, String title, boolean hardMode)
    {
        System.out.println("Making Image...");
        
        //there are 37 quarters of the major diagonal of a hexagon across the image
        //final int hexSizeX = (int) ((sizeXInPixels / 37.0) * 4);
        final int hexSizeX = ViewBoard.getHexWidth(sizeXInPixels, 12);
        final int hexSizeY = (int) (hexSizeX * 0.865);
        
        final int sizeYINPixels = hexSizeY * 10;

        //create the image and graphics
        BufferedImage bi = new BufferedImage(
                sizeXInPixels,
                sizeYINPixels,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = bi.createGraphics();
        
        //draw white background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, sizeXInPixels, sizeYINPixels);

        //draw the hexes
        drawHexes(g, board.getBoardIterator(), hexSizeX, new Dimension(0, hexSizeY/2), Settings.loadColors());

        //draw titles
        g.setFont(g.getFont().deriveFont(30f));
        g.setColor(Color.BLACK);
        g.setFont(new Font("Helvetica", Font.PLAIN, (int)(hexSizeY * 0.4)));
        //title 1
        g.drawString("BOARD "+(boardIndex+1), (int)(hexSizeY*0.05), (int)(hexSizeY*0.45));
        //easy/hard
        String mode = hardMode?"HARD":"EASY";
        int modeStringWidth = g.getFontMetrics().stringWidth(mode);
        g.drawString(mode, (sizeXInPixels - modeStringWidth) / 2, (int)(hexSizeY*0.45));
        //title 2
        //truncate until it fits
        int lettersIncluded = title.length();
        String title2Mod = title.toUpperCase();//modified title (uppercase, truncated with ellipses)
        int title2Width = g.getFontMetrics().stringWidth(title2Mod);
        while(title2Width > (int)((sizeXInPixels-modeStringWidth)*0.45))
        {
            lettersIncluded--;
            title2Mod = title.substring(0, lettersIncluded).toUpperCase() + "...";
            title2Width = g.getFontMetrics().stringWidth(title2Mod);
        }
        g.drawString(title2Mod, (int)(sizeXInPixels - ((hexSizeY*0.05) + title2Width)), (int)(hexSizeY*0.45));


        g.dispose();
        
        String outputFileName = Paths.get(outputFolder.toString(), "Board"+(boardIndex+1)+".png").toString();

        System.out.println("Writing board image to file ("+outputFileName+") ...");

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