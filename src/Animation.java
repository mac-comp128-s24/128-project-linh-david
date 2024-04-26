import edu.macalester.graphics.Rectangle;

public class Animation {
    private Rectangle disk;
    private double start_x, start_y, end_x, end_y, step_x, step_y;

    public Animation(Rectangle disk, double end_x, double end_y, double step_x,
        double step_y) {
        this.disk = disk;
        this.start_x = disk.getCenter().getX();
        this.start_y = disk.getCenter().getY();
        this.end_x = end_x;
        this.end_y = end_y;
        this.step_x = step_x;
        this.step_y = step_y;
    }

    public void run() {
        start_x += step_x;
        start_y += step_y;
        disk.setCenter(start_x, start_y);
    }

    public boolean isDone() {
        return disk.getCenter().getX() == end_x && disk.getCenter().getY() == end_y;
    }
}
