package services;

import beans.Pollution;
import beans.PollutionModels;
import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;

@Path("/Pollution")
public class PollutionRequest {


    @Path("/get")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getPollution(){
        return Response.ok(PollutionModels.getInstance().getListPollution()).build();
    }


    @Path("get/{n}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getLastNPollution(String robotID, @PathParam("n") int n){
        double l = PollutionModels.getInstance().getLastNPollution(robotID, n);
        return Response.ok(l).build();
    }

    @Path("get/timeP/{t1}-{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAvgKm(@PathParam("t1") long t1, @PathParam("t2") long t2){
        return Response.ok(PollutionModels.getInstance().getAvgPollutionTimestamp(t1, t2)).build();
    }


}
