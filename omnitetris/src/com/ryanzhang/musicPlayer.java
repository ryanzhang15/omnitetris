package com.ryanzhang;

import com.ryanzhang.panic.panic;
import com.ryanzhang.utility.fetch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public class musicPlayer {

    //background music player. you can start looping audio and stop audio.

    private final AudioInputStream audio;
    private final Clip clip;
    private boolean playing = false;

    musicPlayer(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audio = fetch.fetchAudio(path);
        clip = AudioSystem.getClip(); // may not be the best way to do it. but i dont care.
        clip.open(audio);
    }

    void start() throws LineUnavailableException, IOException {
        if(playing) return; // if it is already playing, just ignore the request
        playing = true; // set it to playing
        clip.start(); //start music!
        clip.loop(Clip.LOOP_CONTINUOUSLY); //loop music!
    }

    void stop() {
        if(!playing) return; // if it is not playing, just ignore the request
        playing = false; // set it to not playing
        clip.stop();
    }

    void toggle() throws LineUnavailableException, IOException {
        panic.log("Music toggled!", "musicPlayer");
        if(playing) stop(); // to toggle music, if it is playing, stop it
        else start(); // to toggle music, if it is not playing, start it
    }

}
