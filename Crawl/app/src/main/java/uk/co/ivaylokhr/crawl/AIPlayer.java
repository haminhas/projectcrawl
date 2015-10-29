package uk.co.ivaylokhr.crawl;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jahangir on 29/10/2015.
 */
public class AIPlayer {
    private Cup[] cups;
    public AIPlayer(Cup[] cups){
        this.cups=cups;
    }
    public void doMove(){
        ArrayList<PocketCup> temp = new ArrayList<>();
        //Assuming ai player is at the top
        for (int i = 8; i < 15; i++) {
            if (!((PocketCup) cups[i]).isEmpty()) {
                temp.add((PocketCup)cups[i]);
            }
        }
        Random rand = new Random();

        int randCup = rand.nextInt(temp.size());
        PocketCup chosenCup= temp.get(randCup);
        int id = chosenCup.getId();
        int marblesFromEmptiedCup = chosenCup.emptyCup();
        //putMarblesInNextCups(id, marblesFromEmptiedCup);

    }

}
