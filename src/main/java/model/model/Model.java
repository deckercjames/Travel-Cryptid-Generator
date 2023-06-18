package main.java.model.model;

import main.java.model.game.board.Board;
import main.java.model.game.board.BoardSectioned;
import main.java.model.game.hexs.HexLocation;

import java.util.*;

public class Model
{

    public Model() {}

    // Stores board information, all hexes and structures
    private Board board;
    
    // For every possible rule, it stores the set of hexes that are possible for that given rule
    private Map<Rule, Set<HexLocation>> rulesToPossibleHexes;

    public void initializeRandomBoard()
    {
        System.out.println("Initializing Board");

        this.board = new BoardSectioned();

        System.out.println("  Randomizing Tiles");
        this.board.randomizeTiles();

        System.out.println("  Randomizing Structures");
        this.board.randomizeStructures();

        System.out.println("  Assigning hexes to Rules");

        // Assign possible hex locations for rules
        this.rulesToPossibleHexes = new HashMap<>();
        for(Rule rule : RuleManager.getAllRules())
        {
            rulesToPossibleHexes.put(rule, board.getHexesMatchingRule(rule));
        }

    }
    
    
    
    public Set<HexLocation> getPossibleLocations(Rule rule)
    {
        return this.rulesToPossibleHexes.get(rule);
    }
    
    
    /**
     * Returns all hex locations that are possible given this rule combo. This may not be a valid rule
     * combo,  in which case there may not be exactly one solution (either zero or >=2)
     * @return
     */
    public Set<HexLocation> getSolutions(Set<Rule> rules)
    {
        //initial set from first rule
        Set<HexLocation> solutions = new HashSet<>(board.getAllHexLocations());

        //retain sets possible locations from all other turles
        for (Rule r : rules)
        {
            solutions.retainAll(this.rulesToPossibleHexes.get(r));
        }

        return solutions;
    }

    
    /**
     * Returns true if all possible subsets give multiple solutions.
     * false otherwise. If any subset produces one solutions then
     * the set is not fair. It is not fair, because any players
     * that have a rule not in that subset have extranious information
     * 
     * @return
     */
    public boolean isFair(RuleCombo combo)
    {
        RuleCombo[] subsets = combo.getAllStrictSubsets();

        // check all subsets
        for (RuleCombo comboSubset : subsets)
        {
            // if the size of this subset is only one, we do not need to bother checking if it is fair
            // (it can't possible limit the number of possible hexes down to one)
            if (comboSubset.size() <= 1) continue;
            
            // If any sub set also yeilds exactly one solution, the combo is not fair
            if(this.getSolutions(comboSubset).size() == 1)
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Returns the keys {3, 4, 5} mapped to
     * valid, fair sets of rules for that number of players.
     *
     */
    public RuleCombo getRandomValidFairRuleCombo(boolean hardMode, int numPlayers)
    {
        Random rd = new Random();
        
        //get the list of rules to randomly select from
        RuleCombo[] allRuleCombos = RuleManager.getAllRuleCombos(numPlayers, hardMode);
        
        // repeatedly select a random combo until a valid, fair one is selected
        while (true)
        {
            int index = rd.nextInt(allRuleCombos.length);
            RuleCombo randomCombo = allRuleCombos[index];
            
            if (this.getSolutions(randomCombo).size() != 1) continue;
            if (!this.isFair(randomCombo)) continue;
            
            return randomCombo;
        }
    }


    public Board getBoard(){
        return board;
    }


    // public List<RuleCombo> getAllValidFairRuleCombos(int numPlayers, boolean hardMode)
    // {
    //     System.out.println("copying valid fair rules over");

    //     //get the valid solutions from the rules combos
    //     List<RuleCombo> allCombos = ruleManager.getAllRuleCombos(numPlayers, hardMode);

    //     List<RuleCombo> validFairCombos = new ArrayList<>(allCombos.size());

    //     for(RuleCombo combo : allCombos){

    //         if(combo.getValidSolution() != null && combo.isFair()) {

    //             validFairCombos.add(combo);
    //         }
    //     }

    //     System.out.println(" valid fair rules copied");

    //     return validFairCombos;

    // }



}
