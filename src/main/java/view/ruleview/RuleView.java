package view.ruleview;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class RuleView {

    public abstract void outputRules(String gameSetName, int numEasyGames, List<Map<Integer, List<String>>> ruleTable) throws IOException;
    //ruleTable: player index -> number of players -> game number

}
