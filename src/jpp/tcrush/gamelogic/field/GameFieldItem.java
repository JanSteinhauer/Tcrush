package jpp.tcrush.gamelogic.field;

import jpp.tcrush.gamelogic.utils.Coordinate2D;
import jpp.tcrush.gamelogic.utils.Move;

import java.util.Objects;

public class GameFieldItem {
    public boolean bewegungsprozess;
    private GameFieldItemType type;
    public Coordinate2D aktuellstartpos;
    public Coordinate2D aktuellendpos;

    public boolean EndmoveWurdeAufgerufen = false;

    public GameFieldItem(GameFieldItemType type){
        this.type = type;
    }

    public GameFieldItemType getType(){
        return type;
    }

    public void startMove(Coordinate2D startPosition){
        if(Objects.isNull(startPosition)){
            throw new IllegalArgumentException();
        }else{
            if(bewegungsprozess == false){
                aktuellstartpos = startPosition;
                bewegungsprozess = true;
            }else {
                throw new IllegalStateException(); // sollte sich das Item schon in einem Bewegungsprozess befinden werfen Sie eine IllegalStateException
            }                                           // wenn aktuellstartpos schon initalisiert, dann ist es in Bewegung

        }
    }

    public Move endMove(Coordinate2D endPosition){
        if(Objects.isNull(endPosition)){
            throw new IllegalArgumentException();
        }else{
            if(bewegungsprozess == false){
                throw new IllegalStateException(); // sollte sich das Item schon in einem Bewegungsprozess befinden werfen Sie eine IllegalStateException
            }else {
                this.aktuellendpos = endPosition;
                bewegungsprozess = false;
                EndmoveWurdeAufgerufen = true;
                return  new Move(aktuellstartpos, aktuellendpos);
            }
        }
    }

    public boolean isOnMove(){
        if(Objects.isNull(aktuellstartpos)|| EndmoveWurdeAufgerufen){
            return false; // Objekt befindet sich noch nicht in der Bewegung
        }else {
           return true;
        }
    }

    public boolean getbewegungsprozess(){
        return bewegungsprozess;
    }
    public void setBewegungsprozess(boolean b){
        bewegungsprozess = b;
    }
}
