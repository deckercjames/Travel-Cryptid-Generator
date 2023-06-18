package main.java.model.model;

import java.util.*;

public class RuleCombo extends HashSet<Rule>
{
    RuleCombo(RuleCombo combo)
    {
        super(combo);
    }

    RuleCombo() {

    }

    /**
     * Gets all strict subsets of this rules set. All sets, not including this set itself
     * 
     * @return
     */
    public RuleCombo[] getAllStrictSubsets()
    {
        // Convert this set of rules into an array that can be indexed
        Rule[] rules = toArray(new Rule[size()]);

        List<RuleCombo> subsets = new ArrayList<>();

        for(int ruleMask = 0; ruleMask < (1<<rules.length); ruleMask++)
        {
            // Make a subset with this mask
            RuleCombo subset = new RuleCombo();
            for(int i = 0; i < rules.length; i++)
            {
                if((ruleMask & (1 << i)) == 0) continue;

                subset.add(rules[i]);
            }
            
            // If this is not a strict subset
            if(subset.size() == size()) continue;
            
            // add this subset to the list
            subsets.add(subset);
        }

        return subsets.toArray(new RuleCombo[subsets.size()]);
    }

    
    public Rule[] getRulesInRandomOrder()
    {
        List<Rule> rules_in_random_order = new ArrayList<>(this);
        Collections.shuffle(rules_in_random_order);
        return rules_in_random_order.toArray(new Rule[rules_in_random_order.size()]);
    }



    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Rule rule : this){
            str.append(rule);
            str.append('\n');
        }
        return str.toString();
    }

    @Override
    public int hashCode(){
        int hash = 0;
        int i = 1;
        for(Rule rule : this){
            hash += rule.hashCode() * i * 7;
            i++;
        }
        return hash;
    }

}
