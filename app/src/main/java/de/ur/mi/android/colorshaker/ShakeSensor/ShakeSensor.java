package de.ur.mi.android.colorshaker.ShakeSensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeSensor implements SensorEventListener {

    private static final float SPEED_THRESHOLD = 10;
    private static final long LISTENER_UPDATE_THRESHOLD_IN_MS = 5000;

    private ShakeSensorListener listener;
    private long lastUpdate;
    private long lastListenerUpdate;
    private float lastY;
    private boolean initialSensorChangeReceived = false;

    public ShakeSensor(Context context, ShakeSensorListener listener) {
        this.listener = listener;
        initShakeSensor(context);
    }

    private void initShakeSensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null) {
            return;
        }
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (initialSensorChangeReceived == false) {
            lastUpdate = System.currentTimeMillis();
            lastListenerUpdate = lastUpdate;
            lastY = event.values[1];
            initialSensorChangeReceived = true;
            return;
        }
        long now = System.currentTimeMillis();
        long updateDelta = now - lastUpdate;
        if(updateDelta < 1000) {
            return;
        }
        float y = event.values[1];
        float speed = Math.abs(lastY - y) / updateDelta * 1000;
        lastUpdate = now;
        lastY = y;
        if (speed > SPEED_THRESHOLD) {
            long listenerUpdateDelta = now - lastListenerUpdate;
            if (listenerUpdateDelta > LISTENER_UPDATE_THRESHOLD_IN_MS) {
                listener.onShakingDetected();
                lastListenerUpdate = now;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
