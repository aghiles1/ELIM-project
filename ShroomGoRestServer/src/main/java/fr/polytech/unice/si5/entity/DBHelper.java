package fr.polytech.unice.si5.entity;

import fr.polytech.unice.si5.kmeans.KMeans;
import fr.polytech.unice.si5.webservices.Mushroom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private Connection connect = null;

    public DBHelper(){
        try {
            // This will load the MySQL driver, each DB has its own driver
            //Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb?user=root&password=root&useLegacyDatetimeCode=false&serverTimezone=UTC");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public int addPosition(MushroomFound mf){
        try {
            Statement statement = connect.createStatement();
            String value = "VALUES (" + mf.getPosition().getLongitude() + "," + mf.getPosition().getLatitude() +
                    "," + mf.getUserID() + ",'" + mf.getType() + "')";
            String query = "INSERT INTO mushroomPos (longitude, latitude, userid, type) " + value ;
            statement.executeUpdate(query);
            ResultSet resultSet = statement.executeQuery( "select * from mydb.mushroompos ORDER BY id DESC LIMIT 1");
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void sharePosition(int sharer, int posId,List<String> receivers) throws SQLException {
        Statement statement = connect.createStatement();
        for (String id: receivers) {
            String query = "INSERT INTO shared values ("+ sharer +","+ id +","+ posId+", 1);";
            statement.executeUpdate(query);
        }
    }

    public List<MushroomFound> getMushroomsPos() throws SQLException {
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from mydb.mushroompos");
        return resultSetToList(resultSet);
    }

    public List<Cluster> getMushroomsPosSHared(String centerLong,String centerLat,String size,int userID,List<String> array) throws SQLException {
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from shared, mushroompos\n" +
                "where shared.receiver = " + userID + " and shared.position = mushroompos.id and shared.sharer = mushroompos.userid;");

        Position center = new Position(Double.valueOf(centerLong),Double.valueOf(centerLat));
        List<MushroomFound> lst = resultSetSHaredToList(resultSet);
        List<MushroomFound> res = new ArrayList<>();
        Double radius = Double.valueOf(size);

        for (MushroomFound mushroom: lst) {
            Double distance = distanceInKmBetweenEarthCoordinates(center,mushroom.getPosition());
            if(distance < radius && array.contains(mushroom.getType().toString()))
                res.add(mushroom);
        }

        resultSet = statement.executeQuery("select * from mushroompos where userid = " + userID + " ;");
        lst = resultSetToList(resultSet);

        for (MushroomFound mushroom: lst) {
            Double distance = distanceInKmBetweenEarthCoordinates(center,mushroom.getPosition());
            if(distance < radius && array.contains(mushroom.getType().toString()))
                res.add(mushroom);
        }
        System.out.println(res);
        return KMeans.getKmeansByType(res);
    }

    private List<MushroomFound> resultSetToList(ResultSet resultSet) throws SQLException {
        List<MushroomFound> res = new ArrayList<>();
        while (resultSet.next()){
            int user = resultSet.getInt("id");
            Double longitude = resultSet.getDouble("longitude");
            Double latitude = resultSet.getDouble("latitude");
            int userid = resultSet.getInt("userid");
            String type = resultSet.getString("type");

            MushroomFound mush = new MushroomFound(MushroomType.valueOf(type),new Position(longitude,latitude),userid);
            mush.setDegradation(0);
            res.add(mush);
        }
        return res;
    }

    private List<MushroomFound> resultSetSHaredToList(ResultSet resultSet) throws SQLException {
        List<MushroomFound> res = new ArrayList<>();
        while (resultSet.next()) {
            int user = resultSet.getInt("id");
            Double longitude = resultSet.getDouble("longitude");
            Double latitude = resultSet.getDouble("latitude");
            int userid = resultSet.getInt("userid");
            String type = resultSet.getString("type");
            int degradation = resultSet.getInt("degradation");

            MushroomFound mush = new MushroomFound(MushroomType.valueOf(type),new Position(longitude,latitude),userid);
            mush.setDegradation(degradation);
            res.add(mush);
        }
        return res;
    }

    public int getUserID(String userName) throws SQLException {
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from mydb.user where username='" + userName +"'");
        resultSet.next();

        return resultSet.getInt("iduser");
    }

    public List<User> getUsers() throws SQLException{
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from mydb.user");
        List<User> res = new ArrayList<>();
        while (resultSet.next()) {
            res.add(new User(resultSet.getString("username"),String.valueOf(resultSet.getInt("iduser"))));
        }
        return res;
    }

    public double degreesToRadians(Double degrees) {
        return degrees * Math.PI / 180;
    }

    public double distanceInKmBetweenEarthCoordinates(Position pos1, Position pos2){
        int earthRadiusKm = 6371;

        double dLat = degreesToRadians(pos2.getLatitude()-pos1.getLatitude());
        double dLon = degreesToRadians(pos2.getLongitude()-pos1.getLongitude());

        double lat1d = degreesToRadians(pos1.getLatitude());
        double lat2d = degreesToRadians(pos2.getLatitude());

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1d) * Math.cos(lat2d);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }
}