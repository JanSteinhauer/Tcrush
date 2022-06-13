package jpp.tcrush.gamelogic.field;

import jpp.tcrush.gamelogic.utils.Coordinate2D;
import jpp.tcrush.gamelogic.utils.Move;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public interface GameFieldElement {
    public Optional<GameFieldItem> getItem();

    public GameFieldElementType getType();

    public Optional<GameFieldElement> getPredecessor();

    public Optional<GameFieldElement> getSuccessor();

    public Coordinate2D getPos();

    public void setItem(Optional<GameFieldItem> item);

    public void setPredecessor(GameFieldElement field);

    public void setSuccessor(GameFieldElement field);

    public void update(Collection<Move> moves);

    public static GameFieldElement createCell(GameFieldItem item, Coordinate2D pos){
        if(Objects.isNull(pos)){
            throw new IllegalArgumentException();
        }else {
            return new CELL(item,pos);
        }
    };

    public static GameFieldElement createBlock(Coordinate2D pos){
        if(Objects.isNull(pos)){
            throw new IllegalArgumentException();
        }else {
            return new BLOCK(pos);
        }

    };

    public static GameFieldElement createFallthrough(Coordinate2D pos){
        if(Objects.isNull(pos)){
            throw new IllegalArgumentException();
        }else {
            return new FALLTHROUGH(pos);
        }
    };

    //todo extra methode von mir, welche aussagt, ob Zelle ihr item gechanged hat
    public boolean hasitemchanged();

    public void setitemchanged(boolean b);


}
