package uk.co.ivaylokhr.crawl.Model;

/**
 * Cup is the general class for the properties that hold all the marbles on the board.
 * @see PocketCup
 * @see PlayerCup
 */

public class Cup {

    protected int marbles;

    /**
     * Basic constructor for a Cup objects
     */
    public Cup() {
        marbles = 0;
    }

    /**
     * Get the current quantity of the cup
     * @return the number of marbles in the cup
     */
    public int getMarbles(){
        return marbles;
    }

    /**
     * Increments the number of marbles in the cup
     * @param x the desired number of marbles that needs to be added to the cup
     */
    public void addMarbles(int x){
        marbles += x;
    }

    /**
     * Checcks if the cup has no marbles left
     * @return boolean if the cup is empty
     */
    public boolean isEmpty(){
        return marbles == 0;
    }
}
