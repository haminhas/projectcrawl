package uk.co.ivaylokhr.crawl.Model;

/**
 * Class representing a Player object. Player objects can represent both human or AI players in the game.
 * @see uk.co.ivaylokhr.crawl.Controller.Game
 * @see uk.co.ivaylokhr.crawl.Controller.AIGame
 */
public class Player {
    private String name ;
    private boolean turn ;

    /**
     * Initializes a Player object
     */
    public Player(){
        turn = true;
    }

    /**
     * Gives or takes the option for the player to make a move
     * @param turn boolean which
     */
    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    /**
     * Sets the name of the player
     * @param newName the desired name for the player
     */
    public void setName(String newName) { name = newName; }

    /**
     * Checks if the player is the one who has to make a move
     * @return boolean if the player has to make a move
     */
    public boolean getTurn() {
        return turn;
    }

    /**
     * Gets the name of the player
     * @return name of the player
     */
    public String getName() { return name; }
}
