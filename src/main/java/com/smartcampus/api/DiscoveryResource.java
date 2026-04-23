package com.smartcampus.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {
        Map<String, Object> response = new HashMap<>();
        
        //API versioning info
        response.put("name", "Smart Campus API");
        response.put("version", "1.0");
        response.put("description", "Smart Campus Sensor & Room Management API");
        
        //admin contact
        Map<String, String> contact = new HashMap<>();
        contact.put("name", "Smart Campus Admin");
        contact.put("email", "admin@smartcampus.com");
        response.put("contact", contact);
        
        //resource links 
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        response.put("resources", links);
        
        return Response.ok(response).build();
    }
}