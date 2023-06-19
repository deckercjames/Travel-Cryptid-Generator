package main.java.model.model;

import main.java.model.game.hexs.Animal;
import main.java.model.game.hexs.StructureColor;
import main.java.model.game.hexs.StructureType;
import main.java.model.game.hexs.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ClueManager
{
    private ClueManager() {}
    
    private static Clue[] normalClues = {
            //one of two types of terrain
            new Clue(0, Terrain.FOREST, Terrain.DESERT),
            new Clue(0, Terrain.FOREST, Terrain.WATER),
            new Clue(0, Terrain.FOREST, Terrain.SWAMP),
            new Clue(0, Terrain.FOREST, Terrain.MOUNTAINS),
            new Clue(0, Terrain.DESERT, Terrain.WATER),
            new Clue(0, Terrain.DESERT, Terrain.SWAMP),
            new Clue(0, Terrain.DESERT, Terrain.MOUNTAINS),
            new Clue(0, Terrain.WATER, Terrain.SWAMP),
            new Clue(0, Terrain.WATER, Terrain.MOUNTAINS),
            new Clue(0, Terrain.SWAMP, Terrain.MOUNTAINS),

            //within one of terrain type or animal territory
            new Clue(1, Terrain.FOREST), //[10]
            new Clue(1, Terrain.DESERT),
            new Clue(1, Terrain.SWAMP),
            new Clue(1, Terrain.MOUNTAINS),
            new Clue(1, Terrain.WATER),
            new Clue(1, Animal.COUGAR, Animal.BEAR),

            //within two of a type of animal territory or type of structure
            new Clue(2, Animal.COUGAR), //[16]
            new Clue(2, Animal.BEAR),
            new Clue(2, StructureType.ABANDONED_SHACK),
            new Clue(2, StructureType.STANDING_STONE),

            //within three of a color of structure
            new Clue(3, StructureColor.BLACK), //[20]
            new Clue(3, StructureColor.WHITE),
            new Clue(3, StructureColor.BLUE),
            new Clue(3, StructureColor.GREEN)
    };

    private static Clue[] allClues;
    
    // The possible clue combos will always be the same
    // [numPlayers(3-5)][hardMode/easyMode] -> array of all possible clue combos
    private static Map<Integer, Map<Boolean, ClueCombo[]>> allClueCombosByPlayerNumberAndMode;
    //true = hard mode, false = easy mode


    static
    {
        //create the negative clues and add all the clues to 'allClues'
        allClues = new Clue[normalClues.length * 2];
        for(int i = 0; i < normalClues.length; i++)
        {
            allClues[i] = normalClues[i];
            allClues[i + normalClues.length] = normalClues[i].getNegative();
        }
        
        allClueCombosByPlayerNumberAndMode = new HashMap<>();
        
        // Create a cache of all possible clue combos
        for (int numPlayers = 3; numPlayers <= 5; numPlayers++)
        {
            Map<Boolean, ClueCombo[]> modeToClueCombos = new HashMap<>();
            modeToClueCombos.put(false, getAllCombos(numPlayers, false));
            modeToClueCombos.put(true,  getAllCombos(numPlayers, true));
            allClueCombosByPlayerNumberAndMode.put(numPlayers, modeToClueCombos);
        }

    }


    public static Clue[] getNormalClues()
    {
        return normalClues;
    }

    public static Clue[] getAllClues()
    {
        return allClues;
    }

    public static ClueCombo[] getAllClueCombos(int numPlayers, boolean hardMode)
    {
        return allClueCombosByPlayerNumberAndMode.get(numPlayers).get(hardMode);
    }



    // recursively make all clue combos
    private static void makeAllCombos(Clue[] usingClues,
                               int numPlayers,
                               int newClueStartIndex,
                               ClueCombo combo,
                               List<ClueCombo> allCombos)
    {
        // base case, if the combo is full, add it
        if(combo.size() == numPlayers)
        {
            allCombos.add(combo);
            return;
        }

        // add a new clue to the set
        for(int i = newClueStartIndex; i < usingClues.length; i++)
        {
            Clue clueToAdd = usingClues[i];
            
            // if adding clue i, to the current combo would create an inherent contradiction, do not bother recursing
            boolean isContradiction = false;
            for (Clue r : combo)
            {
                if (Clue.canCluesBeUsedTogether(r, clueToAdd)) continue;
                
                isContradiction = true;
                break;
            }
            if (isContradiction) continue;

            //duplicate the combo
            ClueCombo combo2 = new ClueCombo(combo);
            
            //add the next clue to the duplicate
            combo2.add(clueToAdd);

            //recurse
            makeAllCombos(usingClues, numPlayers, i+1, combo2, allCombos);
        }
    }


    private static ClueCombo[] getAllCombos(int numPlayers, boolean hardMode)
    {
        // create a list for all the clue combos
        List<ClueCombo> allCombos = new ArrayList<>(getTotalComboCount(numPlayers));

        // set which set of clues are bing used
        Clue[] usingClues = (hardMode) ? allClues : normalClues;

        // start recursion
        makeAllCombos(usingClues, numPlayers, 0, new ClueCombo(), allCombos);
        
        return allCombos.toArray(new ClueCombo[allCombos.size()]);
    }


    private static int getTotalComboCount(int n)
    {
        int comboCount = 1;
        for(int i = 48; i > 48-n; i--)
        {
            comboCount *= i;
        }
        for(int i = 1; i <= n; i++)
        {
            comboCount /= i;
        }
        return comboCount;
    }
}
