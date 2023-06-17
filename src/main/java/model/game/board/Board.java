package model.game.board;


import model.game.hexs.StructureColor;
import model.game.hexs.StructureType;
import model.model.Rule;
import model.game.hexs.Hex;
import model.game.hexs.HexLocation;

import java.util.*;

public abstract class Board{


    Map<HexLocation, Hex> board;

    private Map<HexLocation, Boolean> knownEdgeTiles;

    public final static int SIZE = 108;

    public Iterator<Map.Entry<HexLocation, Hex>> getBoardIterator(){ return board.entrySet().iterator(); }

    public abstract void randomizeTiles();

    public void randomizeStructures(){

        Set<Integer> usedIndexes = new TreeSet<>();
        Random rd = new Random();

        for(StructureType structureType : StructureType.values()){

            for(StructureColor structureColor : StructureColor.values()) {

                //get an unused index
                int index;
                do {
                    index = rd.nextInt(board.size() - usedIndexes.size());
                } while (usedIndexes.contains(index));
                usedIndexes.add(index);

//            System.out.println("index: "+index);

                //iterate to that index and add the structure
                Iterator<Map.Entry<HexLocation, Hex>> itr = board.entrySet().iterator();
                for (int i = 0; i < index - 1; i++) {
                    itr.next();
                }
                itr.next().getValue().addStructure(structureType, structureColor);

            }

        }


    }

    public void assignHexesToRule(Rule rule){

//        System.out.println(rule);

        //check every hex to see if it is possible for the given rule
        for(HexLocation testHex : board.keySet()){

            //get the surroundings of the hex
            Set<HexLocation> surroundings = HexLocation.getSurrounding(rule.getRange());

            boolean possible = false;

            //check all surroundings
            for(HexLocation neighborOffset : surroundings){

                //get the neighbor hex to check
                Hex neighbor = board.get(testHex.translate(neighborOffset));

                //ignore if it is off the board
                if(neighbor == null) continue;

                //if true: this neighbor makes 'testHex' a valid option for 'rule'
                if(neighbor.containsAnyOf(rule.getTypes())){
                    possible = true;
                    break;
                }

            }

            if(possible == rule.isNormal())
                rule.addPossibleLocation(testHex);

        }


    }

    public boolean isEdge(HexLocation location){

        if(knownEdgeTiles == null)
            knownEdgeTiles = new HashMap<>(200);

        if(knownEdgeTiles.containsKey(location))
            return knownEdgeTiles.get(location);

        Set<HexLocation> surroundings = HexLocation.getSurrounding(1);

        for(HexLocation neighbor : surroundings){
            //get the neighbor hex to check
            Hex hex = board.get(location.translate(neighbor));
            if(hex == null){
                knownEdgeTiles.put(location, true);
                return true;
            }
        }

        knownEdgeTiles.put(location, false);
        return false;

    }

}