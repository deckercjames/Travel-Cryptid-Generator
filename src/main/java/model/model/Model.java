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
    
    // For every possible clue, it stores the set of hexes that are possible for that given clue
    private Map<Clue, Set<HexLocation>> cluesToPossibleHexes;

    public void initializeRandomBoard()
    {
        System.out.println("Initializing Board");

        this.board = new BoardSectioned();

        System.out.println("  Randomizing Tiles");
        this.board.randomizeHexes();

        System.out.println("  Randomizing Structures");
        this.board.randomizeStructures();

        System.out.println("  Assigning hexes to Clues");

        // Assign possible hex locations for clues
        this.cluesToPossibleHexes = new HashMap<>();
        for(Clue clue : ClueManager.getAllClues())
        {
            cluesToPossibleHexes.put(clue, board.getHexesMatchingClue(clue));
        }

    }
    
    
    /**
     * Returns all possible location that could be the solution on the given board for the given clue
     */
    public Set<HexLocation> getPossibleLocations(Clue clue)
    {
        return this.cluesToPossibleHexes.get(clue);
    }
    
    
    /**
     * Returns all hex locations that are possible given this clue combo. This may not be a valid clue
     * combo,  in which case there may not be exactly one solution (either zero or >=2)
     * @return
     */
    public Set<HexLocation> getSolutions(Set<Clue> clues)
    {
        //initial set from first clue
        Set<HexLocation> solutions = new HashSet<>(board.getAllHexLocations());

        //retain sets possible locations from all other turles
        for (Clue r : clues)
        {
            solutions.retainAll(this.cluesToPossibleHexes.get(r));
        }

        return solutions;
    }

    
    /**
     * Returns true if all possible subsets give multiple solutions.
     * false otherwise. If any subset produces one solutions then
     * the set is not fair. It is not fair, because any players
     * that have a clue not in that subset have extranious information
     * 
     * @return
     */
    public boolean isFair(ClueCombo combo)
    {
        ClueCombo[] subsets = combo.getAllStrictSubsets();

        // check all subsets
        for (ClueCombo comboSubset : subsets)
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
     * valid, fair sets of clues for that number of players.
     *
     */
    public ClueCombo getRandomValidFairClueCombo(boolean hardMode, int numPlayers)
    {
        Random rd = new Random();
        
        //get the list of clues to randomly select from
        ClueCombo[] allClueCombos = ClueManager.getAllClueCombos(numPlayers, hardMode);
        
        // repeatedly select a random combo until a valid, fair one is selected
        while (true)
        {
            int index = rd.nextInt(allClueCombos.length);
            ClueCombo randomCombo = allClueCombos[index];
            
            if (this.getSolutions(randomCombo).size() != 1) continue;
            if (!this.isFair(randomCombo)) continue;
            
            return randomCombo;
        }
    }


    public Board getBoard()
    {
        return board;
    }
}
