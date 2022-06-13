package jpp.tcrush.gamelogic;

import jpp.tcrush.gamelogic.utils.Coordinate2D;

import java.util.*;

public class Shape {
    public List<Coordinate2D> points;
    public String name;
    public int amount;

    public Shape(List<Coordinate2D> points, String name, int amount) {
        if(Objects.isNull(points) || Objects.isNull(name) || amount <= 0 || points.size() == 0){ //points.size() == 0 = points leer sein
            throw new IllegalArgumentException();
        } else {
            this.points = points;
            this.name = name;
            this.amount = amount;
        }

    }
    public Shape(List<Coordinate2D> points, int amount){
        if(Objects.isNull(points) || amount <= 0 || 0 == points.size()){
            throw new IllegalArgumentException();
        } else{
            this.points = points;
            this.amount = amount;
        }
    }

    public List<Coordinate2D> getPoints(){  //Gibt die Liste der Punkte zurück, die diese Form definieren. Achtung: Die zurückgegebene Liste darf nicht modifizierbar, also veränderbar sein.
        return Collections.unmodifiableList(points);
    }

    public int getAmount(){ // Gibt die Anzahl der verfügbaren Formen dieses Typs an
        return amount;
    }

    public boolean reduceAmount() { //Verringert die Anzahl der verfügbaren Formen dieses Typs um eins. Sollten dadurch noch mehr als null Formen vorhanden sein, soll true zurückgegeben werden.
        amount = amount -1;
        if(amount > 0){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public String toString() {
        if(Objects.isNull(name)){
            StringBuilder sb = new StringBuilder();
            sb.append(amount);
            sb.append(":");
            for (int i = 0; i < points.size(); i++) {
                sb.append(points.get(i).toString());
                sb.append(";");
            }
            return sb.toString();
        }else{
            StringBuilder sb = new StringBuilder();
            sb.append(amount);
            sb.append(":");
            sb.append(name);
            return sb.toString();
        }
    }

    @Override
    public boolean equals(Object obj){

        Shape sb = (Shape) obj;
        if(Objects.isNull(sb)){
            return false;
        }
        int size1 = points.size();
        int size2 = sb.points.size();

        if(size1 > size2){
            for (int i = 0; i <size1; i++) {
                if(points.contains(sb.points.get(i))){
                    continue;
                }else {return false;}
            }

        }else {
            for (int i = 0; i <size2; i++) {
                if(sb.points.contains(points.get(i))){
                    continue;
                }else {return false;}
            }
        }
        return true;
    }
    //todo stimmt das jeder Referenzpunkt mit 0,0 initialisiert wird
    public static Shape getPointShape(int amount){ //Laut meiner Auffassung soll jeder Referenypunkt 0,0 beinhalten
        List<Coordinate2D> lc2D = new ArrayList<>();
        Coordinate2D c2D = new Coordinate2D(0,0);

            lc2D.add(c2D);

        return new Shape(lc2D, "point", amount);
    }

    public static Shape getsRowShape(int amount){ //meiner Meinung nach wird hier kein 2D List benoetigt, da dass Program quasi von Referenzpunkt zu Referenzpunkt denkt.
        //es erfaehrt dabei wie viele refernzpunkte es gibt uber das amount
        List<Coordinate2D> lc2D = new ArrayList<>();
        Coordinate2D c2D = new Coordinate2D(0,0);
        Coordinate2D c2DVersion2 = new Coordinate2D(-1,0);

            lc2D.add(c2D);
            lc2D.add(c2DVersion2);

        return new Shape(lc2D, "sRow", amount);
    }

    public static Shape getsColumnShape(int amount){
        List<Coordinate2D> lc2D = new ArrayList<>();
        Coordinate2D c2D = new Coordinate2D(0,0);
        Coordinate2D c2DVersion2 = new Coordinate2D(0,1);

            lc2D.add(c2D);
            lc2D.add(c2DVersion2);

        return new Shape(lc2D, "sColumn", amount);
    }

    public static Shape getRowShape(int amount){
        List<Coordinate2D> lc2D = new ArrayList<>();
        Coordinate2D c2D = new Coordinate2D(0,0);
        Coordinate2D c2DVersion2 = new Coordinate2D(-1,0);
        Coordinate2D c2DVersion3 = new Coordinate2D(1,0);

            lc2D.add(c2D);
            lc2D.add(c2DVersion2);
            lc2D.add(c2DVersion3);

        return new Shape(lc2D, "row", amount);
    }

    public static Shape getColumnShape(int amount){
        List<Coordinate2D> lc2D = new ArrayList<>();
        Coordinate2D c2D = new Coordinate2D(0,0);
        Coordinate2D c2DVersion2 = new Coordinate2D(0,1);
        Coordinate2D c2DVersion3 = new Coordinate2D(0,2);

            lc2D.add(c2D);
            lc2D.add(c2DVersion2);
            lc2D.add(c2DVersion3);

        return new Shape(lc2D, "column", amount);
    }

    public static Shape getUpTShape(int amount){
        List<Coordinate2D> lc2D = new ArrayList<>();
        Coordinate2D c2D = new Coordinate2D(0,0);
        Coordinate2D c2DVersion2 = new Coordinate2D(-1,0);
        Coordinate2D c2DVersion3 = new Coordinate2D(1,0);
        Coordinate2D c2DVersion4 = new Coordinate2D(0,1);

            lc2D.add(c2D);
            lc2D.add(c2DVersion2);
            lc2D.add(c2DVersion3);
            lc2D.add(c2DVersion4);

        return new Shape(lc2D, "upT", amount);
    }

    public static Shape getDownTShape(int amount){
        List<Coordinate2D> lc2D = new ArrayList<>();
        Coordinate2D c2D = new Coordinate2D(0,0);
        Coordinate2D c2DVersion2 = new Coordinate2D(-1,1);
        Coordinate2D c2DVersion3 = new Coordinate2D(1,1);
        Coordinate2D c2DVersion4 = new Coordinate2D(0,1);

            lc2D.add(c2D);
            lc2D.add(c2DVersion2);
            lc2D.add(c2DVersion3);
            lc2D.add(c2DVersion4);

        return new Shape(lc2D, "downT", amount);
    }

    public static Shape getRightTShape(int amount){
        List<Coordinate2D> lc2D = new ArrayList<>();
        Coordinate2D c2D = new Coordinate2D(0,0);
        Coordinate2D c2DVersion2 = new Coordinate2D(0,1);
        Coordinate2D c2DVersion3 = new Coordinate2D(0,2);
        Coordinate2D c2DVersion4 = new Coordinate2D(1,1);

            lc2D.add(c2D);
            lc2D.add(c2DVersion2);
            lc2D.add(c2DVersion3);
            lc2D.add(c2DVersion4);

        return new Shape(lc2D, "rightT", amount);
    }

    public static Shape getLeftTShape(int amount){
        List<Coordinate2D> lc2D = new ArrayList<>();
        Coordinate2D c2D = new Coordinate2D(0,0);
        Coordinate2D c2DVersion2 = new Coordinate2D(0,1);
        Coordinate2D c2DVersion3 = new Coordinate2D(0,2);
        Coordinate2D c2DVersion4 = new Coordinate2D(-1,1);

            lc2D.add(c2D);
            lc2D.add(c2DVersion2);
            lc2D.add(c2DVersion3);
            lc2D.add(c2DVersion4);

        return new Shape(lc2D, "leftT", amount);
    }


}

