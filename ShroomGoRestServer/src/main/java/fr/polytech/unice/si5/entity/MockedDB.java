package fr.polytech.unice.si5.entity;

import java.util.ArrayList;
import java.util.List;

public class MockedDB {
    public static MockedDB mockedDB = new MockedDB();
    private List<MushroomFound> mushroomsPos;

    public MockedDB(){
        mushroomsPos = new ArrayList<>();
        addFakePos();
    }

    public boolean addMushroom(MushroomFound mushroomFound){
        boolean add = mushroomsPos.add(mushroomFound);
        return add;
    }

    public void addFakePos() {
        addMushroom(new MushroomFound(MushroomType.valueOf("Bolet"),new Position(3.14f,3.15f),"bibix44"));
        addMushroom(new MushroomFound(MushroomType.valueOf("Bolet"),new Position(3.14f,3.15f),"bibix44"));
        addMushroom(new MushroomFound(MushroomType.valueOf("Bolet"),new Position(3.14f,3.15f),"bibix44"));
        addMushroom(new MushroomFound(MushroomType.valueOf("Bolet"),new Position(3.14f,3.15f),"bibix44"));
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
