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
    public static final double DISK_WIDTH_FINAL = 200;
    public static final double DISK_WIDTH_INCREMENT = (DISK_WIDTH_FINAL - DISK_WIDTH_STARTING) / NUM_DISKS;
    public static final double DISK_HEIGHT = 20;
    public static final double DISK_X = 20;
    public static final double TOWER_WIDTH = 10;
    public static final double TOWER_Y = 150;
    public static final double TOWER_HEIGHT = DISK_HEIGHT * (NUM_DISKS + 1);
    public static final double DISK_Y_STARTING = TOWER_Y + TOWER_HEIGHT - DISK_HEIGHT;
    public static final double TOWER_X_STARTING = 20 + DISK_WIDTH_STARTING + NUM_TOWERS * DISK_WIDTH_INCREMENT;
    public static final double DISTANCEBETWEEN = 10;
    public static final String TITLE_TEXT = "Tower of Hanoi";

    private CanvasWindow canvas;
    private GraphicsText label;
    private ArrayList<Deque<Rectangle>> towers;
    private double[] towers_X;
    private Rectangle selectedDisk;

    /**
     * Initialize instance variables
     */
    public TowerOfHanoi() {
        canvas = new CanvasWindow("Tower of Hanoi", 1000, 375);
        label = new GraphicsText(TITLE_TEXT, (float) DISK_X, 50.0f);
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
            towers.get(0).push(disk);
        }
    }

    public void selectDisk(int tower) {
        if (!towers.get(tower).isEmpty()) {
            Rectangle disk = towers.get(tower).pop();
            disk.setFillColor(Color.RED);
            selectedDisk = disk;
        }
    }

    public void placeDisk(int tower) {
        System.out.println("Placing disk");
        if (towers.get(tower).isEmpty() || towers.get(tower).peekFirst().getWidth() > selectedDisk.getWidth()) {
            selectedDisk.setFillColor(Color.CYAN);
            selectedDisk.setPosition(towers_X[tower] - selectedDisk.getWidth() / 2,
                TOWER_Y + TOWER_HEIGHT - DISK_HEIGHT * (towers.get(tower).size() + 1));
            towers.get(tower).push(selectedDisk);
            selectedDisk = null;
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
        System.out.println("Mouse pressed at " + event.getPosition());
        System.out.println("Selected disk: " + selectedDisk);
        if (selectedDisk == null) {
            if (!(canvas.getElementAt(event.getPosition()) instanceof Rectangle)) {
                return;
            }
            Rectangle disk = (Rectangle) canvas.getElementAt(event.getPosition());
            if (disk.equals(towers.get(0).peekFirst())) {
                selectDisk(0);
            } else if (disk.equals(towers.get(1).peekFirst())) {
                selectDisk(1);
            } else if (disk.equals(towers.get(2).peekFirst())) {
                selectDisk(2);
            } else {
                return;
            }
        } else {
            if (event.getPosition().getX() > towers_X[0] - DISK_WIDTH_FINAL / 2
                && event.getPosition().getX() < towers_X[0] + DISK_WIDTH_FINAL / 2) {
                System.out.println("Placing disk to tower 0");
                placeDisk(0);
            } else if (event.getPosition().getX() > towers_X[1] - DISK_WIDTH_FINAL / 2
                && event.getPosition().getX() < towers_X[1] + DISK_WIDTH_FINAL / 2) {
                System.out.println("Placing disk to tower 1");
                placeDisk(1);
            } else if (event.getPosition().getX() > towers_X[2] - DISK_WIDTH_FINAL / 2
                && event.getPosition().getX() < towers_X[2] + DISK_WIDTH_FINAL / 2) {
                System.out.println("Placing disk to tower 2");
                placeDisk(2);
            } else {
                return;
            }
            if (checkForWin()) {
                label.setText("You win!");
            }
        }

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