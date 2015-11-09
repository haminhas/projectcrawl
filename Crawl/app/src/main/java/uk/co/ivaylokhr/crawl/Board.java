package uk.co.ivaylokhr.crawl;

import android.util.Log;
import android.view.View;

/**
 * Created by k1464377 on 09/11/15.
 */
public class Board {
    PlayerCup playerCup1, playerCup2;
    Cup[] cups;

    public Board() {
        playerCup1 = new PlayerCup();
        playerCup2 = new PlayerCup();
        cups = new Cup[16];
        fillCupsArray();
    }
    public Cup[] getCups(){
        return cups;
    }




    public void fillCupsArray(){
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


}
