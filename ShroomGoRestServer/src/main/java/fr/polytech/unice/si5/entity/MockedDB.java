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
        List<MushroomFound> mushroomFounds = new ArrayList<>();
        //mushroomFounds.add();
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
