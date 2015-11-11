package Model;


public class PocketCup extends Cup {

    public PocketCup() {
        marbles = 7;
    }

    public boolean isEmpty(){
        return marbles == 0;
    }

    public int emptyCup(){
        int toReturn = marbles;
        marbles = 0;
        return toReturn;
    }
}
