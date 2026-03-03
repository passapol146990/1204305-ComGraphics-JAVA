package gradle.java;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window3D {

    private long window;
    private int backgroundTextureID;

    // ตัวแปรตำแหน่ง
    float x = 0f, y = 0f, z = -2.0f;
    float xs = 0f, ys = 0f, zs = -2.0f;

    private Map<Integer, Boolean> keys = new HashMap<>();

    public void run() {
        init();
        loop();

        glDeleteTextures(backgroundTextureID);
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void init() {
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        window = glfwCreateWindow(600, 600, "LWJGL 3D with Background", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS)
                keys.put(key, true);
            else if (action == GLFW_RELEASE)
                keys.put(key, false);
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();

        glShadeModel(GL_SMOOTH);
        glClearColor(0f, 0f, 0f, 1f);
        glClearDepth(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        // โหลด Texture พื้นหลัง
        backgroundTextureID = loadTexture("D:\\66011212067\\0_WORK_DIR\\1204305-ComGraphics-JAVA\\rust.jpg");

        reshape(600, 600);
    }

    // ฟังก์ชันสำหรับโหลดรูปภาพ
    private int loadTexture(String path) {
        int textureID;
        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = stbi_load(path, w, h, comp, 4);
            if (image == null) {
                System.err.println("Could not load image at: " + path);
                return 0;
            }

            textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureID);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w.get(), h.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

            stbi_image_free(image);
        }
        return textureID;
    }

    private void reshape(int width, int height) {
        if (height <= 0)
            height = 1;
        float h = (float) width / (float) height;
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        perspective(45.0f, h, 0.1f, 100.0f);
        glMatrixMode(GL_MODELVIEW);
    }

    private void perspective(float fovY, float aspect, float zNear, float zFar) {
        float f = (float) (1.0 / Math.tan(Math.toRadians(fovY) / 2.0));
        glMultMatrixf(new float[] {
                f / aspect, 0, 0, 0,
                0, f, 0, 0,
                0, 0, (zFar + zNear) / (zNear - zFar), -1,
                0, 0, (2.0f * zFar * zNear) / (zNear - zFar), 0
        });
    }

    private void drawBackground() {
        glDisable(GL_DEPTH_TEST); // ปิด Depth เพื่อวาดเป็นพื้นหลัง
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(-1, 1, -1, 1, -1, 1); // ใช้ Ortho เพื่อให้ภาพเต็มหน้าจอ

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, backgroundTextureID);
        glColor3f(1, 1, 1); // ให้ภาพมีสีปกติ

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(-1, -1);
        glTexCoord2f(1, 0);
        glVertex2f(1, -1);
        glTexCoord2f(1, 1);
        glVertex2f(1, 1);
        glTexCoord2f(0, 1);
        glVertex2f(-1, 1);
        glEnd();

        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_DEPTH_TEST); // เปิด Depth คืนสำหรับวาดวัตถุ 3D
    }

    private void updateLogic() {
        float speed = 0.02f;
        if (keys.getOrDefault(GLFW_KEY_LEFT, false))
            x -= speed;
        if (keys.getOrDefault(GLFW_KEY_RIGHT, false))
            x += speed;
        if (keys.getOrDefault(GLFW_KEY_UP, false))
            y += speed;
        if (keys.getOrDefault(GLFW_KEY_DOWN, false))
            y -= speed;
        if (keys.getOrDefault(GLFW_KEY_Z, false))
            z += speed;
        if (keys.getOrDefault(GLFW_KEY_X, false))
            z -= speed;
        if (keys.getOrDefault(GLFW_KEY_V, false))
            zs -= speed;
        if (keys.getOrDefault(GLFW_KEY_C, false))
            zs += speed;
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            updateLogic();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            drawBackground();

            drawCharacter();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public void drawCharacter() {
        float bodyW = 0.15f, bodyH = 0.20f, bodyD = 0.1f;
        float headS = 0.12f; 

        glLoadIdentity();
        glTranslatef(x, y, z);
        glRotatef(10, 1.0f, 0.0f, 0.0f);

        // ลำตัว
        drawBox(0, 0, 0, bodyW, bodyH, bodyD, 0.0f, 0.0f, 1.0f);

        // หัว
        drawBox(0, bodyH + headS - 0.05f, 0, headS, headS, headS, 1, 1, 1);

        // ตา
        drawBox(-0.04f, bodyH + headS, headS, 0.02f, 0.02f, 0.01f, 0, 0, 0);
        drawBox(0.04f, bodyH + headS, headS, 0.02f, 0.02f, 0.01f, 0, 0, 0);
        // ปาก
        drawBox(0.0f, bodyH, headS, 0.04f, 0.01f, 0.01f, 0, 0, 0);

        // แขน
        drawBox(-(bodyW + 0.05f), 0.05f, 0, 0.04f, 0.15f, 0.04f, 1, 1, 1);
        drawBox((bodyW + 0.05f), 0.05f, 0, 0.04f, 0.15f, 0.04f, 1, 1, 1);

        // ขา
        drawBox(-0.07f, -(bodyH + 0.15f), 0, 0.05f, 0.15f, 0.05f, 1, 1, 1);
        drawBox(0.07f, -(bodyH + 0.15f), 0, 0.05f, 0.15f, 0.05f, 1, 1, 1);
    }

    private void drawBox(float x, float y, float z, float w, float h, float d, float r, float g, float b) {
        glPushMatrix();
        glTranslatef(x, y, z);
        glBegin(GL_QUADS);
        glColor3f(r, g, b);

        glVertex3f(-w, -h, d);
        glVertex3f(w, -h, d);
        glVertex3f(w, h, d);
        glVertex3f(-w, h, d);
        glVertex3f(-w, -h, -d);
        glVertex3f(-w, h, -d);
        glVertex3f(w, h, -d);
        glVertex3f(w, -h, -d);
        glVertex3f(-w, h, -d);
        glVertex3f(-w, h, d);
        glVertex3f(w, h, d);
        glVertex3f(w, h, -d);
        glVertex3f(-w, -h, -d);
        glVertex3f(w, -h, -d);
        glVertex3f(w, -h, d);
        glVertex3f(-w, -h, d);
        glVertex3f(w, -h, -d);
        glVertex3f(w, h, -d);
        glVertex3f(w, h, d);
        glVertex3f(w, -h, d);
        glVertex3f(-w, -h, -d);
        glVertex3f(-w, -h, d);
        glVertex3f(-w, h, d);
        glVertex3f(-w, h, -d);
        glEnd();
        glPopMatrix();
    }

    public void head() {
        float c = 0.1f;
        glLoadIdentity();
        glDisable(GL_TEXTURE_2D);
        glTranslatef(x, y, z);
        glRotatef(20, 1.0f, 0.0f, 0.0f);

        glBegin(GL_QUADS);
        glColor3f(1f, 0f, 0f);
        glVertex3f(c, c, -c);
        glVertex3f(-c, c, -c);
        glVertex3f(-c, c, c);
        glVertex3f(c, c, c);
        glColor3f(0f, 1f, 0f);
        glVertex3f(c, -c, c);
        glVertex3f(-c, -c, c);
        glVertex3f(-c, -c, -c);
        glVertex3f(c, -c, -c);
        glColor3f(0f, 0f, 1f);
        glVertex3f(c, c, c);
        glVertex3f(-c, c, c);
        glVertex3f(-c, -c, c);
        glVertex3f(c, -c, c);
        glColor3f(1f, 1f, 0f);
        glVertex3f(c, -c, -c);
        glVertex3f(-c, -c, -c);
        glVertex3f(-c, c, -c);
        glVertex3f(c, c, -c);
        glColor3f(1f, 0f, 1f);
        glVertex3f(-c, c, c);
        glVertex3f(-c, c, -c);
        glVertex3f(-c, -c, -c);
        glVertex3f(-c, -c, c);
        glColor3f(0f, 1f, 1f);
        glVertex3f(c, c, -c);
        glVertex3f(c, c, c);
        glVertex3f(c, -c, c);
        glVertex3f(c, -c, -c);
        glEnd();
    }

    public void cubetexture() {
        float s = 1.0f;
        glLoadIdentity();
        glDisable(GL_TEXTURE_2D);
        glTranslatef(xs, ys, zs);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glColor3f(1f, 1f, 1f);
        glBegin(GL_QUADS);
        glVertex3f(-s, -s, s);
        glVertex3f(s, -s, s);
        glVertex3f(s, s, s);
        glVertex3f(-s, s, s);
        glVertex3f(-s, -s, -s);
        glVertex3f(s, -s, -s);
        glVertex3f(s, s, -s);
        glVertex3f(-s, s, -s);
        glEnd();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    public static void main(String[] args) {
        new Window3D().run();
    }
}