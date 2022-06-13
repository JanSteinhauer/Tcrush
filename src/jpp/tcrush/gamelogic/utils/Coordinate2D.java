package jpp.tcrush.gamelogic.utils;

import java.util.Objects;

public class Coordinate2D {
    private int x;
    private int y;

    public Coordinate2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }

    //TODO hier steht maybe auch Object
    public boolean equals(Object obj ) {
        if(Objects.isNull(obj)){
            return false;
        }
        Coordinate2D coordinate2D = (Coordinate2D) obj;
        if(x == coordinate2D.getX() && y == coordinate2D.getY()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 7 * ((x+5)*7) * (y*11);   //hier mache ich basically eine Primzahl (wegen hash) mal die Koordinaten (die werden wieder mit 7 multipliziert, sonst könnte wenn zwei Klassen multiplizeirt die gleiche Summe hat, sie als gleich gewertet werden).
    }



}
