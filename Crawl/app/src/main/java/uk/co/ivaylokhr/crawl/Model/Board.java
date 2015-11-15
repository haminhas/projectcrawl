package uk.co.ivaylokhr.crawl.Model;

/**
 * A class representing the board of the game.
 */

public class Board {

    private PlayerCup playerCup1, playerCup2;
    private Cup[] cups;

    /**
     * Constructor for the board object. It sets up all the
     * @see Cup
     * @see PocketCup
     * @see PlayerCup
     */
    public Board() {
        playerCup1 = new PlayerCup();
        playerCup2 = new PlayerCup();
        cups = new Cup[16];
        fillCupsArray();
    }

    private void fillCupsArray(){
        for (int i = 0; i < cups.length; i++) {
            //ignore the player cups
            if (i == 7) {
                cups[i] = playerCup1;
            }else if(i == 15){
                cups[i] = playerCup2;
            }
            else {
                cups[i] = new PocketCup() ;
            }
        }
    }

    /**
     * Gets the array representing all the cups on the board
     * @return the array of cups on the board
     * @see Cup
     */
    public Cup[] getCups(){
        return cups;
    }

    /**
     * Gets the PlayerCup of the player, who is playing on the bottom part of the board
     * @return the PlayerCup of the player, who plays on the bottom part of the board
     * @see PlayerCup
     */
    public PlayerCup getPlayerCup1(){
        return playerCup1;
    }

    /**
     * Gets the score of the player, who plays on the bottom part of the board
     * @return the quantity of the bottom player's PlayerCup
     * @see PlayerCup
     */
    public int getPlayerCup1Marbles(){
        return playerCup1.getMarbles();
    }

    /**
     * Gets the PlayerCup of the player, who is playing on the top part of the board
     * @return the PlayerCup of the player, who plays on the top part of the board
     * @see PlayerCup
     */
    public PlayerCup getPlayerCup2(){
        return playerCup2;
    }

    /**
     * Gets the score of the player, who plays on the top part of the board
     * @return the quantity of the top player's PlayerCup
     * @see PlayerCup
     */
    public int getPlayerCup2Marbles(){
        return playerCup2.getMarbles();
    }

}
