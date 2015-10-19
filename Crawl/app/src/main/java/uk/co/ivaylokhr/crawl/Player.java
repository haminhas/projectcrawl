package uk.co.ivaylokhr.crawl;

/**
 * Created by Hassan on 19/10/2015.
 */
public class Player {
    String name ;
    PlayerCup playerCup;
    boolean turn ;

    public Player(String name, PlayerCup playerCup) {
        this.name = name;
        this.playerCup = playerCup;
        turn = false;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean getTurn() {
        return turn;
    }

    public void increaseScore(int marblesToAdd) {
        playerCup.addMarbles(marblesToAdd);
    }

    public int getScore() {
        return playerCup.getMarbles();
    }
}
