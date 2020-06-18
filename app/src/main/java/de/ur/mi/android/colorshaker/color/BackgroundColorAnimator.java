package de.ur.mi.android.colorshaker.color;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;

/**
 * Diese Klasse stellt die Funktionalität bereit, um die Änderung der Hintergrundfarbe eines Views
 * schrittweise zu animieren. Grundlage ist ein ValueAnimator, der die stufenweise Änderung zwischen
 * zwei Farbwerten nutzt, um regelmäßig die Hintergrundfarbe eines definierten Views zu ändern.
 */
public class BackgroundColorAnimator {

    // Intendierte Dauer der gesamten Animation
    private static final int DEFAULT_ANIMATION_DURATION = 2000;

    private final ValueAnimator animator;

    // Dem Konstruktor werden das zu ändernde View, die Ausgangsfarbe und die Zielfarbe übergeben
    public BackgroundColorAnimator(final View view, Color currentColor, Color targetColor) {
        // Die Animation wird vorbereitet
        animator = ValueAnimator.ofObject(new ArgbEvaluator(), currentColor.toArgb(), targetColor.toArgb());
        animator.setDuration(DEFAULT_ANIMATION_DURATION);
        // Der hier registrierte Listener wird über die einzelnen Animationsschritte informiert ...
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            // ... und die regelmäßig aufgerufene Callback-Methode wird verwendet, um die Hintergrundfarbe
            // des Views anzupassen.
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                // Der aktuelle Wert der Tranistion zwischen den gegebenen Farben kann zu jedem Zeitpunkt
                // ausgelesen werden.
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
    }

    // Die im Konstruktor erzeugte Animation wird durch den Aufruf dieser Methode gestartet. Der hier
    // übergebene BackgroundColorAnimatorListener wird über das Ende der Animation informiert.
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