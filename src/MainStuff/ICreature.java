package MainStuff;

import processing.core.PGraphics;

public interface ICreature {
    void run();
    void draw(PGraphics screenBuffer);
    int getX();
    int getY();
    CreatureType getCreatureType();

    enum CreatureType {
        VIRUS,
        ANTIVIRUS
    }
}
