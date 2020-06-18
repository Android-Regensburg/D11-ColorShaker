package de.ur.mi.android.colorshaker.color;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import androidx.core.graphics.ColorUtils;

import de.ur.mi.android.colorshaker.R;

/**
 * Diese Klasse stellt wichtige Hilfsfunktionen bereit, die an verschiedenen Stellen der Anwendung
 * für die Arbeit mit Farben und Farbrepräsentationen benötigt werden.
 */
public class ColorHelper {

    private static final float DEFAULT_COLOR_RATIO = 0.5f;
    private Context context;
    private Color firstColor;
    private Color secondColor;

    public ColorHelper(Context context) {
        this.context = context;
        firstColor = getDefaultColor();
        secondColor = getDefaultColor();
    }

    public int getDefaultColorID() {
        return R.color.colorDefault;
    }

    public Color getDefaultColor() {
        int defaultColorAsInt = context.getResources().getColor(R.color.colorDefault, context.getTheme());
        return Color.valueOf(defaultColorAsInt);
    }

    public Color getBackgroundColor() {
        int backgroundColorAsInt = context.getResources().getColor(R.color.colorBackground, context.getTheme());
        return Color.valueOf(backgroundColorAsInt);
    }

    public String getHexValueForColor(Color color) {
        // From: https://stackoverflow.com/a/6540378
        return String.format("#%06X", (0xFFFFFF & color.toArgb()));
    }

    // Speichert eine Farbe für die spätere Farbmischung
    public void setFirstColor(Color color) {
        this.firstColor = color;
    }

    // Speichert eine weitere Farbe für die spätere Farbmischung
    public void setSecondColor(Color color) {
        this.secondColor = color;
    }

    // Gibt die Farbe zurück, die sich aus der Mischung der beiden vorher gespeicherten Farben (im RGB-Raum) ergibt
    public Color getCurrentColorMix() {
        int mixColor = ColorUtils.blendARGB(firstColor.toArgb(), secondColor.toArgb(), DEFAULT_COLOR_RATIO);
        return Color.valueOf(mixColor);
    }

    // Erzeugt eine ColorStateList aus der übergebene Farbe, die z.B. für die Änderung der Hintergrundfarbe
    // eines Views über den BackgroundTint benötigt wird.
    public ColorStateList createColorStateList(Color color) {
        return ColorStateList.valueOf(color.toArgb());
    }
}
