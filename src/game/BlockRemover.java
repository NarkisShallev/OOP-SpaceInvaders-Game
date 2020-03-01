package game;

import basic.HitListener;

/**
 * a BlockRemover is in charge of removing blocks from the game, as well as keeping count of the number of blocks
 * that remain.
 */
public class BlockRemover implements HitListener {
    // members
    private GameLevel game;

    /**
     * constructor.
     *
     * @param game current game
     */
    public BlockRemover(GameLevel game) {
        this.game = game;
    }

    /**
     * Blocks that are hit and reach 0 hit-points should be removed from the game.
     *
     * @param beingHit object that was hit
     * @param hitter   the Ball that's doing the hitting
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        beingHit.removeFromGame(this.game);
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

    }
}