
package gradle.java;

import static org.lwjgl.opengl.GL11.*;

public class EventListener {

	public void display() {
		// 1. ล้างหน้าจอเพียงครั้งเดียว
		// glClear(GL_COLOR_BUFFER_BIT);

		// // วาด POINTS
		// glColor3d(1, 0, 0);
		// glPointSize(5);
		// glBegin(GL_POINTS);
		// glVertex2i(50, 50);
		// glVertex2i(100, 100);
		// glEnd();

		// // LINES
		// glClear(GL_COLOR_BUFFER_BIT);
		// glColor3d(0, 1, 0);
		// glLineWidth(5);
		// glBegin(GL_LINES);
		// glVertex2i(50, 50);
		// glVertex2i(100, 100);
		// glEnd();

		// // GL_LINE_STRIP
		// glClear(GL_COLOR_BUFFER_BIT);
		// glColor3d(1, 0, 1);
		// glBegin(GL_LINE_STRIP);
		// glVertex2d(200, 200);
		// glVertex2d(250, 400);
		// glVertex2d(250, 200);
		// glVertex2d(300, 400);
		// glEnd();
		// // Recti 4 เหลี่ยม
		// glColor3d(0, 0, 1);
		// glRecti(50, 50, 250, 150);

		// glClear(GL_COLOR_BUFFER_BIT);
		// glBegin(GL_POLYGON);
		// glVertex2i(50, 100);
		// glVertex2i(400, 100);
		// glVertex2i(400, 400);
		// glVertex2i(225, 600);
		// glVertex2i(50,400);
		// glEnd();
		// ==================================[ 1 ] ==================================
		c1();
		// ==================================[ 2 ] ==================================
		c2();
		// ==================================[ 3 ] ==================================
		c3();
	}

	public void c1(){
		glPointSize(10);
		glBegin(GL_POINTS);
		glVertex2d(50, 500);
		glVertex2d(100, 525);
		glEnd();
		glBegin(GL_LINES);
		glVertex2d(50, 500);
		glVertex2d(100, 525);
		glEnd();
		glBegin(GL_POINTS);
		glVertex2d(125, 475);
		glVertex2d(100, 400);
		glEnd();
		glBegin(GL_LINES);
		glVertex2d(125, 475);
		glVertex2d(100, 400);
		glEnd();
		glBegin(GL_POINTS);
		glVertex2d(60, 420);
		glVertex2d(80, 475);
		glEnd();
		glBegin(GL_LINES);
		glVertex2d(60, 420);
		glVertex2d(80, 475);
		glEnd();
	}

	public void c2(){
		glBegin(GL_LINE_LOOP);
		glLineWidth(2);
		glColor3d(1, 1, 0);
		glVertex2d(50, 50);
		glVertex2d(50, 150);
		glVertex2d(100, 200);
		glVertex2d(150, 150);
		glVertex2d(150, 50);
		glEnd();
		glBegin(GL_LINE_STRIP);
		glVertex2d(75, 175);
		glVertex2d(75, 200);
		glVertex2d(90, 200);
		glVertex2d(90, 190);
		glEnd();
		glBegin(GL_LINE_STRIP);
		glVertex2d(75, 50);
		glVertex2d(75, 100);
		glVertex2d(90, 100);
		glVertex2d(90, 50);
		glEnd();
		glBegin(GL_LINE_LOOP);
		glVertex2d(110, 100);
		glVertex2d(110, 125);
		glVertex2d(140, 125);
		glVertex2d(140, 100);
		glEnd();
	}
	public void c3(){
		// glClear(GL_COLOR_BUFFER_BIT);
		glColor3d(1, 1, 1);
		glRecti(100+200, 100, 200+200, 200);
		glEnd();

		glBegin(GL_POLYGON);
		glColor3d(1, 0, 0);
		glVertex2d(100+200, 200);
		glVertex2d(150+200, 275);
		glVertex2d(200+200, 200);
		glEnd();

		glBegin(GL_POLYGON);
		glColor3d(0, 1, 0);
		glVertex2d(200+200, 100);
		glVertex2d(275+200, 150);
		glVertex2d(275+200, 250);
		glVertex2d(200+200, 200);
		glEnd();

		glBegin(GL_POLYGON);
		glColor3d(0, 0, 1);
		glVertex2d(275+200, 250);
		glVertex2d(225+200, 325);
		glVertex2d(150+200, 275);
		glVertex2d(200+200, 200);
		glEnd();
	}

	public void dispose() {
		// Cleanup code
		System.out.println("Cleaning up...");
	}

	public void init() {
		// Set clear color (background)
		// glClearColor(0.2f, 0.3f, 0.4f, 1.0f);
	}

	public void reshape(int x, int y, int width, int height) {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		glOrtho(0, 640, 0, 640, 0, 1);
		glMatrixMode(GL_MODELVIEW);
	}
}