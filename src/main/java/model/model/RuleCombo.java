package main.java.model.model;

import main.java.model.game.hexs.HexLocation;

import java.util.*;

public class RuleCombo extends HashSet<Rule> {


    RuleCombo(RuleCombo combo) {
        super(combo);
    }

    RuleCombo() {

    }

    public Set<HexLocation> getSolutions(){

        //find the intersection of all possible location for each rule in the combo
        Iterator<Rule> ruleIterator = iterator();

        //initial set from first rule
        Set<HexLocation> solutions = new HashSet<>(ruleIterator.next().getPossibleLocations());

        //retain sets possible locations from all other turles
        while(ruleIterator.hasNext()){
            solutions.retainAll(ruleIterator.next().getPossibleLocations());
        }

        return solutions;
    }

    /**
     * If this rule combo produces a valid solution (only one location),
     * then that location is returned. Otherwise null is returned. Null
     * is returned when there are either no solutions or more than one.
     * @return
     */
    public HexLocation getValidSolution(){
        Set<HexLocation> solutions = getSolutions();
        if(solutions.size() == 1)
            return solutions.iterator().next();
        return null;
    }


    private List<RuleCombo> getAllSubsetsWithTwoOrMoreElements(){
        Rule[] rules = toArray(new Rule[size()]);

        List<RuleCombo> subsets = new ArrayList<>();

        for(int i = 0; i < (1<<rules.length); i++){
            RuleCombo subset = new RuleCombo();
            for(int j = 0; j < rules.length; j++){
                if((i & (1 << j)) > 0){
                    subset.add(rules[j]);
                }
            }
            if(subset.size() >= 2 && subset.size() < size()){
                subsets.add(subset);
            }
        }

        return subsets;

    }


    /**
     * Returns true if all possible subsets give multiple solutions.
     * false otherwise. If any subset produces one solutions then
     * the set is not fair.
     * @return
     */
    public boolean isFair(){

        List<RuleCombo> subsets = getAllSubsetsWithTwoOrMoreElements();

        //check all subsets
        for(RuleCombo combo : subsets){
            if(combo.getSolutions().size() <= 1){
//                System.out.print(this);
//                System.out.println(getValidSolution());
//                System.out.println("Unfair; valid subset:");
//                System.out.println(combo);
//                System.out.println();
                return false;
            }
        }

        return true;

    }


    public List<Rule> getRulesInRandomOrder(){
        List<Rule> set = new ArrayList<>(this);
        List<Rule> random = new ArrayList<>(set.size());
        Random rd = new Random();
        while(!set.isEmpty()){
            random.add(set.remove(rd.nextInt(set.size())));
        }
        return random;
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
