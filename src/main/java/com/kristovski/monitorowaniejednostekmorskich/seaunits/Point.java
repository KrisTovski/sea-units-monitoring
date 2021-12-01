package com.kristovski.monitorowaniejednostekmorskich.seaunits;

public class Point {

    private double y;
    private double x;
    private String name;
    private double destinationY;
    private double destinationX;

    public Point(double y, double x, String name, double destinationY, double destinationX) {
        this.y = y;
        this.x = x;
        this.name = name;
        this.destinationY = destinationY;
        this.destinationX = destinationX;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDestinationY() {
        return destinationY;
    }

    public void setDestinationY(double destinationY) {
        this.destinationY = destinationY;
    }

    public double getDestinationX() {
        return destinationX;
    }

    public void setDestinationX(double destinationX) {
        this.destinationX = destinationX;
    }
}
