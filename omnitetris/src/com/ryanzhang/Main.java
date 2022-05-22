package com.ryanzhang;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        //nothing to see here!
        //move along!!!
        omnitetris instance = new omnitetris();
        instance.start();

    }
}
