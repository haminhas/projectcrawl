package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Hassan on 19/10/2015.
 */
public class Cup extends Button {

    protected int marbles;

    public Cup(Context context, AttributeSet attrs) {
        super(context, attrs);
        marbles = 0;
        //marbles = 7;
    }

    public int getMarbles(){
        return marbles;
    }

    public void addMarbles(int x){
        marbles += x;
    }
}
