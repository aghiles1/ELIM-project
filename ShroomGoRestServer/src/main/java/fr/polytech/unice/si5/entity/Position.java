package fr.polytech.unice.si5.entity;

public class Position {
    private float longitude;
    private float latitude;

    public Position(float longitude, float latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return Float.toString(longitude) + "," + Float.toString(latitude);
    }
}
