package game;

import animation.EndScreen;
import animation.AnimationRunner;
import animation.CountdownAnimation;
import animation.HighScoresAnimation;
import animation.KeyPressStoppableAnimation;
import basic.Counter;
import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import io.HighScoresTable;

import java.io.File;
import java.io.IOException;

/**
 * This class will be in charge of creating the differnet levels, and moving from one level to the next.
 */
public class GameFlow {
    private AnimationRunner runner;
    private biuoop.KeyboardSensor keyboard;
    private Counter score = new Counter();
    private Counter numberOfLives = new Counter();
    private File file;
    private HighScoresTable tableScores;

    /**
     * constructor.
     *
     * @param runner      runner
     * @param keyboard    keyboard
     * @param file        file
     * @param tableScores table scores
     */
    public GameFlow(AnimationRunner runner, biuoop.KeyboardSensor keyboard, File file, HighScoresTable tableScores) {
        this.runner = runner;
        this.keyboard = keyboard;
        this.numberOfLives.increase(3);
        this.file = file;
        this.tableScores = tableScores;
    }

    /**
     * run the level.
     */
    public void runLevel() {
        GameLevel level = new GameLevel(this.keyboard, this.runner, this.score, this.numberOfLives);
        level.initialize();
        this.runner.run(new CountdownAnimation(2, 3, level.getSprites()));
        while (this.numberOfLives.getValue() > 0) {
            level.playOneTurn();
            if (level.getPaddle().checkIfHit()) {
                this.numberOfLives.decrease(1);
                if (this.numberOfLives.getValue() == 0) {
                    break;
                }
                level.initializeAfterLoosingALife();
                this.runner.run(new CountdownAnimation(2, 3, level.getSprites()));
            }
            if (level.getAliensNum().getValue() == 0) {
                level.getNumOfLevel().increase(1);
                level.initializeAnotherLevel();
                this.runner.run(new CountdownAnimation(2, 3, level.getSprites()));
            }
        }
        EndScreen endScreen = new EndScreen(this.keyboard, this.score, this.numberOfLives);
        KeyPressStoppableAnimation keyPressEnd = new KeyPressStoppableAnimation(this.keyboard,
                KeyboardSensor.SPACE_KEY, endScreen);
        this.runner.run(keyPressEnd);
        saveScore();
        HighScoresAnimation highScoresAnimation = new HighScoresAnimation(this.keyboard, this.tableScores);
        KeyPressStoppableAnimation keyPressScores = new KeyPressStoppableAnimation(this.keyboard,
                KeyboardSensor.SPACE_KEY, highScoresAnimation);
        this.runner.run(keyPressScores);
    }

    /**
     *
     */
    public void saveScore() {
        if (this.score.getValue() > 0) {
            DialogManager dialog = runner.getGui().getDialogManager();
            String name = dialog.showQuestionDialog("Name", "What is your name?", "");
            tableScores.add(new ScoreInfo(name, score.getValue()));
            try {
                tableScores.save(file);
            } catch (IOException ex) {
                System.err.println("Failed saving object" + ", message:" + ex.getMessage());
            }
        }
    }
}
