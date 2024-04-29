import edu.macalester.graphics.*;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.events.*;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * A simple game of Tower of Hanoi
 */
public class TowerOfHanoi {

    private static final int NUM_TOWERS = 3;
    private static final int NUM_DISKS = 3;
    private static final int MAX_TIME = 10;
    private static final double DISK_WIDTH_STARTING = 50;
    private static final double DISK_WIDTH_FINAL = 200;
    private static final double DISK_WIDTH_INCREMENT = (DISK_WIDTH_FINAL - DISK_WIDTH_STARTING) / NUM_DISKS;
    private static final double DISK_HEIGHT = 20;
    private static final double TOWER_WIDTH = 10;
    private static final double TOWER_Y = 150;
    private static final double TOWER_HEIGHT = DISK_HEIGHT * (NUM_DISKS + 1);
    private static final double DISK_Y_STARTING = TOWER_Y + TOWER_HEIGHT - DISK_HEIGHT;
    private static final double TOWER_X_STARTING = DISK_WIDTH_FINAL;
    private static final double DISTANCEBETWEEN = 100;
    private static final int ANIMATION_DURATION = 10; // 1/60 of a second
    private static final String TITLE_TEXT = "Tower of Hanoi";

    private CanvasWindow canvas;
    private GraphicsText label;
    private boolean isRunning;
    private Timer timer;
    private int move_counter;
    private GraphicsText counter;
    private edu.macalester.graphics.ui.Button solveButton;
    private Deque<Animation> animations;
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

        isRunning = false;

        timer = new Timer(canvas, 500, 38, MAX_TIME);
        timer.run();

        move_counter = 0;
        counter = new GraphicsText("Moves: " + move_counter, canvas.getWidth() - 150, 50.0f);
        counter.setFont(FontStyle.PLAIN, 24);
        canvas.add(counter);

        solveButton = new edu.macalester.graphics.ui.Button("Solve");
        solveButton.setCenter(500, 80);
        canvas.add(solveButton);
        solveButton.onClick(() -> {
            reset();
            solve(NUM_DISKS, 0, 2, 1);
            stopGame();
        });

        animations = new ArrayDeque<Animation>();

        towers = new ArrayList<Deque<Rectangle>>();
        for (int i = 0; i < NUM_TOWERS; i++) {
            towers.add(new ArrayDeque<Rectangle>());
        }
        towers_X = new double[NUM_TOWERS];

        createTowers();
        createDisks();
        canvas.onMouseDown(this::mousePressed);
        canvas.animate(() -> {
            while (!animations.isEmpty()) {
                Animation anim = animations.peek();
                anim.run();
                if (anim.isDone()) {
                    animations.poll();
                    if (!animations.isEmpty()) {
                        animations.peek().update();
                    }
                }
                break;
            }
        });
    }

    /**
     * Reset the game
     */
    public void reset() {
        isRunning = false;

        timer.reset();

        for (Deque<Rectangle> tower : towers) {
            for (Rectangle disk : tower) {
                canvas.remove(disk);
            }
            tower.clear();
        }

        animations = new ArrayDeque<Animation>();
        selectedDisk = null;
        createDisks();
        move_counter = 0;
    }

    /**
     * Create the towers
     */
    private void createTowers() {
        for (int i = 0; i < NUM_TOWERS; i++) {
            Rectangle tower = new Rectangle(
                TOWER_X_STARTING + i * (DISK_WIDTH_FINAL + DISTANCEBETWEEN) - TOWER_WIDTH / 2,
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
     * Start the game
     */
    public void startGame() {
        if (isRunning) {
            return;
        }

        reset();
        isRunning = true;
        label.setText(TITLE_TEXT);
        timer.startTimer();
        canvas.animate(() -> { timer.update(); });
    }

    public void stopGame() {
        if (!isRunning) {
            return;
        }

        isRunning = false;
        timer.reset();
    }

    /**
     * Increment the move counter
     */
    public void incrementCounter() {
        move_counter++;
        counter.setText("Moves: " + move_counter);
    }

    public void moveDisk(Rectangle disk, double end_x, double end_y) {
        animations.add(new Animation(disk, end_x, end_y, ANIMATION_DURATION));
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
        if (towers.get(tower).isEmpty() || towers.get(tower).peekFirst().getWidth() > selectedDisk.getWidth()) {
            selectedDisk.setFillColor(Color.CYAN);
            moveDisk(selectedDisk, towers_X[tower],
                TOWER_Y + TOWER_HEIGHT - DISK_HEIGHT * towers.get(tower).size() - DISK_HEIGHT / 2);
            towers.get(tower).push(selectedDisk);
            selectedDisk = null;
            incrementCounter();
        }
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
        if (selectedDisk == null) {
            if (!(canvas.getElementAt(event.getPosition()) instanceof Rectangle)) {
                return;
            }

            startGame();

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
                placeDisk(0);
            } else if (x > middle_bound1 && x < middle_bound2) {
                placeDisk(1);
            } else if (x > middle_bound2 && x < right_bound) {
                placeDisk(2);
            } else {
                return;
            }

            if (checkForWin()) {
                label.setText("You win!");
                stopGame();
            }
        }

    }

    /**
     * Solve the game
     */
    public void solve(int n, int start, int end, int aux) {
        if (n == 0) {
            return;
        }
        solve(n - 1, start, aux, end);
        selectDisk(start);
        placeDisk(end);
        solve(n - 1, aux, end, start);
    }

    public static void main(String[] args) {
        TowerOfHanoi game = new TowerOfHanoi();
    }

}
