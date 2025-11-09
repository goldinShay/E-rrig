package org.errig.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Actuator {

    @Id
    @Column(name = "unique_id", nullable = false, updatable = false, unique = true)
    protected String uniqueId;

    @Column(name = "is_on")
    protected boolean isOn;

    @Column(name = "mode")
    protected String mode = "Off"; // Default mode

    @Column(name = "name")
    protected String name;

    @Column(name = "brand")
    protected String brand;

    @Column(name = "updated_ts")
    protected LocalDateTime updatedTS;

    @Column(name = "power_consumption")
    protected double powerConsumption;

    @Column(name = "input_voltage")
    protected double inputVoltage;

    @Column(name = "ampere")
    protected double ampere;

    @Column(length = 512)
    private String note;

    // Lifecycle methods
    public boolean isTestable() {
        return !isOn;
    }

    public void beginTest() {
        this.isOn = true;
        this.updatedTS = LocalDateTime.now();
        System.out.println("🧪 Test started...");
    }

    public void endTest() {
        this.isOn = false;
        this.updatedTS = LocalDateTime.now();
        System.out.println("✅ Test ended.");
    }

    // Getters and setters

    public String getDeviceID() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(double powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public double getInputVoltage() {
        return inputVoltage;
    }

    public void setInputVoltage(double inputVoltage) {
        this.inputVoltage = inputVoltage;
    }

    public double getAmpere() {
        return ampere;
    }

    public void setAmpere(double ampere) {
        this.ampere = ampere;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public LocalDateTime getUpdatedTS() {
        return updatedTS;
    }

    public void setUpdatedTS(LocalDateTime updatedTS) {
        this.updatedTS = updatedTS;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}