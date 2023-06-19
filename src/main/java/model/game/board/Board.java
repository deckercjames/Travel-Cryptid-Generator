package main.java.model.game.board;


import main.java.model.game.hexs.StructureColor;
import main.java.model.game.hexs.StructureType;
import main.java.model.model.Clue;
import main.java.model.game.hexs.Hex;
import main.java.model.game.hexs.HexLocation;

import java.util.*;

public abstract class Board
{

    Map<HexLocation, Hex> board;

    /**
     * This is a cache of which Hex locations are on the edge
     */
    private Map<HexLocation, Boolean> knownEdgeTiles;

    /**
     * Returns an iterator over all hexLocation/Hex entries in the board
     * @return
     */
    public Iterator<Map.Entry<HexLocation, Hex>> getBoardIterator(){ return board.entrySet().iterator(); }

    /**
     * Randomizes the terrain hexes on the board
     */
    public abstract void randomizeHexes();

    /**
     * Does a simple random function of adding all 8 structures to the board, such that
     * no two structures share a hex
     */
    public void randomizeStructures()
    {
        Random rd = new Random();
        
        // convert the hex locations on the board to a list where random indexes can be selected
        List<HexLocation> boardLocations = new ArrayList<>(board.keySet());

        for(StructureType structureType : StructureType.values())
        {
            for(StructureColor structureColor : StructureColor.values())
            {
                while (true)
                {
                    int index = rd.nextInt(boardLocations.size());
                    
                    // if this board location already has a structure, do not add another one
                    if (board.get(boardLocations.get(index)).hasStructure())
                    {
                        continue;
                    }
                    
                    board.get(boardLocations.get(index)).addStructure(structureType, structureColor);
                }
            }
        }
    }
    
    public Set<HexLocation> getAllHexLocations()
    {
        return board.keySet();
    }

    public Set<HexLocation> getHexesMatchingClue(Clue clue)
    {
        // Possible locations assuming the clue in normal (the locations will be negated at the end of the functions for a hard clue)
        Set<HexLocation> possibleLocations = new HashSet<>();
        
        //check every hex to see if it is possible for the given clue
        for(HexLocation testHex : board.keySet())
        {
            //get the surroundings of the hex
            Set<HexLocation> offsets = HexLocation.getSurrounding(clue.getRange());
            
            //check all surroundings
            for(HexLocation offset : offsets)
            {
                //get the neighbor hex to check
                Hex neighbor = board.get(testHex.translate(offset));

                //ignore if it is off the board
                if(neighbor == null) continue;

                //if true: this neighbor makes 'testHex' a valid option for 'clue'
                if(!neighbor.containsAnyOf(clue.getTypes())) continue;
                
                // If we have reached this point, then 'testHex' is a valid location
                // for the clue, if the clue is normal
                possibleLocations.add(testHex);
                break;
            }
        }
        
        if (clue.isNormal())
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