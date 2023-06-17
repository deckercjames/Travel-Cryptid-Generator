package model.model;

import model.game.hexs.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class Rule implements Serializable {

    Rule(int range, HexTag... tags){

        this.range = range;
        this.normal = true;
        this.tags = new HashSet<>(Arrays.asList(tags));

        possible = new HashSet<>(200);

    }

    //negation constructor
    private Rule(int range, boolean normal, Set<HexTag> tags){

        this.range = range;
        this.normal = normal;
        this.tags = new HashSet<>(tags);

        possible = new HashSet<>(200);

    }

    //rule definition
    private int range;
    private boolean normal;
    private Set<HexTag> tags;

    public int getRange(){ return range; }
    public boolean isNormal(){ return normal; }
    public Set<HexTag> getTypes(){ return tags; }


    void clearHexes(){
        this.possible.clear();
    }

    //categorize all sets
    private Set<HexLocation> possible;

    public Set<HexLocation> getPossibleLocations(){ return possible; }

    public void addPossibleLocation(HexLocation possibility){
        possible.add(possibility);
    }

    Rule getNegative(){
        return new Rule(range, false, tags);
    }

    @Override
    public String toString(){

        HexTag firstTag = tags.iterator().next();

        StringBuilder str = new StringBuilder();
        if(range == 0){
            if(normal) str.append("On ");
            else str.append("Not on ");
            Iterator<HexTag> itr = tags.iterator();
            str.append(HexTag.capitalized(itr.next()));
            str.append(" or ");
            str.append(HexTag.capitalized(itr.next()));
            return str.toString();
        }

        if(normal) str.append("Within ");
        else str.append("Not within ");
        str.append(range);
        str.append(" of ");

        if(range == 1 && tags.size()==2){
            str.append("Animal Territory");
        }

        else if(range == 2){
            if(firstTag == StructureType.ABANDONED_SHACK)
                str.append("Abandoned Shack");
            else if(firstTag == StructureType.STANDING_STONE)
                str.append("Standing Stone");
            else {
                str.append(firstTag);
            }
        }

        else if(range == 3){
            str.append("a ");
            str.append(firstTag);
        }

        else{
            str.append(firstTag);
        }

        return str.toString();
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