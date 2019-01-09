package fr.polytech.unice.si5.entity;

public class MushroomFound {
    private MushroomType type;
    private Position position;
    private String userID;
    private int degradation;

    public MushroomFound(MushroomType type, Position position, String userID, int degradation) {
        this.type = type;
        this.position = position;
        this.userID = userID;
        this.degradation = degradation;
    }

    public MushroomType getType() {
        return type;
    }

    public void setType(MushroomType type) {
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getDegradation() {
        return degradation;
    }

    public void setDegradation(int degradation) {
        this.degradation = degradation;
    }
    @Override
    public String toString() {
        return type.toString() + "-" + position.toString() + "-" + userID;
    }
}
