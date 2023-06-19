package main.java.view.clueview;

import main.java.model.model.Clue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class ClueBookletView {

    public abstract void outputClues(String gameSetName, int numEasyGames, List<Map<Integer, List<Clue>>> clueTable) throws IOException;
    //clueTable: player index -> number of players -> game number

}
