package com.ryanzhang.drawTools;

import acm.graphics.GImage;
import com.ryanzhang.omnitetris;
import com.ryanzhang.utility.fetch;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class otterOmni { // draws a celebratory otter on the screen.

    private final omnitetris instance;

    public otterOmni(omnitetris cur) {instance = cur;}

    public void scoreIncreaseOtter() {
        new Thread(() -> {
            GImage miniOtter = null;
            try {
                miniOtter = fetch.fetchGImage("images/omni.gif");
            } catch (IOException e) {
                e.printStackTrace();
            }
            int xc = ThreadLocalRandom.current().nextInt(0, 2) == 0 ? 0 : (int)(omnitetris.canvasWidth-miniOtter.getWidth());
            int yc = 75+ThreadLocalRandom.current().nextInt(0, (int)(omnitetris.canvasHeight-miniOtter.getHeight()-75));
            instance.add(miniOtter, xc, yc);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            instance.remove(miniOtter);
        }).start();
    }

}
