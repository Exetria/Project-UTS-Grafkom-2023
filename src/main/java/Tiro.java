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

public class Tiro
{
    private final Window window = new Window(800, 800, "window");
    Camera camera = new Camera();
    Projection projection = new Projection(window.getWidth(), window.getHeight());

    ArrayList<Sphere> spheres = new ArrayList<>();

    public static void main(String[] args)
    {
        new Tiro().run();
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
        glEnable(GL_DEPTH_TEST);
        camera.setPosition(0, 0, 0.5f);
        camera.setRotation((float) Math.toRadians(0f), (float) Math.toRadians(0f));

        //NOSE (PARENT)
        {
            spheres.add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 0.1, 0.1, 0.1, 0f, 0, 0, 8
                    )
            );
            spheres.get(0).scaleObject(0.1f, 0.1f, 0.1f);
            spheres.get(0).scaleObject(0.9f, 1f, 1f);
            spheres.get(0).rotateObject(-4f, 1f, 0f, 0f);
        }


        //TABUNG BELAKANG NOSE
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 0.1, 0.1, 0.2, 0f, 0, 0, 3
                    )
            );
            spheres.get(0).getChildObjects().get(0).scaleObject(0.65f, 0.65f, 3f);
            spheres.get(0).getChildObjects().get(0).scaleObject(0.9f, 1f, 1f);
            spheres.get(0).getChildObjects().get(0).translateObject(0f, 0.0233f, 0.95f);
        }


        //COCKPIT
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.5f, 0.5f, 1.0f, 1.0f), 0.1, 0.1, 0.1, 0f, 0, 0, 4
                    )
            );
            spheres.get(0).getChildObjects().get(1).rotateObject(90f, 0f, 1f, 0f);
            spheres.get(0).getChildObjects().get(1).rotateObject(-15f, 1f, 0f, 0f);

            spheres.get(0).getChildObjects().get(1).scaleObject(0.75f, 0.77f, 1.5f);
            spheres.get(0).getChildObjects().get(1).translateObject(0f, 0.073f, 0.33f);
        }


        //INTAKE KIRI
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 0.1, 0.1, 0.5, 0f, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(2).translateObject(-0.11f, 0.02f, 0.75f);

        }


        //INTAKE KANAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 0.1, 0.1, 0.5, 0f, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(3).translateObject(0.11f, 0.02f, 0.75f);
        }


        //BODY TENGAH
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), 0.322, 0.1, 0.5, 0f, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(4).translateObject(0f, 0.02f, 1.2f);
        }


        //SAYAP KIRI
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 1.0f, 1.0f), 0.5, 0.01, 0.55, 0f, 0, 0, 10
                    )
            );
            spheres.get(0).getChildObjects().get(5).rotateObject(180f, 0f, 0f, 1f);
            spheres.get(0).getChildObjects().get(5).translateObject(-0.161f, 0.05f, 0.83f);
        }


        //SAYAP KANAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 1.0f, 1.0f), 0.5, 0.01, 0.55, 0f, 0, 0, 10
                    )
            );
            spheres.get(0).getChildObjects().get(6).translateObject(0.161f, 0.05f, 0.83f);
        }


        //INTAKE DEPAN KIRI
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 1.0f, 1.0f), 0.1, 0.1, 0.2, 0f, 0, 0, 11
                    )
            );
            spheres.get(0).getChildObjects().get(7).translateObject(-0.11f, 0.02f, 0.5f);
        }


        //INTAKE DEPAN KANAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 1.0f, 1.0f), 0.1, 0.1, 0.2, 0f, 0, 0, 11
                    )
            );
            spheres.get(0).getChildObjects().get(8).translateObject(0.11f, 0.02f, 0.5f);
        }


        //ENGINE KIRI
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.44f, 0.44f, 0.44f, 1.0f), 0.1, 0.1, 0.2, 0f, 0, 0, 8
                    )
            );
            spheres.get(0).getChildObjects().get(9).scaleObject(0.1f, 0.1f, 0.2f);
            spheres.get(0).getChildObjects().get(9).scaleObject(0.9f, 0.9f, 1f);
            spheres.get(0).getChildObjects().get(9).translateObject(-0.0585f, 0.027f, 0.8f);
        }


        //ENGINE KANAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.44f, 0.44f, 0.44f, 1.0f), 0.1, 0.1, 0.2, 0f, 0, 0, 8
                    )
            );
            spheres.get(0).getChildObjects().get(10).scaleObject(0.1f, 0.1f, 0.2f);
            spheres.get(0).getChildObjects().get(10).scaleObject(0.9f, 0.9f, 1f);
            spheres.get(0).getChildObjects().get(10).translateObject(0.0585f, 0.027f, 0.8f);
        }


        //PENUTUP ENGINE KIRI
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 0.01, 0.09, 0.017, 0f, 0, 0, 12
                    )
            );
            spheres.get(0).getChildObjects().get(11).rotateObject(90f, 1f, 0, 0);
            spheres.get(0).getChildObjects().get(11).translateObject(-0.15f, 0.053f, 1.49f);
        }


        //PENUTUP ENGINE KANAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 0.01, 0.09, 0.017, 0f, 0, 0, 12
                    )
            );
            spheres.get(0).getChildObjects().get(12).rotateObject(90f, 1f, 0, 0);
            spheres.get(0).getChildObjects().get(12).translateObject(0.15f, 0.053f, 1.49f);
        }


        //EKOR KIRI
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 0.2, 0.005, 0.2, 0f, 0, 0, 13
                    )
            );
            spheres.get(0).getChildObjects().get(13).translateObject(-0.152f, 0.07f, 1.45f);
        }


        //EKOR KANAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 0.2, 0.005, 0.2, 0f, 0, 0, 13
                    )
            );
            spheres.get(0).getChildObjects().get(14).translateObject(0.152f, 0.07f, 1.45f);
        }


        //SAYAP BELAKANG KIRI
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 0.23, 0.005, 0.23, 0f, 0, 0, 14
                    )
            );
            spheres.get(0).getChildObjects().get(15).rotateObject(180f, 0f, 0f, 1f);
            spheres.get(0).getChildObjects().get(15).translateObject(-0.1619f, 0.06f, 1.45f);
        }


        //SAYAP BELAKANG KANAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 0.23, 0.005, 0.23, 0f, 0, 0, 14
                    )
            );
            spheres.get(0).getChildObjects().get(16).translateObject(0.16f, 0.06f, 1.45f);
        }
    }

    public void input()
    {

//        if(window.isKeyPressed(GLFW_KEY_N))
//        {
//            Sphere i = ((Sphere) spheres.get(0).getChildObjects().get(15));
//            Sphere j = ((Sphere) spheres.get(0).getChildObjects().get(16));
//
//            System.out.println(i.getRotateX());
//            System.out.println(i.getRotateY());
//            System.out.println(i.getRotateZ());
//            System.out.println();
//            if(i.getRotateX() > -i.getRotationLimit() && i.getRotateX() < i.getRotationLimit())
//            {
//                i.rotateObjectOnPoint(1f, 1, 0,0, i.getCpx(), i.getCpy(), i.getCpz());
//                j.rotateObjectOnPoint(1f, 1, 0,0, j.getCpx(), j.getCpy(), j.getCpz());
//            }
//            else
//            {
//                i.rotateObjectOnPoint(-(float) (i.getRotateX()-i.getRotationLimit() + 1), 1, 0,0, i.getCpx(), i.getCpy(), i.getCpz());
//                j.rotateObjectOnPoint(-(float) (j.getRotateX()-j.getRotationLimit() + 1), 1, 0,0, j.getCpx(), j.getCpy(), j.getCpz());
//            }
//        }
//
//        if(window.isKeyPressed(GLFW_KEY_M))
//        {
//            Sphere i = ((Sphere) spheres.get(0).getChildObjects().get(15));
//            Sphere j = ((Sphere) spheres.get(0).getChildObjects().get(16));
//
//            System.out.println(i.getRotateX());
//            System.out.println(i.getRotateY());
//            System.out.println(i.getRotateZ());
//            System.out.println();
//            if(i.getRotateX() > -i.getRotationLimit() && i.getRotateX() < i.getRotationLimit())
//            {
//                i.rotateObjectOnPoint(-1f, 1, 0,0, i.getCpx(), i.getCpy(), i.getCpz());
//                j.rotateObjectOnPoint(-1f, 1, 0,0, j.getCpx(), j.getCpy(), j.getCpz());
//            }
//            else
//            {
//                i.rotateObjectOnPoint((float) (i.getRotateX()+i.getRotationLimit() + 1), 1, 0,0, i.getCpx(), i.getCpy(), i.getCpz());
//                j.rotateObjectOnPoint((float) (j.getRotateX()+j.getRotationLimit() + 1), 1, 0,0, j.getCpx(), j.getCpy(), j.getCpz());
//            }
//        }

        {
            if(window.isKeyPressed(GLFW_KEY_Q))
            {
                camera.moveForward(0.01f);
                for (Sphere i: spheres)
                {
//                    i.rotateObjectOnPoint(1f, 0, 0, 1, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_E))
            {
                camera.moveBackwards(0.01f);
                for (Sphere i: spheres)
                {
//                    i.rotateObjectOnPoint(-1f, 0, 0, 1, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_W))
            {
                camera.moveUp(0.01f);
                for (Sphere i: spheres)
                {
//                    i.rotateObjectOnPoint(1f, 1, 0, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_S))
            {
                camera.moveDown(0.01f);
                for (Sphere i: spheres)
                {
//                    i.rotateObjectOnPoint(-1f, 1, 0, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_A))
            {
                camera.moveLeft(0.01f);
                for (Sphere i: spheres)
                {
//                    i.rotateObjectOnPoint(1f, 0, 1, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_D))
            {
                camera.moveRight(0.01f);
                for (Sphere i: spheres)
                {
//                    i.rotateObjectOnPoint(-1f, 0, 1, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }
        }

        //================================================================================

        {
            if(window.isKeyPressed(GLFW_KEY_UP))
            {
                camera.addRotation(0.01f, 0);
            }

            if(window.isKeyPressed(GLFW_KEY_DOWN))
            {
                camera.addRotation(-0.01f, 0);
            }

            if(window.isKeyPressed(GLFW_KEY_LEFT))
            {
                camera.addRotation(0f, -0.01f);
            }

            if(window.isKeyPressed(GLFW_KEY_RIGHT))
            {
                camera.addRotation(0f, 0.01f);
            }
        }

        //================================================================================

        {
            if(window.isKeyPressed(GLFW_KEY_U))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0f, 0, 0.1f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_O))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0f, 0, -0.1f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_I))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0f, 0.1f, 0f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_K))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0f, -0.1f, 0f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_J))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(-0.1f, 0f, 0f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_L))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0.1f, 0f, 0f);
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
                objects.draw(camera, projection);
            }

            //Poll for window event
            glDisableVertexAttribArray(0);
            glfwPollEvents();

            input();
        }
    }
}