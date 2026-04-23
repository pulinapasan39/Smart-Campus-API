package com.smartcampus.api;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    
    //singleton
    private static final DataStore instance = new DataStore();
    
    //in-memory storage
    private final Map<String, Room> rooms = new HashMap<>();
    private final Map<String, Sensor> sensors = new HashMap<>();
    
    private DataStore() {
    }
    
    //get the single instance
    public static DataStore getInstance() {
        return instance;
    }
    
    //room methods
    public Map<String, Room> getRooms() {
        return rooms;
    }
    
    //sensr methods
    public Map<String, Sensor> getSensors() {
        return sensors;
    }
}