package com.ryanzhang.utility;

public class coord { // stores a coordinate point in the form (x, y)

    private int xc;
    private int yc;

    public coord() {
        xc = yc = 0;
    }

    public coord(int sx, int sy) {
        xc = sx;
        yc = sy;
    }

    // getters + setters. very simple.
    public void setX(int sx) {xc = sx;}
    public void setY(int sy) {yc = sy;}
    public void updX(int sx) {xc += sx;};
    public void updY(int sy) {yc += sy;};
    public int x() {return xc;}
    public int y() {return yc;}

}
