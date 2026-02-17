package gradle.java;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

public class EventListener {

    double x1, y1;
    boolean firstPoint = true;

    ArrayList<Line> lines = new ArrayList<>();
    ArrayList<Point> polygonPoints = new ArrayList<>();
    boolean isClosed = false;
    Point t1 = new Point(200, 200);
    Point t2 = new Point(400, 200);
    Point t3 = new Point(300, 400);

    boolean isFilled = false;

    class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public void handleMouseClick(double x, double y) {

        if (isInsideTriangle((int) x, (int) y)) {
            isFilled = true;
            System.out.println("Fill triangle!");
        }
    }

    void fillTriangle() {

        glColor3d(1, 0, 0);

        Point[] pts = { t1, t2, t3 };

        for (int i = 0; i < 3 - 1; i++) {
            for (int j = i + 1; j < 3; j++) {
                if (pts[i].y > pts[j].y) {
                    Point temp = pts[i];
                    pts[i] = pts[j];
                    pts[j] = temp;
                }
            }
        }

        Point p0 = pts[0];
        Point p1 = pts[1];
        Point p2 = pts[2];

        double invSlope1 = 0;
        double invSlope2 = 0;

        if (p1.y - p0.y != 0) {

            invSlope1 = (double) (p1.x - p0.x) / (p1.y - p0.y);
            invSlope2 = (double) (p2.x - p0.x) / (p2.y - p0.y);

            double curx1 = p0.x;
            double curx2 = p0.x;

            for (int y = p0.y; y <= p1.y; y++) {

                int startX = (int) Math.min(curx1, curx2);
                int endX = (int) Math.max(curx1, curx2);

                glBegin(GL_POINTS);
                for (int x = startX; x <= endX; x++) {
                    glVertex2i(x, y);
                }
                glEnd();

                curx1 += invSlope1;
                curx2 += invSlope2;
            }

        }

        if (p2.y - p1.y != 0) {

            invSlope1 = (double) (p2.x - p1.x) / (p2.y - p1.y);
            invSlope2 = (double) (p2.x - p0.x) / (p2.y - p0.y);

            double curx1 = p1.x;
            double curx2 = p0.x + (p1.y - p0.y) *
                    (double) (p2.x - p0.x) / (p2.y - p0.y);

            for (int y = p1.y; y <= p2.y; y++) {

                int startX = (int) Math.min(curx1, curx2);
                int endX = (int) Math.max(curx1, curx2);

                glBegin(GL_POINTS);
                for (int x = startX; x <= endX; x++) {
                    glVertex2i(x, y);
                }
                glEnd();

                curx1 += invSlope1;
                curx2 += invSlope2;
            }
        }
    }

    public void removeLastLine() {
        if (!lines.isEmpty()) {
            lines.remove(lines.size() - 1);
            System.out.println("Remove last line");
        }
    }

    public void display() {

        glClear(GL_COLOR_BUFFER_BIT);

        if (isFilled) {
            fillTriangle();
        }

        glColor3d(1, 1, 1);
        ddaline(t1.x, t1.y, t2.x, t2.y);
        ddaline(t2.x, t2.y, t3.x, t3.y);
        ddaline(t3.x, t3.y, t1.x, t1.y);
    }

    boolean isInsideTriangle(int x, int y) {

        double denominator = ((t2.y - t3.y) * (t1.x - t3.x) + (t3.x - t2.x) * (t1.y - t3.y));

        double a = ((t2.y - t3.y) * (x - t3.x) + (t3.x - t2.x) * (y - t3.y)) / denominator;

        double b = ((t3.y - t1.y) * (x - t3.x) + (t1.x - t3.x) * (y - t3.y)) / denominator;

        double c = 1 - a - b;

        return a >= 0 && b >= 0 && c >= 0;
    }

    void drawPoint(double x, double y) {
        glPointSize(5);
        glBegin(GL_POINTS);
        glVertex2i((int) x, (int) y);
        glEnd();
    }

    public void ddaline(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double step = Math.max(Math.abs(dx), Math.abs(dy));
        double xc = dx / step;
        double yc = dy / step;

        double x = x1;
        double y = y1;

        glBegin(GL_POINTS);
        for (int i = 0; i <= step; i++) {
            glVertex2i((int) x, (int) y);
            x += xc;
            y += yc;
        }
        glEnd();
    }

    public void init() {
        glClearColor(0, 0, 0, 1);
    }

    public void reshape(int x, int y, int width, int height) {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, 0, height, -1, 1);
        glMatrixMode(GL_MODELVIEW);
    }

    public void dispose() {
        System.out.println("Cleaning up...");
    }
}

class Line {
    double x1, y1, x2, y2;

    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
}