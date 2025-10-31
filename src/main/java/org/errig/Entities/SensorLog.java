package org.errig.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_log")
public class SensorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String messageId; // Unique ID for tracking each pulse

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    // System states
    private boolean growBloom;
    private boolean lightsOn;
    private boolean pumpActive;
    private boolean fanActive;
    private boolean blowerActive;
    private boolean heaterActive;

    // Power and environment
    private double powerUse;     // General power usage (watts)
    private double airTemp;      // °C
    private double airHum;       // %
    private double airPres;      // hPa
    private double CO2ppm;       // ppm

    // Water metrics
    private double waterTemp;    // °C
    private double waterPH;      // pH
    private double waterEC;      // µS/cm
    private double waterLevel;   // %

    // Getters and setters...

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    // (All other getters/setters remain unchanged)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isGrowBloom() {
        return growBloom;
    }

    public void setGrowBloom(boolean growBloom) {
        this.growBloom = growBloom;
    }

    public boolean isLightsOn() {
        return lightsOn;
    }

    public void setLightsOn(boolean lightsOn) {
        this.lightsOn = lightsOn;
    }

    public boolean isPumpActive() {
        return pumpActive;
    }

    public void setPumpActive(boolean pumpActive) {
        this.pumpActive = pumpActive;
    }

    public boolean isFanActive() {
        return fanActive;
    }

    public void setFanActive(boolean fanActive) {
        this.fanActive = fanActive;
    }

    public boolean isBlowerActive() {
        return blowerActive;
    }

    public void setBlowerActive(boolean blowerActive) {
        this.blowerActive = blowerActive;
    }

    public boolean isHeaterActive() {
        return heaterActive;
    }

    public void setHeaterActive(boolean heaterActive) {
        this.heaterActive = heaterActive;
    }

    public double getPowerUse() {
        return powerUse;
    }

    public void setPowerUse(double powerUse) {
        this.powerUse = powerUse;
    }

    public double getAirTemp() {
        return airTemp;
    }

    public void setAirTemp(double airTemp) {
        this.airTemp = airTemp;
    }

    public double getAirHum() {
        return airHum;
    }

    public void setAirHum(double airHum) {
        this.airHum = airHum;
    }

    public double getAirPres() {
        return airPres;
    }

    public void setAirPres(double airPres) {
        this.airPres = airPres;
    }

    public double getCO2ppm() {
        return CO2ppm;
    }

    public void setCO2ppm(double CO2ppm) {
        this.CO2ppm = CO2ppm;
    }

    public double getWaterTemp() {
        return waterTemp;
    }

    public void setWaterTemp(double waterTemp) {
        this.waterTemp = waterTemp;
    }

    public double getWaterPH() {
        return waterPH;
    }

    public void setWaterPH(double waterPH) {
        this.waterPH = waterPH;
    }

    public double getWaterEC() {
        return waterEC;
    }

    public void setWaterEC(double waterEC) {
        this.waterEC = waterEC;
    }

    public double getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }

}
