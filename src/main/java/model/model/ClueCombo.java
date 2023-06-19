package main.java.model.model;

import java.util.*;

public class ClueCombo extends HashSet<Clue>
{
    ClueCombo(ClueCombo combo)
    {
        super(combo);
    }

    ClueCombo() {

    }

    /**
     * Gets all strict subsets of this clues set. All sets, not including this set itself
     * 
     * @return
     */
    public ClueCombo[] getAllStrictSubsets()
    {
        // Convert this set of clues into an array that can be indexed
        Clue[] clues = toArray(new Clue[size()]);

        List<ClueCombo> subsets = new ArrayList<>();

        for(int clueMask = 0; clueMask < (1<<clues.length); clueMask++)
        {
            // Make a subset with this mask
            ClueCombo subset = new ClueCombo();
            for(int i = 0; i < clues.length; i++)
            {
                if((clueMask & (1 << i)) == 0) continue;

                subset.add(clues[i]);
            }
            
            // If this is not a strict subset
            if(subset.size() == size()) continue;
            
            // add this subset to the list
            subsets.add(subset);
        }

        return subsets.toArray(new ClueCombo[subsets.size()]);
    }

    
    public Clue[] getCluessInRandomOrder()
    {
        List<Clue> clues_in_random_order = new ArrayList<>(this);
        Collections.shuffle(clues_in_random_order);
        return clues_in_random_order.toArray(new Clue[clues_in_random_order.size()]);
    }



    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for(Clue clue : this)
        {
            str.append(clue);
            str.append('\n');
        }
        return str.toString();
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        int i = 1;
        for(Clue clue : this)
        {
            hash += clue.hashCode() * i * 7;
            i++;
        }
        return hash;
    }

}
