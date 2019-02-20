package fr.polytech.unice.si5.entity;

import java.util.Map;

public class Cluster {
    private double rayon;
    private Position mushPos;
    private Map<MushroomType, Double> pourcentage;
    private MushroomType dominantType;

    public Cluster(double rayon, Position mushPos, Map<MushroomType, Double> pourcentage, MushroomType dominantType) {
        this.rayon = rayon;
        this.mushPos = mushPos;
        this.pourcentage = pourcentage;
        this.dominantType = dominantType;
    }
    public Cluster(double rayon, Position mushPos) {
        this.rayon = rayon;
        this.mushPos = mushPos;

    }


    public double getRayon() {
        return rayon;
    }

    public void setRayon(double rayon) {
        this.rayon = rayon;
    }

    public Position getMushPos() {
        return mushPos;
    }

    public void setMushPos(Position mushPos) {
        this.mushPos = mushPos;
    }

    public Map<MushroomType, Double> getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Map<MushroomType, Double> pourcentage) {
        this.pourcentage = pourcentage;
    }

    public MushroomType getDominantType() {
        return dominantType;
    }

    public void setDominantType(MushroomType dominantType) {
        this.dominantType = dominantType;
    }

    @Override
    public String toString() {
        return "rayon:" + rayon + ", position:" + mushPos.toString() + ", type:" +dominantType;
    }
}
