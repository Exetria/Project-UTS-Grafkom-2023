import Engine.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL20.*;

public class Clement
{
    private final Window window = new Window(800, 800, "window");
    Camera camera = new Camera();
    Projection projection = new Projection(window.getWidth(), window.getHeight());

    ArrayList<Objects> objects = new ArrayList<>();
    ArrayList<Sphere> spheres = new ArrayList<>();
    ArrayList<Objects> multipleColorObjects = new ArrayList<>();
    ArrayList<Objects> objectsPointControl =  new ArrayList<>();

    public static void main(String[] args)
    {
        new Clement().run();
    }

    public void run()
    {
        init();
        loop();
    }

    public void init()
    {
        window.init();
        GL.createCapabilities();
        camera.setPosition(0, 0,  0.5f);
        camera.setRotation((float) Math.toRadians(0f),  (float) Math.toRadians(0f));

        //bikin sphere
        spheres.add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1, 0, 0, 0, 3
                )
        );

//        spheres.get(0).getChildObjects().add(new Sphere
//                (
//                        Arrays.asList
//                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
//                        new ArrayList<>(),
//                        new Vector4f(0.0f, 0.0f, 1.0f, 1.0f),0.1, 0.1, 0.1, 0, 0, 0, 1
//                )
//        );

//        spheres.get(0).translateObject(-0.25f, -0.25f, 0);
//        spheres.get(0).getChildObjects().get(0).translateObject(0.5f, 0.5f, 0.5f);
    }

    public void input()
    {
        {
            if(window.isKeyPressed(GLFW_KEY_Q))
            {
                for (Sphere i: spheres)
                {
//                    i.rotateObject(0.01f, 0, 0, 1);
                    i.rotateObjectOnPoint(1f, 0, 0, 1, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_E))
            {
                for (Sphere i: spheres)
                {
//                    i.rotateObject(-0.01f, 0, 0, 1);
                    i.rotateObjectOnPoint(-1f, 0, 0, 1, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_W))
            {
                for (Sphere i: spheres)
                {
//                    i.rotateObject(0.01f, 1, 0, 0);
                    i.rotateObjectOnPoint(1f, 1, 0, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_S))
            {
                for (Sphere i: spheres)
                {
//                    i.rotateObject(-0.01f, 1, 0, 0);
                    i.rotateObjectOnPoint(-1f, 1, 0, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_A))
            {
                for (Sphere i: spheres)
                {
//                    i.rotateObject(0.01f, 0, 1, 0);
                    i.rotateObjectOnPoint(1f, 0, 1, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_D))
            {
                for (Sphere i: spheres)
                {
//                    i.rotateObject(-0.01f, 0, 1, 0);
                    i.rotateObjectOnPoint(-1f, 0, 1, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }
        }

        //================================================================================

        {
            if(window.isKeyPressed(GLFW_KEY_U))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0f, 0, 0.001f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_O))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0f, 0, -0.001f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_I))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0f, 0.001f, 0f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_K))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0f, -0.001f, 0f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_J))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(-0.001f, 0f, 0f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_L))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0.001f, 0f, 0f);
                }
            }
        }

        if(window.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
        {
            camera.moveForward(0.02f);
        }

        if(window.isKeyPressed(GLFW_KEY_LEFT_CONTROL))
        {
            camera.moveBackwards(0.02f);
        }

        /*
        if(window.getMouseInput().isLeftButtonPressed())
        {
            Vector2f pos = window.getMouseInput().getCurrentPos();

            pos.x = (pos.x - (window.getWidth())/2f) / (window.getWidth() / 2f);
            pos.y = (pos.y - (window.getHeight())/2f) / (window.getHeight() / 2f);

            //if buat cek kalo value lebih dari 1/-1 tidak usah ditampilkan
            if((!(pos.x > 1 || pos.x < -0.997) && !(pos.y > 1 || pos.y < -1)))
            {
                objectsPointControl.get(0).addVertices(new Vector3f(pos.x, -pos.y, 0));
            }
        }
        */
    }

    public void loop()
    {
        while (window.isOpen())
        {
            //Restore State
            window.update();
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL.createCapabilities();

            //Code
            for (Sphere objects : this.spheres)
            {
                //gambar sekalian child
                objects.draw(camera, projection, true);
            }

            //Poll for window event
            glDisableVertexAttribArray(0);
            glfwPollEvents();

            input();
        }
    }
}