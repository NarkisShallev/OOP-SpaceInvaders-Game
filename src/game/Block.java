package game;

import basic.Velocity;
import basic.Collidable;
import basic.HitListener;
import basic.HitNotifier;
import sprite.Sprite;
import biuoop.DrawSurface;
import geometry.Point;
import geometry.Rectangle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * classname: Block
 * Creates a block which is a rectangle with color.
 *
 * @author Narkis Kremizi
 * @version 1.0 19/04/2018
 */
public class Block implements Collidable, Sprite, HitNotifier {
    /* members. */
    private Rectangle block;
    private java.awt.Color fillColor;
    private List<HitListener> hitListeners;

    /**
     * A constructor that creates a block.
     *
     * @param block block
     */
    public Block(Rectangle block) {
        this.block = block;
        this.fillColor = Color.CYAN;
        this.hitListeners = new ArrayList<>();
    }

    /**
     * Return the "collision shape" of the object.
     *
     * @return collision shape
     */
    public Rectangle getCollisionRectangle() {
        return this.block;
    }

    /**
     * Notify the object that we collided with it at collisionPoint with a given velocity.
     * The return is the new velocity expected after the hit (based on the force the object inflicted on us).
     *
     * @param collisionPoint  collision point
     * @param currentVelocity the velocity before the collision
     * @param hitter          a ball
     * @return new velocity
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        Velocity newVelocity = new Velocity(currentVelocity.getDx(), currentVelocity.getDy());
        /* Finding the vertices of the rectangle. */
        Point upperLeft = this.block.getUpperLeft();
        Point upperRight = new Point(upperLeft.getX() + this.block.getWidth(), upperLeft.getY());
        Point lowerLeft = new Point(upperLeft.getX(), upperLeft.getY() + this.block.getHeight());
        /* If the point is in up or down we will change its dy to -dy. */
        if ((collisionPoint.getY() == upperLeft.getY()) || (collisionPoint.getY() == lowerLeft.getY())) {
            newVelocity.setDy(-newVelocity.getDy());
        }
        /* If the point is in left or right we will change its dx to -dx. */
        if ((collisionPoint.getX() == upperLeft.getX()) || (collisionPoint.getX() == upperRight.getX())) {
            newVelocity.setDx(-newVelocity.getDx());
        }
        this.notifyHit(hitter);
        return newVelocity;
    }

    /**
     * draw the sprite to the screen.
     *
     * @param d surface
     */
    public void drawOn(DrawSurface d) {
        /* Set the color to the fill's color of the block. */
        d.setColor(this.fillColor);
        /* fill the block. */
        d.fillRectangle((int) (this.block.getUpperLeft().getX()), (int) (this.block.getUpperLeft().getY()),
                (int) (this.block.getWidth()), (int) (this.block.getHeight()));
    }

    /**
     * notify the sprite that time has passed.
     *
     * @param dt frame rate
     */
    public void timePassed(double dt) {
    }

    /**
     * add the block to the game as sprite and as collidable.
     *
     * @param g the current game
     */
    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);
    }

    /**
     * remove the block from the game as sprite and as collidable.
     *
     * @param g the current game
     */
    public void removeFromGame(GameLevel g) {
        g.removeCollidable(this);
        g.removeSprite(this);
        g.removeFromBlocksList(this);
    }

    /**
     * remove the block from the game as sprite.
     *
     * @param g the current game
     */
    public void removeFromSprite(GameLevel g) {
        g.removeSprite(this);
    }

    /**
     * Add hl as a listener to hit events.
     *
     * @param hl Hit listener
     */
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    /**
     * Remove hl from the list of listeners to hit events.
     *
     * @param hl Hit listener
     */
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * the method will be called whenever a hit() occurs, and notifiers all of the registered HitListener objects
     * by calling their hitEvent method.
     *
     * @param hitter a ball
     */
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event.
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }
}
