package com.darkweb.genesisvpn.application.helperManager;

import android.animation.Animator;
import android.os.Handler;

public class DelayedAnimationRepeater implements Animator.AnimatorListener {
    private long delayMillis;

    public DelayedAnimationRepeater(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(final Animator animator) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start delay should only affect initial start, not repeats
                animator.setStartDelay(0);
                animator.start();
            }
        }, delayMillis);
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}