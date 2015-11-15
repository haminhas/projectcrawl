package uk.co.ivaylokhr.crawl.Model;

/**
 * The PocketCup object represents the playable Cup objects on the board.
 * @see Cup
 * @see PlayerCup
 */
public class PocketCup extends Cup {

    /**
     * Constructor for the PocketCup object
     */
    public PocketCup() {
        marbles = 7;
    }

    /**
     * Empties the cup by removing every marble in it.
     * @return the number of removed marbles.
     */
    public int emptyCup(){
        int toReturn = marbles;
        marbles = 0;
        return toReturn;
    }
}
