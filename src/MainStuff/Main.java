package MainStuff;

import MainStuff.VirusSim.VirusSimulator;
import Utilities.FastRand;
import processing.core.PApplet;
import processing.core.PGraphics;

public class Main extends PApplet {
    public final int WORLD_WIDTH = 64;
    public final int WORLD_HEIGHT = 48;

    private PGraphics screenBuffer;

    private VirusSimulator simulator;

    @Override
    public void settings() {
        size(640, 480);
//        fullScreen(P2D);
        noSmooth();
    }

    @Override
    public void setup() {
        surface.setResizable(true);
        blendMode(BLEND);
        screenBuffer = createGraphics(WORLD_WIDTH, WORLD_HEIGHT);
        screenBuffer.noSmooth();
        simulator = new VirusSimulator(WORLD_WIDTH, WORLD_HEIGHT);

//        for (int i = 0; i < 60; i++) {
//            Creature newCreature = new Creature(FastRand.splittableRandom.nextInt(WORLD_WIDTH), FastRand.splittableRandom.nextInt(WORLD_HEIGHT), world);
//            if (world.addCreature(newCreature)) {
//                System.out.println("Successfully creature creature at " + newCreature.getX() + " " + newCreature.getY());
//            } else {
//                System.out.println("Failed to creature creature at " + newCreature.getX() + " " + newCreature.getY());
//            }
//        }

        screenBuffer.beginDraw();
        screenBuffer.background(0);
        screenBuffer.endDraw();
    }

    @Override
    public void draw() {
        //Run the world (everything)
        simulator.run();
//        for (int i = 0; i < 1000; i++) {
//            world.run();
//        }
//        System.out.println("Framerate: " + frameRate * 1000);



        screenBuffer.beginDraw();                        //Begin drawing to buffer
//        for (int i = 0; i < 1000; i++) {
//            world.run();
        screenBuffer.background(0);
            simulator.draw(screenBuffer);
//        }
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
