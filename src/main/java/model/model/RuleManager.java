package main.java.model.model;

import main.java.model.game.hexs.Animal;
import main.java.model.game.hexs.StructureColor;
import main.java.model.game.hexs.StructureType;
import main.java.model.game.hexs.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RuleManager
{
    private static Rule[] normalRules = {
            //one of two types of terrain
            new Rule(0, Terrain.FOREST, Terrain.DESERT),
            new Rule(0, Terrain.FOREST, Terrain.WATER),
            new Rule(0, Terrain.FOREST, Terrain.SWAMP),
            new Rule(0, Terrain.FOREST, Terrain.MOUNTAINS),
            new Rule(0, Terrain.DESERT, Terrain.WATER),
            new Rule(0, Terrain.DESERT, Terrain.SWAMP),
            new Rule(0, Terrain.DESERT, Terrain.MOUNTAINS),
            new Rule(0, Terrain.WATER, Terrain.SWAMP),
            new Rule(0, Terrain.WATER, Terrain.MOUNTAINS),
            new Rule(0, Terrain.SWAMP, Terrain.MOUNTAINS),

            //within one of terrain type or animal territory
            new Rule(1, Terrain.FOREST), //[10]
            new Rule(1, Terrain.DESERT),
            new Rule(1, Terrain.SWAMP),
            new Rule(1, Terrain.MOUNTAINS),
            new Rule(1, Terrain.WATER),
            new Rule(1, Animal.COUGAR, Animal.BEAR),

            //within two of a type of animal territory or type of structure
            new Rule(2, Animal.COUGAR), //[16]
            new Rule(2, Animal.BEAR),
            new Rule(2, StructureType.ABANDONED_SHACK),
            new Rule(2, StructureType.STANDING_STONE),

            //within three of a color of structure
            new Rule(3, StructureColor.BLACK), //[20]
            new Rule(3, StructureColor.WHITE),
            new Rule(3, StructureColor.BLUE),
            new Rule(3, StructureColor.GREEN)
    };

    private static Rule[] allRules;
    
    // The possible rule combos will always be the same
    // [numPlayers(3-5)][hardMode/easyMode] -> array of all possible rule combos
    private static Map<Integer, Map<Boolean, RuleCombo[]>> allRuleCombosByPlayerNumberAndMode;
    //true = hard mode, false = easy mode


    static {
        //create the negative rules and add all the rules to 'allRules'
        allRules = new Rule[normalRules.length * 2];
        for(int i = 0; i < normalRules.length; i++)
        {
            allRules[i] = normalRules[i];
            allRules[i + normalRules.length] = normalRules[i].getNegative();
        }
        
        allRuleCombosByPlayerNumberAndMode = new HashMap<>();
        
        // Create a cache of all possible rule combos
        for (int numPlayers = 3; numPlayers <= 5; numPlayers++)
        {
            Map<Boolean, RuleCombo[]> modeToRuleCombos = new HashMap<>();
            modeToRuleCombos.put(true, getAllCombos(numPlayers, true));
            modeToRuleCombos.put(false, getAllCombos(numPlayers, false));
            allRuleCombosByPlayerNumberAndMode.put(numPlayers, modeToRuleCombos);
        }

    }


    public static Rule[] getNormalRules()
    {
        return normalRules;
    }

    public static Rule[] getAllRules()
    {
        return allRules;
    }



    public static RuleCombo[] getAllRuleCombos(int numPlayers, boolean hardMode)
    {
        return allRuleCombosByPlayerNumberAndMode.get(numPlayers).get(hardMode);
    }



    // recursively make all rule combos
    private static void makeAllCombos(Rule[] usingRules,
                               int numPlayers,
                               int newRuleStartIndex,
                               RuleCombo combo,
                               List<RuleCombo> allCombos)
    {
        // base case, if the combo is full, add it
        if(combo.size() == numPlayers)
        {
            allCombos.add(combo);
            return;
        }

        // add a new rule to the set
        for(int i = newRuleStartIndex; i < usingRules.length; i++)
        {
            //duplicate the combo
            RuleCombo combo2 = new RuleCombo(combo);

            //add the next rule to the duplicate
            combo2.add(usingRules[i]);

            //recurse
            makeAllCombos(usingRules, numPlayers, i+1, combo2, allCombos);
        }
    }


    private static RuleCombo[] getAllCombos(int numPlayers, boolean hardMode)
    {
        //create a list for all the rule combos
        List<RuleCombo> allCombos = new ArrayList<>(getTotalComboCount(numPlayers));

        //set which set of rules are bing used
        Rule[] usingRules = (hardMode) ? allRules : normalRules;

        //start recursion
        makeAllCombos(usingRules, numPlayers, 0, new RuleCombo(), allCombos);

        return allCombos.toArray(new RuleCombo[allCombos.size()]);
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
