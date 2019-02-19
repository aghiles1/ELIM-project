package fr.polytech.unice.si5.entity;

import java.util.ArrayList;
import java.util.List;

public class MockedDB {
    public static MockedDB mockedDB;
    private static MockedDB ourInstance = new MockedDB();
    public static MockedDB getInstance() {
        return ourInstance;
    }
    private static List<MushroomFound> mushroomsPos;

    public MockedDB(){
        mushroomsPos = new ArrayList<>();
        addFakePos();
    }

    public void addMushroom(MushroomFound mushroomFound){
        mushroomsPos.add(mushroomFound);
        System.out.println("shroom added");
    }

    public void addFakePos() {
    }
    public List<MushroomFound> getMushroomsPos() {
        List<MushroomFound> l = new ArrayList<>();
        l.add(new MushroomFound( MushroomType.Bolet,new Position(6.8094635, 43.71751987), 2));
        l.get(0).setDegradation(1);
        l.add(new MushroomFound( MushroomType.Cepe,new Position(6.99485779, 43.79092385), 2));
        l.get(1).setDegradation(2);
        l.add(new MushroomFound( MushroomType.Bolet,new Position(6.97919369, 43.81353446), 2));
        l.get(2).setDegradation(2);
        l.add(new MushroomFound( MushroomType.Morille,new Position(7.00065136, 43.81842714), 2));
        l.get(3).setDegradation(1);
        l.add(new MushroomFound( MushroomType.Cepe,new Position(6.98477268, 43.80529652), 2));
        l.get(4).setDegradation(1);
        l.add(new MushroomFound( MushroomType.Bolet,new Position(6.97292805, 43.81130475), 3));
        l.get(5).setDegradation(1);
        return l;
    }

    public String getMushrooms(){
        String res = "";
        for ( MushroomFound ms : mushroomsPos) {
            res += ms.toString();
            res += "\n";
        }
        return res;
    }
}
