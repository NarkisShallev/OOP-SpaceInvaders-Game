package game;

import animation.PauseScreen;
import animation.KeyPressStoppableAnimation;
import animation.Animation;
import animation.AnimationRunner;
import animation.CountdownAnimation;
import basic.Collidable;
import basic.Counter;
import basic.Velocity;
import levels.SpaceInvadersBackground;
import sprite.Sprite;
import sprite.SpriteCollection;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * classname: Game
 * Holds the sprites and the collidables, and is in charge of the animation.
 *
 * @author Narkis Kremizi
 * @version 1.0 19/04/2018
 */
public class GameLevel implements Animation {
    /* Members. */
    private final int height = 600;
    private final int width = 800;
    private Paddle paddle;
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Counter aliensNum = new Counter();
    private Counter ballsNum = new Counter();
    private Counter score;
    private Counter numberOfLives;
    private BallRemover ballRemoverListener;
    private AnimationRunner runner;
    private boolean running;
    private biuoop.KeyboardSensor keyboard;
    private Counter numOfLevel = new Counter();
    private AlienRemover alienRemoverListener;
    private BlockRemover blockRemoverListener;
    private ScoreTrackingListener scoreListener;
    private List<List<Alien>> aliens;
    private List<Ball> shoots = new ArrayList<>();
    private TeamOfAliens team;
    private double timeBetweenAliensShoots;
    private double timeBetweenPaddlesShoots;
    private List<Block> blocks = new ArrayList<>();

    /**
     * constructor.
     *
     * @param keyboard      keyboard
     * @param runner        runner
     * @param score         score
     * @param numberOfLives number of lives
     */
    public GameLevel(biuoop.KeyboardSensor keyboard, AnimationRunner runner, Counter score,
                     Counter numberOfLives) {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.keyboard = keyboard;
        this.runner = runner;
        this.score = score;
        this.numberOfLives = numberOfLives;
    }

    /**
     * add new collitable to the environment.
     *
     * @param c new collidable
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * remove collitable from the environment.
     *
     * @param c collidable
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     * add new sprite to the sprite collection.
     *
     * @param s new sprite
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * remove sprite from the sprite collection.
     *
     * @param s sprite
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * Initialize a new game: create the Blocks and Ball (and Paddle) and add them to the game.
     */
    public void initialize() {
        alienRemoverListener = new AlienRemover(this, this.aliensNum);
        blockRemoverListener = new BlockRemover(this);
        ballRemoverListener = new BallRemover(this, this.ballsNum);
        scoreListener = new ScoreTrackingListener(this.score);
        ScoreIndicator scoreIndicator = new ScoreIndicator(this.score);
        LivesIndicator livesIndicator = new LivesIndicator(this.numberOfLives);
        this.numOfLevel.increase(1);
        NameIndicator nameIndicator = new NameIndicator(this.numOfLevel);
        // draw the background
        SpaceInvadersBackground background = new SpaceInvadersBackground();
        this.sprites.addSprite(background);
        // creates the paddle and add it to the game
        this.paddle = new Paddle(new Rectangle(new Point(400 - 35, 560), 70, 15), this,
                8 * 60, Color.YELLOW);
        this.paddle.addToGame(this);
        // create the death-region
        Block deathRegion = new Block(new Rectangle(new Point(0, this.height), this.width, 20));
        // register the BallRemover class as a listener of the death-region and make the block invisible.
        deathRegion.addHitListener(ballRemoverListener);
        deathRegion.removeFromSprite(this);
        // add the score indicator, the lives indicator and the name indicator to the game.
        scoreIndicator.addToGame(this);
        livesIndicator.addToGame(this);
        nameIndicator.addToGame(this);
        initializeLevelStuff();
    }

    /**
     * initialize level stuff.
     */
    private void initializeLevelStuff() {
        while (this.shoots.size() != 0) {
            this.shoots.get(0).removeFromGame(this);
        }
        while (this.blocks.size() != 0) {
            this.blocks.get(0).removeFromGame(this);
        }
        // creates the shields
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 30; k++) {
                    blocks.add(new Block(new Rectangle(new Point(100 + i * 250 + k * 5, 500 + j * 5),
                            5, 5)));
                }
            }
        }
        // Adds the shields to the game and register the block remover object as a listener to all the shields
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).addToGame(this);
            blocks.get(i).addHitListener(blockRemoverListener);
            blocks.get(i).addHitListener(ballRemoverListener);
        }
        this.team = new TeamOfAliens();
        this.aliens = team.getMatrixAliens();
        // Adds the aliens to the game and register the alien remover object as a listener to all the aliens
        for (int i = 0; i < aliens.size(); i++) {
            for (int j = 0; j < aliens.get(i).size(); j++) {
                aliens.get(i).get(j).addToGame(this);
                aliens.get(i).get(j).addHitListener(alienRemoverListener);
                this.aliensNum.increase(1);
                aliens.get(i).get(j).addHitListener(scoreListener);
                aliens.get(i).get(j).addHitListener(ballRemoverListener);
                aliens.get(i).get(j).setVelocity(Velocity.fromAngleAndSpeed(90, 100));
            }
        }
    }

    /**
     * initialize another level.
     */
    public void initializeAnotherLevel() {
        initializeLevelStuff();
    }

    /**
     * initialize after loosing a life.
     */
    public void initializeAfterLoosingALife() {
        while (this.team.getUpEdgeOfTeam() >= 50) {
            for (int i = 0; i < aliens.size(); i++) {
                for (int j = 0; j < aliens.get(i).size(); j++) {
                    Rectangle alienShape = this.aliens.get(i).get(j).getCollisionRectangle();
                    alienShape.setUpperLeft(new Point(alienShape.getUpperLeft().getX(), alienShape.getUpperLeft().
                            getY() - 20));
                }
            }
        }
        while (this.team.getLeftEdgeOfTeam() >= 50) {
            for (int i = 0; i < aliens.size(); i++) {
                for (int j = 0; j < aliens.get(i).size(); j++) {
                    Rectangle alienShape = this.aliens.get(i).get(j).getCollisionRectangle();
                    alienShape.setUpperLeft(new Point(alienShape.getUpperLeft().getX() - 20, alienShape.
                            getUpperLeft().getY()));
                }
            }
        }
        for (int i = 0; i < aliens.size(); i++) {
            for (int j = 0; j < aliens.get(i).size(); j++) {
                aliens.get(i).get(j).setVelocity(Velocity.fromAngleAndSpeed(90, 100));
            }
        }
        while (this.shoots.size() != 0) {
            this.shoots.get(0).removeFromGame(this);
        }
    }

    /**
     * Run one turn of the game -- start the animation loop.
     */
    public void playOneTurn() {
        this.paddle.removeFromGame(this);
        // creates the paddle and add it to the game
        this.paddle = new Paddle(new Rectangle(new Point(400 - 35, 560), 70, 15), this,
                8 * 60, Color.YELLOW);
        this.paddle.addToGame(this);
        // use our runner to run the current animation -- which is one turn of the game.
        this.running = true;
        this.runner.run(this);
    }

    /**
     * @return - returns the list of sprites in the game.
     */
    public SpriteCollection getSprites() {
        return sprites;
    }

    /**
     * doOneFrame(DrawSurface) is in charge of the logic.
     *
     * @param d  drawSurface
     * @param dt frame rate
     */
    public void doOneFrame(DrawSurface d, double dt) {
        this.timeBetweenAliensShoots = this.timeBetweenAliensShoots - dt;
        this.timeBetweenPaddlesShoots = this.timeBetweenPaddlesShoots - dt;
        // pause for the key 'p'
        if (this.keyboard.isPressed("p")) {
            KeyPressStoppableAnimation keyPressPause = new KeyPressStoppableAnimation(this.keyboard,
                    KeyboardSensor.SPACE_KEY, new PauseScreen(this.keyboard));
            this.runner.run(keyPressPause);
        }
        // find which of the aliens need to shoot and make it shoot
        if (this.timeBetweenAliensShoots < 0) {
            Alien shooter = this.team.randomLowestAlienToShoot(this.aliens);
            this.alienCreateShoot(shooter);
        }
        if ((this.timeBetweenPaddlesShoots < 0) && (this.keyboard.isPressed("space"))) {
            this.paddleCreateShoot();
        }
        if (team.getRightEdgeOfTeam() > 800) {
            for (int i = 0; i < aliens.size(); i++) {
                for (int j = 0; j < aliens.get(i).size(); j++) {
                    this.aliens.get(i).get(j).setVelocity(new Velocity(-1.1 * this.aliens.get(i).get(j).getV().
                            getDx(), -1.1 * this.aliens.get(i).get(j).getV().getDy()));
                    Rectangle alienShape = this.aliens.get(i).get(j).getCollisionRectangle();
                    alienShape.setUpperLeft(new Point(alienShape.getUpperLeft().getX(), alienShape.getUpperLeft().
                            getY() + 20));
                }
            }
        } else if (team.getLeftEdgeOfTeam() < 0) {
            for (int i = 0; i < aliens.size(); i++) {
                for (int j = 0; j < aliens.get(i).size(); j++) {
                    this.aliens.get(i).get(j).setVelocity(new Velocity(-1.1 * this.aliens.get(i).get(j).getV().
                            getDx(),
                            -1.1 * this.aliens.get(i).get(j).getV().getDy()));
                    Rectangle alienShape = this.aliens.get(i).get(j).getCollisionRectangle();
                    alienShape.setUpperLeft(new Point(alienShape.getUpperLeft().getX(), alienShape.getUpperLeft().
                            getY() + 20));
                }
            }
        } else if (this.team.getDownEdgeOfTeam() > 500) {
            this.numberOfLives.decrease(1);
            if (this.numberOfLives.getValue() != 0) {
                this.initializeAfterLoosingALife();
                this.runner.run(new CountdownAnimation(2, 3, this.sprites));
            }
        }
        /* Draw all the sprites on the screen and notify them about the time. */
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed(dt);
        // Stops the game if there are no aliens
        if ((this.aliensNum.getValue() == 0) || (this.paddle.checkIfHit()) || (this.team.getDownEdgeOfTeam() > 500)) {
            this.running = false;
        }
    }

    /**
     * shouldStop() is in charge of stopping condition.
     *
     * @return T or F
     */
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * return keyboard.
     *
     * @return keyboard
     */
    public KeyboardSensor getKeyboard() {
        return keyboard;
    }

    /**
     * return number of blocks.
     *
     * @return number of blocks
     */
    public Counter getAliensNum() {
        return aliensNum;
    }

    /**
     * return num of level.
     *
     * @return num of level
     */
    public Counter getNumOfLevel() {
        return numOfLevel;
    }

    /**
     * create alien's shoot.
     *
     * @param alien alien
     */
    public void alienCreateShoot(Alien alien) {
        this.timeBetweenAliensShoots = 0.5;
        Ball shoot = new Ball(new Point((alien.getCollisionRectangle().getUpperLeft().getX()
                + alien.getCollisionRectangle().getWidth() / 2), alien.getCollisionRectangle().getUpperLeft().getY()
                + alien.getCollisionRectangle().getHeight() + 5), 5, Color.RED);
        shoot.setVelocity(Velocity.fromAngleAndSpeed(180, 400));
        shoot.setBallGameEnvironment(this.environment);
        shoot.addHitListener(this.ballRemoverListener);
        shoot.addToGame(this);
        this.shoots.add(shoot);
    }

    /**
     * create paddle's shoot.
     */
    public void paddleCreateShoot() {
        this.timeBetweenPaddlesShoots = 0.35;
        Ball shoot = new Ball(new Point((this.paddle.getCollisionRectangle().getUpperLeft().getX()
                + this.paddle.getCollisionRectangle().getWidth() / 2), this.paddle.getCollisionRectangle().
                getUpperLeft().getY() - 3), 3, Color.white);
        shoot.setVelocity(Velocity.fromAngleAndSpeed(0, 400));
        shoot.setBallGameEnvironment(this.environment);
        shoot.addHitListener(this.ballRemoverListener);
        shoot.addToGame(this);
        this.shoots.add(shoot);
    }

    /**
     * remove from aliens list.
     *
     * @param alien alien
     */
    public void removeFromAliensList(Alien alien) {
        for (int i = 0; i < this.aliens.size(); i++) {
            if (this.aliens.get(i).contains(alien)) {
                this.aliens.get(i).remove(alien);
                if (this.aliens.get(i).isEmpty()) {
                    this.aliens.remove(i);
                }
            }
        }
    }

    /**
     * remove from aliens list.
     *
     * @param ball ball
     */
    public void removeFromBallsList(Ball ball) {
        if (this.shoots.contains(ball)) {
            this.shoots.remove(ball);
        }
    }

    /**
     * return paddle.
     *
     * @return paddle
     */
    public Paddle getPaddle() {
        return paddle;
    }

    /**
     * remove from aliens list.
     *
     * @param block block
     */
    public void removeFromBlocksList(Block block) {
        if (this.blocks.contains(block)) {
            this.blocks.remove(block);
        }
    }
}