package main.java.model.game.hexs;

public enum StructureColor implements HexTag
{
    BLACK("Black Structure"),
    WHITE("White Structure"),
    GREEN("Green Structure"),
    BLUE("Blue Structure");

    StructureColor(String rep)
    {
        this.rep = rep;
    }

    private String rep;

    @Override
    public String toString()
    {
        return rep;
    }
}
