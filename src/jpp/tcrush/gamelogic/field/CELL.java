package jpp.tcrush.gamelogic.field;

import jpp.tcrush.gamelogic.utils.Coordinate2D;
import jpp.tcrush.gamelogic.utils.Move;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class CELL implements GameFieldElement{
    private Coordinate2D pos;
    public GameFieldItem item;
    private GameFieldElement Predecessor; // Predecessor aktualisieren
    private GameFieldElement Successor;
    boolean keinItem;

    public boolean itemChanged;
    public Coordinate2D poschanged;

    public CELL(GameFieldItem item, Coordinate2D pos){
        if(Objects.isNull(item)){
            keinItem = true;
        }else{
            this.item = item;
        }

        this.pos = pos;
        poschanged = pos;
    }

    @Override
    public Optional<GameFieldItem> getItem() {
        if(Objects.isNull(item)){
            return Optional.empty();
        }else{
            return Optional.of(item);
        }

    }

    @Override
    public GameFieldElementType getType() {
        return GameFieldElementType.CELL;
    }

    @Override
    public Optional<GameFieldElement> getPredecessor() {
        if(Objects.isNull(Predecessor)){
            return Optional.empty();
        }else{
            return Optional.of(Predecessor);
        }

    }

    @Override
    public Optional<GameFieldElement> getSuccessor() {
        if(Objects.isNull(Successor)){
            return Optional.empty();
        }else{
            return Optional.of(Successor);
        }
    }

    @Override
    public Coordinate2D getPos() {
        return pos;
    }

    @Override
    public void setItem(Optional<GameFieldItem> item) {
        if(item.isEmpty()){ // wird dann vom garb collector geholt
            this.item = null; //Möchte man das Item löschen wird hier ein leeres Optional übergeben
        }else{
            this.item = item.get(); //Setzt das Item dieses Elements
        }

    }

    @Override
    public void setPredecessor(GameFieldElement field) {
        if(Objects.isNull(field)){
            throw new IllegalArgumentException();
        }else{
            Predecessor = field;
        }
    }

    @Override
    public void setSuccessor(GameFieldElement field) {
        if(Objects.isNull(field)){
            throw new IllegalArgumentException();
        }else{
            Successor = field;
        }
    }

    @Override
    public void update(Collection<Move> moves) { //todo  Sie diese Methode am besten rekursiv, da sich in jeder Ebene die gleichen Dinge abspielen. Dazu ein Beispiel:
        if(Objects.isNull(item)){
            //Die Abbruchbedingung ist, dass diese Zelle kein Item besitzt
        }else if(getSuccessor().isPresent() && !Successor.getItem().isPresent()) {//Objects.isNull(Successor) == Besitzt diese Zelle einen Nachfolger
                 // hat dieser aktuell kein Item, so muss das Item dieser Zelle weitergegeben werden.
                    //dann hier startmove

                    if(item.bewegungsprozess){

                    }else{
                        item.startMove(pos);
                    }
                    Successor.setItem(Optional.of(item)); //hat dieser aktuell kein Item, so muss das Item dieser Zelle weitergegeben werden
                    //todo was soll das aussagen? : Nach einer Weitergabe wird die Referenz auf das Item zurückgesetzt.
                    item = null;
                    Successor.setitemchanged(true);//Um die aus der Weitergabe resultierenden Bewegungen zu vollenden, muss der Nachfolger, sowie der Vorgänger (insofern vorhanden) aktualisiert werden.
                    Successor.update(moves); // Der Rekursive Teil
                    if(getPredecessor().isPresent()){
                        Predecessor.setitemchanged(true); //Um die aus der Weitergabe resultierenden Bewegungen zu vollenden, muss der Nachfolger, sowie der Vorgänger (insofern vorhanden) aktualisiert werden.
                        Predecessor.update(moves);
                    }else{
                        //macht am besten gar nichts wenn Predecessor nicht gesetzt ist
                    }
        }else if(itemChanged){ //Besitzt diese Zelle keinen Nachfolger oder besitzt dieser aktuell ein Item, so muss überprüft, werden ob diese Zelle ihr Item durch eine vorherige Aktualisierung erhalten hat. Falls ja, muss die daraus entstandene Bewegung der Collection hinzugefügt werden, da sich das Item nun offensichtlich an einem Endpunkt befindet.
            if(item.isOnMove()){
                moves.add(item.endMove(pos));
            }
        }
    }

    @Override
    public boolean hasitemchanged() { //getter method
        return itemChanged;
    }

    @Override
    public void setitemchanged(boolean b) { //setter method
        itemChanged = b;
    }

    @Override
    public String toString() {
        if(Objects.isNull(item)){
            return "n";
        }else{
            switch (item.getType()){
                case BLUE: return "b";
                case GREEN: return "g";
                case RED: return "r";
                case YELLOW: return "y";
                case PURPLE: return "p";
                case BLACK: return "B";
                case ORANGE: return "o";
                default: return "n";
            }
        }


    }
}
