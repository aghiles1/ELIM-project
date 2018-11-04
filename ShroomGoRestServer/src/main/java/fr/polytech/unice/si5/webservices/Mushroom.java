package fr.polytech.unice.si5.webservices;

import fr.polytech.unice.si5.entity.MockedDB;
import fr.polytech.unice.si5.entity.MushroomFound;
import fr.polytech.unice.si5.entity.MushroomType;
import fr.polytech.unice.si5.entity.Position;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("shroom")
public class Mushroom {

    private MockedDB mockedDB = MockedDB.mockedDB;

    @POST
    @Path("/add")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addMushroom(@QueryParam("type") String type,@QueryParam("userID") String userID,@QueryParam("longitude") String longitude,@QueryParam("latitude") String latitude){
        System.out.println("Someone try to add a new position");
        Position p = new Position(Float.valueOf(longitude),Float.valueOf(latitude));
        MushroomFound mf = new MushroomFound(MushroomType.valueOf(type),p,userID);
        boolean operation = mockedDB.addMushroom(mf);
        if(operation){
            return Response.status(Response.Status.ACCEPTED).build();
        }
        else{
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("/positions")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMushroomPositions(){
        String response = mockedDB.getMushrooms();
        if(response == ""){
            return "Empty";
        }
        else return response;
        //return Response.ok().entity(mockedDB.getMushrooms()).build();
    }


}

