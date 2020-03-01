package game;

import basic.Counter;
import basic.HitListener;

/**
 * a AlienRemover is in charge of removing aliens from the game, as well as keeping count of the number of aliens
 * that remain.
 */
public class AlienRemover implements HitListener {
    // members
    private GameLevel game;
    private Counter remainingAliens;

    /**
     * constructor.
     *
     * @param game          current game
     * @param removedAliens removed aliens
     */
    public AlienRemover(GameLevel game, Counter removedAliens) {
        this.game = game;
        this.remainingAliens = removedAliens;
    }

    /**
     * Blocks that are hit and reach 0 hit-points should be removed from the game.
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
            beingHit.removeFromGame(this.game);
            this.remainingAliens.decrease(1);
        } else {
            hitter.removeFromGame(this.game);
        }
    }
}
