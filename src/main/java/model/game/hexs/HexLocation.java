package main.java.model.game.hexs;

import java.awt.*;
import java.util.*;

/**
 * Does a hexagonal coordinate. Hexagonal coordinates have three integers parameters
 * that must add up to zero
 * 
 * This class is immutable
 */
public class HexLocation
{
    public HexLocation(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        assert (x + y + z) == 0;
    }

    public HexLocation(int x, int y)
    {
        this(x, y, -x-y);
    }

    private final int x, y, z;

    public Dimension getGraphicPosition(int xSize)
    {
        final int ySize = (int) ((int) (xSize / 2) * Math.sqrt(3));
        int x = (int) (this.x * xSize * 0.75);
        int y = (int) ((Math.abs(this.y) - (this.x / 2.0))  * ySize );
        return new Dimension(x, y);
    }

    /**
     * Returns a new hex location that is the result of rotating
     * this position 180 degrees about the origin
     * 
     * @return
     */
    public HexLocation rotate180()
    {
        return new HexLocation(-x, -y, -z);
    }

    /**
     * Returns a new hex location that is the result of shifting the location by the given deltas
     * If the given deltas cause the resulting location to be an invalid hex coordinate, a runtime exception is thrown
     */
    private HexLocation translate(int dx, int dy, int dz)
    {
        return new HexLocation(x+dx, y+dy, z+dz);
    }

    public HexLocation translate(HexLocation delta)
    {
        return translate(delta.x, delta.y, delta.z);
    }



    @Override
    public String toString()
    {
        return  "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public boolean equals(Object other)
    {
        if (! (other instanceof HexLocation)) return false;
        HexLocation h = (HexLocation) other;
        return x==h.x && y==h.y && z==h.z;
    }

    @Override
    public int hashCode()
    {
        return 100 * x + 10 * y + z;
    }


    /**
     * Returns the "manhattan" distance of a hex from the origin. The z coordinate of the
     * hex is implied since hex coordinates are restrict to be (x+y+z)=0. The manhattan distance
     * is measured in standard hex movements, not just x+y distance
     */
    private static int manhattanDistFromOrigin(int x, int y)
    {
        int sum = 0;
        sum += Math.abs(x);
        sum += Math.abs(y);
        sum += Math.abs(-x-y);
        return sum / 2;
    }

    /**
     * Caches the set of hexlocations that are within that disance of the origin
     * 
     * e.g.
     * size(neighbors[0]) = 1
     * size(neighbors[1]) = 7
     * size(neighbors[2]) = 19
     * size(neighbors[3]) = 37
     */
    private static final Map<Integer, Set<HexLocation>> neighborsCache = new HashMap<>();
    static void addNeighborDistanceToCache(int dist)
    {
        //create set of all hexes k away from origin
        Set<HexLocation> set = new HashSet<>();
        //test all possible locations
        for(int x = -dist; x <= dist; x++)
        {
            for(int y = -dist; y <= dist; y++)
            {
                if(manhattanDistFromOrigin(x, y) > dist) continue;
                set.add(new HexLocation(x, y));
            }
        }
        neighborsCache.put(dist, set);
    }

    /**
     * Returns the set of hex locations that are withing dist of the
     * origin
     */
    public static Set<HexLocation> getSurrounding(int dist)
    {
        if (!neighborsCache.containsKey(dist))
        {
            addNeighborDistanceToCache(dist);
        }
            
        return neighborsCache.get(dist);
    }

}