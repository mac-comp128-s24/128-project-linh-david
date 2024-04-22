import edu.macalester.graphics.*;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.events.*;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;

/**
 * A simple game of Tower of Hanoi
 */
public class TowerOfHanoi {

    public static final int NUM_TOWERS = 3;
    public static final int NUM_DISKS = 3;
    public static final double DISK_WIDTH_STARTING = 50;
    public static final double DISK_WIDTH_FINAL = 200;
    public static final double DISK_WIDTH_INCREMENT = (DISK_WIDTH_FINAL - DISK_WIDTH_STARTING) / NUM_DISKS;
    public static final double DISK_HEIGHT = 20;
    public static final double TOWER_WIDTH = 10;
    public static final double TOWER_Y = 150;
    public static final double TOWER_BASE_LENGTH = 50;
    public static final double TOWER_HEIGHT = DISK_HEIGHT * (NUM_DISKS + 1);
    public static final double DISK_Y_STARTING = TOWER_Y + TOWER_HEIGHT - DISK_HEIGHT;
    public static final double TOWER_X_STARTING = DISK_WIDTH_FINAL;
    public static final double DISTANCEBETWEEN = 100;
    public static final String TITLE_TEXT = "Tower of Hanoi";

    private CanvasWindow canvas;
    private GraphicsText label;
    private int move_counter;
    private GraphicsText counter;
    private ArrayList<Deque<Rectangle>> towers;
    private double[] towers_X;
    private Rectangle selectedDisk;

    /**
     * Initialize instance variables
     */
    public TowerOfHanoi() {
        canvas = new CanvasWindow("Tower of Hanoi", 1000, 375);
        label = new GraphicsText(TITLE_TEXT, (float) 20.0f, 50.0f);
        label.setFont(FontStyle.PLAIN, 24);
        canvas.add(label);

        move_counter = 0;
        counter = new GraphicsText("Moves: " + move_counter, 20.0f, 100.0f);
        counter.setFont(FontStyle.PLAIN, 24);
        canvas.add(counter);

        towers = new ArrayList<Deque<Rectangle>>();
        for (int i = 0; i < NUM_TOWERS; i++) {
            towers.add(new ArrayDeque<Rectangle>());
        }
        towers_X = new double[NUM_TOWERS];

        createTowers();
        createDisks();
        canvas.onMouseDown(this::mousePressed);
    }

    /**
     * Create the towers
     */
    private void createTowers() {
        for (int i = 0; i < NUM_TOWERS; i++) {
            Rectangle tower = new Rectangle(
                (TOWER_X_STARTING + i * (DISK_WIDTH_FINAL + DISTANCEBETWEEN)),
                TOWER_Y, TOWER_WIDTH, TOWER_HEIGHT);
            tower.setFillColor(Color.DARK_GRAY);
            tower.setFilled(true);
            canvas.add(tower);
            towers_X[i] = tower.getX() + TOWER_WIDTH / 2;
        }
    }

    /**
     * Create the disks
     */
    public void createDisks() {
        for (int i = 0; i < NUM_DISKS; i++) {
            double DISK_WIDTH = DISK_WIDTH_FINAL - i * DISK_WIDTH_INCREMENT;
            Rectangle disk = new Rectangle(
                (towers_X[0] - DISK_WIDTH / 2),
                (DISK_Y_STARTING - i * DISK_HEIGHT), DISK_WIDTH, DISK_HEIGHT);
            disk.setFillColor(Color.CYAN);
            disk.setFilled(true);
            canvas.add(disk);
            towers.get(0).push(disk);
        }
    }

    /**
     * Select a disk and hold it
     * 
     * @param tower
     */
    public void selectDisk(int tower) {
        if (!towers.get(tower).isEmpty()) {
            Rectangle disk = towers.get(tower).pop();
            disk.setFillColor(Color.RED);
            selectedDisk = disk;
        }
    }

    /**
     * Place the disk being selected/held
     * 
     * @param tower
     */
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

    /**
     * Increment the move counter
     */
    public void incrementCounter() {
        move_counter++;
        counter.setText("Moves: " + move_counter);
    }

    /**
     * Check if the game is won
     * 
     * @return
     */
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
            double x = event.getPosition().getX();
            double left_bound = towers_X[0] - DISK_WIDTH_FINAL / 2;
            double right_bound = towers_X[2] + DISK_WIDTH_FINAL / 2;
            double middle_bound1 = towers_X[1] - DISK_WIDTH_FINAL / 2;
            double middle_bound2 = towers_X[1] + DISK_WIDTH_FINAL / 2;

            if (x > left_bound && x < middle_bound1) {
                System.out.println("Placing disk to tower 0");
                placeDisk(0);
            } else if (x > middle_bound1 && x < middle_bound2) {
                System.out.println("Placing disk to tower 1");
                placeDisk(1);
            } else if (x > middle_bound2 && x < right_bound) {
                System.out.println("Placing disk to tower 2");
                placeDisk(2);
            } else {
                return;
            }

            incrementCounter();

            if (checkForWin()) {
                label.setText("You win!");
            }
        }

    }

    public static void main(String[] args) {
        TowerOfHanoi game = new TowerOfHanoi();
    }

}
