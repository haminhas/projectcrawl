package uk.co.ivaylokhr.crawl;

/**
 * Created by k1464377 on 09/11/15.
 */
public class Game {
    Player player1, player2;
    Board board;
    //first turn stuff
    private Player firstPlayer;
    private int firstID, secondID;
    private boolean firstHasPlayed, secondHasPlayed;
    private boolean isFirstTurn;

    public Game() {
        player1 = new Player();
        player2 = new Player();
        board = new Board();
        playFirstTurn();
    }

    //a separate method for initializing values of the variables neeeded for first Turn
    private void playFirstTurn(){
        player1.setTurn(false);
        player2.setTurn(false);
        //The player who will have the first play
        firstPlayer = null;
        firstID = -1; secondID = -1;
        firstHasPlayed = false; secondHasPlayed = false;
        isFirstTurn = true;
    }

    public Board getBoard(){
        return board;
    }


}
