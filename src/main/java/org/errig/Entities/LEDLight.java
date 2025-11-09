package org.errig.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "led_light")
public class LEDLight extends Actuator {
    @Enumerated(EnumType.STRING)
    private LEDModel model;
    private String spectrumGrow = "400–500nm";
    private String spectrumBloom = "750–780nm";
    private String optimalSurfaceArea;
    private double ppe;
    private int lumen;
    private String cycleType = "Grow"; // or "Bloom"

    public LEDLight(LEDModel model) {
        this.model = model;
        this.uniqueId = DeviceIdGenerator.generate(DeviceType.LEDLight); // ✅
        this.powerConsumption = model.getPowerConsumption();
        this.inputVoltage = model.getInputVoltage();
        this.ampere = model.getAmpere();
        this.optimalSurfaceArea = model.getSurfaceArea();
        this.ppe = model.getPpe();
        this.lumen = model.getLumen();

        // ✅ Inherited fields
        this.name = "Mars LED #" + this.uniqueId;
        this.brand = "Mars Hydro";
        this.updatedTS = LocalDateTime.now();
    }
    public LEDLight() {
        // Required by JPA
    }

    // Getters and setters...

    public LEDModel getModel() {
        return model;
    }

    public void setModel(LEDModel model) {
        this.model = model;
    }

    public String getSpectrumGrow() {
        return spectrumGrow;
    }

    public void setSpectrumGrow(String spectrumGrow) {
        this.spectrumGrow = spectrumGrow;
    }

    public String getSpectrumBloom() {
        return spectrumBloom;
    }

    public void setSpectrumBloom(String spectrumBloom) {
        this.spectrumBloom = spectrumBloom;
    }

    public String getOptimalSurfaceArea() {
        return optimalSurfaceArea;
    }

    public void setOptimalSurfaceArea(String optimalSurfaceArea) {
        this.optimalSurfaceArea = optimalSurfaceArea;
    }

    public double getPpe() {
        return ppe;
    }

    public void setPpe(double ppe) {
        this.ppe = ppe;
    }

    public int getLumen() {
        return lumen;
    }

    public void setLumen(int lumen) {
        this.lumen = lumen;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }
}
