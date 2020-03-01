package basic;

import game.Alien;
import game.Ball;
import game.Block;

/**
 * Objects that want to be notified of hit events, implements the HitListener interface.
 */
public interface HitListener {

    /**
     * For blocks.
     * This method is called whenever the beingHit object is hit.
     * The hitter parameter is the Ball that's doing the hitting.
     *
     * @param beingHit object that was hit
     * @param hitter   the Ball that's doing the hitting
     */
    void hitEvent(Block beingHit, Ball hitter);

    /**
     * For Aliens.
     * This method is called whenever the beingHit object is hit.
     * The hitter parameter is the Ball that's doing the hitting.
     *
     * @param beingHit object that was hit
     * @param hitter   the Ball that's doing the hitting
     */
    void hitEvent(Alien beingHit, Ball hitter);
}