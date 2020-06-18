package de.ur.mi.android.colorshaker.ShakeSensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeSensor implements SensorEventListener {

    // Zielwert für zeitliche Abstände zwischen Sensor-Updates
    private static final int SLOW_SENSOR_DELAY = 1000000; // Mikrosekunden!
    // Minimale Beschleunigung auf der y-Achse (m/s^2) um Bewegung als Schütteln zu erkennen
    private static final float SENSOR_SPEED_THRESHOLD = 2f;
    // Minimaler zeitlicher Abstand zwischen zwei Aufrufen der Listener-Methoden (
    private static final long LISTENER_UPDATE_THRESHOLD_IN_MS = 5000;

    private ShakeSensorListener listener;
    private Context context;
    private long lastListenerUpdate;
    private float lastSensorValue;
    private boolean initialSensorChangeReceived = false;

    public ShakeSensor(Context context, ShakeSensorListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     * Registrieren des Listeners für den Beschleunigungssensor beim SensorManager . Als Zielwert für
     * den zeitlichen Abstand zwischen zwei Updates wird ein relativ hoher Wert (hier 1 Sekunde)
     * angegeben, da eine höhere Auflösung für diesen Anwendungsfall nicht notwendig ist.
     */
    public void start() {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null) {
            return;
        }
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SLOW_SENSOR_DELAY);
    }

    /**
     * Deregistrieren des Listeners für den Beschleunigungssensor beim SensorManger.
     */
    public void stop() {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Um die Bewegungsgeschwindigkeit einschätzen zu können, benötigen wir ein Wertepaar, dessen
        // Differenz die absolute Geschwindigkeitsveränderung zwischen den beiden Messpunkten darstellt.
        // Der erste Sensorwert, der im Callback eingeht, wird verwendet, um inital einen Vergleichswert
        // zu speichern. Erst beim zweiten Aufruf der Methode (also beim zweiten Sensorwert den wir
        // vom SensorManager erhalten) beginnen wir mit der Berechnung des Delta-Werts (Geschwindigkeits-
        // veränderung).
        if (initialSensorChangeReceived == false) {
            // Der Beschleunigungssensor liefert die aktuellen Werte für die Beschleunigung auf der
            // x-, y- und z-Achse. Wir interessieren uns nur für die y-Achse, da wahrscheinlich ist,
            // das die meisten NutzerInnen das Gerät entlang dieser Achse "schütteln" werden. Der Wert
            // für die y-Achse findet sich im Array an zweiter Stelle.
            lastSensorValue = event.values[1];
            lastListenerUpdate = 0;
            initialSensorChangeReceived = true;
            return;
        }
        float currentSensorValue = event.values[1];
        float speedDelta = Math.abs(lastSensorValue - currentSensorValue);
        lastSensorValue = currentSensorValue;
        // Überschreitet der berechnete Geschwindigkeitswert den angegebene Schwellwert, prüfen wir, ob
        // eine Benarchitigung des ShakeSensorListeners notwendig ist
        if (speedDelta > SENSOR_SPEED_THRESHOLD) {
            // Wir berechnen die Zeit, die seit dem letzten Update des ShakeSensorListeners vergangen ist.
            long now = System.currentTimeMillis();
            long listenerUpdateDelta = System.currentTimeMillis() - lastListenerUpdate;
            // Wenn der berechnete Wert den angegebenen Schwellwert überschreitet, informieren wir den listener
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
