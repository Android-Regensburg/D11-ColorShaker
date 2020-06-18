package de.ur.mi.android.colorshaker.ShakeSensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ShakeSensor implements SensorEventListener {

    private static final float SPEED_THRESHOLD = 400;
    private static final long LISTENER_UPDATE_THRESHOLD_IN_MS = 5000;

    private ShakeSensorListener listener;
    private long lastUpdate;
    private long lastListenerUpdate;
    private float lastX;
    private float lastY;
    private float lastZ;
    private boolean initalSensorChangeReceived = false;

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
        if (initalSensorChangeReceived == false) {
            lastUpdate = System.currentTimeMillis();
            lastListenerUpdate = lastUpdate;
            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];
            initalSensorChangeReceived = true;
            return;
        }
        long now = System.currentTimeMillis();
        long updateDelta = now - lastUpdate;
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float speed = Math.abs((x + y + z) - (lastX - lastY - lastZ)) / updateDelta * 1000;
        lastUpdate = now;
        lastX = x;
        lastY = y;
        lastZ = z;
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
