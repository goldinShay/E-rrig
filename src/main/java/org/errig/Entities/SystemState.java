package org.errig.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    /// Grow/Bloom cycle tracking
    private boolean growCycle;
    private boolean bloomCycle;
    private LocalDateTime cycleStartTime;

    // Auto schedule and duration — now using LocalTime
    private LocalTime autoOnTime;      // when cycle starts each day
    private LocalTime autoOffTime;     // when cycle ends each day
    private Integer cycleHoursDuration;
    private Integer cycleDaysDuration;

    // Environmental parameters for cycle logging
    private Double temperature;   // in °C
    private Double ec;            // Electrical conductivity
    private Double ph;            // Acidity/alkalinity


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

    // --- Getters and Setters ---

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

    public int getColorFreq() { return colorFreq; }
    public void setColorFreq(int colorFreq) { this.colorFreq = colorFreq; }

    public int getColorTemp() { return colorTemp; }
    public void setColorTemp(int colorTemp) { this.colorTemp = colorTemp; }

    // 🌱 Robust time handling
    public LocalTime getAutoOnTime() { return autoOnTime; }
    public void setAutoOnTime(LocalTime autoOnTime) { this.autoOnTime = autoOnTime; }

    public LocalTime getAutoOffTime() { return autoOffTime; }
    public void setAutoOffTime(LocalTime autoOffTime) { this.autoOffTime = autoOffTime; }

    public Integer getCycleHoursDuration() { return cycleHoursDuration; }
    public void setCycleHoursDuration(Integer cycleHoursDuration) { this.cycleHoursDuration = cycleHoursDuration; }

    public Integer getCycleDaysDuration() { return cycleDaysDuration; }
    public void setCycleDaysDuration(Integer cycleDaysDuration) { this.cycleDaysDuration = cycleDaysDuration; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getEc() { return ec; }
    public void setEc(Double ec) { this.ec = ec; }

    public Double getPh() { return ph; }
    public void setPh(Double ph) { this.ph = ph; }
}