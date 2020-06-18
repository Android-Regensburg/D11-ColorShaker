package de.ur.mi.android.colorshaker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import de.ur.mi.android.colorshaker.ShakeSensor.ShakeSensor;
import de.ur.mi.android.colorshaker.ShakeSensor.ShakeSensorListener;
import de.ur.mi.android.colorshaker.colorselector.SelectorActivity;

public class MainActivity extends AppCompatActivity  implements ShakeSensorListener {

    private static final int REQUEST_FIRST_COLOR = 101;
    private static final int REQUEST_SECOND_COLOR = 102;

    private View firstColorSelector;
    private View secondColorSelector;
    private TextView resultColor;

    private Color[] selectedColors;
    private ShakeSensor shakeSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initColors();
        initUI();
        setDefaults();
        initSensor();
    }

    private void initColors() {
        selectedColors = new Color[2];
        selectedColors[0] = Color.valueOf(getResources().getColor(R.color.colorDefault, getTheme()));
        selectedColors[1] = Color.valueOf(getResources().getColor(R.color.colorDefault, getTheme()));
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        firstColorSelector = findViewById(R.id.first_color);
        secondColorSelector = findViewById(R.id.second_color);
        resultColor = findViewById(R.id.result_color);
        firstColorSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestColorFormUser(REQUEST_FIRST_COLOR);
            }
        });
        secondColorSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestColorFormUser(REQUEST_SECOND_COLOR);
            }
        });
    }

    private void setDefaults() {
        firstColorSelector.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDefault, getTheme())));
        secondColorSelector.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDefault, getTheme())));
        updateColorMix();
    }

    private void initSensor() {
        shakeSensor = new ShakeSensor(this, this);
    }

    private void updateColorMix() {
        int mixedColor = ColorUtils.blendARGB(selectedColors[0].toArgb(), selectedColors[1].toArgb(), 0.5f);
        // From: https://stackoverflow.com/a/6540378
        String hexColor = String.format("#%06X", (0xFFFFFF & mixedColor));
        resultColor.setBackgroundColor(mixedColor);
        resultColor.setText(hexColor);
    }

    private void requestColorFormUser(int requestCode) {
        Intent intent = new Intent(this, SelectorActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || (requestCode != REQUEST_FIRST_COLOR && requestCode != REQUEST_SECOND_COLOR)) {
            return;
        }
        int colorValue = data.getIntExtra(SelectorActivity.USER_SELECTED_COLOR_AS_ARGB, R.color.colorDefault);
        switch (requestCode) {
            case REQUEST_FIRST_COLOR:
                firstColorSelector.setBackgroundTintList(ColorStateList.valueOf(colorValue));
                selectedColors[0] = Color.valueOf(colorValue);
                break;
            case REQUEST_SECOND_COLOR:
                secondColorSelector.setBackgroundTintList(ColorStateList.valueOf(colorValue));
                selectedColors[1] = Color.valueOf(colorValue);
                break;

        }
    }

    @Override
    public void onShakingDetected() {
        Log.d("ColorShaker", "Mixing Colors ...");
        updateColorMix();
    }
}
