package jpp.tcrush.gamelogic;

import jpp.tcrush.gamelogic.field.*;
import jpp.tcrush.gamelogic.utils.Coordinate2D;
import jpp.tcrush.gamelogic.utils.Move;
import jpp.tcrush.parse.ParserUtils;

import javax.swing.text.html.parser.Parser;
import java.util.*;

public class Level {
    public Map<Coordinate2D, GameFieldElement> fieldMap;
    private List<Shape> allowedShapes;

    public static void main(String[] args) {
        List<Shape> shapeList = new ArrayList<>();
        shapeList.add(ParserUtils.parseStringToShape("2:point"));
        shapeList.add(ParserUtils.parseStringToShape("2:sRow"));
        shapeList.add(ParserUtils.parseStringToShape("1:sColumn"));

        Level level = new Level(ParserUtils.parseStringToFieldMap("bg\nbg\nbb\n#+\nbb"), shapeList);
        level.setShapeAndUpdate(ParserUtils.parseStringToShape("1:sRguiow"), new Coordinate2D(1,2));
    }


    public Level(Map<Coordinate2D, GameFieldElement> fieldMap, List<Shape> allowedShapes){
        if(Objects.isNull(fieldMap) || Objects.isNull(allowedShapes)){
            throw new IllegalArgumentException();
        }
        this.fieldMap = fieldMap;
        this.allowedShapes = allowedShapes;
        if(getHeight() < 2 || getWidth() < 2 || allowedShapes.isEmpty()){ //Sollten die Dimensionen dieses Rechtecks hinsichtlich Höhe oder Breite aber kleiner zwei sein, werfen Sie hier eine IllegalArgumentException
            throw new IllegalArgumentException();
        }

    }

    public int getHeight(){ // wir können mit foreach jeden Referenzpunkt aufrufen und dazu noch seine Shape im Gamefield daraus lässt sich eine form bilden
        // danach müssen wir irgendwie schauen, wie man ein Rechteck darumherum legen kann
        int heightPositiv = 0;
        int heightNegativ = 0;
        for (var entry : fieldMap.entrySet()) { // Beachten Sie, dass im Spielfeld (0,0) sich oben links befindet und die y-Koordinate mit sinkender Reihe steigt
            if(entry.getKey().getY() > heightPositiv){
                heightPositiv = entry.getKey().getY();
            }else if(entry.getKey().getY() < heightNegativ){
                heightNegativ = entry.getKey().getY();
            }
        }

        return heightPositiv +1;
    }

    public int getWidth(){
        int heightPositiv = 0;
        int heightNegativ = 0;
        for (var entry : fieldMap.entrySet()) {
            if(entry.getKey().getX() > heightPositiv){
                heightPositiv = entry.getKey().getX();
            }else if( entry.getKey().getX() < heightNegativ){
                heightNegativ = entry.getKey().getX();
            }
        }
        return heightPositiv+1;
    }

    public Map<Coordinate2D, GameFieldElement> getField(){
        Map<Coordinate2D, GameFieldElement> un = Collections.unmodifiableMap(fieldMap);
        return  un;
    }

    public List<Shape> getAllowedShapes(){
        List<Shape> un = Collections.unmodifiableList(allowedShapes);
        return un;
    }

    public Optional<Collection<Coordinate2D>> fit(Shape shape, Coordinate2D position){
        if(Objects.isNull(shape) || Objects.isNull(position)){
            throw new IllegalArgumentException();
        }else{
            /*List<Coordinate2D> coordinate2DList2 = new ArrayList<>();
            List<Coordinate2D> coordinate2DList = shape.getPoints();
            for (int i = 0; i <coordinate2DList.size(); i++) {
                Coordinate2D cd = new Coordinate2D(coordinate2DList.get(i).getX() + position.getX(),-coordinate2DList.get(i).getY() +(position.getY()));

                coordinate2DList2.add(cd);
                //throw new IllegalArgumentException(coordinate2DList.toString() + "lol " + coordinate2DList.get(i).getY() + position.getY()+ "was"+position.getY());
            }*/

            List<Coordinate2D> coordinate2DList2 = new ArrayList<>();
            for(Coordinate2D coorShape: shape.getPoints()){
                coordinate2DList2.add(new Coordinate2D(position.getX() + coorShape.getX(), position.getY() - coorShape.getY()));
            }

            for (int i = 0; i < coordinate2DList2.size(); i++) {
                try {
                    if(fieldMap.get(coordinate2DList2.get(i)).getType() == GameFieldElementType.CELL){
                        //Also ok hier sind nur Cells für das Shape
                    }else{
                        //throw new IllegalArgumentException(coordinate2DList2.toString());
                        return Optional.empty();
                    }
                }catch (Exception e){
                    return Optional.empty();}

            }

            if(fieldMap.get(coordinate2DList2.get(0)).getItem().isEmpty()){
                return Optional.empty();
            }

            GameFieldItemType t = fieldMap.get(coordinate2DList2.get(0)).getItem().get().getType();
            for (int i = 1; i < coordinate2DList2.size(); i++) {
                if(fieldMap.get(coordinate2DList2.get(i)).getItem().get().getType() == t){
                    //Also ok hier sind nur Cells für das Shape
                }else if(fieldMap.get(coordinate2DList2.get(i)).getItem().isEmpty()){
                    //throw new IllegalArgumentException(fieldMap.get(coordinate2DList2.get(i)).getItem().get().getType() +" lol " + i + "lol"+coordinate2DList2.toString());

                    return Optional.empty();
                }
                else {
                    return Optional.empty();
                }
            }
            return Optional.of(coordinate2DList2);
        }
    }

    public boolean isWon(){
        for (var entry : fieldMap.entrySet()) {
            if(entry.getValue().getType() == GameFieldElementType.CELL){// schauen ob es eine Cell ist
                if(entry.getValue().getItem().isPresent()){
                    return false;
                }
            }else {            }
        }
        return true;
    }

    /*public Collection<Move> setShapeAndUpdate(Shape shape, Coordinate2D position){

    }*/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TCrush-LevelDefinition:\n");
        int width = getWidth();
        int height = getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(fieldMap.get(new Coordinate2D(j,i)).toString());
            }
                sb.append("\n");

        }
        sb.append("\n");
        sb.append("Shapes:\n");
        for (int i = 0; i < allowedShapes.size(); i++) {
            if(i== 0){

            }else {
                sb.append("\n");
            }
            sb.append(allowedShapes.get(i).toString());
        }


        return sb.toString();


    }


    public Collection<Move> setShapeAndUpdate(Shape shape, Coordinate2D position){
        if(Objects.isNull(shape) || Objects.isNull(position) || shape.getAmount() <= 0){
            throw new IllegalArgumentException();
        }
        try {
           List<Coordinate2D> coordinate2DList2 = new ArrayList<>();
            for(Coordinate2D coorShape: shape.getPoints()){
                coordinate2DList2.add(new Coordinate2D(position.getX() + coorShape.getX(), position.getY() - coorShape.getY())); // 1+ weil man muss ja immer das element oben drüber nehemen
            }
            for (int i = 0; i < coordinate2DList2.size(); i++) {
                if(!fieldMap.get(coordinate2DList2.get(0)).getItem().get().getType().equals(fieldMap.get(coordinate2DList2.get(i)).getItem().get().getType())){
                    throw new IllegalArgumentException(); //todo hier wurde die Illegal Argumentexception ausgegraut
                }//view did update
            }
            Optional<Collection<Coordinate2D>> fit = fit(shape, position);
            List<Move> moveList = new ArrayList<>(); //Der Rückgabewert enthält dabei alle durch die Löschung der Items entstandenen Bewegungen
            if (fit.isPresent()) {
                List<GameFieldElement> listeDerPredecessors = new ArrayList<>();
                for (int i = 0; i < coordinate2DList2.size(); i++) {

                    GameFieldElement gameFieldElement = fieldMap.get(coordinate2DList2.get(i));
                    if (gameFieldElement.getPredecessor().isPresent()) {
                        listeDerPredecessors.add(gameFieldElement.getPredecessor().get());
                    }
                    if(gameFieldElement.getType() == GameFieldElementType.CELL){
                        ((CELL) gameFieldElement).item = null;// Dabei werden die betroffenen Items gelöscht
                    }
                }
                //todo hier wird versucht immer die niedrigste Y Koordinate zu bekommen
                /*for (int i = 0; i < getWidth(); i++) {
                    int counter = 0;
                    int indexAlt = 0;
                    for (int j = 0; j < listeDerPredecessors.size(); j++) {
                        if(i == listeDerPredecessors.get(j).getPos().getX()){
                           counter += 1;
                           indexAlt = j;
                        }
                        if(counter >= 2){
                            if(listeDerPredecessors.get(i).getPos().getY() > listeDerPredecessors.get(indexAlt).getPos().getY()){
                                listeDerPredecessors.remove(j);
                            }else{
                                listeDerPredecessors.remove(indexAlt);
                                indexAlt = j;
                            }
                        }
                    }

                }*/

                for (int i = 0; i < listeDerPredecessors.size(); i++) {

                        listeDerPredecessors.get(i).update(moveList);

                }


            }
            int index = 0;
            int gibtShape = 0;

                for (int j = 0; j < allowedShapes.size(); j++) {
                    try{
                        if(allowedShapes.get(j).equals(shape)){
                            index = j;
                            gibtShape += 1;
                        }
                    }catch (Exception e){

                    }
                }
                if(gibtShape == 0){
                    throw new IllegalArgumentException();
                }


            for (int i = 0; i < shape.amount; i++) { //Der Rückgabewert enthält dabei alle durch die Löschung der Items entstandenen Bewegungen. Außerdem soll die Menge der erlaubten Formen um die jeweilige reduziert werden
                allowedShapes.get(index).reduceAmount();
                if(allowedShapes.get(index).amount == 0){
                    allowedShapes.remove(index);
                }
            }
            return moveList;
            //throw new IllegalArgumentException(moveList.get(0).getFrom().toString() +"lol"+moveList.get(0).getTo().toString()); //[(1,1) -> (1,2), (1,0) -> (1,1), (0,1) -> (0,2), (0,0) -> (0,1)] so soll es aussehen
        }catch (Exception e){
            throw new IllegalArgumentException();
        }
    }
    public boolean canMakeAnyMove(){
        if(isWon()){
            return false;
        }
        for (int i = 0; i < allowedShapes.size(); i++) {
            try {
                for(Map.Entry<Coordinate2D, GameFieldElement> entry : fieldMap.entrySet()) {
                    Optional<Collection<Coordinate2D>> fit = fit(allowedShapes.get(i),entry.getKey());
                    if(fit.isPresent()){
                        return  true;
                    }

                }
            }catch (Exception e){

            }
         }
        return false;
    }

    public boolean canMakeAnyMove2(){
        if(isWon()){
            return false;
        }
        for (int i = 0; i < allowedShapes.size(); i++) {
            try {
                for(Map.Entry<Coordinate2D, GameFieldElement> entry : fieldMap.entrySet()) {
                    Optional<Collection<Coordinate2D>> fit = fit2(allowedShapes.get(i),entry.getKey());
                    if(fit.isPresent()){
                        return  true;
                    }

                }
            }catch (Exception e){

            }
        }
        return false;
    }

    public Optional<Collection<Coordinate2D>> fit2(Shape shape, Coordinate2D position){
        if(Objects.isNull(shape) || Objects.isNull(position)){
            throw new IllegalArgumentException();
        }else{
            /*List<Coordinate2D> coordinate2DList2 = new ArrayList<>();
            List<Coordinate2D> coordinate2DList = shape.getPoints();
            for (int i = 0; i <coordinate2DList.size(); i++) {
                Coordinate2D cd = new Coordinate2D(coordinate2DList.get(i).getX() + position.getX(),-coordinate2DList.get(i).getY() +(position.getY()));

                coordinate2DList2.add(cd);
                //throw new IllegalArgumentException(coordinate2DList.toString() + "lol " + coordinate2DList.get(i).getY() + position.getY()+ "was"+position.getY());
            }*/

            List<Coordinate2D> coordinate2DList2 = new ArrayList<>();
            for(Coordinate2D coorShape: shape.getPoints()){
                coordinate2DList2.add(new Coordinate2D(position.getX() + coorShape.getX(), position.getY() - coorShape.getY()));
            }

            for (int i = 0; i < coordinate2DList2.size(); i++) {
                try {
                    if(fieldMap.get(coordinate2DList2.get(i)).getType() == GameFieldElementType.CELL){
                        //Also ok hier sind nur Cells für das Shape
                    }else{
                        //throw new IllegalArgumentException(coordinate2DList2.toString());
                        return Optional.empty();
                    }
                }catch (Exception e){
                    return Optional.empty();}

            }

            if(fieldMap.get(coordinate2DList2.get(0)).getItem().isEmpty()){
                return Optional.empty();
            }

            GameFieldItemType t = fieldMap.get(coordinate2DList2.get(0)).getItem().get().getType();

            for (int i = 1; i < coordinate2DList2.size(); i++) {

                if(!(fieldMap.get(coordinate2DList2.get(i)).getType() == GameFieldElementType.CELL)){ //todo diese Zeile wurde hinzugefügt
                    return Optional.empty();
                }

                try {
                    if(fieldMap.get(coordinate2DList2.get(i)).getItem().get().getType() == t){

                    }
                }catch (Exception e){
                    return Optional.empty();
                }


                if(fieldMap.get(coordinate2DList2.get(i)).getItem().get().getType() == t){
                    //Also ok hier sind nur Cells für das Shape
                }else if(fieldMap.get(coordinate2DList2.get(i)).getItem().isEmpty()){
                    //throw new IllegalArgumentException(fieldMap.get(coordinate2DList2.get(i)).getItem().get().getType() +" lol " + i + "lol"+coordinate2DList2.toString());

                    return Optional.empty();
                }
                else {
                    return Optional.empty();
                }
            }
            return Optional.of(coordinate2DList2);
        }
    }
}
