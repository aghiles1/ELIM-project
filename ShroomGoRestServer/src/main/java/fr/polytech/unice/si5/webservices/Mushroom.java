package fr.polytech.unice.si5.webservices;

import fr.polytech.unice.si5.entity.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("shroom")
public class Mushroom {
    private DBHelper dbh = new DBHelper();

    @POST
    @Path("/add")
    @Produces(MediaType.TEXT_PLAIN)
    public String addMushroom(@QueryParam("type") String type,@QueryParam("userID") int userID,@QueryParam("longitude") String longitude,@QueryParam("latitude") String latitude,@QueryParam("users") List<String> users) throws SQLException {
        System.out.println("Someone want to add a new position");
        Position p = new Position(Double.valueOf(longitude),Double.valueOf(latitude));
        MushroomFound mf = new MushroomFound(MushroomType.valueOf(type),p,userID);
        int idPos = dbh.addPosition(mf);
        dbh.sharePosition(userID,idPos,users);
        return "ok";
    }

    @GET
    @Path("/positions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MushroomFound> getMushroomPositions(){
        System.out.println("Someone want the list of all position");
        try {
            return dbh.getMushroomsPos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("/connexion")
    @Produces(MediaType.APPLICATION_JSON)
    public int getUserId(@QueryParam("userName") String userName){
        System.out.println("Someone want to get the id of user: " + userName);
        try {
            return dbh.getUserID(userName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @GET
    @Path("/position")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MushroomFound> getMushroomPosition(@QueryParam("centerLong") String centerLong,@QueryParam("centerLat") String centerLat,@QueryParam("size") String size, @QueryParam("userID") int userID,@QueryParam("array") List<String> array){
        try {
            return dbh.getMushroomsPosSHared(centerLong,centerLat,size,userID,array);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("/getUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers(){
        try {
            return dbh.getUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

