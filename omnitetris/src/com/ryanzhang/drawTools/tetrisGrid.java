package com.ryanzhang.drawTools;

import acm.graphics.GRect;
import com.ryanzhang.omnitetris;

import java.awt.*;
import java.util.ArrayList;

public class tetrisGrid {

    // class for storing and drawing a tetris grid on the screen, given some grid data
    // 0 - No color
    // 1-7 - Has color

    private GRect gridBorder;
    private GRect gridBackground;

    //some settings
    private final int tileSize = 30;
    private final int tileBorderWidth = 3;
    private final int gridBorderWidth = 20;
    public final static int gridWidthTileAmount = 10; // accessed in tetrisMino
    public final static int gridHeightTileAmount = 20;

    public int[][] tetrisData = new int[gridHeightTileAmount][gridWidthTileAmount]; // not bothered for getters and setters, packaging is for noobs
    public int[][] drewData = new int[gridHeightTileAmount][gridWidthTileAmount]; // save the past drawn data, as some tiles are not touched
    private tetrisRect[][] tetrisGrid = new tetrisRect[gridHeightTileAmount][gridWidthTileAmount]; // store the tetrisRect for replacement

    private final int canvasMidX = omnitetris.canvasWidth/2;
    private final int canvasMidY = omnitetris.canvasHeight/2+20;

    private final Color gridBorderColor = omnitetris.backgroundColor.brighter(); // java's Color works pretty well actually

    // instance, for adding and deleting
    private final omnitetris instance;

    public tetrisGrid(omnitetris inst) { // constructor, which takes an instance of omnitetris
        instance = inst;
        for(int i = 0; i < gridHeightTileAmount; ++i) for(int j = 0; j < gridWidthTileAmount; ++j) drewData[i][j] = -1;
    }

    public void setup() {
        gridBorder = new GRect(canvasMidX-gridWidthTileAmount/2.0*tileSize-gridBorderWidth,
                canvasMidY-gridHeightTileAmount/2.0*tileSize-gridBorderWidth,
                tileSize*gridWidthTileAmount+2*gridBorderWidth,
                tileSize*gridHeightTileAmount+2*gridBorderWidth); // some math
        gridBorder.setColor(gridBorderColor);
        gridBorder.setFillColor(gridBorderColor);
        gridBorder.setFilled(true);
        gridBackground = new GRect(canvasMidX-gridWidthTileAmount/2.0*tileSize,
                canvasMidY-gridHeightTileAmount/2.0*tileSize,
                tileSize*gridWidthTileAmount,
                tileSize*gridHeightTileAmount); // some more math
        gridBackground.setColor(Color.black); // a black grid screen
        gridBackground.setFillColor(Color.black);
        gridBackground.setFilled(true);
        instance.add(gridBorder); // add them to the screen
        instance.add(gridBackground);
    }

    private void clearTiles() { // very simple, just remove all the tiles from the screen
        for(int i = 0; i < gridHeightTileAmount; ++i) for(int j = 0; j < gridWidthTileAmount; ++j) tetrisGrid[i][j].delete(); // remove every tilegridWidthTileAmount
    }

    public void drawTiles() { // using tetrisData, draw out the tiles.
        for(int i = 0; i < gridHeightTileAmount; ++i) for(int j = 0; j < gridWidthTileAmount; ++j) if(tetrisData[i][j] != drewData[i][j]) {
            if(drewData[i][j] != -1) tetrisGrid[i][j].delete(); // remove the existing tile
            int y = (int) (canvasMidY - gridHeightTileAmount / 2.0 * tileSize + i * tileSize); // some math stuffs
            int x = (int) (canvasMidX - gridWidthTileAmount / 2.0 * tileSize + j * tileSize); // i could store it as a variable, but i dont care
            tetrisRect cur;
            if(tetrisData[i][j] != 0) {
                // lets draw this tile
                cur = new tetrisRect(x, y, tileSize, tileSize, tileBorderWidth,
                        omnitetris.tetrisColors[tetrisData[i][j] - 1], omnitetris.tetrisBorder, instance); // use our tetrisRect object!!
            } else {
                cur = new tetrisRect(x, y, tileSize, tileSize, tileBorderWidth,
                        Color.black, new Color(49, 49, 49), instance); // use our tetrisRect object!!
            }
            cur.draw();
            drewData[i][j] = tetrisData[i][j];
            tetrisGrid[i][j] = cur;
        }
    }

    public int clearFinishedRows() { // clears finished rows, returns the updated score
        int score = 0;
        boolean hasCleared = false; // check if any row has been cleared in this run. if not, return.
        do {
            hasCleared = false;
            for (int i = 0; i < gridHeightTileAmount; ++i) {
                boolean curRowFull = true; // self-explanatory
                for(int j = 0; j < gridWidthTileAmount; ++j) if(tetrisData[i][j] == 0) curRowFull = false;
                if(!curRowFull) continue; // we only want full rows. if it is not full, keep looking for a full row.
                hasCleared = true; // we've cleared something!
                ++score; // update score
                for(int j = i; j > 0; --j) System.arraycopy(tetrisData[j-1], 0, tetrisData[j], 0, gridWidthTileAmount); // update from prev rows ("dropping")
                for(int j = 0; j < gridWidthTileAmount; ++j) tetrisData[0][j] = 0; // make the first row empty!
                break; // try again later
            }
        } while(hasCleared);
        return score;
    }

    public void delete() {
        clearTiles();
        instance.remove(gridBorder);
        instance.remove(gridBackground);
    }

}
