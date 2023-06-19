package main.java.view.clueview;

import main.java.model.game.hexs.*;
import main.java.model.model.Clue;

import java.util.Iterator;

public class ClueView
{

    public static String getClueAsString(Clue clue)
    {   
        HexTag firstTag = clue.getTypes().iterator().next();

        StringBuilder str = new StringBuilder();
        if(clue.getRange() == 0)
        {
            if (clue.isNormal()) str.append("On ");
            else str.append("Not on ");
            Iterator<HexTag> itr = clue.getTypes().iterator();
            str.append(HexTag.capitalized(itr.next()));
            str.append(" or ");
            str.append(HexTag.capitalized(itr.next()));
            return str.toString();
        }

        if (clue.isNormal()) str.append("Within ");
        else str.append("Not within ");
        str.append(clue.getRange());
        str.append(" of ");

        if (clue.getRange() == 1 && clue.getTypes().size() == 2)
        {
            str.append("Animal Territory");
        }
        else if (clue.getRange() == 2)
        {
            if (firstTag == StructureType.ABANDONED_SHACK)
                str.append("an Abandoned Shack");
            else if (firstTag == StructureType.STANDING_STONE)
                str.append("a Standing Stone");
            else {
                str.append(firstTag);
            }
        }
        else if (clue.getRange() == 3)
        {
            str.append("a ");
            str.append(firstTag);
        }
        else
        {
            str.append(firstTag);
        }

        return str.toString();
    }
}
