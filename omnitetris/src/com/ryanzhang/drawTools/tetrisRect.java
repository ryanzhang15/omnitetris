package com.ryanzhang.drawTools;

import acm.graphics.G3DRect;
import acm.graphics.GRect;
import com.ryanzhang.omnitetris;

import java.awt.*; // * again?? i really dont care.
import java.lang.reflect.GenericArrayType;

public class tetrisRect {

    // a class which can create some sort of a GRect, used for some buttons and also the tetris grid rectangles.
    // hence tetrisRect.

    // I want it too look nice. Therefore, it draws a white border around a G3D rect, of a specified color.

    // some settings
    private Color borderColor; // just grey

    private final Color foregroundColor; // must be initialized to sth else
    private final Color secondColor; // used for... i dont konw
    private final omnitetris instance; // grab some instance of the game, to display it in
    private final int x, y, width, height, borderWidth; // we all know what these are

    private GRect backRect; // to destroy later
    private GRect secondRect; // to destroy!
    private GRect foreRect; // to destroy later

    public tetrisRect(int cx, int cy, int cwidth, int cheight, int cborderWidth, Color col, Color borderCol, omnitetris curGame) { // some initializing tings
        x = cx;
        y = cy;
        width = cwidth-1;
        height = cheight-1;
        foregroundColor = col;
        secondColor = foregroundColor.darker(); // this is quite useful
        borderColor = borderCol;
        borderWidth = cborderWidth;
        instance = curGame; // passed by reference, as its an object. the only good thing about java, i swear
    }

    public void draw() {
        // draws object on the screen
        backRect = new GRect(x, y, width, height);
        backRect.setFilled(true);
        backRect.setColor(borderColor);
        backRect.setFillColor(borderColor); // finished making border

        secondRect = new GRect(x+2*borderWidth, y+2*borderWidth, width-2*borderWidth, height-2*borderWidth);
        secondRect.setFilled(true);
        secondRect.setColor(secondColor);
        secondRect.setFillColor(secondColor);

        foreRect = new GRect(x+borderWidth, y+borderWidth, width-2*borderWidth, height-2*borderWidth); // sum math
        foreRect.setFilled(true);
        foreRect.setColor(foregroundColor);
        foreRect.setFillColor(foregroundColor); // finished making actual rectangle

        instance.add(backRect);
        instance.add(secondRect);
        instance.add(foreRect); // actually add them to the screen
    }

    public void delete() {
        // deletes object from screen and (probably, i hope) frees memory
        // a deconstructor i guess
        instance.remove(backRect);
        instance.remove(secondRect);
        instance.remove(foreRect); // yay!
    }

}
