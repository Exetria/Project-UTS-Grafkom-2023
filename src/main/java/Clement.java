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

        //segitiga pake 1 warna
        /*{
            objects.add(new Objects(
                            Arrays.asList
                                    (
                                            new ShaderProgram.ShaderModuleData
                                                    ("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData
                                                    ("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                                    ),
                            new ArrayList<>
                                    (
                                            List.of
                                                    (
                                                            new Vector3f(0.0f, 0.5f, 0.0f),
                                                            new Vector3f(-0.5f, -0.5f, 0.0f),
                                                            new Vector3f(0.5f, -0.5f, 0.0f)
                                                    )
                                    ),
                            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f)    //color
                    )
            );
        }
        objects.get(0).translateObject(0.2f, 0.2f, 0.2f);
        objects.get(0).rotateObject((float) Math.toRadians(45), 0f, 0f, 1f);
        objects.get(0).scaleObject(0.5f, 0.5f, 0.5f);*/

       //segitiga pake banyak warna
        /*{
            multipleColorObjects.add(new Object2d(
                            Arrays.asList
                                    (
                                            new ShaderProgram.ShaderModuleData
                                                    ("resources/shaders/sceneWithVerticesColor.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData
                                                    ("resources/shaders/sceneWithVerticescolor.frag", GL_FRAGMENT_SHADER)
                                    ),
                            new ArrayList<>
                                    (
                                            List.of
                                                    (
                                                            new Vector3f(0.0f, 0.5f, 0.0f),
                                                            new Vector3f(-0.5f, -0.5f, 0.0f),
                                                            new Vector3f(0.5f, -0.5f, 0.0f)
                                                    )
                                    ),
                            new ArrayList<>                                         //color
                                    (
                                            List.of
                                                    (
                                                            new Vector3f(0.75f, 0.50f, 0.60f),
                                                            new Vector3f(0.50f, 1.0f, 0.80f),
                                                            new Vector3f(0.5f, 0.0f, 1.0f)
                                                    )
                                    )
                    )
            );
        }*/

        //bikin kotak pake 2 segitiga yang digambar urut per titik
        /*{
            objects.add(new Rectangle
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>
                                    (
                                            List.of
                                                    (
                                                            new Vector3f(0.0f, 0.0f, 0.0f),
                                                            new Vector3f(0.5f, 0.0f, 0.0f),
                                                            new Vector3f(0.0f, 0.5f, 0.0f),
                                                            new Vector3f(0.5f, 0.5f, 0.0f)
                                                    )
                                    ),
                            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),    //color,
                            Arrays.asList(0, 1, 2, 1, 2, 3)
                    )
            );
        }*/

        //objectsPointControl
        /*{
            objectsPointControl.add(new Objects(
                            Arrays.asList
                                    (
                                            new ShaderProgram.ShaderModuleData
                                                    ("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData
                                                    ("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                                    ),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f)    //color
                    )
            );
        }*/

        //bikin sphere
        spheres.add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1, 0, 0, 0, 1
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
        if(window.isKeyPressed(GLFW_KEY_Q))
        {
            for (Sphere i: spheres)
            {
                i.rotateObject(0.01f, 0, 0, 1);
                //, (float) i.getCpx(), (float) i.getCpy()
            }
        }

        if(window.isKeyPressed(GLFW_KEY_E))
        {
            for (Sphere i: spheres)
            {
                i.rotateObject(-0.01f, 0, 0, 1);
                //, (float) i.getCpx(), (float) i.getCpy()
            }
        }

        if(window.isKeyPressed(GLFW_KEY_A))
        {
            for (Sphere i: spheres)
            {
                i.rotateObject(0.01f, 0, 1, 0);
                //, (float) i.getCpx(), (float) i.getCpy()
            }
        }

        if(window.isKeyPressed(GLFW_KEY_D))
        {
            for (Sphere i: spheres)
            {
                i.rotateObject(-0.01f, 0, 1, 0);
                //, (float) i.getCpx(), (float) i.getCpy()
            }
        }

        if(window.isKeyPressed(GLFW_KEY_W))
        {
            for (Sphere i: spheres)
            {
                i.rotateObject(0.01f, 1, 0, 0);
                //, (float) i.getCpx(), (float) i.getCpy()
            }
        }

        if(window.isKeyPressed(GLFW_KEY_S))
        {
            for (Sphere i: spheres)
            {
                i.rotateObject(-0.01f, 1, 0, 0);
                //, (float) i.getCpx(), (float) i.getCpy()
            }
        }

        if(window.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
        {
            for (Sphere i: spheres)
            {
                camera.moveForward(0.01f);
                //, (float) i.getCpx(), (float) i.getCpy()
            }
        }

        if(window.isKeyPressed(GLFW_KEY_LEFT_CONTROL))
        {
            for (Sphere i: spheres)
            {
                camera.moveBackwards(0.01f);
                //, (float) i.getCpx(), (float) i.getCpy()
            }
        }


        if(window.getMouseInput().isLeftButtonPressed())
        {
            Vector2f pos = window.getMouseInput().getCurrentPos();

            pos.x = (pos.x - (window.getWidth())/2f) / (window.getWidth() / 2f);
            pos.y = (pos.y - (window.getHeight())/2f) / (window.getHeight() / 2f);

            //if buat cek kalo value lebih dari 1/-1 tidak usah ditampilkan
            if((!(pos.x > 1 || pos.x < -0.997) && !(pos.y > 1 || pos.y < -1)))
            {
                System.out.println("x: " + pos.x + "   y: " + pos.y);
//                objectsPointControl.get(0).addVertices(new Vector3f(pos.x, -pos.y, 0));
            }
        }
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
//            for (Objects objects : this.objects)
//            {
//                //1 warna
//                objects.draw();
//            }

            for (Sphere objects : this.spheres)
            {
                //gambar sekalian child
                objects.draw(true, camera, projection);
            }

//            for (Objects objects : multipleColorObjects)
//            {
//                //banyak warna
//                objects.drawWithVerticesColor();
//            }
//
//            for (Objects objects : objectsPointControl)
//            {
//                //banyak warna
//                objects.drawLine();
//            }

            //Poll for window event
            //
            glDisableVertexAttribArray(0);
            glfwPollEvents();

            input();
        }
    }
}