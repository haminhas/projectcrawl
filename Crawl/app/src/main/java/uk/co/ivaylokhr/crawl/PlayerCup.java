package uk.co.ivaylokhr.crawl;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Hassan on 19/10/2015.
 */
public class PlayerCup extends Cup {

    public PlayerCup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEnabled(false);
        marbles = 0;
    }
}
