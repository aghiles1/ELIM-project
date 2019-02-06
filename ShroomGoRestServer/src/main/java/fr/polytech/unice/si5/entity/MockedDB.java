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
        addMushroom(new MushroomFound(MushroomType.valueOf("Bolet"),new Position(7.08015462f,43.61507822f),1));
        addMushroom(new MushroomFound(MushroomType.valueOf("Bolet"),new Position(3.14f,3.15f),2));
        addMushroom(new MushroomFound(MushroomType.valueOf("Bolet"),new Position(3.14f,3.15f),3));
        addMushroom(new MushroomFound(MushroomType.valueOf("Bolet"),new Position(3.14f,3.15f),3));
    }
    public List<MushroomFound> getMushroomsPos() {
        return mushroomsPos;
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
