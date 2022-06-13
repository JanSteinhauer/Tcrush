package jpp.tcrush.gamelogic.utils;

import java.util.Objects;

public class Move {
    private Coordinate2D from;
    private Coordinate2D to;

    public Move(Coordinate2D from, Coordinate2D to){
        if(Objects.isNull(from) || Objects.isNull(to)){
            throw new IllegalArgumentException();
        }else{
            this.from = from;
            this.to = to;
        }
    }

    public Coordinate2D getFrom(){
        return from;
    }

    public Coordinate2D getTo(){
        return to;
    }

}
