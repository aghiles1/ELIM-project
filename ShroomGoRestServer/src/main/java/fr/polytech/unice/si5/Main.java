package fr.polytech.unice.si5;

import fr.polytech.unice.si5.entity.DBHelper;
import fr.polytech.unice.si5.entity.MushroomFound;
import fr.polytech.unice.si5.entity.MushroomType;
import fr.polytech.unice.si5.entity.Position;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
A l'attention de Pierre Bonny(profitiez bien des fautes mon chère)
Alors pour le moment on peut faire que deux choses récuperer la liste des postions.
Pour faire ca tu fais la requete hhtp suivante: "http://127.0.0.1:8080/ShroomGo/shroom/positions"
et tu récupère ca:
Bolet-3.14,3.15-bibix44
Bolet-3.14,3.15-bibix44
Bolet-3.14,3.15-bibix44
Bolet-3.14,3.15-bibix44
Donc une donnée par ligne (le type, la position et l'utilisateur) donc normalement pas dure à parser.

Pour l'ajout c'est plus compliqué, j'ai pas réussi à tester. Tu fais une requete http post à l'url suivante : "127.0.0.1:8080/ShroomGo/shroom/add"
Et tu a 4 paramètre à renseigner :
-le type (Bolet,Cepe,Chanterelle)
-l'utilisateur
-longitude
-latitude

Et normalement ca donne une requete qui a cette gueule : "127.0.0.1:8080/ShroomGo/shroom/add?type=Bolet&userID=bibi78&longitude=33.3&latitude=44.6"

 */

public class Main {

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/ShroomGo/";
    private static final String PACKAGE_WS = "fr.polytech.unice.si5.webservices";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example package
        final ResourceConfig rc = new ResourceConfig().packages(PACKAGE_WS);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at %sapplication.wadl\nHit enter to stop it...", BASE_URI));
        DBHelper db = new DBHelper();
        try {
            System.out.println(db.getMushroomsPosSHared("6.97292805","43.81130475","50",2,MushroomType.names()));
            System.out.println(db.getUsers());
            /*List<String> lst = new ArrayList<>();
            lst.add("3");
            lst.add("4");
            int res = db.addPosition(new MushroomFound(MushroomType.Cepe,new Position(43.71751987,6.8094635),1));
            db.sharePosition(1,res,lst);*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.in.read();
        server.shutdown();
    }
}