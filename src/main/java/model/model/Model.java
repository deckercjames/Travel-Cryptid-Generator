package model.model;

import model.game.board.Board;
import model.game.board.BoardSectioned;

import java.util.*;

public class Model {

    public Model(){
        this.ruleManager = new RuleManager();
    }


    private Board board;

    private RuleManager ruleManager;

    public void initializeRandomBoard(){

        System.out.println("Initializing Board");

        this.board = new BoardSectioned();

        System.out.println("  Randomizing Tiles");
        this.board.randomizeTiles();

        System.out.println("  Randomizing Structures");
        this.board.randomizeStructures();

        System.out.println("  Assigning hexes to Rules");

        //Make assign hexes to rules
        for(Rule rule : ruleManager.getAllRules()){
//            System.out.println("  Assigning rule "+rule);
            rule.clearHexes();
            board.assignHexesToRule(rule);
        }

    }

    /**
     * Returns the keys {3, 4, 5} mapped to
     * valid, fair sets of rules for that number of players.
     *
     */
    public Map<Integer, RuleCombo> getCompleteRandomRuleCombos(boolean hardMode){
        Map<Integer, RuleCombo> completeRuleCombos = new TreeMap<>();
        Random rd = new Random();
        //select a random combo for each number of players
        for(int i = 3; i <= 5; i++){
            //get the list of rules to randomly select from
            List<RuleCombo> allRuleCombos = ruleManager.getAllRuleCombos(i, hardMode);
            RuleCombo randomCombo;
            //repeatedly select a random combo until a valid fair one is selected
            do{
                int index = rd.nextInt(allRuleCombos.size());
                randomCombo = allRuleCombos.get(index);
            }while(randomCombo.getValidSolution() == null || !randomCombo.isFair());
            completeRuleCombos.put(i, randomCombo);
        }
        return completeRuleCombos;
    }


    public Board getBoard(){
        return board;
    }







    public List<RuleCombo> getAllRuleCombos(int numPlayers, boolean hardMode){
        return ruleManager.getAllRuleCombos(numPlayers, hardMode);
    }

    public List<RuleCombo> getAllValidFairRuleCombos(int numPlayers, boolean hardMode){

        System.out.println("copying valid fair rules over");

        //get the valid solutions from the rules combos
        List<RuleCombo> allCombos = ruleManager.getAllRuleCombos(numPlayers, hardMode);

        List<RuleCombo> validFairCombos = new ArrayList<>(allCombos.size());

        for(RuleCombo combo : allCombos){

            if(combo.getValidSolution() != null && combo.isFair()) {

                validFairCombos.add(combo);
            }
        }

        System.out.println(" valid fair rules copied");

        return validFairCombos;

    }



}
