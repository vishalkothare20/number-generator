package com.numbergenerator.app.resources;

import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Service
@Path("/api/")
public class TaskResource {

    @GET
    @Path("/hello")
    public String hello() {
        return "HELLO";
    }

}
