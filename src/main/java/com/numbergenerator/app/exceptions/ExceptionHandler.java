package com.numbergenerator.app.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ExceptionHandler implements ExceptionMapper<BaseException> {
    @Override
    public Response toResponse(BaseException e) {
        if(e.getCode() == 400) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
