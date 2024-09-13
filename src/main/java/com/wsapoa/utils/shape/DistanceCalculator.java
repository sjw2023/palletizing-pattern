package com.wsapoa.utils.shape;

public class DistanceCalculator {
    public static double calculateDistance(Coordinate c1, Coordinate c2) {
        return Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2) +
                Math.pow(c1.getY() - c2.getY(), 2) +
                Math.pow(c1.getZ() - c2.getZ(), 2));
    }
}