package main.java.model.game.hexs;

import java.util.Set;

/**
 * This contians all information about a given hex on the board
 * 
 * i.e.
 * The Terrain
 * Any animal territory
 * Any Structure information (color/type)
 */
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
    
    public boolean hasStructure()
    {
        return structureType != null;
    }

    /**
     * Returns true if any of the hex attributes match any of the given tags
     * 
     * @param tags Set of tags to compare against
     * @return true if this hex matches
     */
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
