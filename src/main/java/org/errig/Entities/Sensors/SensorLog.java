package org.errig.Entities.Sensors;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_log")
public class SensorLog {

    // 🔑 Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 📜 Sequential message number
    @Column(name = "message_number")
    private Long messageNumber;

    // 🕒 Timestamp of log entry
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    // 🌱 System states
    @Column(name = "grow_bloom")
    private boolean growBloom;

    @Column(name = "lights_on")
    private boolean lightsOn;

    @Column(name = "pump_active")
    private boolean pumpActive;

    @Column(name = "fan_active")
    private boolean fanActive;

    @Column(name = "blower_active")
    private boolean blowerActive;

    @Column(name = "heater_active")
    private boolean heaterActive;

    // ⚡ Power and environment
    @Column(name = "power_use")
    private double powerUse;

    @Column(name = "air_temp")
    private double airTemp; // internal air temp

    @Column(name = "external_air_temp")
    private double externalAirTemp; // new external temp

    @Column(name = "air_hum")
    private double airHum;

    @Column(name = "air_pres")
    private double airPres;

    @Column(name = "co2ppm")
    private double CO2ppm;

    // 💧 Water metrics
    @Column(name = "water_temp")
    private double waterTemp;

    @Column(name = "waterph", nullable = false)
    private double waterPH;

    @Column(name = "waterec", nullable = false)
    private double waterEC;

    @Column(name = "water_level")
    private double waterLevel;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMessageNumber() { return messageNumber; }
    public void setMessageNumber(Long messageNumber) { this.messageNumber = messageNumber; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isGrowBloom() { return growBloom; }
    public void setGrowBloom(boolean growBloom) { this.growBloom = growBloom; }

    public boolean isLightsOn() { return lightsOn; }
    public void setLightsOn(boolean lightsOn) { this.lightsOn = lightsOn; }

    public boolean isPumpActive() { return pumpActive; }
    public void setPumpActive(boolean pumpActive) { this.pumpActive = pumpActive; }

    public boolean isFanActive() { return fanActive; }
    public void setFanActive(boolean fanActive) { this.fanActive = fanActive; }

    public boolean isBlowerActive() { return blowerActive; }
    public void setBlowerActive(boolean blowerActive) { this.blowerActive = blowerActive; }

    public boolean isHeaterActive() { return heaterActive; }
    public void setHeaterActive(boolean heaterActive) { this.heaterActive = heaterActive; }

    public double getPowerUse() { return powerUse; }
    public void setPowerUse(double powerUse) { this.powerUse = powerUse; }

    public double getAirTemp() { return airTemp; }
    public void setAirTemp(double airTemp) { this.airTemp = airTemp; }

    public double getExternalAirTemp() { return externalAirTemp; }
    public void setExternalAirTemp(double externalAirTemp) { this.externalAirTemp = externalAirTemp; }

    public double getAirHum() { return airHum; }
    public void setAirHum(double airHum) { this.airHum = airHum; }

    public double getAirPres() { return airPres; }
    public void setAirPres(double airPres) { this.airPres = airPres; }

    public double getCO2ppm() { return CO2ppm; }
    public void setCO2ppm(double CO2ppm) { this.CO2ppm = CO2ppm; }

    public double getWaterTemp() { return waterTemp; }
    public void setWaterTemp(double waterTemp) { this.waterTemp = waterTemp; }

    public double getWaterPH() { return waterPH; }
    public void setWaterPH(double waterPH) { this.waterPH = waterPH; }

    public double getWaterEC() { return waterEC; }
    public void setWaterEC(double waterEC) { this.waterEC = waterEC; }

    public double getWaterLevel() { return waterLevel; }
    public void setWaterLevel(double waterLevel) { this.waterLevel = waterLevel; }

    @Override
    public String toString() {
        return "SensorLog{" +
                "id=" + id +
                ", messageNumber=" + messageNumber +
                ", timestamp=" + timestamp +
                ", airTemp=" + airTemp +
                ", externalAirTemp=" + externalAirTemp +
                ", airHum=" + airHum +
                ", airPres=" + airPres +
                ", CO2ppm=" + CO2ppm +
                ", waterTemp=" + waterTemp +
                ", waterPH=" + waterPH +
                ", waterEC=" + waterEC +
                ", waterLevel=" + waterLevel +
                ", powerUse=" + powerUse +
                '}';
    }
}