package gradle.java;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

public class EventListener {

    double x1, y1;
    boolean firstPoint = true;

    ArrayList<Line> lines = new ArrayList<>();

    public void handleMouseClick(double x, double y) {
		if (firstPoint) {
			x1 = x;
			y1 = y;
			firstPoint = false;
			System.out.println("Point 1: " + x1 + ", " + y1);
		} else {
			lines.add(new Line(x1, y1, x, y));
			firstPoint = true;
			System.out.println("Point 2: " + x + ", " + y);
		}
	}

	public void removeLastLine() {
		if (!lines.isEmpty()) {
			lines.remove(lines.size() - 1);
			System.out.println("Remove last line");
		}
	}



    public void display() {
		glColor3d(1, 1, 1);

		for (Line line : lines) {
			ddaline(line.x1, line.y1, line.x2, line.y2);
		}

		// แสดงจุดแรก (optional)
		if (!firstPoint) {
			drawPoint(x1, y1);
		}
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