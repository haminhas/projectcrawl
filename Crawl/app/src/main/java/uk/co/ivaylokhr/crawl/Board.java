package uk.co.ivaylokhr.crawl;

/**
 * Created by k1464377 on 09/11/15.
 */
public class Board {
    PlayerCup playerCup1, playerCup2;
    PocketCup[] cups;

    public Board() {
        playerCup1 = new PlayerCup();
        playerCup2 = new PlayerCup();
        cups = new PocketCup[14];
    }


}
