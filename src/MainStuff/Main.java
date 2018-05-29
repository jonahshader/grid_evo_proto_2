package MainStuff;

import MainStuff.VirusSim.VirusSimulator;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main extends PApplet {
    public int worldWidth = 26;
    public int worldHeight = 30;
    private int timeRemainingInitial = 126;
    private int populationSizeInitial = 80;
    private int virusClusterCreatureCount = 6;
    private int antiVirusClusterCreatureCount = 10;
    private int iterationsPerGeneration = 5;

    public static double mutationRate = 0.00125;

    String widthString, heightString, timeRemainingString, populationSizeString, virusCountString, antiVirusCountString, iterationsPerGenerationString, mutationRateString;


    boolean fastMode = false;

    private PGraphics screenBuffer;
    private VirusSimulator simulator;

    @Override
    public void settings() {
        Properties simulationProperties = new Properties();
        try {
            simulationProperties.load(new FileInputStream("settings.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        widthString = simulationProperties.getProperty("world_width");
        heightString = simulationProperties.getProperty("world_height");
        timeRemainingString = simulationProperties.getProperty("cycles_per_episode");
        populationSizeString = simulationProperties.getProperty("population_size");
        virusCountString = simulationProperties.getProperty("virus_count");
        antiVirusCountString = simulationProperties.getProperty("antivirus_count");
        iterationsPerGenerationString = simulationProperties.getProperty("iterations_per_generation");
        mutationRateString = simulationProperties.getProperty("mutation_rate");

        worldWidth = Integer.parseInt(widthString);
        worldHeight = Integer.parseInt(heightString);
        mutationRate = Double.parseDouble(mutationRateString);
        size(worldWidth * 8, worldHeight * 8);
        noSmooth();
    }

    @Override
    public void setup() {

        frameRate(20);
        surface.setResizable(true);
        blendMode(BLEND);
        screenBuffer = createGraphics(worldWidth, worldHeight);
        screenBuffer.noSmooth();


//        simulator = new VirusSimulator(worldWidth, worldHeight, timeRemainingInitial, populationSizeInitial, virusClusterCreatureCount, antiVirusClusterCreatureCount, iterationsPerGeneration);
        simulator = new VirusSimulator(Integer.parseInt(widthString), Integer.parseInt(heightString), Integer.parseInt(timeRemainingString), Integer.parseInt(populationSizeString), Integer.parseInt(virusCountString), Integer.parseInt(antiVirusCountString), Integer.parseInt(iterationsPerGenerationString));

        screenBuffer.beginDraw();
        screenBuffer.background(0);
        screenBuffer.endDraw();
    }

    @Override
    public void draw() {
        //Run the world (everything)
        if (fastMode) {
            for (int i = 0; i < 50000; i++) {
                simulator.run();
            }
            surface.setTitle("FPS: " + frameRate * 50000);
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
            frameRate(fastMode ? 1000 : 20);
        }
    }

    public static void main(String[] args) {
        PApplet.main("MainStuff.Main");
    }
}
