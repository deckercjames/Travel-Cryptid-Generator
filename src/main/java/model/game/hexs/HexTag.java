package main.java.model.game.hexs;

import java.io.Serializable;

public interface HexTag extends Serializable
{
    static String capitalized(HexTag tag)
    {
        return "" + tag.toString().charAt(0) + tag.toString().toLowerCase().substring(1);
    }
}