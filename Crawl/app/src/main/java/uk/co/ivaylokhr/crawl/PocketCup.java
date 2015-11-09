package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Hassan on 19/10/2015.
 */
public class PocketCup extends Cup {

    public PocketCup() {
        marbles = 7;
    }

    public boolean isEmpty(){
        return marbles == 0;
    }

    public int emptyCup(){
        int toReturn = marbles;
        marbles = 0;
        return toReturn;
    }
}
