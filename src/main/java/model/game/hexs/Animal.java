package main.java.model.game.hexs;

public enum Animal implements HexTag
{
    BEAR("Bear Territory"),
    COUGAR("Cougar Territory");

    Animal(String rep)
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
