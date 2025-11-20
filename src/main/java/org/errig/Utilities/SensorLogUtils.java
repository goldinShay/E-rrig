package org.errig.Utilities;

import org.errig.Entities.Sensors.SensorLog;

/**
 * Utility class for rounding and sanitizing SensorLog values.
 *
 * - Provides consistent rounding to one decimal place.
 * - Applies safe defaults to avoid invalid zero/null values.
 * - Designed as a stateless helper (no instantiation).
 */
public final class SensorLogUtils {

    // Prevent instantiation
    private SensorLogUtils() {}

    /**
     * Rounds and sanitizes all numeric values in a SensorLog.
     * Ensures safe defaults for critical fields.
     *
     * @param log the SensorLog to sanitize
     */
    public static void sanitize(SensorLog log) {
        if (log == null) return;

        log.setAirTemp(roundToOneDecimal(log.getAirTemp()));
        log.setAirHum(roundToOneDecimal(log.getAirHum()));
        log.setAirPres(roundToOneDecimal(log.getAirPres()));
        log.setCO2ppm(roundToOneDecimal(log.getCO2ppm()));

        log.setWaterTemp(roundToOneDecimalOrDefault(log.getWaterTemp(), 20.0));
        log.setWaterPH(roundToOneDecimalOrDefault(log.getWaterPH(), 7.0));
        log.setWaterEC(roundToOneDecimalOrDefault(log.getWaterEC(), 1.0));
        log.setWaterLevel(roundToOneDecimalOrDefault(log.getWaterLevel(), 1.0));

        log.setExternalAirTemp(roundToOneDecimalOrDefault(log.getExternalAirTemp(), 15.0));
    }

    /**
     * Rounds a value to one decimal place.
     *
     * @param value the input value
     * @return rounded value
     */
    public static double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    /**
     * Rounds a value to one decimal place, falling back to a safe default
     * if the value is zero or invalid.
     *
     * @param value the input value
     * @param fallback safe default if value is zero
     * @return rounded value or fallback
     */
    public static double roundToOneDecimalOrDefault(double value, double fallback) {
        double rounded = roundToOneDecimal(value);
        return (rounded == 0.0) ? fallback : rounded;
    }
}