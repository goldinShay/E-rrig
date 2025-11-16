package org.errig.Entities.Actuators;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class CycleLog {

    @Id
    @SequenceGenerator(name = "cycle_log_seq", sequenceName = "cycle_log_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cycle_log_seq")
    private Long id;

    private String cycleType;           // "Grow" or "Bloom"
    private LocalDateTime updatedTs;    // timestamp of this log entry
    private boolean active;             // is this cycle active right now
    private boolean co2;                // future use; default false

    // 🌱 Robust time control
    private LocalTime powerOnTime;      // when lights/pumps/etc. turn on
    private LocalTime powerOffTime;     // when they turn off

    private int cycleDurationDays;      // days
    private int spectrum;               // e.g., 450 or 660
    private double temp;                // °C
    private double ec;
    private double ph;

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getCycleType() {
        return cycleType;
    }
    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public LocalDateTime getUpdatedTs() {
        return updatedTs;
    }
    public void setUpdatedTs(LocalDateTime updatedTs) {
        this.updatedTs = updatedTs;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCo2() {
        return co2;
    }
    public void setCo2(boolean co2) {
        this.co2 = co2;
    }

    public LocalTime getPowerOnTime() {
        return powerOnTime;
    }
    public void setPowerOnTime(LocalTime powerOnTime) {
        this.powerOnTime = powerOnTime;
    }

    public LocalTime getPowerOffTime() {
        return powerOffTime;
    }
    public void setPowerOffTime(LocalTime powerOffTime) {
        this.powerOffTime = powerOffTime;
    }

    public int getCycleDurationDays() {
        return cycleDurationDays;
    }
    public void setCycleDurationDays(int cycleDurationDays) {
        this.cycleDurationDays = cycleDurationDays;
    }

    public int getSpectrum() {
        return spectrum;
    }
    public void setSpectrum(int spectrum) {
        this.spectrum = spectrum;
    }

    public double getTemp() {
        return temp;
    }
    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getEc() {
        return ec;
    }
    public void setEc(double ec) {
        this.ec = ec;
    }

    public double getPh() {
        return ph;
    }
    public void setPh(double ph) {
        this.ph = ph;
    }
}