package de.ur.mi.android.colorshaker.ShakeSensor;

/**
 * Der ShakeSensor kann Komponenten, die dieses Interface implementieren, über relevante Bewegungen
 * des Geräts informieren.
 */
public interface ShakeSensorListener {

    void onShakingDetected();

}
