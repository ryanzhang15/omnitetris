package com.ryanzhang;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import com.ryanzhang.drawTools.otterOmni;
import com.ryanzhang.drawTools.tetrisGrid;
import com.ryanzhang.drawTools.tetrisMino;
import com.ryanzhang.drawTools.tetrisRect;
import com.ryanzhang.panic.panic;
import com.ryanzhang.utility.fetch;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*; // its bad to import by *. but i dont care.
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class omnitetris extends GraphicsProgram implements KeyListener {

    // screen attributes, for init and other uses
    private final String titleText = "OmniTetris"; // set title of the window
    public final static int canvasWidth = 500; // width of the canvas
    public final static int canvasHeight = 800; // height of the canvas
    private final Dimension canvasSize = new Dimension(canvasWidth, canvasHeight); // make dimension object for ease of use
    private final Dimension size = Toolkit.getDefaultToolkit().getScreenSize(); // grab screen size of user
    private final int canvasX = size.width / 2; // center the canvas
    private final int canvasY = size.height / 2; // center the canvas
    private final Point canvasPos = new Point(canvasX-canvasWidth/2, canvasY-canvasHeight/2); // make point object for ease of use
    public final static Color backgroundColor = new Color(33, 3, 49); //hex = #210331

    // list of colors, for tetris blocks
    public final static Color tetrisBorder = new Color(219, 219, 219);
    private final static Color tetrisYellow = new Color(238, 255, 0);
    private final static Color tetrisCyan = new Color(0, 255, 247);
    private final static Color tetrisBlue = new Color(0, 64, 255);
    private final static Color tetrisGreen = new Color(0, 255, 106);
    private final static Color tetrisPink = new Color(238, 85, 255);
    private final static Color tetrisPurple = new Color(203, 168, 253);
    private final static Color tetrisOrange = new Color(255, 171, 46);
    public final static Color[] tetrisColors = {tetrisYellow, tetrisCyan, tetrisBlue, tetrisGreen, tetrisPink, tetrisPurple, tetrisOrange}; // for use in tetrisGrid

    //current frame, saved so that it is easy to switch between pages
    // I used enums!!!!! I am so cool!!!!!
    private enum frame { //for good practice, capitalize!!! distinguish from variables!!!
        MENU, //menu screen
        INSTRUCTIONS, //instructions (manual) screen
        INGAME, //in the game
        GAMEOVER, //game over screen
        EMPTY // empty screen
    }
    frame currentFrame;

    //for background music playing
    private final musicPlayer backgroundPlayer;

    //for main menu frame, to add and later destroy
    private GImage backgroundLayer;
    private GImage startText;
    private GRect invisClickDetection; // can you guess what this does?? can you guess???
    private tetrisRect[] startButton = new tetrisRect[5];

    //for game over frame, to add and later destroy
    private GImage gameOverImage;
    private GLabel scoreLabel;

    //for ingame frame, to add and later destroy
    private tetrisGrid grid;
    private GImage inGameScoreLayer;
    private GLabel inGameScore;

    //for instructions frame
    private GImage instructionsImage;

    //actual gameplay use
    private boolean downDown; // checks if down button is down
    private boolean busy; // check if the mino is currently moving, i.e. the game is busy. this is to avoid concurrent modification
    private int score = 816724;
    private tetrisMino mino;
    private gameThread blockDropper;
    private int fallSpeed;

    //most importantly, the otters!
    otterOmni omni = new otterOmni(this);

    public omnitetris() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        //init music, as soon as run
        backgroundPlayer = new musicPlayer("media/backgroundMusic.wav");
    }

    public void init() {

        // self-explanatory
        this.setLocation(canvasPos); // for some reason, this does not work. I cannot figure out why.
        this.setTitle(titleText);
        this.setSize(canvasSize);
        this.setBackground(backgroundColor);

        currentFrame = frame.EMPTY; // we are on the menu, so currentFrame is menu

    }

    public void run() {

        this.addKeyListeners(); // add key listeners
        try {
            mainMenu(); // draw main menu
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestFocus(); // for some reason, this does not work. I cannot figure out why.

    }

    public void resetScreen() { // function to reset the screen to the background color, for switching between screens

        if(currentFrame == frame.EMPTY) return;

        panic.log("Screen reset!", "resetScreen");

        // we can't simply cover the screen with something else and call it a day.
        // our game is quite complicated and we MUST free memory.
        // based on my research used elements are not automatically garbage-collected. i should remove them.
        // let's clear some memory... (why didn't I just code this with Obj-C? So much better than some
        // loosely documented and probably really old java library...
        // java is such a bad idea

        if(currentFrame == frame.MENU) {
            // tear down menu elements
            this.remove(backgroundLayer);
            for(int i = 0; i < 5; ++i) startButton[i].delete();
            this.remove(startText);
            this.remove(invisClickDetection);
        } else if(currentFrame == frame.GAMEOVER) {
            // just get rid of everything
            this.remove(gameOverImage);
            this.remove(scoreLabel);
        } else if(currentFrame == frame.INGAME) {
            // everything gone!!!
            grid.delete();
            this.remove(inGameScoreLayer);
            this.remove(inGameScore);
        } else if(currentFrame == frame.INSTRUCTIONS) {
            //you get the point.
            this.remove(instructionsImage);
        }

        currentFrame = frame.EMPTY; // no more stuff on screen...

    }

    public void mainMenu() throws IOException {

        resetScreen();
        currentFrame = frame.MENU; // we are on the menu screen!~~~

        backgroundLayer = fetch.fetchGImage("images/menuLayer_v5.png"); // load background layer
        this.add(backgroundLayer, 0, 0); // add it to the screen

        //make the start button
        startButton[0] = new tetrisRect(50, 350, 80, 80, 5, tetrisYellow, tetrisBorder, this); // carefully calculated numbers
        startButton[1] = new tetrisRect(130, 350, 80, 80, 5, tetrisYellow, tetrisBorder, this);
        startButton[2] = new tetrisRect(210, 350, 80, 80, 5, tetrisYellow, tetrisBorder, this);
        startButton[3] = new tetrisRect(290, 350, 80, 80, 5, tetrisYellow, tetrisBorder, this);
        startButton[4] = new tetrisRect(370, 350, 80, 80, 5, tetrisYellow, tetrisBorder, this);
        for(int i = 0; i < 5; ++i) startButton[i].draw();

        startText = fetch.fetchGImage("images/startText.png"); // load start text
        this.add(startText, 0, -15);

        //load an invisible rectangle around the "Start" text tiles in order to detect clicky clicky
        invisClickDetection = new GRect(50, 350, 400, 80);
        invisClickDetection.setColor(new Color(255, 255, 255, 0)); // transparent color, so its invis of course
        this.add(invisClickDetection);

        MouseListener detectStart = new MouseListener() { // for detecting when we should start the game
            @Override
            public void mouseClicked(MouseEvent e) {
                panic.log("Start clicked!", "mainMenu");
                try {
                    inGame();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            // all unused
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };
        invisClickDetection.addMouseListener(detectStart);

    }

    public void gameOver() throws IOException {

        resetScreen();
        currentFrame = frame.GAMEOVER;

        gameOverImage = fetch.fetchGImage("images/gameOver.png"); // load game over background image

        scoreLabel = new GLabel(Integer.toString(score));
        scoreLabel.setColor(tetrisYellow);
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 100)); // load score, to display

        this.add(gameOverImage, 0, 0);
        this.add(scoreLabel,250-scoreLabel.getBounds().getWidth()/2, 555); // centered text

    }

    public void instructions() throws IOException {

        resetScreen();
        currentFrame = frame.INSTRUCTIONS;

        instructionsImage = fetch.fetchGImage("images/instttructasftions.png");
        this.add(instructionsImage, 0, 0);

    }

    public void inGame() throws IOException {

        resetScreen();
        currentFrame = frame.INGAME;

        grid = new tetrisGrid(this); // grab a grid, place it in "this" instance
        grid.setup(); // set up the grid

        score = 0;
        inGameScore = new GLabel(Integer.toString(score));
        inGameScore.setFont(new Font("Monospaced", Font.BOLD, 60)); // display a score
        inGameScore.setColor(tetrisYellow);
        this.add(inGameScore, 250, 70);

        inGameScoreLayer = fetch.fetchGImage("images/inGameScoreLayer.png");
        this.add(inGameScoreLayer, 0, -700);

        // ok. now that setup is done, do the actual gameplay.
        // interestingly its better to reset the down key, so they don't carry over.
        downDown = false;

        fallSpeed = 1000; // default fall speed

        busy = false; // we are not busy yet

        dropNewMino();

        blockDropper = new gameThread(); // as a C++ developer, i absolutely hate the idea of threads
        blockDropper.start(); // but it is pretty cool i guess, and does the job. very nice.

    }

    private class gameThread extends Thread  { //ok, i know nested classes suck. but i dont care

        // how this will work:
        // while the game is not over, keep dropping blocks.
        // if the newly generated block cannot be placed on the grid, end the game.
        // clear any rows needed, update the score and start the process over again.
        // for other keys (left - right - up - spacebar), handle separately.

        private final static int minWaitTime = 80;
        private long lastDrop = System.currentTimeMillis();

        @Override
        public void run() {
            while(currentFrame == frame.INGAME) {
                if(!busy) {
                    //voluntary drop?
                    long ts = System.currentTimeMillis();
                    if (downDown && ts - lastDrop > minWaitTime) {
                        // try to make it drop
                        busy = true;
                        if (mino.canMoveDown(grid)) {
                            mino.moveDownInGrid(grid);
                            mino.drawWithGrid(grid);
                            lastDrop = ts;
                        }
                        busy = false;
                    }
                    if (ts - lastDrop > fallSpeed) {
                        // try to make it drop
                        busy = true;
                        if (mino.canMoveDown(grid)) {
                            mino.moveDownInGrid(grid);
                            mino.drawWithGrid(grid);
                        } else { // otherwise, it's reached the end!
                            try {
                                minoDoneDropping();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        busy = false;
                        lastDrop = ts;
                    }
                }
                try {
                    sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int cycle = 0;
    private ArrayList<Integer> cycleTiles = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    private void dropNewMino() throws IOException { // greates a new tetromino, and places it at the top of the screen, ready to drop
        if(cycle == 0) {
            cycle = 1;
            Collections.shuffle(cycleTiles);
        }
        int col = cycleTiles.get(cycle-1); // generate a random color (easier to use than Math.random())
        panic.log("block col = "+col, "omnitetris");
        cycle = (cycle+1)%8;
        mino = new tetrisMino(col);
        if(mino.illegal(grid)) {
            // if the new tetromino can't even fit, you've lost the game.
            gameOver();
            return;
        }
        // otherwise happily draw it, let it drop!
        mino.drawWithGrid(grid);
    }

    private void minoDoneDropping() throws IOException { // mino has already reached the bottom!
        // we need to combine the finished dropping mino with the grid, clear any completed rows, update the score
        // and finally drop a new mino. let's do these one by one!
        mino.addToGrid(grid); // the only good part of object-oriented programming: this is so clear!
        int scoreChange = 80*grid.clearFinishedRows();
        score += scoreChange;
        if(scoreChange != 0) { // you added some score: you get an otter!
            omni.scoreIncreaseOtter();
        }
        inGameScore.setLabel(Integer.toString(score));
        if(score <= 10000) fallSpeed = (int)(1000-Math.sqrt(90*score));
        else if(score <= 15000) fallSpeed = 40;
        else fallSpeed = ThreadLocalRandom.current().nextInt(30, 101);
        dropNewMino();
    }

    //what if a key is pressed?
    @Override // this was not taught in CS class? It is so important!
    public void keyPressed(KeyEvent e) {
//        panic.log("Key pressed!", "omnitetris");

        // now, we know a key is pressed. but what key? in which frame?

        // there are some controls which are universal, across frames.
        // these are: M = toggle music, Q = quit
        if(e.getKeyCode() == KeyEvent.VK_M) {
            try {
                backgroundPlayer.toggle(); // i would put this in a method, but i dont care
            } catch (LineUnavailableException | IOException ex) {
                ex.printStackTrace();
            }
        } else if(e.getKeyCode() == KeyEvent.VK_Q) System.exit(0); // i would put this in a method, but i dont care
        //also some stuffs for testing
//        else if(e.getKeyCode() == KeyEvent.VK_T) {
//            // run some stuff to test here
////            gameOver();
//            mino.moveDownInGrid(grid);
//            mino.drawWithGrid(grid);
//        } else if(e.getKeyCode() == KeyEvent.VK_U) {
//            try {
//                gameOver();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }

        if(currentFrame == frame.GAMEOVER) { // if we are on gameover page
            if(e.getKeyCode() == KeyEvent.VK_R) {
                //simply restart
                try {
                    mainMenu();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if(currentFrame == frame.INGAME) {
            if(e.getKeyCode() == KeyEvent.VK_UP) {
                if(busy) return;
                busy = true;
                mino.rotateInGrid(grid);
                mino.drawWithGrid(grid);
                busy = false;
            } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                downDown = true;
            } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if(busy) return;
                busy = true;
                mino.moveRightInGrid(grid);
                mino.drawWithGrid(grid);
                busy = false;
            } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                if(busy) return;
                busy = true;
                mino.moveLeftInGrid(grid);
                mino.drawWithGrid(grid);
                busy = false;
            } else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                if(busy) return;
                busy = true;
                mino.moveToBottom(grid);
                mino.drawWithGrid(grid);
                try {
                    minoDoneDropping();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                busy = false;
            }
        } else if(currentFrame == frame.INSTRUCTIONS) {
            if(e.getKeyCode() == KeyEvent.VK_B) {
                try {
                    mainMenu();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if(currentFrame == frame.MENU) {
            if(e.getKeyCode() == KeyEvent.VK_I) {
                try {
                    instructions();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { // for down key
//        panic.log("Key released!", "omnitetris");
        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            downDown = false;
        }
    }

}
