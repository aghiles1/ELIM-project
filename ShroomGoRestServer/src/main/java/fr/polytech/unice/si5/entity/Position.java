package fr.polytech.unice.si5.entity;

public class Position {
    private Double longitude;
    private Double latitude;

    public Position(Double longitude, Double latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return Double.toString(longitude) + "," + Double.toString(latitude);
    }
}
