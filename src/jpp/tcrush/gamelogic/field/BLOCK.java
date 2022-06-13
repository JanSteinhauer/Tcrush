package jpp.tcrush.gamelogic.field;

import jpp.tcrush.gamelogic.utils.Coordinate2D;
import jpp.tcrush.gamelogic.utils.Move;

import java.util.Collection;
import java.util.Optional;

public class BLOCK implements GameFieldElement{
    private Coordinate2D pos;

    public BLOCK(Coordinate2D pos){
        this.pos = pos;
    }

    @Override
    public Optional<GameFieldItem> getItem() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GameFieldElementType getType() {
        return GameFieldElementType.BLOCK;
    }

    @Override
    public Optional<GameFieldElement> getPredecessor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<GameFieldElement> getSuccessor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Coordinate2D getPos() {
        return pos;
    }

    @Override
    public void setItem(Optional<GameFieldItem> item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPredecessor(GameFieldElement field) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSuccessor(GameFieldElement field) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Collection<Move> moves) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasitemchanged() {
        return false;
    }

    @Override
    public void setitemchanged(boolean b) {

    }



    @Override
    public String toString() {
      return "#";

    }


}
