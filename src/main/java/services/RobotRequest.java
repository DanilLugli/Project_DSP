package services;

import beans.CoordRobot;
import beans.Robot;
import beans.RobotList;
import beans.RobotModels;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/Robot")
public class RobotRequest {

    @Path("addRobot")
    @PUT
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public Response addRobot(Robot robot) {

        CoordRobot result = RobotModels.getInstance().addRobot(robot);
        System.out.println("--> NEW Robot has been added to GREENFIELD with ID: "+ robot.getID() + " in district " + robot.getDistrict());
        if (result != null) {
            return Response.ok().entity(result).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @Path("/getList")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getRobotList() {
        ArrayList<Robot> l = RobotModels.getInstance().getListRobot();
        return Response.ok(new RobotList(l)).build();
    }

    @Path("get/{id}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getById(@PathParam("id") String id) {
        Robot rob = RobotModels.getInstance().getById(id);
        if (rob != null)
            return Response.ok(rob).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("delete/{id}")
    @DELETE
    @Produces({"application/json", "application/xml"})
    public Response deleteById(@PathParam("id") String id) {
        boolean c = RobotModels.getInstance().deleteRobot(id);
        if (c != false)
            return Response.ok(c).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

}
