package de.ur.mi.android.colorshaker.colorselector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.ur.mi.android.colorshaker.R;

/**
 * Diese Activity erlaubt es NutzerInnen, eine RGB-Farbe durch Veränderung dreier Schieberegeler
 * zu definieren. Die Regler stellen dabei den Wert der einzelnen Farbkanäle dar. Die aktuell ausgewählte
 * Farbe wird in einem Vorschau-View angzeigt. Bestätigen die NutzerInnen die Farbwahl, wird
 * die Activity beendet und die ausgewählte Farbe als Ergebnis gesetzt. Andere Teile der Anwendung
 * können diese Activity nutzen (startActivityForResult) um NutzerInnen eine Möglichkeit zur Auswahl
 * einer RGB-Farbe zu geben. Die ausgwählte Farbe wird dabei als Android-Farbwert (int) zurückgegeben.
 */
public class SelectorActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final int DEFAULT_SEEKBAR_VALUE = 125;
    public static final String USER_SELECTED_COLOR_AS_RGB = "USER_SELECTED_COLOR_AS_RGB";

    private View colorPreview;
    private SeekBar redValueBar;
    private SeekBar greenValueBar;
    private SeekBar blueValueBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    /**
     * Referenziert die notwendigen UI-Komponenten. setzt die Listener auf den Schiebereglern
     * (Seekbar) und Button und initalisiert die Regler mit Default-Werten.
     */
    private void initUI() {
        setContentView(R.layout.activity_colorselector);
        colorPreview = findViewById(R.id.color_preview);
        redValueBar = findViewById(R.id.red_value_bar);
        greenValueBar = findViewById(R.id.green_value_bar);
        blueValueBar = findViewById(R.id.blue_value_bar);
        redValueBar.setOnSeekBarChangeListener(this);
        greenValueBar.setOnSeekBarChangeListener(this);
        blueValueBar.setOnSeekBarChangeListener(this);
        redValueBar.setProgress(DEFAULT_SEEKBAR_VALUE);
        greenValueBar.setProgress(DEFAULT_SEEKBAR_VALUE);
        blueValueBar.setProgress(DEFAULT_SEEKBAR_VALUE);
        Button confirmColorButton = findViewById(R.id.select_color_button);
        confirmColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmColorButtonClicked();
            }
        });
        // Aktualisiert die Farbforschau auf Basis der initalisierte Schieberegeler
        updatePreviewColor();
    }

    /**
     * Liest die aktuellen Werte der drei Schieberegler aus und gibt die entsprechende RGB-Farbe
     * als int-Wert zurück.
     */
    private int getColorFromCurrentSeekbarValues() {
        int red = redValueBar.getProgress();
        int green = greenValueBar.getProgress();
        int blue = blueValueBar.getProgress();
        return Color.rgb(red, green, blue);
    }

    /**
     * Verwendet die aktuellen Werte der Schieberegler um die Farbe im Vorschau-View zu aktualisieren.
     */
    private void updatePreviewColor() {
        int currentColor = getColorFromCurrentSeekbarValues();
        colorPreview.setBackgroundColor(currentColor);
    }

    /**
     * Sobald der Bestätigungs-Button angeklickt wird, wird die aktuell ausgewählte Farbe als Ergebniss
     * der Activity gesetzt und diese beendet. Die ausgewählte Farbe wird so an die aufrufende Activity
     * zurückgegeben.
     */
    private void onConfirmColorButtonClicked() {
        int resultColor = getColorFromCurrentSeekbarValues();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(USER_SELECTED_COLOR_AS_RGB, resultColor);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    /**
     * Wird einer der Schieberegler aktualisiert, wird auch die Farbe im Vorschau-View angepasst.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updatePreviewColor();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
