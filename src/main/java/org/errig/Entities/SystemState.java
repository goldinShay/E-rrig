package org.errig.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_state")
public class SystemState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_state_seq")
    @SequenceGenerator(name = "system_state_seq", sequenceName = "system_state_seq", allocationSize = 1)
    private Long id;

    // Read-only indicators
    private boolean generalPower;
    private String waterLevelStatus; // "Too High", "Optimal", "Low", "Too Low"

    // Control modes
    private String lightsMode;       // Off / On / Auto
    private String pumpsMode;
    private String blowersMode;
    private String fansMode;
    private String fogInducerMode;
    private String heaterMode;
    private String airVentsMode;

    // Real-time device states
    @Column(nullable = true)
    private boolean lightsOn;
    @Column(nullable = true)
    private boolean pumpsOn;

    @Column(nullable = true)
    private boolean blowersOn;

    @Column(nullable = true)
    private boolean fansOn;

    @Column(nullable = true)
    private boolean fogInducerOn;

    @Column(nullable = true)
    private boolean heaterOn;

    @Column(nullable = true)
    private boolean airVentsOn;

    // Grow/Bloom cycle
    private String cycleMode;        // "Grow" or "Bloom"
    private LocalDateTime cycleStartTime;

    // Power monitor
    private double currentPowerUse;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isGeneralPower() {
        return generalPower;
    }

    public void setGeneralPower(boolean generalPower) {
        this.generalPower = generalPower;
    }

    public String getWaterLevelStatus() {
        return waterLevelStatus;
    }

    public void setWaterLevelStatus(String waterLevelStatus) {
        this.waterLevelStatus = waterLevelStatus;
    }

    public String getLightsMode() {
        return lightsMode;
    }

    public void setLightsMode(String lightsMode) {
        this.lightsMode = lightsMode;
    }

    public String getPumpsMode() {
        return pumpsMode;
    }

    public void setPumpsMode(String pumpsMode) {
        this.pumpsMode = pumpsMode;
    }

    public String getBlowersMode() {
        return blowersMode;
    }

    public void setBlowersMode(String blowersMode) {
        this.blowersMode = blowersMode;
    }

    public String getFansMode() {
        return fansMode;
    }

    public void setFansMode(String fansMode) {
        this.fansMode = fansMode;
    }

    public String getFogInducerMode() {
        return fogInducerMode;
    }

    public void setFogInducerMode(String fogInducerMode) {
        this.fogInducerMode = fogInducerMode;
    }

    public String getHeaterMode() {
        return heaterMode;
    }

    public void setHeaterMode(String heaterMode) {
        this.heaterMode = heaterMode;
    }

    public String getAirVentsMode() {
        return airVentsMode;
    }

    public void setAirVentsMode(String airVentsMode) {
        this.airVentsMode = airVentsMode;
    }

    public boolean isLightsOn() {
        return lightsOn;
    }

    public void setLightsOn(boolean lightsOn) {
        this.lightsOn = lightsOn;
    }

    public boolean isPumpsOn() {
        return pumpsOn;
    }

    public void setPumpsOn(boolean pumpsOn) {
        this.pumpsOn = pumpsOn;
    }

    public boolean isBlowersOn() {
        return blowersOn;
    }

    public void setBlowersOn(boolean blowersOn) {
        this.blowersOn = blowersOn;
    }

    public boolean isFansOn() {
        return fansOn;
    }

    public void setFansOn(boolean fansOn) {
        this.fansOn = fansOn;
    }

    public boolean isFogInducerOn() {
        return fogInducerOn;
    }

    public void setFogInducerOn(boolean fogInducerOn) {
        this.fogInducerOn = fogInducerOn;
    }

    public boolean isHeaterOn() {
        return heaterOn;
    }

    public void setHeaterOn(boolean heaterOn) {
        this.heaterOn = heaterOn;
    }

    public boolean isAirVentsOn() {
        return airVentsOn;
    }

    public void setAirVentsOn(boolean airVentsOn) {
        this.airVentsOn = airVentsOn;
    }

    public String getCycleMode() {
        return cycleMode;
    }

    public void setCycleMode(String cycleMode) {
        this.cycleMode = cycleMode;
    }

    public LocalDateTime getCycleStartTime() {
        return cycleStartTime;
    }

    public void setCycleStartTime(LocalDateTime cycleStartTime) {
        this.cycleStartTime = cycleStartTime;
    }

    public double getCurrentPowerUse() {
        return currentPowerUse;
    }

    public void setCurrentPowerUse(double currentPowerUse) {
        this.currentPowerUse = currentPowerUse;
    }
}
