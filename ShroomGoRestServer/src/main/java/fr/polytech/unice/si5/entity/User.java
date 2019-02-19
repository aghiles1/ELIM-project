package fr.polytech.unice.si5.entity;

public class User {
    private String name;
    private String id;

    public User(String name, String id){
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return id + ":" + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
