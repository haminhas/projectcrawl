package uk.co.ivaylokhr.crawl.Model;


public class PocketCup extends Cup {

    public PocketCup() {
        marbles = 7;
    }

    public int emptyCup(){
        int toReturn = marbles;
        marbles = 0;
        return toReturn;
    }
}
