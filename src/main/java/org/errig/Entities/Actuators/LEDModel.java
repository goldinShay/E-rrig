package org.errig.Entities.Actuators;

public enum LEDModel {
    MARS_HYDRO_TSL_2000(300, 240, 1.31, "140x90 cm", 2.6, 50304),
    KINGLED_KP4000(400, 240, 1.5, "120x80 cm", 2.4, 48000); // estimated values

    private final double powerConsumption;
    private final double inputVoltage;
    private final double ampere;
    private final String surfaceArea;
    private final double ppe;
    private final int lumen;

    LEDModel(double powerConsumption, double inputVoltage, double ampere,
             String surfaceArea, double ppe, int lumen) {
        this.powerConsumption = powerConsumption;
        this.inputVoltage = inputVoltage;
        this.ampere = ampere;
        this.surfaceArea = surfaceArea;
        this.ppe = ppe;
        this.lumen = lumen;
    }

    // Getters...
    public double getPowerConsumption() {
        return powerConsumption;
    }

    public double getInputVoltage() {
        return inputVoltage;
    }

    public double getAmpere() {
        return ampere;
    }

    public String getSurfaceArea() {
        return surfaceArea;
    }

    public double getPpe() {
        return ppe;
    }

    public int getLumen() {
        return lumen;
    }

}
