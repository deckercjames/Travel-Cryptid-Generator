package model.game.hexs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface HexTag extends Serializable {

    static String capitalized(HexTag tag){
        return "" + tag.toString().charAt(0) + tag.toString().toLowerCase().substring(1);
    }

}