package MainStuff;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Main extends PApplet {
    public final int WORLD_WIDTH = 640;
    public final int WORLD_HEIGHT = 480;

    private PGraphics screenBuffer;

    private World world;

    @Override
    public void settings() {
        size(640, 480);
    }

    @Override
    public void setup() {
        surface.setResizable(true);
        screenBuffer = createGraphics(width, height);
        world = new World(WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Override
    public void draw() {
        screenBuffer.beginDraw();
        screenBuffer.background(0);
        world.draw(screenBuffer);
        screenBuffer.endDraw();
        image(screenBuffer, 0, 0, width, height);
    }

    public static void main(String[] args) {
        PApplet.main("MainStuff.Main");
    }
}
