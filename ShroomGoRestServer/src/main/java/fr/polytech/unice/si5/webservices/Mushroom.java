package fr.polytech.unice.si5.webservices;

import fr.polytech.unice.si5.entity.MockedDB;
import fr.polytech.unice.si5.entity.MushroomFound;
import fr.polytech.unice.si5.entity.MushroomType;
import fr.polytech.unice.si5.entity.Position;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("shroom")
public class Mushroom {

    private MockedDB mockedDB = MockedDB.getInstance();

    @POST
    @Path("/add")
    @Produces(MediaType.TEXT_PLAIN)
    public String addMushroom(@QueryParam("type") String type,@QueryParam("userID") String userID,@QueryParam("longitude") String longitude,@QueryParam("latitude") String latitude,@QueryParam("degradation") int degradation){
        System.out.println("Someone try to add a new position");
        Position p = new Position(Float.valueOf(longitude),Float.valueOf(latitude));
        MushroomFound mf = new MushroomFound(MushroomType.valueOf(type),p,userID,degradation);
        mockedDB.addMushroom(mf);
        return "ok";
    }

    @GET
    @Path("/positions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MushroomFound> getMushroomPositions(){
        return mockedDB.getMushroomsPos();
        //return Response.ok().entity(mockedDB.getMushrooms()).build();
    }
}

