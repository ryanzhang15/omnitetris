package com.ryanzhang.drawTools;

import com.ryanzhang.panic.panic;
import com.ryanzhang.utility.coord;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class tetrisMino { // stores a tetromino

    private ArrayList<coord> tiles; // the tiles that it takes up
    private final int col; // its color
    private int rotState; // its rotation state

    public tetrisMino(int initCol) {
        col = initCol;
        rotState = 0;
        tiles = new ArrayList<coord>();
        switch (col) { // so much hard-coding... but what can i do
            // actually java's enhanced switch statement is not bad... but i barely use switch in the first place
            case 1 -> {
                tiles.add(new coord(0, 3));
                tiles.add(new coord(0, 4));
                tiles.add(new coord(0, 5));
                tiles.add(new coord(0, 6));
            }
            case 2 -> {
                tiles.add(new coord(0, 3));
                tiles.add(new coord(1, 3));
                tiles.add(new coord(1, 4));
                tiles.add(new coord(1, 5));
            }
            case 3 -> {
                tiles.add(new coord(0, 5));
                tiles.add(new coord(1, 3));
                tiles.add(new coord(1, 4));
                tiles.add(new coord(1, 5));
            }
            case 4 -> {
                tiles.add(new coord(0, 4));
                tiles.add(new coord(0, 5));
                tiles.add(new coord(1, 4));
                tiles.add(new coord(1, 5));
            }
            case 5 -> {
                tiles.add(new coord(0, 4));
                tiles.add(new coord(0, 5));
                tiles.add(new coord(1, 3));
                tiles.add(new coord(1, 4));
            }
            case 6 -> {
                tiles.add(new coord(0, 4));
                tiles.add(new coord(1, 3));
                tiles.add(new coord(1, 4));
                tiles.add(new coord(1, 5));
            }
            case 7 -> {
                tiles.add(new coord(0, 3));
                tiles.add(new coord(0, 4));
                tiles.add(new coord(1, 4));
                tiles.add(new coord(1, 5));
            }
            default -> panic.fatalError("Tetrimino color must be an integer between 1 and 7.");
        }
    }

    public void addToGrid(tetrisGrid grid) {
        for(coord i : tiles) grid.tetrisData[i.x()][i.y()] = col; // just add it to the grid
    }

    public void removeFromGrid(tetrisGrid grid) {
        for(coord i : tiles) grid.tetrisData[i.x()][i.y()] = 0; // just add it to the grid
    }

    public void drawWithGrid(tetrisGrid grid) {
        this.addToGrid(grid);
        grid.drawTiles();
        this.removeFromGrid(grid);
    }

    public boolean illegal(tetrisGrid grid) { // checks if the tetromino's position is illegal on the grid
        for(coord i : tiles) {
            if(i.x() < 0 || i.x() >= tetrisGrid.gridHeightTileAmount) return true; // check if its x-coord is legal
            if(i.y() < 0 || i.y() >= tetrisGrid.gridWidthTileAmount) return true; // similar for y-coord
            if(grid.tetrisData[i.x()][i.y()] != 0) return true; // if tile is already occupied
        }
        return false; // otherwise, return false
    }

    public boolean canMoveDown(tetrisGrid grid) {
        moveDown();
        boolean rv = !illegal(grid);
        moveUp();
        return rv;
    }

    // the following methods are private because they are UNSAFE (in the context of the grid).
    // see moveDownInGrid, moveLeftInGrid etc.

    private void moveDown() { // move tetrimino down
        for(coord i : tiles) i.updX(1); // just move all the tiles down
    }

    private void moveUp() { // this is only used by moveToBottom, and has no purpose in actual gameplay
        for(coord i : tiles) i.updX(-1); // just move all the tiles up
    }

    private void moveLeft() { // move tetrimino left
        for(coord i : tiles) i.updY(-1); // move everything left
    }

    private void moveRight() { // you get the idea
        for(coord i : tiles) i.updY(1);
    }

    public void moveToBottom(tetrisGrid grid) { // move tile to the very bottom (for example, when spacebar is pressed)
        while (this.canMoveDown(grid)) this.moveDown(); // while you can move down, move down
    }

    private void rotate() { // an actual nightmare to implement
        panic.log("rotated!!", "tetrisMino");
        switch(col) {
            case 1 -> { // its a line
                switch(rotState) { // this took way, way too long... all of it...
                    case 0 -> {
                        tiles.get(0).updX(-2);
                        tiles.get(0).updY(2);
                        tiles.get(1).updX(-1);
                        tiles.get(1).updY(1);
                        tiles.get(3).updX(1);
                        tiles.get(3).updY(-1);
                    }
                    case 1 -> {
                        tiles.get(0).updX(1);
                        tiles.get(0).updY(1);
                        tiles.get(2).updX(-1);
                        tiles.get(2).updY(-1);
                        tiles.get(3).updX(-2);
                        tiles.get(3).updY(-2);
                    }
                    case 2 -> {
                        tiles.get(0).updX(2);
                        tiles.get(0).updY(-2);
                        tiles.get(1).updX(1);
                        tiles.get(1).updY(-1);
                        tiles.get(3).updX(-1);
                        tiles.get(3).updY(1);
                    }
                    case 3 -> {
                        tiles.get(0).updX(-1);
                        tiles.get(0).updY(-1);
                        tiles.get(2).updX(1);
                        tiles.get(2).updY(1);
                        tiles.get(3).updX(2);
                        tiles.get(3).updY(2);
                    }
                }
            }
            case 2 -> { // L shape
                switch(rotState) {
                    case 0 -> {
                        tiles.get(0).updY(2);
                        tiles.get(1).updX(-1);
                        tiles.get(1).updY(1);
                        tiles.get(3).updX(1);
                        tiles.get(3).updY(-1);
                    }
                    case 1 -> {
                        tiles.get(0).updX(2);
                        tiles.get(1).updX(1);
                        tiles.get(1).updY(1);
                        tiles.get(3).updX(-1);
                        tiles.get(3).updY(-1);
                    }
                    case 2 -> {
                        tiles.get(0).updY(-2);
                        tiles.get(1).updX(1);
                        tiles.get(1).updY(-1);
                        tiles.get(3).updX(-1);
                        tiles.get(3).updY(1);
                    }
                    case 3 -> {
                        tiles.get(0).updX(-2);
                        tiles.get(1).updX(-1);
                        tiles.get(1).updY(-1);
                        tiles.get(3).updX(1);
                        tiles.get(3).updY(1);
                    }
                }
            }
            case 3 -> {
                switch(rotState) {
                    case 0 -> {
                        tiles.get(0).updX(2);
                        tiles.get(1).updX(-1);
                        tiles.get(1).updY(1);
                        tiles.get(3).updX(1);
                        tiles.get(3).updY(-1);
                    }
                    case 1 -> {
                        tiles.get(0).updY(-2);
                        tiles.get(1).updX(1);
                        tiles.get(1).updY(1);
                        tiles.get(3).updX(-1);
                        tiles.get(3).updY(-1);
                    }
                    case 2 -> {
                        tiles.get(0).updX(-2);
                        tiles.get(1).updX(1);
                        tiles.get(1).updY(-1);
                        tiles.get(3).updX(-1);
                        tiles.get(3).updY(1);
                    }
                    case 3 -> {
                        tiles.get(0).updY(2);
                        tiles.get(1).updX(-1);
                        tiles.get(1).updY(-1);
                        tiles.get(3).updX(1);
                        tiles.get(3).updY(1);
                    }
                }
            }
            case 4 -> {} // its a square - do nothing
            case 5 -> {
                switch(rotState) {
                    case 0 -> {
                        tiles.get(0).updX(1);
                        tiles.get(0).updY(1);
                        tiles.get(1).updX(2);
                        tiles.get(2).updX(-1);
                        tiles.get(2).updY(1);
                    }
                    case 1 -> {
                        tiles.get(0).updX(1);
                        tiles.get(0).updY(-1);
                        tiles.get(1).updY(-2);
                        tiles.get(2).updX(1);
                        tiles.get(2).updY(1);
                    }
                    case 2 -> {
                        tiles.get(0).updX(-1);
                        tiles.get(0).updY(-1);
                        tiles.get(1).updX(-2);
                        tiles.get(2).updX(1);
                        tiles.get(2).updY(-1);
                    }
                    case 3 -> {
                        tiles.get(0).updX(-1);
                        tiles.get(0).updY(1);
                        tiles.get(1).updY(2);
                        tiles.get(2).updX(-1);
                        tiles.get(2).updY(-1);
                    }
                }
            }
            case 6 -> {
                switch(rotState) {
                    case 0 -> {
                        tiles.get(0).updX(1);
                        tiles.get(0).updY(1);
                        tiles.get(1).updX(-1);
                        tiles.get(1).updY(1);
                        tiles.get(3).updX(1);
                        tiles.get(3).updY(-1);
                    }
                    case 1 -> {
                        tiles.get(0).updX(1);
                        tiles.get(0).updY(-1);
                        tiles.get(1).updX(1);
                        tiles.get(1).updY(1);
                        tiles.get(3).updX(-1);
                        tiles.get(3).updY(-1);
                    }
                    case 2 -> {
                        tiles.get(0).updX(-1);
                        tiles.get(0).updY(-1);
                        tiles.get(1).updX(1);
                        tiles.get(1).updY(-1);
                        tiles.get(3).updX(-1);
                        tiles.get(3).updY(1);
                    }
                    case 3 -> {
                        tiles.get(0).updX(-1);
                        tiles.get(0).updY(1);
                        tiles.get(1).updX(-1);
                        tiles.get(1).updY(-1);
                        tiles.get(3).updX(1);
                        tiles.get(3).updY(1);
                    }
                }
            }
            case 7 -> {
                switch(rotState) {
                    case 0 -> {
                        tiles.get(0).updY(2);
                        tiles.get(1).updX(1);
                        tiles.get(1).updY(1);
                        tiles.get(3).updX(1);
                        tiles.get(3).updY(-1);
                    }
                    case 1 -> {
                        tiles.get(0).updX(2);
                        tiles.get(1).updX(1);
                        tiles.get(1).updY(-1);
                        tiles.get(3).updX(-1);
                        tiles.get(3).updY(-1);
                    }
                    case 2 -> {
                        tiles.get(0).updY(-2);
                        tiles.get(1).updX(-1);
                        tiles.get(1).updY(-1);
                        tiles.get(3).updX(-1);
                        tiles.get(3).updY(1);
                    }
                    case 3 -> {
                        tiles.get(0).updX(-2);
                        tiles.get(1).updX(-1);
                        tiles.get(1).updY(1);
                        tiles.get(3).updX(1);
                        tiles.get(3).updY(1);
                    }
                }
            }
        }
        rotState = (rotState+1)%4; // only 4 rotstates, and it cycles
    }

    // these are the safer variants of the movement functions,
    // which take into account the grid and can reject movement if deemed illegal.

    public void moveLeftInGrid(tetrisGrid grid) {
        moveLeft();
        if(illegal(grid)) moveRight(); // if you can't move left, go back where you started
    }

    public void moveRightInGrid(tetrisGrid grid) {
        moveRight();
        if(illegal(grid)) moveLeft(); // you get the idea
    }

    public void moveDownInGrid(tetrisGrid grid) {
        moveDown();
        if(illegal(grid)) moveUp();
    }

    public void rotateInGrid(tetrisGrid grid) {
        // this one... this one is a lot more complicated.
        // reason being, not only do i have to rotate it, some movements (up to +- 2) might be necessary
        // for example, after rotating a piece might clip into the grid. we have to find a way to cope with this.

        // in order to fix this, we first try to rotate. if we find a place to slot it in, we put it there
        // and gladly return. otherwise, the rotation is rejected and we rotate 3 more times to go back to the
        // original state.

        // to avoid any strange bugs, we move the block to the closest (manhattan distance) viable block.

        rotate();
        if(!illegal(grid)) return;
        int opt = -1, x = 0, y = 0; // optimal answer, x-shift, y-shift
        for(int tx = -2; tx <= 2; ++tx) for(int ty = -2; ty <= 2; ++ty) { // this part is complicated. but i dont care
            for(int i = tx; i < 0; ++i) moveRight();
            for(int i = tx; i > 0; --i) moveLeft();
            for(int i = ty; i < 0; ++i) moveDown();
            for(int i = ty; i > 0; --i) moveUp();
            if(!illegal(grid)) {
                int c = abs(tx)+abs(ty); // some math
                if(opt == -1 || c < opt) {
                    opt = c;
                    x = tx;
                    y = ty;
                }
            }
            for(int i = tx; i < 0; ++i) moveLeft(); // reset what we did
            for(int i = tx; i > 0; --i) moveRight();
            for(int i = ty; i < 0; ++i) moveUp();
            for(int i = ty; i > 0; --i) moveDown();
        }
        if(opt == -1) { // if you haven't found an answer, just rotate it back and give up.
            rotate();
            rotate();
            rotate();
        } else { // this part is a bit complicated. figure it out yourself
            for(int i = x; i < 0; ++i) moveRight();
            for(int i = x; i > 0; --i) moveLeft();
            for(int i = y; i < 0; ++i) moveDown();
            for(int i = y; i > 0; --i) moveUp();
        }
    }

}
