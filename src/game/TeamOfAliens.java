package game;

import geometry.Point;
import geometry.Rectangle;
import sprite.ImageBackground;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * create team of aliens.
 */
public class TeamOfAliens {
    private List<List<Alien>> matrixAliens;

    /**
     * constructor.
     */
    public TeamOfAliens() {
        this.matrixAliens = new ArrayList<>();
        Image image;
        try {
            image = ImageIO.read(ClassLoader.getSystemClassLoader().getResourceAsStream("alien.png"));
        } catch (IOException ex) {
            throw new RuntimeException("Failed loading image");
        }
        for (int i = 0; i < 10; i++) {
            List<Alien> column = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                column.add(new Alien(new Rectangle(new Point(40 * i + 50 + 10 * i, 30 * j + 50 + 10 * j),
                        40, 30), new ImageBackground(image)));
            }
            this.matrixAliens.add(column);
        }
    }

    /**
     * get  matrix of aliens.
     *
     * @return matrix of aliens
     */
    public List<List<Alien>> getMatrixAliens() {
        return matrixAliens;
    }

    /**
     * pick which alien need to shoot.
     *
     * @param list the aliens who in the game
     * @return alien
     */
    public Alien randomLowestAlienToShoot(List<List<Alien>> list) {
        Random random = new Random();
        int randomColumn = random.nextInt(list.size());
        List<Alien> column = list.get(randomColumn);
        while (column.isEmpty()) {
            randomColumn = random.nextInt(list.size());
            column = list.get(randomColumn);
        }
        return column.get(column.size() - 1);
    }

    /**
     * get down edge of team.
     *
     * @return down edge
     */
    public double getDownEdgeOfTeam() {
        double max = 0;
        for (int i = 0; i < this.matrixAliens.size(); i++) {
            double lastY = this.matrixAliens.get(i).get(this.matrixAliens.get(i).size() - 1).getCollisionRectangle().
                    getUpperLeft().getY() + 30;
            if (lastY > max) {
                max = lastY;
            }
        }
        return max;
    }

    /**
     * get up edge of team.
     *
     * @return up edge
     */
    public double getUpEdgeOfTeam() {
        double min = 600;
        for (int i = 0; i < this.matrixAliens.size(); i++) {
            double firstY = this.matrixAliens.get(i).get(0).getCollisionRectangle().getUpperLeft().getY();
            if (firstY < min) {
                min = firstY;
            }
        }
        return min;
    }

    /**
     * get right edge of team.
     *
     * @return right edge
     */
    public double getRightEdgeOfTeam() {
        return this.matrixAliens.get(this.matrixAliens.size() - 1).get(0).getCollisionRectangle().getUpperLeft().
                getX() + 40;
    }

    /**
     * get left edge of team.
     *
     * @return left edge
     */
    public double getLeftEdgeOfTeam() {
        return this.matrixAliens.get(0).get(0).getCollisionRectangle().getUpperLeft().getX();
    }
}
