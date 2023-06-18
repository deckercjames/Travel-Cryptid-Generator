package main.java.view.ruleview;

import main.java.model.game.hexs.*;
import main.java.model.model.Rule;

import java.util.Iterator;

public class RuleView
{

    public static String getRuleAsString(Rule rule)
    {
        
        HexTag firstTag = rule.getTypes().iterator().next();

        StringBuilder str = new StringBuilder();
        if(rule.getRange() == 0)
        {
            if (rule.isNormal()) str.append("On ");
            else str.append("Not on ");
            Iterator<HexTag> itr = rule.getTypes().iterator();
            str.append(HexTag.capitalized(itr.next()));
            str.append(" or ");
            str.append(HexTag.capitalized(itr.next()));
            return str.toString();
        }

        if (rule.isNormal()) str.append("Within ");
        else str.append("Not within ");
        str.append(rule.getRange());
        str.append(" of ");

        if (rule.getRange() == 1 && rule.getTypes().size() == 2)
        {
            str.append("Animal Territory");
        }
        else if (rule.getRange() == 2)
        {
            if (firstTag == StructureType.ABANDONED_SHACK)
                str.append("Abandoned Shack");
            else if (firstTag == StructureType.STANDING_STONE)
                str.append("Standing Stone");
            else {
                str.append(firstTag);
            }
        }
        else if (rule.getRange() == 3)
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
