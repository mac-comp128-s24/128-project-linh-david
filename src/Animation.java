import edu.macalester.graphics.Rectangle;

public class Animation {
    private Rectangle disk;
    private double x, y, end_x, end_y, step_x, step_y;
    private int duration;

    public Animation(Rectangle disk, double end_x, double end_y, int duration) {
        this.disk = disk;
        this.x = disk.getCenter().getX();
        this.y = disk.getCenter().getY();
        this.end_x = end_x;
        this.end_y = end_y;
        this.duration = duration;
        this.step_x = (end_x - x) / duration;
        this.step_y = (end_y - y) / duration;
    }

    public void run() {
        x += step_x;
        y += step_y;
        disk.setCenter(x, y);
    }

    public void update() {
        this.x = disk.getCenter().getX();
        this.y = disk.getCenter().getY();
        this.step_x = (end_x - x) / duration;
        this.step_y = (end_y - y) / duration;
    }

    public boolean isDone() {
        boolean x_done = Math.abs(disk.getCenter().getX() - end_x) < 0.1;
        boolean y_done = Math.abs(disk.getCenter().getY() - end_y) < 0.1;
        if (x_done) {
            disk.setCenter(end_x, disk.getCenter().getY());
        }
        if (y_done) {
            disk.setCenter(disk.getCenter().getX(), end_y);
        }
        return x_done && y_done;
    }
}
