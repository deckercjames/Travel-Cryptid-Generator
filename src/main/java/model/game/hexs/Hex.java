package main.java.model.game.hexs;

import java.util.Set;

public class Hex
{
    public Hex(HexTag... tags)
    {
        for (HexTag tag : tags)
        {
            if(tag instanceof Terrain)
                this.terrain = (Terrain) tag;
            else if(tag instanceof Animal)
                this.animal = (Animal) tag;
        }
    }

    public Hex(Terrain terrain)
    {
        this.terrain = terrain;
    }

    public Hex(Terrain terrain, Animal animal)
    {
        this.terrain = terrain;
        this.animal = animal;
    }

    private Terrain terrain;
    private Animal animal;
    private StructureType structureType;
    private StructureColor structureColor;

    public Terrain getTerrain(){ return terrain; }
    public Animal getAnimal(){ return animal; }
    public StructureType getStructureType(){ return structureType; }
    public StructureColor getStructureColor(){ return structureColor; }

    public void addStructure(StructureType type, StructureColor color)
    {
        this.structureType = type;
        this.structureColor = color;
    }

    public boolean containsAnyOf(Set<HexTag> tags)
    {
       for(HexTag tag : tags)
       {
            if(tag == terrain || tag == animal || tag == structureType || tag == structureColor)
                return true;
        }
        return false;
    }

}
