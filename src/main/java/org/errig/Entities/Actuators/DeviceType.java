package org.errig.Entities.Actuators;

public enum DeviceType {
    LEDLight("LED"),
    WaterPump("wPMP"),
    AirPump("aPMP"),
    Blower("BLWR"),
    Fan("FAN"),
    FogInducer("FOG"),
    CO2Valve("CO2V"),
    Heater("HEAT"),
    AirVentMotor("aVNT");

    private final String idPrefix;

    DeviceType(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public String getIdPrefix() {
        return idPrefix;
    }
}
