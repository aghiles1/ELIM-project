package fr.polytech.unice.si5.entity;

public class MushroomFound {
    private MushroomType type;
    private Position position;
    private String userID;

    public MushroomFound(MushroomType type, Position position, String userID) {
        this.type = type;
        this.position = position;
        this.userID = userID;
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

    @Override
    public String toString() {
        return type.toString() + "-" + position.toString() + "-" + userID;
    }
}
