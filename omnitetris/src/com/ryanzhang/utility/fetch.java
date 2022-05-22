package com.ryanzhang.utility;

import acm.graphics.GImage;
import com.ryanzhang.Main;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class fetch { // fetches file from .jar file

    public static GImage fetchGImage(String pth) throws IOException {
        BufferedImage buff = ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream(pth)));
        return new GImage(buff);
//        return new GImage("src/com/ryanzhang/"+pth);
    }

    public static AudioInputStream fetchAudio(String pth) throws UnsupportedAudioFileException, IOException {
        InputStream source = Main.class.getResourceAsStream(pth);
        assert source != null;
        InputStream buffer = new BufferedInputStream(source);
        return AudioSystem.getAudioInputStream(buffer);
    }
}
