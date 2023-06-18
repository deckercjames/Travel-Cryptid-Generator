package main.java.model.model;

import main.java.model.game.hexs.*;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Rule implements Serializable {

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
    public int hashCode(){
        int hash = this.range * 17;
        if(normal) hash += 113;
        for(HexTag tag : tags){
            hash += tag.hashCode() * 397;
        }
        return hash;
    }

}