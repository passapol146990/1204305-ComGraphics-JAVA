
package gradle.java;

import static org.lwjgl.opengl.GL11.*;

public class EventListener {

	public void display() {
		main();
		// transalation();
		// scaling();
		// scalingTransition();
		rotation();
	}
	void main(){
		glColor3d(1, 1, 1);
		ddaline(100, 100, 200, 100);
		ddaline(200, 100, 200, 200);
		ddaline(100, 100, 200, 200);
	}

	void rotation() {
		glColor3d(1, 1, 1);
		double angle = Math.toRadians(30);
		int px = 100;
		int py = 100;
		ddaline(
			rotX(100, 100, px, py, angle), rotY(100, 100, px, py, angle),
			rotX(200, 100, px, py, angle), rotY(200, 100, px, py, angle)
		);

		ddaline(
			rotX(200, 100, px, py, angle), rotY(200, 100, px, py, angle),
			rotX(200, 200, px, py, angle), rotY(200, 200, px, py, angle)
		);

		ddaline(
			rotX(100, 100, px, py, angle), rotY(100, 100, px, py, angle),
			rotX(200, 200, px, py, angle), rotY(200, 200, px, py, angle)
		);
	}

	int rotX(int x, int y, int px, int py, double ang) {
		x -= px;
		y -= py;
		int xr = (int)(x * Math.cos(ang) + y * Math.sin(ang));
		return xr + px;
	}

	int rotY(int x, int y, int px, int py, double ang) {
		x -= px;
		y -= py;
		int yr = (int)(-x * Math.sin(ang) + y * Math.cos(ang));
		return yr + py;
	}



	void transalation(){
		glColor3d(0, 0, 1);
		int tx=100,ty=100;
		ddaline(tranX(100, tx), tranY(200, ty), tranX(200, tx), tranY(200, ty));
		ddaline(tranX(200, tx), tranY(200, ty), tranX(200, tx), tranY(300, ty));
		ddaline(tranX(100, tx), tranY(200, ty), tranX(200, tx), tranY(300, ty));
	}

	void scaling(){
		glColor3d(0, 1, 0);
		int sx=2, sy=3;
		ddaline(100*sx, 100*sy, 200*sx, 100*sy);
		ddaline(200*sx, 100*sy, 200*sx, 200*sy);
		ddaline(100*sx, 100*sy, 200*sx, 200*sy);
	}

	void scalingTransition(){
		glColor3d(1, 0, 0);
		int sx=2, sy=3;
		int nx=100*sx, ny=100*sy;
		int tx=100-nx, ty=100-ny;
		ddaline(calScalingTran(100,sx,tx), calScalingTran(100,sy,ty), calScalingTran(200,sx,tx), calScalingTran(100,sy,ty));
		ddaline(calScalingTran(200,sx,tx), calScalingTran(100,sy,ty), calScalingTran(200,sx,tx), calScalingTran(200,sy,ty));
		ddaline(calScalingTran(100,sx,tx), calScalingTran(100,sy,ty), calScalingTran(200,sx,tx), calScalingTran(200,sy,ty));
	}

	int calScalingTran(int x,int sxsy, int txty){
		return x*sxsy+txty;
	}

	int tranX(int x,int tx){
		return x+tx;
	}
	int tranY(int y,int ty){
		return y+ty;
	}

	public void ddaline(double x1,double y1,double x2,double y2) {
		double dx,dy,step,xc,yc,x,y;
		dx=x2-x1;
		dy=y2-y1;
		if(Math.abs(dx)>Math.abs(dy)) step=dx;
		else step=dy;
		
		xc=(dx/step);
		yc=(dy/step);
		
		x=x1;
		y=y1;
		
		glBegin(GL_POINTS);
		glVertex2i((int)x, (int)y);
		glEnd();
		for(int k=1;k<=step;k++) {
			x=x+xc;
			y=y+yc;
			glBegin(GL_POINTS);
			glVertex2i((int)x, (int)y);
			glEnd();
		}
		
	}

	public void dispose() {
		System.out.println("Cleaning up...");
	}

	public void init() {}

	public void reshape(int x, int y, int width, int height) {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		glOrtho(0, 640, 0, 640, 0, 1);
		glMatrixMode(GL_MODELVIEW);
	}
}