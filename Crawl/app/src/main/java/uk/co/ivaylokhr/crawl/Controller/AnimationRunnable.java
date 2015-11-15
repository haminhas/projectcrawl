package uk.co.ivaylokhr.crawl.Controller;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import uk.co.ivaylokhr.crawl.R;

/**
 * Created by Ivaylo on 12/11/2015.
 */
public class AnimationRunnable implements Runnable{

    private Button view;
    private Animation animation;

    /**
     * loads the animation
     * @param c
     * @param v
     */
    public AnimationRunnable(Context c, Button v){
        view = v;
        animation = AnimationUtils.loadAnimation(c, R.anim.zoomanim);
    }

    /**
     * runs the animation
     */
    @Override
    public void run() {
        view.startAnimation(animation);
    }
}