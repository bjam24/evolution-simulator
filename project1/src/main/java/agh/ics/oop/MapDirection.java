package agh.ics.oop;

import java.util.Random;

public enum MapDirection {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

    private static final Random rand = new Random();

    public MapDirection next(){
        switch(this){
            case NORTH:
                return NORTH_EAST;
            case NORTH_EAST:
                return EAST;
            case EAST:
                return SOUTH_EAST;
            case SOUTH_EAST:
                return SOUTH;
            case SOUTH:
                return SOUTH_WEST;
            case SOUTH_WEST:
                return WEST;
            case WEST:
                return NORTH_WEST;
            case NORTH_WEST:
                return NORTH;
        }
        return null;
    }

    public Vector2d toUnitVector(){
        switch(this){
            case NORTH:
                return new Vector2d(0,1);
            case NORTH_EAST:
                return new Vector2d(1,1);
            case EAST:
                return new Vector2d(1, 0);
            case SOUTH_EAST:
                return new Vector2d(1,-1);
            case SOUTH:
                return new Vector2d(0,-1);
            case SOUTH_WEST:
                return new Vector2d(-1,-1);
            case WEST:
                return new Vector2d(-1,0);
            case NORTH_WEST:
                return new Vector2d(-1,1);
        }
        return null;
    }

    public static MapDirection randomMapDirection(){
        MapDirection[] directions = values();
        return directions[rand.nextInt(directions.length)];
    }
}
