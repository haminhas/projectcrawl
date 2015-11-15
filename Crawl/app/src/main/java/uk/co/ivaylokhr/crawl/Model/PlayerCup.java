package uk.co.ivaylokhr.crawl.Model;

/**
 * The PlayerCup object represents the non-playable Cup objects on the board, that hold information about the player's score.
 * @see Cup
 * @see PocketCup
 */

public class PlayerCup extends Cup {

    /**
     * Constructor for a PlayerCup object
     */
    public PlayerCup() {
        marbles = 0;
    }


}
