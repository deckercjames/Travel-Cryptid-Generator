package main.java.model.model;

import main.java.model.game.hexs.*;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Rule implements Serializable
{
    Rule(int range, HexTag... tags)
    {
        this.range = range;
        this.normal = true;
        this.tags = new HashSet<>(Arrays.asList(tags));
    }

    //negation constructor
    private Rule(int range, boolean normal, Set<HexTag> tags)
    {
        this.range = range;
        this.normal = normal;
        this.tags = new HashSet<>(tags);
    }

    //rule definition
    private int range;
    private boolean normal;
    private Set<HexTag> tags;

    public int getRange(){ return range; }
    public boolean isNormal(){ return normal; }
    public Set<HexTag> getTypes(){ return tags; }


    Rule getNegative()
    {
        return new Rule(range, false, tags);
    }
    
    /**
     * @param r1
     * @param r2
     * @return
     */
    public static boolean canRulesBeUsedTogether(Rule r1, Rule r2)
    {
        // If the ranges are different, they can be used together
        if (r1.range != r2.range)
        {
            return true;
        }
        
        // If the rules are negations of each other, they can not be used together
        if (r1.tags.equals(r2.tags))
        {
            return false;
        }
        
        // Rules about on/not_on type of terrain, must share atleast one terrain
        if (r2.range == 0) // we know the ranges are equal at this point
        {
            Set<HexTag> intersection = new HashSet<>(r1.tags);
            intersection.retainAll(r2.tags);
            if (intersection.size() == 0) return false;
            if (intersection.size() == r1.tags.size() || intersection.size() == r2.tags.size()) return false;
        }
        
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Rule(");
        sb.append(normal ? "Normal" : "Not");
        sb.append(", ");
        sb.append(range);
        sb.append(", {");
        List<String> tags_as_strings = new ArrayList<>();
        for (HexTag tag : tags)
        {
            tags_as_strings.add(tag.toString());
        }
        sb.append(String.join(", ", tags_as_strings));
        sb.append("})");
        return sb.toString();
    }

    @Override
    public int hashCode()
    {
        int hash = this.range * 17;
        if(normal) hash += 113;
        for(HexTag tag : tags)
        {
            hash += tag.hashCode() * 397;
        }
        return hash;
    }

}