package com.smartcampus.api;

import java.util.UUID;

public class SensorReading {
    
    private String id;
    private long timestamp;
    private double value;
    private String sensorId;
    
    
    public SensorReading() {
    }
   
    public SensorReading(String sensorId, double value) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.value = value;
        this.sensorId = sensorId;
    }
    
    //getters/setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public double getValue() {
        return value;
    }
    
    public void setValue(double value) {
        this.value = value;
    }
    
    public String getSensorId() {
        return sensorId;
    }
    
    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
}