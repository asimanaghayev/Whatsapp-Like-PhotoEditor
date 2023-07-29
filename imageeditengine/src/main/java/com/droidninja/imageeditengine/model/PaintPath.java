package com.droidninja.imageeditengine.model;

import java.util.ArrayList;
import java.util.List;

public class PaintPath {
    public List<Coordinate> coordinates = new ArrayList<>();
    public int color;

    public PaintPath(float x, float y, int color) {
        coordinates.add(new Coordinate(x, y));
        this.color = color;
    }

    public static class Coordinate {
        public float x;
        public float y;

        public Coordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}