import edu.macalester.graphics.*;

public class Timer {
    private CanvasWindow canvas;
    private GraphicsText secondsPrint;
    private String formattedTime;
    private double x, y;
    private long startTime;
    private boolean isActive = false; // whether the timer has started
    private boolean isOut = false; // whether the timer has finished counting down
    private int firstKey = 1; // making sure the timer only restarted once it is finished
    private int maxTime;

    /**
     * Creates a visual of the timer. The input coordinates specify the center of the timer graphic
     * 
     * @param maxTime the number of seconds to countdown from
     */
    public Timer(CanvasWindow canvas, double x, double y, int maxTime) {
        this.canvas = canvas;
        this.x = x;
        this.y = y;
        this.maxTime = maxTime;
        formattedTime = "00:";
        secondsPrint = new GraphicsText();
        secondsPrint.setFontSize(24);
        secondsPrint.setText("You've got " + maxTime + " seconds");
        secondsPrint.setCenter(x, y);
    }

    /**
     * Start the timer once, ensure that the timer cannot be restarted until it has finished counting
     * down
     */
    public void startTimer() {
        firstKey--;
        if (firstKey == 0) {
            startTime = System.currentTimeMillis();
            isActive = true;
        }
    }

    /**
     * Resets the timer, including its internal time keeping and the visual onscreen
     */
    public void reset() {
        canvas.remove(secondsPrint);
        secondsPrint.setText("You've got " + maxTime + " seconds");
        secondsPrint.setCenter(x, y);
        canvas.add(secondsPrint);
        firstKey = 1;
        isActive = false;
        isOut = false;

    }

    /**
     * Visually shows the timer running by constantly checking the internal time, updating the graphics
     * only once a second has passed, stopping when the timer has finished counting down
     */
    public void update() {
        if (isActive) {
            long timePassed = System.currentTimeMillis() - startTime;
            long secondsDisplay = maxTime - timePassed / 1000;
            secondsPrint.setText(secondsDisplay < 10 ? (formattedTime + "0" + String.valueOf(secondsDisplay))
                : (formattedTime + String.valueOf(secondsDisplay)));
            secondsPrint.setCenter(x, y);
            if (secondsDisplay <= 0) {
                isActive = false;
                isOut = true;
            }
        }
    }

    /**
     * @return whether the timer has started
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Adding the timer to the canvas
     */
    public void run() {
        canvas.add(secondsPrint);
    }

    /**
     * Removing the timer from the canvas
     */
    public void remove() {
        canvas.remove(secondsPrint);
    }
}
