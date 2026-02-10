package gradle.java;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class App {
    
    private long window;
    private EventListener listener = new EventListener();

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(800, 600, "OpenGL Window", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                double[] xpos = new double[1];
                double[] ypos = new double[1];
                glfwGetCursorPos(window, xpos, ypos);

                listener.handleMouseClick(
                    xpos[0],
                    600 - ypos[0]
                );
            }
        });
        glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
            if (action == GLFW_PRESS) {

                double[] xpos = new double[1];
                double[] ypos = new double[1];
                glfwGetCursorPos(window, xpos, ypos);

                double x = xpos[0];
                double y = 600 - ypos[0]; // กลับแกน Y

                if (button == GLFW_MOUSE_BUTTON_LEFT) {
                    listener.handleMouseClick(x, y);
                }

                if (button == GLFW_MOUSE_BUTTON_RIGHT) {
                    listener.removeLastLine(); // คลิกขวา
                }
            }
        });

    }

    private void loop() {
        GL.createCapabilities();

        listener.init();
        listener.reshape(0, 0, 800, 600);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            listener.display();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        listener.dispose();
    }

    public static void main(String[] args) {
        new App().run();
    }
}