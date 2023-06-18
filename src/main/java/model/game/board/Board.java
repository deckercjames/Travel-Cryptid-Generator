package main.java.model.game.board;


import main.java.model.game.hexs.StructureColor;
import main.java.model.game.hexs.StructureType;
import main.java.model.model.Rule;
import main.java.model.game.hexs.Hex;
import main.java.model.game.hexs.HexLocation;

import java.util.*;

public abstract class Board
{

    Map<HexLocation, Hex> board;

    private Map<HexLocation, Boolean> knownEdgeTiles;

    public final static int SIZE = 108;

    public Iterator<Map.Entry<HexLocation, Hex>> getBoardIterator(){ return board.entrySet().iterator(); }

    public abstract void randomizeTiles();

    public void randomizeStructures()
    {
        Set<Integer> usedIndexes = new TreeSet<>();
        Random rd = new Random();

        for(StructureType structureType : StructureType.values())
        {
            for(StructureColor structureColor : StructureColor.values())
            {
                //get an unused index
                int index;
                do {
                    index = rd.nextInt(board.size() - usedIndexes.size());
                } while (usedIndexes.contains(index));
                usedIndexes.add(index);

                //iterate to that index and add the structure
                Iterator<Map.Entry<HexLocation, Hex>> itr = board.entrySet().iterator();
                for (int i = 0; i < index - 1; i++) {
                    itr.next();
                }
                itr.next().getValue().addStructure(structureType, structureColor);
            }
        }
    }
    
    public Set<HexLocation> getAllHexLocations()
    {
        return board.keySet();
    }

    public Set<HexLocation> getHexesMatchingRule(Rule rule)
    {
        // Possible locations assuming the rule in normal (the locations will be negated at the end of the functions for a hard rule)
        Set<HexLocation> possibleLocations = new HashSet<>();
        
        //check every hex to see if it is possible for the given rule
        for(HexLocation testHex : board.keySet())
        {
            //get the surroundings of the hex
            Set<HexLocation> offsets = HexLocation.getSurrounding(rule.getRange());
            
            //check all surroundings
            for(HexLocation offset : offsets)
            {
                //get the neighbor hex to check
                Hex neighbor = board.get(testHex.translate(offset));

                //ignore if it is off the board
                if(neighbor == null) continue;

                //if true: this neighbor makes 'testHex' a valid option for 'rule'
                if(!neighbor.containsAnyOf(rule.getTypes())) continue;
                
                // If we have reached this point, then 'testHex' is a valid location
                // for the rule, if the rule is normal
                possibleLocations.add(testHex);
                break;
            }
        }
        
        if (rule.isNormal())
        {
            return possibleLocations;
        }
        
        // Take the set difference to get all other locations
        Set<HexLocation> negativePossibleLocations = new HashSet<>(board.keySet());
        negativePossibleLocations.removeAll(possibleLocations);
        return negativePossibleLocations;
    }

    // Unused
    public boolean isEdge(HexLocation location)
    {
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