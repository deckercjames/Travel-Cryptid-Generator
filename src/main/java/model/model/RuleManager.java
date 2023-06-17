package model.model;

import model.game.hexs.Animal;
import model.game.hexs.StructureColor;
import model.game.hexs.StructureType;
import model.game.hexs.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RuleManager {

    RuleManager(){

        allRuleCombosByModeAndPlayerNumber = new TreeMap<>();
        allRuleCombosByModeAndPlayerNumber.put(true, new TreeMap<>());
        allRuleCombosByModeAndPlayerNumber.put(false, new TreeMap<>());
        //generate combos on startup
//        for(int i = 3; i <= 5; i++){
//            makeAllCombos(i, true);
//            makeAllCombos(i, false);
//        }

    }

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
    static {
        //create the negative rules and add all the rules to 'all rules'
        allRules = new Rule[normalRules.length * 2];
        for(int i = 0; i < normalRules.length; i++){
            allRules[i] = normalRules[i];
            allRules[i + normalRules.length] = normalRules[i].getNegative();
        }
    }



    private Map<Boolean, Map<Integer, List<RuleCombo>>> allRuleCombosByModeAndPlayerNumber;
    //true = hard mode, false = easy mode


    public static Rule[] getNormalRules() {
        return normalRules;
    }

    static Rule[] getAllRules(){
        return allRules;
    }



    List<RuleCombo> getAllRuleCombos(int numPlayers, boolean hardMode){
        //see if the list of rule combos has bee created
        List<RuleCombo> combos = allRuleCombosByModeAndPlayerNumber.get(hardMode).get(numPlayers);

        //if it has, return it
        if(combos != null)
            return combos;

        //if it is not created, make it and add it to the map
        makeAllCombos(numPlayers, hardMode);

        return allRuleCombosByModeAndPlayerNumber.get(hardMode).get(numPlayers);
    }





    //recursively make all rule combos
    private void makeAllCombos(Rule[] usingRules,
                               int numPlayers,
                               int newRuleStartIndex,
                               RuleCombo combo,
                               List<RuleCombo> allCombos){

        //base case, if the combo is full, add it
        if(combo.size() == numPlayers) {
            allCombos.add(combo);
            return;
        }

        //add a new rule to the set
        for(int i = newRuleStartIndex; i < usingRules.length; i++){

            //duplicate the combo
            RuleCombo combo2 = new RuleCombo(combo);

            //add the next rule to the duplicate
            combo2.add(usingRules[i]);

            //recurse
            makeAllCombos(usingRules, numPlayers, i+1, combo2, allCombos);

        }

    }


    private void makeAllCombos(int numPlayers, boolean hardMode){

        System.out.print("Making rule combos: "+numPlayers+" players "+(hardMode?"hard":"easy")+"...");

        //create a list for all the rule combos
        List<RuleCombo> allCombos = new ArrayList<>(getTotalComboCount(numPlayers));

        //set which set of rules are bing used
        Rule[] usingRules = normalRules;
        if(hardMode) usingRules = allRules;

        //start recursion
        makeAllCombos(usingRules, numPlayers, 0, new RuleCombo(), allCombos);

        //debug
        System.out.printf("Completed: %,d combos\n", allCombos.size());

        this.allRuleCombosByModeAndPlayerNumber.get(hardMode).put(numPlayers, allCombos);
    }


    private static int getTotalComboCount(int n){
        int comboCount = 1;
        for(int i = 48; i > 48-n; i--){
            comboCount *= i;
        }
        for(int i = 1; i <= n; i++){
            comboCount /= i;
        }
        return comboCount;
    }


}
