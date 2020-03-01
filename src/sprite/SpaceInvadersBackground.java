package levels;

import biuoop.DrawSurface;
import sprite.Sprite;

import java.awt.Color;

/**
 * Space invaders background.
 */
public class SpaceInvadersBackground implements Sprite {
    // members
    private final int width = 800;

    /**
     * draw the sprite to the screen.
     *
     * @param d surface
     */
    public void drawOn(DrawSurface d) {
        // black background
        d.setColor(Color.BLACK);
        d.fillRectangle(0, 0, 800, 600);
    }

    /**
     * notify the sprite that time has passed.
     *
     * @param dt frame rate
     */
    public void timePassed(double dt) {

    }
}
