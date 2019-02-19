package fr.polytech.unice.si5.entity;


import java.util.ArrayList;
import java.util.List;

public enum MushroomType {
    Bolet,
    Cepe,
    Chanterelle,
    Morille;

    public static List<String> names() {
        MushroomType[] states = values();
        List<String> names = new ArrayList<>();

        for (int i = 0; i < states.length; i++) {
            names.add(states[i].name());
        }

        return names;
    }
}
