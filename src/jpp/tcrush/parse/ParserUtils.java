package jpp.tcrush.parse;

import jpp.tcrush.gamelogic.Shape;
import jpp.tcrush.gamelogic.field.*;
import jpp.tcrush.gamelogic.utils.Coordinate2D;

import java.util.*;

public class ParserUtils {

    public static void main(String[] args) {
        Map<Coordinate2D, GameFieldElement>  map = parseStringToFieldMap("bb\nb#\nb+\nbb");
        Optional<GameFieldElement> gfe = map.get(new Coordinate2D(3,6)).getSuccessor();


    }

    public static Coordinate2D parseStringToCoordinate(String s){
        if(!s.contains("(") || !s.contains(")") || !s.contains(",") || s.length() > 10 || s.contains(".")){
            throw new InputMismatchException();
        }

        try {
            int zahl1 = 0;//(11,-20)
            int zahl2 = 0;
            s = s.replace("(","");
            s = s.replace(")", "");
            String[] sp = s.split(",");

            zahl1 = Integer.parseInt(sp[0]);
            zahl2 = Integer.parseInt(sp[1]);

/*
            if(sp[0].length() == 3){
                StringBuilder sb = new StringBuilder();
                sb.append(sp[0]);
                String zahl = sb.substring(sb.length(), sb.length());
                zahl1 = Integer.parseInt(zahl);
            }else{
                StringBuilder sb = new StringBuilder();
                sb.append(sp[0]);
                String zahl = sb.substring(sb.length()-1, sb.length());
                zahl1 = Integer.parseInt(zahl);
            }
            if(sp[1].length() == 3){
                StringBuilder sb = new StringBuilder();
                sb.append(sp[1]);
                String zahl = sb.substring(sb.length(), sb.length());
                zahl2 = Integer.parseInt(zahl);
            }else{
                StringBuilder sb = new StringBuilder();
                sb.append(sp[1]);
                String zahl = sb.substring(sb.length()-1, sb.length());
                zahl2 = Integer.parseInt(zahl);
            }*/
            return new Coordinate2D(zahl1, zahl2);
        }catch (InputMismatchException e){
            throw new InputMismatchException();
        }
    }

    public static Shape parseStringToShape(String s){
        try {
            StringBuilder sb = new StringBuilder(); //6:row

            int amount = s.charAt(0) - '0';
            sb.append(s);
            sb.replace(0,2,""); // soll die Zahl und den Doppelpunkt entfernen
            switch (sb.toString()){
                case "point": return Shape.getPointShape(amount);
                case "sRow": return Shape.getsRowShape(amount);
                case "sColumn": return Shape.getsColumnShape(amount);
                case "row": return Shape.getRowShape(amount);
                case "column": return Shape.getColumnShape(amount);
                case "upT": return Shape.getUpTShape(amount);
                case "downT": return Shape.getDownTShape(amount);
                case "rightT": return Shape.getRightTShape(amount);
                case "leftT": return Shape.getLeftTShape(amount);
                default: return MehrereKoordinaten(sb.toString(), amount);
            }
        }catch (InputMismatchException e){
            throw new InputMismatchException();
        }
    }

    public static Shape MehrereKoordinaten(String s, int amount){ // 8:(6,-5);(-6,-1);(0,7);(3,-9);(9,-1);
        List<Coordinate2D> Koordinaten = new ArrayList<>();
        char last = s.charAt(s.length()-1);
        StringBuilder sb = new StringBuilder();
        sb.append(last);
        if(!(sb.toString().equals(";") )){
            throw new InputMismatchException();
        }
        if(s.contains(";")){
            String[] Koor = s.split(";");
            for (int i = 0; i < Koor.length; i++) {
                Koordinaten.add(parseStringToCoordinate(Koor[i]));
            }
            return new Shape(Koordinaten, amount);
        }else{
            throw new InputMismatchException();
        } // 8:(-4,2);(2,-10);(5,-6);(-9,2);(-4,-2) caused eine Exceptin

    }

    public static Map<Coordinate2D, GameFieldElement> parseStringToFieldMap(String s){
        HashMap<Coordinate2D, GameFieldElement> hashMap = new HashMap<>();
        String[] row = s.split("\n");
        try {
           List<GameFieldElement> gameFieldElementList = new ArrayList<>();
           List<Coordinate2D> coordinate2DList = new ArrayList<>();

            for (int i = 0; i < row.length; i++) {


                for (int j = 0; j < row[0].length(); j++) { // mit row null soll festgestellt werden das es ein Rechteck wird, falls dass ncht der fall seinen soll, soll der catchblock aktiv werden


                    GameFieldElement gameFieldElement;
                    switch (row[i].charAt(j)){ //todo probleme mit set successor:  Cell at (0,0) has no successor
                        case 'b': gameFieldElement = new CELL(new GameFieldItem(GameFieldItemType.BLUE),new Coordinate2D(j,i));break;
                        case 'g': gameFieldElement = new CELL(new GameFieldItem(GameFieldItemType.GREEN),new Coordinate2D(j,i)); break;
                        case 'r': gameFieldElement = new CELL(new GameFieldItem(GameFieldItemType.RED),new Coordinate2D(j,i)); break;
                        case 'y': gameFieldElement = new CELL(new GameFieldItem(GameFieldItemType.YELLOW),new Coordinate2D(j,i)); break;
                        case 'p': gameFieldElement = new CELL(new GameFieldItem(GameFieldItemType.PURPLE),new Coordinate2D(j,i)); break;
                        case 'B': gameFieldElement = new CELL(new GameFieldItem(GameFieldItemType.BLACK),new Coordinate2D(j,i)); break;
                        case 'o': gameFieldElement = new CELL(new GameFieldItem(GameFieldItemType.ORANGE),new Coordinate2D(j,i)); break;
                        case 'n': gameFieldElement = new CELL(null,new Coordinate2D(j,i)); break;
                        case '+': gameFieldElement = new FALLTHROUGH(new Coordinate2D(j,i)); break;
                        case '#': gameFieldElement = new BLOCK(new Coordinate2D(j,i)); break;
                        default:
                            throw new InputMismatchException();
                    }

                    hashMap.put(new Coordinate2D(j,i), gameFieldElement);
                    gameFieldElementList.add(gameFieldElement);
                    coordinate2DList.add(new Coordinate2D(j,i));

                }

            }
            int counter = 0;
            int height = row.length;
            int width = row[0].length();
            int heightCounter = 0;
            int widthCounter = 0;
            for (int i = 0; i < gameFieldElementList.size() ; i++) {
                if(heightCounter == 0){ // hier für die oberste Zeile
                    if (hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.FALLTHROUGH) { //hier gibt es keinen Vorgänger da wir ja in Zeile 0 sind
                        throw new InputMismatchException();
                    }else if (hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.BLOCK){
                        // wenn es ein Block ist mache nichts
                    }
                    else{
                        if(hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.CELL){
                            for (int j = 0; j < height; j++) {
                                if(hashMap.get(coordinate2DList.get(i+width*(j+1))).getType() == GameFieldElementType.BLOCK){
                                    break;
                                }
                                if(hashMap.get(coordinate2DList.get(i+width*(j+1))).getType() == GameFieldElementType.CELL){
                                    hashMap.get(coordinate2DList.get(i)).setSuccessor(hashMap.get(coordinate2DList.get(i+width*(j+1))));
                                    break;
                                }
                            }

                        }
                    }
                    /*counter += 1;*/
                } else if (heightCounter == height-1) {


                    if (hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.FALLTHROUGH) { //hier gibt es keinen Vorgänger da wir ja in Zeile 0 sind
                        throw new InputMismatchException();
                    }else if (hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.BLOCK){
                        // wenn es ein Block ist mache nichts
                    }
                    else{

                            for (int j = 0; j < height; j++) {
                                if(hashMap.get(coordinate2DList.get(i-width*(j+1))).getType() == GameFieldElementType.BLOCK){
                                    break;
                                }
                                if(hashMap.get(coordinate2DList.get(i-width*(j+1))).getType() == GameFieldElementType.CELL){
                                    hashMap.get(coordinate2DList.get(i)).setPredecessor(hashMap.get(coordinate2DList.get(i-width*(j+1))));
                                    break;
                                }
                            }


                    }
                } else{ // objekt sitzt in der Mitte
                    if (gameFieldElementList.get(i).getType() == GameFieldElementType.FALLTHROUGH) {
                        if (gameFieldElementList.get(i + width).getType() == GameFieldElementType.BLOCK || gameFieldElementList.get(i - width).getType() == GameFieldElementType.BLOCK) {
                            throw new InputMismatchException();
                        }

                        /*if(hashMap.get(coordinate2DList.get(i+width)).getType() == GameFieldElementType.CELL && hashMap.get(coordinate2DList.get(i-width)).getType() == GameFieldElementType.CELL){
                            // wenn über und unten eine Cell ist dann ist alles gut
                        }else{throw new InputMismatchException();}*/
                    }else if (gameFieldElementList.get(i).getType() == GameFieldElementType.BLOCK){
                        // wenn es ein Block ist mache nichts
                    }
                    else{
                        if (hashMap.get(coordinate2DList.get(i + width)).getType() == GameFieldElementType.CELL || hashMap.get(coordinate2DList.get(i + width)).getType() == GameFieldElementType.FALLTHROUGH) {
                            for (int j = 0; j < height; j++) {
                                if(hashMap.get(coordinate2DList.get(i+width*(j+1))).getType() == GameFieldElementType.BLOCK){
                                    break;
                                }
                                if(hashMap.get(coordinate2DList.get(i+width*(j+1))).getType() == GameFieldElementType.CELL){
                                    hashMap.get(coordinate2DList.get(i)).setSuccessor(hashMap.get(coordinate2DList.get(i+width*(j+1))));
                                    break;
                                }
                            }
                        }
                        if (hashMap.get(coordinate2DList.get(i - width)).getType() == GameFieldElementType.CELL || hashMap.get(coordinate2DList.get(i - width)).getType() == GameFieldElementType.FALLTHROUGH) {
                            for (int j = 0; j < height; j++) {
                                if(hashMap.get(coordinate2DList.get(i-width*(j+1))).getType() == GameFieldElementType.BLOCK){
                                    break;
                                }
                                if(hashMap.get(coordinate2DList.get(i-width*(j+1))).getType() == GameFieldElementType.CELL){
                                    hashMap.get(coordinate2DList.get(i)).setPredecessor(hashMap.get(coordinate2DList.get(i-width*(j+1))));
                                    break;
                                }
                            }
                        }
                    }
                }
                /*
                else if (counter % width == 0) {
                    counter = 0;
                    if (hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.FALLTHROUGH) { // quasi situation am rand
                        throw new InputMismatchException();
                    } else {
                        hashMap.get(coordinate2DList.get(i)).setPredecessor(hashMap.get(coordinate2DList.get(i - 1)));
                    }

                } else if (counter == 1) {
                    if (hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.FALLTHROUGH) {

                    } else {
                        hashMap.get(coordinate2DList.get(i)).setSuccessor(hashMap.get(coordinate2DList.get(i + 1)));
                    }


                } else {
                    if (hashMap.get(coordinate2DList.get(i)).getType() == GameFieldElementType.FALLTHROUGH) {

                    } else {
                        hashMap.get(coordinate2DList.get(i)).setSuccessor(hashMap.get(coordinate2DList.get(i + 1)));
                        hashMap.get(coordinate2DList.get(i)).setPredecessor(hashMap.get(coordinate2DList.get(i - 1)));

                    }

                }*/
                //counter += 1;
                if((i+1) % width == 0 && i != 0){
                    heightCounter += 1;
                }

            }
            /*hashMap.forEach((k,v) -> {
                if(Objects.isNull(v)){
                    hashMap.put(k, v.setItem(GameFieldItemType.))
                }
            });*/

            return hashMap;
        }catch (Exception e){
            throw new InputMismatchException();
        }
    }
}

