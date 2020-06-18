package de.ur.mi.android.colorshaker.colorselector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.ur.mi.android.colorshaker.R;

public class SelectorActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final int DEFAULE_SEEKBAR_VALUE = 125;
    public static final String USER_SELECTED_COLOR_AS_ARGB = "USER_SELECTED_COLOR_AS_ARGB";

    private View colorPreview;
    private SeekBar redValueBar;
    private SeekBar greenValueBar;
    private SeekBar blueValueBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_colorselector);
        colorPreview = findViewById(R.id.color_preview);
        redValueBar = findViewById(R.id.red_value_bar);
        greenValueBar = findViewById(R.id.green_value_bar);
        blueValueBar = findViewById(R.id.blue_value_bar);
        redValueBar.setOnSeekBarChangeListener(this);
        greenValueBar.setOnSeekBarChangeListener(this);
        blueValueBar.setOnSeekBarChangeListener(this);
        redValueBar.setProgress(DEFAULE_SEEKBAR_VALUE);
        greenValueBar.setProgress(DEFAULE_SEEKBAR_VALUE);
        blueValueBar.setProgress(DEFAULE_SEEKBAR_VALUE);
        Button confirmColorButton = findViewById(R.id.select_color_button);
        confirmColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onmConfirmColorButtonClicked();
            }
        });
        updatePreviewColor();
    }

    private int getColorFromCurrentSeekbarValues() {
        int red = redValueBar.getProgress();
        int green = greenValueBar.getProgress();
        int blue = blueValueBar.getProgress();
        Log.d("ColorShaker", String.format("Red: %s, Green: %s, Blue: %s", red, green, blue));
        return Color.rgb(red, green, blue);
    }

    private void updatePreviewColor() {
        int currentColor = getColorFromCurrentSeekbarValues();
        colorPreview.setBackgroundColor(currentColor);
    }

    private void onmConfirmColorButtonClicked() {
        int resultColor = getColorFromCurrentSeekbarValues();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(USER_SELECTED_COLOR_AS_ARGB, resultColor);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

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
