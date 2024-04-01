import edu.macalester.graphics.*;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.events.*;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;

/**
 * Created by bjackson on 3/2/2016.
 */
public class TowerOfHanoi {

    public static final int NUM_TOWERS = 3;
    public static final int NUM_DISKS = 5;
    public static final double DISK_WIDTH_STARTING = 75;
    public static final double DISK_WIDTH_INCREMENT = 25;
    public static final double DISK_HEIGHT = 20;
    public static final double DISK_X = 20;
    public static final double TOWER_WIDTH = 10;
    public static final double TOWER_Y = 150;
    public static final double TOWER_HEIGHT = DISK_HEIGHT * (NUM_DISKS + 1);
    public static final double DISK_Y_STARTING = TOWER_Y + TOWER_HEIGHT - DISK_HEIGHT;
    public static final double TOWER_X_STARTING = 20 + DISK_WIDTH_STARTING + NUM_TOWERS * DISK_WIDTH_INCREMENT;
    public static final double DISTANCEBETWEEN = 10;
    public static final String PLAYER1_TEXT = "Player 1's turn:";
    public static final String PLAYER2_TEXT = "Player 2's turn:";

    private CanvasWindow canvas;
    private GraphicsText label;
    private ArrayList<Deque<Rectangle>> towers;
    private double[] towers_X;

    /**
     * Initialize instance variables
     */
    public TowerOfHanoi() {
        canvas = new CanvasWindow("Tower of Hanoi", 1000, 375);
        label = new GraphicsText(PLAYER1_TEXT, (float) DISK_X, 50.0f);
        label.setFont(FontStyle.PLAIN, 24);
        canvas.add(label);

        towers = new ArrayList<Deque<Rectangle>>();
        for (int i = 0; i < NUM_TOWERS; i++) {
            towers.add(new ArrayDeque<Rectangle>());
        }
        towers_X = new double[NUM_TOWERS];

        createTowers();
        createDisks();
        canvas.onMouseDown(this::mousePressed);
        canvas.onMouseUp(this::mouseReleased);
    }

    private void createTowers() {
        for (int i = 0; i < NUM_TOWERS; i++) {
            Rectangle tower = new Rectangle(
                (TOWER_X_STARTING
                    + i * ((DISK_WIDTH_STARTING + NUM_TOWERS * DISK_WIDTH_INCREMENT * 2) + DISTANCEBETWEEN)),
                TOWER_Y, TOWER_WIDTH, TOWER_HEIGHT);
            tower.setFillColor(Color.DARK_GRAY);
            tower.setFilled(true);
            canvas.add(tower);
            towers_X[i] = tower.getX() + TOWER_WIDTH / 2;
        }
    }

    public void createDisks() {
        for (int i = 0; i < NUM_DISKS; i++) {
            Rectangle disk = new Rectangle(
                (towers_X[0] - (DISK_WIDTH_STARTING + (NUM_TOWERS - i) * DISK_WIDTH_INCREMENT) / 2),
                (DISK_Y_STARTING - i * DISK_HEIGHT), (DISK_WIDTH_STARTING + (NUM_TOWERS - i) * DISK_WIDTH_INCREMENT),
                DISK_HEIGHT);
            disk.setFillColor(Color.CYAN);
            disk.setFilled(true);
            canvas.add(disk);
            towers.get(0).add(disk);
        }
    }

    private boolean checkForWin() {
        return towers.get(0).isEmpty() && towers.get(1).isEmpty() || towers.get(0).isEmpty() && towers.get(2).isEmpty();
    }


    /**
     * Responds to mouse press down events
     * 
     * @param event
     */
    public void mousePressed(MouseButtonEvent event) {

    }

    /**
     * Responds to mouse button released events
     * 
     * @param event
     */
    public void mouseReleased(MouseButtonEvent event) {
    }

    public static void main(String[] args) {
        TowerOfHanoi game = new TowerOfHanoi();
    }

}
