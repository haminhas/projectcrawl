package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * Created by Hassan on 19/10/2015.
 */
public class Board {
    Cup[] cups;

    public Board(Context ct, AttributeSet as){
        cups = new Cup[16];
        for(int i = 0; i < 16; i++){
            Cup c = new Cup(ct,as);
            if(i != 7 || i != 15){
                c = new PocketCup(ct, as);
            }
            else{
                c = new PlayerCup(ct, as);
            }
            cups[i] = c;
        }
    }
}
