package MainStuff;

import MainStuff.VirusSim.VirusSimulator;
import Utilities.CirclePoints;
import Utilities.FastRand;
import processing.core.PApplet;
import processing.core.PGraphics;

public class Main extends PApplet {
    public final int WORLD_WIDTH = 64;
    public final int WORLD_HEIGHT = 49;

    private PGraphics screenBuffer;

    private VirusSimulator simulator;

    @Override
    public void settings() {
        size(640, 480);
        noSmooth();
    }

    @Override
    public void setup() {
//        frameRate(999);
        surface.setResizable(true);
        blendMode(BLEND);
        screenBuffer = createGraphics(WORLD_WIDTH, WORLD_HEIGHT);
        screenBuffer.noSmooth();
        simulator = new VirusSimulator(WORLD_WIDTH, WORLD_HEIGHT);

        screenBuffer.beginDraw();
        screenBuffer.background(0);
        screenBuffer.endDraw();
    }

    @Override
    public void draw() {
        //Run the world (everything)
        simulator.run();

        screenBuffer.beginDraw();                        //Begin drawing to buffer
        screenBuffer.background(0);
        simulator.draw(screenBuffer);
        screenBuffer.endDraw();                          //End drawing to buffer

        image(screenBuffer, 0, 0, width, height); //Draw buffer to screen
    }

    @Override
    public void mousePressed() {
        screenBuffer.save("Background.png");
    }

    public static void main(String[] args) {
        PApplet.main("MainStuff.Main");
    }
}
