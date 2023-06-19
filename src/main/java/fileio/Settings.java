package main.java.fileio;

import main.java.model.game.hexs.Animal;
import main.java.model.game.hexs.HexTag;
import main.java.model.game.hexs.StructureColor;
import main.java.model.game.hexs.Terrain;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Settings
{
    private static Map<HexTag, Color> defaultColors;
    static
    {
        defaultColors = new HashMap<>();
        defaultColors.put(Terrain.WATER,        new Color(0, 132, 255));
        defaultColors.put(Terrain.SWAMP,        new Color(101, 48, 36));
        defaultColors.put(Terrain.MOUNTAINS,    new Color(130, 131, 132));
        defaultColors.put(Terrain.DESERT,       new Color(255, 241, 31));
        defaultColors.put(Terrain.FOREST,       new Color(0, 110, 2));

        defaultColors.put(Animal.COUGAR,        new Color(255, 0, 0));
        defaultColors.put(Animal.BEAR,          new Color(0, 0, 0));

        defaultColors.put(StructureColor.BLACK, new Color(0, 0, 0));
        defaultColors.put(StructureColor.WHITE, new Color(255, 255, 255));
        defaultColors.put(StructureColor.BLUE,  new Color(0, 0, 255));
        defaultColors.put(StructureColor.GREEN, new Color(0, 255, 0));
    }

    private static final String COLOR_SETTING_FILENAME = ".color_settings.data";

    private static void save(String filename, Object object)
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(object);
            out.close();
            fileOut.close();
        }
        catch (Exception ignored)
        {
            System.out.println("could not save "+filename);
        }
    }

    private static Object load(String filename) throws IOException, ClassNotFoundException
    {
        FileInputStream fi = new FileInputStream(filename);
        ObjectInputStream oi = new ObjectInputStream(fi);

        // Read objects
        Object inputObject = oi.readObject();

        oi.close();
        fi.close();

        return inputObject;
    }


    @SuppressWarnings("unchecked")
    public static Map<HexTag, Color> loadColors()
    {
        try
        {
            return (Map<HexTag, Color>) load(COLOR_SETTING_FILENAME);
        }
        catch (Exception ignored){}

        return defaultColors;
    }

    public static void saveColors(Map<HexTag, Color> colors)
    {
        save(COLOR_SETTING_FILENAME, colors);
    }
}
