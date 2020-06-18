package de.ur.mi.android.colorshaker;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.ur.mi.android.colorshaker.ShakeSensor.ShakeSensor;
import de.ur.mi.android.colorshaker.ShakeSensor.ShakeSensorListener;
import de.ur.mi.android.colorshaker.color.BackgroundColorAnimator;
import de.ur.mi.android.colorshaker.color.BackgroundColorAnimatorListener;
import de.ur.mi.android.colorshaker.color.ColorHelper;
import de.ur.mi.android.colorshaker.colorselector.SelectorActivity;

/**
 * Zentrale Activity der ColorShaker-App
 *
 * Diese App erlaubt die Auswahl zweier RGB-Farben, die beim Schütteln des Geräts zu einer neuen
 * Farbmischung kombiniert werden.
 */
public class MainActivity extends AppCompatActivity implements ShakeSensorListener {

    private static final int REQUEST_FIRST_COLOR = 101;
    private static final int REQUEST_SECOND_COLOR = 102;

    private View firstColorSelector;
    private View secondColorSelector;
    private TextView resultColor;
    private ColorHelper colorHelper;
    private ShakeSensor shakeSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initColors();
        initUI();
        initSensor();
        setDefaults();
    }

    private void initColors() {
        colorHelper = new ColorHelper(this);
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
        setBackgroundColorForSelector(firstColorSelector, colorHelper.getDefaultColor());
        setBackgroundColorForSelector(secondColorSelector, colorHelper.getDefaultColor());
        updateColorMix();
    }

    public void initSensor() {
        shakeSensor = new ShakeSensor(this, this);
        shakeSensor.start();
    }
    
    private void setBackgroundColorForSelector(View selector, Color color) {
        ColorStateList colorStateList = colorHelper.createColorStateList(color);
        selector.setBackgroundTintList(colorStateList);
    }

    private void updateColorMix() {
        final Color mixColor = colorHelper.getCurrentColorMix();
        BackgroundColorAnimator animator = new BackgroundColorAnimator(resultColor, colorHelper.getBackgroundColor(), mixColor);
        resultColor.setText("");
        animator.start(new BackgroundColorAnimatorListener() {
            @Override
            public void onAnimationFinished() {
                resultColor.setText(colorHelper.getHexValueForColor(mixColor));
            }
        });
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
        Color color = Color.valueOf(data.getIntExtra(SelectorActivity.USER_SELECTED_COLOR_AS_RGB, colorHelper.getDefaultColorID()));
        switch (requestCode) {
            case REQUEST_FIRST_COLOR:
                setBackgroundColorForSelector(firstColorSelector, color);
                colorHelper.setFirstColor(color);
                break;
            case REQUEST_SECOND_COLOR:
                setBackgroundColorForSelector(secondColorSelector, color);
                colorHelper.setSecondColor(color);
                break;
        }
    }

    @Override
    public void onShakingDetected() {
        updateColorMix();
    }
}
