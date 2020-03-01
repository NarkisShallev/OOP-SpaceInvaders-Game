package game;

import basic.Collidable;
import basic.HitListener;
import basic.HitNotifier;
import basic.Velocity;
import biuoop.DrawSurface;
import geometry.Point;
import geometry.Rectangle;
import sprite.Background;
import sprite.Sprite;
import java.util.ArrayList;
import java.util.List;

/**
 * alien class.
 */
public class Alien implements Collidable, Sprite, HitNotifier {
    /* members. */
    private Rectangle alien;
    private List<HitListener> hitListeners;
    private Background background;
    private Velocity v;

    /**
     * A constructor that creates a block.
     *
     * @param rectangle  rectangle
     * @param background background
     */
    public Alien(Rectangle rectangle, Background background) {
        this.alien = rectangle;
        this.hitListeners = new ArrayList<HitListener>();
        this.background = background;
    }

    /**
     * Return the "collision shape" of the object.
     *
     * @return collision shape
     */
    public Rectangle getCollisionRectangle() {
        return this.alien;
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
    public Velocity hit(Ball hitter, geometry.Point collisionPoint, Velocity currentVelocity) {
        Velocity newVelocity = new Velocity(currentVelocity.getDx(), currentVelocity.getDy());
        /* Finding the vertices of the rectangle. */
        geometry.Point upperLeft = this.alien.getUpperLeft();
        geometry.Point upperRight = new geometry.Point(upperLeft.getX() + this.alien.getWidth(), upperLeft.getY());
        geometry.Point lowerLeft = new Point(upperLeft.getX(), upperLeft.getY() + this.alien.getHeight());
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
        this.background.drawOn(d, (int) this.alien.getUpperLeft().getX(), (int) this.alien.getUpperLeft().getY(),
                (int) this.alien.getWidth(), (int) this.alien.getHeight());
    }

    /**
     * notify the sprite that time has passed.
     *
     * @param dt frame rate
     */
    public void timePassed(double dt) {
        this.moveOneStep(dt);
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
        g.removeFromAliensList(this);
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
        List<HitListener> listeners = new ArrayList<>(this.hitListeners);
        // Notify all listeners about a hit event.
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * Promotes the alien in its trajectory, in case of a collision and in case of non-collision.
     *
     * @param dt frame rate
     */
    public void moveOneStep(double dt) {
        // the alien move
        this.alien.setUpperLeft(this.v.applyToPoint(this.alien.getUpperLeft(), dt));
    }

    /**
     * Sets the speed of the alien to the velocity we received.
     *
     * @param newV velocity of the ball
     */
    public void setVelocity(Velocity newV) {
        this.v = newV;
    }

    /**
     * return velocity.
     *
     * @return velocity
     */
    public Velocity getV() {
        return v;
    }
}