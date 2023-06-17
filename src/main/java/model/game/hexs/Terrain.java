package model.game.hexs;

public enum Terrain implements HexTag {

    WATER("Water"),
    DESERT("Desert"),
    FOREST("Forest"),
    SWAMP("Swamp"),
    MOUNTAINS("Mountains");

    Terrain(String rep){
        this.rep = rep;
    }

    private String rep;

    @Override
    public String toString(){
        return rep;
    }

}
