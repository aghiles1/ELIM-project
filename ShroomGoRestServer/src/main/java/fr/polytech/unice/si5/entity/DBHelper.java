package fr.polytech.unice.si5.entity;

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
            connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb?user=root&password=asmpedro&useLegacyDatetimeCode=false&serverTimezone=UTC");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void addPosition(MushroomFound mf){
        try {
            Statement statement = connect.createStatement();
            String value = "VALUES (" + mf.getPosition().getLongitude() + "," + mf.getPosition().getLatitude() +
                    "," + mf.getUserID() + ",'" + mf.getType() + "')";
            String query = "INSERT INTO mushroomPos (longitude, latitude, userid, type) " + value ;
            //System.out.println(query);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MushroomFound> getMushroomsPos() throws SQLException {
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from mydb.mushroompos");
        return resultSetToList(resultSet);
    }

    private List<MushroomFound> resultSetToList(ResultSet resultSet) throws SQLException {
        List<MushroomFound> res = new ArrayList<>();
        while (resultSet.next()) {
            int user = resultSet.getInt("id");
            float longitude = resultSet.getFloat("longitude");
            float latitude = resultSet.getFloat("latitude");
            int userid = resultSet.getInt("userid");
            String type = resultSet.getString("type");
            res.add(new MushroomFound(MushroomType.valueOf(type),new Position(longitude,latitude),userid));
        }
        return res;
    }

    public int getUserID(String userName) throws SQLException {
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from mydb.user where username='" + userName +"'");
        resultSet.next();
        return resultSet.getInt("iduser");
    }
}