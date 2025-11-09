package org.errig.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "system_state")
public class SystemState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_state_seq")
    @SequenceGenerator(name = "system_state_seq", sequenceName = "system_state_seq", allocationSize = 1)
    private Long id;

    // System indicators
    private boolean generalPower;
    private String waterLevelStatus;
    private double currentPowerUse;

    // Control modes
    private String lightsMode;
    // Light spectrum and temperature
    private int colorFreq; // in nanometers (e.g. 650 for red, 450 for blue)
    private int colorTemp; // in Kelvin (e.g. 2700K warm, 6500K cool)
    private String pumpsMode;
    private String blowersMode;
    private String fansMode;
    private String fogInducerMode;
    private String heaterMode;
    private String airVentsMode;
    // Grow/Bloom cycle tracking
    private boolean growCycle;
    private boolean bloomCycle;
    private LocalDateTime cycleStartTime;

    // Auto schedule and duration — now using wrapper types
    private Integer autoOnHour;
    private Integer autoOnMinute;
    private Integer autoOffHour;
    private Integer autoOffMinute;
    private Integer cycleHoursDuration;
    private Integer cycleDaysDuration;

    // Real-time actuator states
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

    // LED lights (not persisted)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "system_state_id") // foreign key in LEDLight table
    private List<LEDLight> ledLights = new ArrayList<>();

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isGeneralPower() { return generalPower; }
    public void setGeneralPower(boolean generalPower) { this.generalPower = generalPower; }

    public String getWaterLevelStatus() { return waterLevelStatus; }
    public void setWaterLevelStatus(String waterLevelStatus) { this.waterLevelStatus = waterLevelStatus; }

    public double getCurrentPowerUse() { return currentPowerUse; }
    public void setCurrentPowerUse(double currentPowerUse) { this.currentPowerUse = currentPowerUse; }

    public String getLightsMode() { return lightsMode; }
    public void setLightsMode(String lightsMode) { this.lightsMode = lightsMode; }

    public String getPumpsMode() { return pumpsMode; }
    public void setPumpsMode(String pumpsMode) { this.pumpsMode = pumpsMode; }

    public String getBlowersMode() { return blowersMode; }
    public void setBlowersMode(String blowersMode) { this.blowersMode = blowersMode; }

    public String getFansMode() { return fansMode; }
    public void setFansMode(String fansMode) { this.fansMode = fansMode; }

    public String getFogInducerMode() { return fogInducerMode; }
    public void setFogInducerMode(String fogInducerMode) { this.fogInducerMode = fogInducerMode; }

    public String getHeaterMode() { return heaterMode; }
    public void setHeaterMode(String heaterMode) { this.heaterMode = heaterMode; }

    public String getAirVentsMode() { return airVentsMode; }
    public void setAirVentsMode(String airVentsMode) { this.airVentsMode = airVentsMode; }

    public boolean isLightsOn() { return lightsOn; }
    public void setLightsOn(boolean lightsOn) { this.lightsOn = lightsOn; }

    public boolean isPumpsOn() { return pumpsOn; }
    public void setPumpsOn(boolean pumpsOn) { this.pumpsOn = pumpsOn; }

    public boolean isBlowersOn() { return blowersOn; }
    public void setBlowersOn(boolean blowersOn) { this.blowersOn = blowersOn; }

    public boolean isFansOn() { return fansOn; }
    public void setFansOn(boolean fansOn) { this.fansOn = fansOn; }

    public boolean isFogInducerOn() { return fogInducerOn; }
    public void setFogInducerOn(boolean fogInducerOn) { this.fogInducerOn = fogInducerOn; }

    public boolean isHeaterOn() { return heaterOn; }
    public void setHeaterOn(boolean heaterOn) { this.heaterOn = heaterOn; }

    public boolean isAirVentsOn() { return airVentsOn; }
    public void setAirVentsOn(boolean airVentsOn) { this.airVentsOn = airVentsOn; }

    public boolean isGrowCycle() { return growCycle; }
    public void setGrowCycle(boolean growCycle) { this.growCycle = growCycle; }

    public boolean isBloomCycle() { return bloomCycle; }
    public void setBloomCycle(boolean bloomCycle) { this.bloomCycle = bloomCycle; }

    public LocalDateTime getCycleStartTime() { return cycleStartTime; }
    public void setCycleStartTime(LocalDateTime cycleStartTime) { this.cycleStartTime = cycleStartTime; }

    public List<LEDLight> getLedLights() { return ledLights; }
    public void setLedLights(List<LEDLight> ledLights) { this.ledLights = ledLights; }
    public int getColorFreq() {
        return colorFreq;
    }

    public void setColorFreq(int colorFreq) {
        this.colorFreq = colorFreq;
    }

    public int getColorTemp() {
        return colorTemp;
    }

    public void setColorTemp(int colorTemp) {
        this.colorTemp = colorTemp;
    }

    public int getAutoOnHour() {
        return autoOnHour;
    }

    public void setAutoOnHour(int autoOnHour) {
        this.autoOnHour = autoOnHour;
    }

    public int getAutoOnMinute() {
        return autoOnMinute;
    }

    public void setAutoOnMinute(int autoOnMinute) {
        this.autoOnMinute = autoOnMinute;
    }

    public int getAutoOffHour() {
        return autoOffHour;
    }

    public void setAutoOffHour(int autoOffHour) {
        this.autoOffHour = autoOffHour;
    }

    public int getAutoOffMinute() {
        return autoOffMinute;
    }

    public void setAutoOffMinute(int autoOffMinute) {
        this.autoOffMinute = autoOffMinute;
    }

    public int getCycleHoursDuration() {
        return cycleHoursDuration;
    }

    public void setCycleHoursDuration(int cycleHoursDuration) {
        this.cycleHoursDuration = cycleHoursDuration;
    }

    public int getCycleDaysDuration() {
        return cycleDaysDuration;
    }

    public void setCycleDaysDuration(int cycleDaysDuration) {
        this.cycleDaysDuration = cycleDaysDuration;
    }

    public void setAutoOnHour(Integer autoOnHour) {
        this.autoOnHour = autoOnHour;
    }

    public void setAutoOnMinute(Integer autoOnMinute) {
        this.autoOnMinute = autoOnMinute;
    }

    public void setAutoOffHour(Integer autoOffHour) {
        this.autoOffHour = autoOffHour;
    }

    public void setAutoOffMinute(Integer autoOffMinute) {
        this.autoOffMinute = autoOffMinute;
    }

    public void setCycleHoursDuration(Integer cycleHoursDuration) {
        this.cycleHoursDuration = cycleHoursDuration;
    }

    public void setCycleDaysDuration(Integer cycleDaysDuration) {
        this.cycleDaysDuration = cycleDaysDuration;
    }
}