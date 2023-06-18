package main.java.view.ruleview;

import main.java.model.model.Rule;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class RuleBookletView {

    public abstract void outputRules(String gameSetName, int numEasyGames, List<Map<Integer, List<Rule>>> ruleTable) throws IOException;
    //ruleTable: player index -> number of players -> game number

}
