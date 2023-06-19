package main.java.model.game.hexs;

import java.awt.*;
import java.util.*;

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

    public HexLocation rotate180()
    {
        return new HexLocation(-x, -y, -z);
    }

    private HexLocation translate(int dx, int dy, int dz)
    {
        return  new HexLocation(x+dx, y+dy, z+dz);
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



    private static int manhattanDistFromOrigin(int x, int y)
    {
        int sum = 0;
        sum += Math.abs(x);
        sum += Math.abs(y);
        sum += Math.abs(-x-y);
        return sum / 2;
    }



    private static final Map<Integer, Set<HexLocation>> neighbors;
    static
    {
        neighbors = new TreeMap<>();
        for(int k = 0; k <= 3; k++)
        {
            //create set of all hexes k away from origin
            Set<HexLocation> set = new HashSet<>();
            //test all possible locations
            for(int x = -k; x <= k; x++)
            {
                for(int y = -k; y <= k; y++)
                {
                    if(manhattanDistFromOrigin(x, y) > k) continue;
                    set.add(new HexLocation(x, y));
                }
            }
            neighbors.put(k, set);
        }
    }

    public static Set<HexLocation> getSurrounding(int dist)
    {
        return neighbors.get(dist);
    }

}