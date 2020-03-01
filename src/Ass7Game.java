import animation.HighScoresAnimation;
import animation.KeyPressStoppableAnimation;
import animation.MenuAnimation;
import basic.Task;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import io.HighScoresTable;
import animation.AnimationRunner;
import game.GameFlow;

import java.io.File;
import java.io.IOException;

/**
 * classname: Ass7Game
 * The main.
 *
 * @author Narkis Kremizi
 */
public class Ass7Game {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int FRAME_RATE = 60;

    /**
     * Create a game object, initializes and runs it.
     *
     * @param args nothing
     */
    public static void main(String[] args) {
        // create new scores table
        HighScoresTable tableScores = new HighScoresTable(10);
        File file = new File("highscores.txt");
        // load the table if its exists
        if ((file.exists()) && (!file.isDirectory())) {
            try {
                tableScores.load(file);
            } catch (IOException ex) {
                System.err.println("Failed loading file");
                ex.printStackTrace(System.err);
            }
        } else {
            // else, create new file
            try {
                tableScores.save(file);
            } catch (IOException ex) {
                System.err.println("Failed saving object");
                ex.printStackTrace(System.err);
            }
        }
        // Create a window with the title "New Game" which is 800 pixels wide and 600 pixels high
        GUI gui = new GUI("Game", WIDTH, HEIGHT);
        AnimationRunner runner = new AnimationRunner(gui, FRAME_RATE);
        while (true) {
            GameFlow game = new GameFlow(runner, gui.getKeyboardSensor(), file, tableScores);
            /* create the level. */
            // menu - do something according to status: play a game, show high score, or quit
            MenuAnimation<Task<Void>> menu = new MenuAnimation<>("space-invaders", runner, gui.getKeyboardSensor());
            menu.addSelection("s", "Start Game", new Task<Void>() {
                public Void run() {
                    game.runLevel();
                    return null;
                }
            });
            menu.addSelection("h", "High Scores", new Task<Void>() {
                public Void run() {
                    HighScoresAnimation highScoresAnimation = new HighScoresAnimation(gui.getKeyboardSensor(),
                            tableScores);
                    KeyPressStoppableAnimation keyPressScores = new KeyPressStoppableAnimation(gui.getKeyboardSensor(),
                            KeyboardSensor.SPACE_KEY, highScoresAnimation);
                    runner.run(keyPressScores);
                    return null;
                }
            });
            menu.addSelection("q", "Quit", new Task<Void>() {
                public Void run() {
                    System.exit(-1);
                    return null;
                }
            });
            runner.run(menu);
            Task<Void> task = menu.getStatus();
            task.run();
            menu.setStatus(null);
            menu.setStop(false);
        }
    }
}