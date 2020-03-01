package game;

import basic.Counter;
import sprite.Sprite;
import biuoop.DrawSurface;

import java.awt.Color;

/**
 * Displaying the name of the level.
 */
public class NameIndicator implements Sprite {
    // members
    private Counter numOfLevel;

    /**
     * constructor.
     *
     * @param numOfLevel current level
     */
    public NameIndicator(Counter numOfLevel) {
        this.numOfLevel = numOfLevel;
    }

    /**
     * draw the sprite to the screen.
     *
     * @param d surface
     */
    public void drawOn(DrawSurface d) {
        // name of level
        d.setColor(Color.BLACK);
        d.drawText(550, 15, "Level Name: Battle no." + this.numOfLevel.getValue() + "", 15);
    }

    /**
     * notify the sprite that time has passed.
     *
     * @param dt frame rate
     */
    public void timePassed(double dt) {
    }

    /**
     * add the indicator to the game as sprite.
     *
     * @param g the current game
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }
}