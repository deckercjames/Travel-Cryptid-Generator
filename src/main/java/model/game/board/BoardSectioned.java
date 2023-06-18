package main.java.model.game.board;

import main.java.model.game.hexs.*;

import java.util.*;

public class BoardSectioned extends Board {

    public BoardSectioned(){

        this.board = new HashMap<>();

    }


    @Override
    public void randomizeTiles(){

        HexLocation rotateTranslation = new HexLocation(5, -5, 0);

        //randomize areaLocations
        List<HexLocation> areaLocations = new ArrayList<>(6);
        areaLocations.addAll(Arrays.asList(this.areaLocations));
        Random rd = new Random();
        for(int i = 6; i > 0; i--){
            areaLocations.add(areaLocations.remove(rd.nextInt(i)));
        }

        //randomize each section
        for(int i = 0; i < sections.length; i++){

            boolean rotated = Math.random() < 0.5;

            //add all hexes from a section to the board
            for(int j = 0; j < areas.length; j++){

                HexLocation location = areas[j];

                //rotate
                if(rotated){
                    location = location.rotate180();
                    location = location.translate(rotateTranslation);
                }

                //move section
                location = location.translate(areaLocations.get(i));

                //add to board
                this.board.put(location, new Hex(sections[i][j]));

            }
        }

    }







    private final HexLocation[] areaLocations = {
            new HexLocation(0, 0, 0),
            new HexLocation(0, -3, 3),
            new HexLocation(0, -6, 6),
            new HexLocation(6, -3, -3),
            new HexLocation(6, -6, 0),
            new HexLocation(6, -9, 3),
    };

    private final HexLocation[] areas = {
            new HexLocation(0, 0 ),
            new HexLocation(0, -1),
            new HexLocation(0, -2),
            new HexLocation(1, -1),
            new HexLocation(1, -2),
            new HexLocation(1, -3),
            new HexLocation(2, -1),
            new HexLocation(2, -2),
            new HexLocation(2, -3),
            new HexLocation(3, -2),
            new HexLocation(3, -3),
            new HexLocation(3, -4),
            new HexLocation(4, -2),
            new HexLocation(4, -3),
            new HexLocation(4, -4),
            new HexLocation(5, -3),
            new HexLocation(5, -4),
            new HexLocation(5, -5)
    };

    private final HexTag[][][] sections = {
            //1
            {
                    {Terrain.WATER},
                    {Terrain.SWAMP},
                    {Terrain.SWAMP},
                    {Terrain.WATER},
                    {Terrain.SWAMP},
                    {Terrain.SWAMP},
                    {Terrain.WATER},
                    {Terrain.WATER},
                    {Terrain.DESERT},
                    {Terrain.WATER},
                    {Terrain.DESERT},
                    {Terrain.DESERT,     Animal.BEAR},
                    {Terrain.FOREST},
                    {Terrain.FOREST},
                    {Terrain.DESERT,     Animal.BEAR},
                    {Terrain.FOREST},
                    {Terrain.FOREST},
                    {Terrain.FOREST},
            },

            //2
            {
                    {Terrain.SWAMP,      Animal.COUGAR},
                    {Terrain.SWAMP},
                    {Terrain.SWAMP},
                    {Terrain.FOREST,     Animal.COUGAR},
                    {Terrain.SWAMP},
                    {Terrain.MOUNTAINS},
                    {Terrain.FOREST,     Animal.COUGAR},
                    {Terrain.FOREST},
                    {Terrain.MOUNTAINS},
                    {Terrain.FOREST},
                    {Terrain.DESERT},
                    {Terrain.MOUNTAINS},
                    {Terrain.FOREST},
                    {Terrain.DESERT},
                    {Terrain.MOUNTAINS},
                    {Terrain.FOREST},
                    {Terrain.DESERT},
                    {Terrain.DESERT},
            },

            //3
            {
                    {Terrain.SWAMP},
                    {Terrain.SWAMP,      Animal.COUGAR},
                    {Terrain.MOUNTAINS,  Animal.COUGAR},
                    {Terrain.SWAMP},
                    {Terrain.SWAMP,      Animal.COUGAR},
                    {Terrain.MOUNTAINS},
                    {Terrain.FOREST},
                    {Terrain.FOREST},
                    {Terrain.MOUNTAINS},
                    {Terrain.FOREST},
                    {Terrain.MOUNTAINS},
                    {Terrain.MOUNTAINS},
                    {Terrain.FOREST},
                    {Terrain.WATER},
                    {Terrain.WATER},
                    {Terrain.WATER},
                    {Terrain.WATER},
                    {Terrain.WATER},
            },

            //4
            {
                    {Terrain.DESERT},
                    {Terrain.DESERT},
                    {Terrain.DESERT},
                    {Terrain.DESERT},
                    {Terrain.DESERT},
                    {Terrain.DESERT},
                    {Terrain.MOUNTAINS},
                    {Terrain.MOUNTAINS},
                    {Terrain.DESERT},
                    {Terrain.MOUNTAINS},
                    {Terrain.WATER},
                    {Terrain.FOREST},
                    {Terrain.MOUNTAINS},
                    {Terrain.WATER},
                    {Terrain.FOREST},
                    {Terrain.MOUNTAINS},
                    {Terrain.WATER,      Animal.COUGAR},
                    {Terrain.FOREST,     Animal.COUGAR},
            },

            //5
            {
                    {Terrain.SWAMP},
                    {Terrain.SWAMP},
                    {Terrain.DESERT},
                    {Terrain.SWAMP},
                    {Terrain.DESERT},
                    {Terrain.DESERT},
                    {Terrain.SWAMP},
                    {Terrain.DESERT},
                    {Terrain.WATER},
                    {Terrain.MOUNTAINS},
                    {Terrain.WATER},
                    {Terrain.WATER},
                    {Terrain.MOUNTAINS},
                    {Terrain.MOUNTAINS},
                    {Terrain.WATER,      Animal.BEAR},
                    {Terrain.MOUNTAINS},
                    {Terrain.MOUNTAINS,  Animal.BEAR},
                    {Terrain.WATER,      Animal.BEAR},
            },

            //6
            {
                    {Terrain.DESERT,     Animal.BEAR},
                    {Terrain.MOUNTAINS,  Animal.BEAR},
                    {Terrain.MOUNTAINS},
                    {Terrain.DESERT},
                    {Terrain.MOUNTAINS},
                    {Terrain.WATER},
                    {Terrain.SWAMP},
                    {Terrain.SWAMP},
                    {Terrain.WATER},
                    {Terrain.SWAMP},
                    {Terrain.SWAMP},
                    {Terrain.WATER},
                    {Terrain.SWAMP},
                    {Terrain.FOREST},
                    {Terrain.WATER},
                    {Terrain.FOREST},
                    {Terrain.FOREST},
                    {Terrain.FOREST},
            }
    };

}
