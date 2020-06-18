package de.ur.mi.android.colorshaker.color;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;

public class BackgroundColorAnimator {

    private static final int DEFAULT_ANIMATION_DURATION = 2000;

    private final ValueAnimator animator;

    public BackgroundColorAnimator(final View view, Color currentColor, Color targetColor) {
        animator = ValueAnimator.ofObject(new ArgbEvaluator(), currentColor.toArgb(), targetColor.toArgb());
        animator.setDuration(DEFAULT_ANIMATION_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
    }

    public void start(final BackgroundColorAnimatorListener listener) {
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationFinished();
            }
        });
        animator.start();
    }


}