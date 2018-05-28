package MainStuff;

import MainStuff.VirusSim.VirusSimulator;
import Utilities.CirclePoints;
import Utilities.FastRand;
import processing.core.PApplet;
import processing.core.PGraphics;

public class Main extends PApplet {
    public final int WORLD_WIDTH = 26;
    public final int WORLD_HEIGHT = 30;

    boolean fastMode = false;

    private PGraphics screenBuffer;
    private VirusSimulator simulator;

    @Override
    public void settings() {
        size(WORLD_WIDTH * 8, WORLD_HEIGHT * 8);
        noSmooth();
    }

    @Override
    public void setup() {
        frameRate(15);
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
        if (fastMode) {
            for (int i = 0; i < 30000; i++) {
                simulator.run();
            }
            surface.setTitle("FPS: " + frameRate * 30000);
        } else {
            simulator.run();
            surface.setTitle("FPS: " + frameRate);
        }

        screenBuffer.beginDraw();                        //Begin drawing to buffer
        screenBuffer.background(0);
        simulator.draw(screenBuffer);
        screenBuffer.endDraw();                          //End drawing to buffer

        image(screenBuffer, 0, 0, width, height); //Draw buffer to screen
    }

    @Override
    public void mousePressed() {
    }

    @Override
    public void keyPressed() {
        if (key == 'o' || key == 'O') {
            fastMode = !fastMode;
            frameRate(fastMode ? 1000 : 15);
        }
    }

    public static void main(String[] args) {
        PApplet.main("MainStuff.Main");
    }
}
