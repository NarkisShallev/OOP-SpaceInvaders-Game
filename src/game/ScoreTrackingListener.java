package game;

import basic.Counter;
import basic.HitListener;

/**
 * update score counter when blocks are being hit and removed.
 */
public class ScoreTrackingListener implements HitListener {
    // members
    private Counter currentScore;

    /**
     * constructor.
     *
     * @param scoreCounter counter
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }

    /**
     * hitting a block is worth 5 points, and destroying a block is worth and additional 10 points.
     *
     * @param beingHit object that was hit
     * @param hitter   the Ball that's doing the hitting
     */
    public void hitEvent(Block beingHit, Ball hitter) {
    }

    /**
     * For Aliens.
     * This method is called whenever the beingHit object is hit.
     * The hitter parameter is the Ball that's doing the hitting.
     *
     * @param beingHit object that was hit
     * @param hitter   the Ball that's doing the hitting
     */
    public void hitEvent(Alien beingHit, Ball hitter) {
        if (hitter.getVelocity().getDy() != 400) {
            currentScore.increase(100);
        }
    }
}