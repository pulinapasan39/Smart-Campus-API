package com.smartcampus.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import jakarta.ws.rs.PathParam;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final DataStore dataStore = DataStore.getInstance();

    //register a new sensor
    @POST
    public Response createSensor(Sensor sensor) {
        //validate sensor has a ID
        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Sensor ID is required\"}")
                    .build();
        }
        
        //validate room id exists in the system
        if (sensor.getRoomId() == null || sensor.getRoomId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Room ID is required\"}")
                    .build();
        }
        
        //check if the room actually exists
        if (!dataStore.getRooms().containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room with ID " + sensor.getRoomId() + " does not exist.");
        }
        
        //check if sensor already exists
        if (dataStore.getSensors().containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Sensor with this ID already exists\"}")
                    .build();
        }
        
        //add sensor to the datastore
        dataStore.getSensors().put(sensor.getId(), sensor);
        
        //add sensor id to the sensor list
        dataStore.getRooms().get(sensor.getRoomId()).getSensorIds().add(sensor.getId());
        
        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    //get all sensors 
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensorList = new ArrayList<>(dataStore.getSensors().values());
        
        //filter by type 
        if (type != null && !type.isEmpty()) {
            List<Sensor> filtered = new ArrayList<>();
            for (Sensor sensor : sensorList) {
                if (sensor.getType() != null && sensor.getType().equalsIgnoreCase(type)) {
                    filtered.add(sensor);
                }
            }
            return Response.ok(filtered).build();
        }
        
        return Response.ok(sensorList).build();
    }
    
    //sub-resource locator
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
    
}