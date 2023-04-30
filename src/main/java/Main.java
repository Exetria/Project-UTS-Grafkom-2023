import Engine.*;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL20.*;

public class Main
{
    private final Window window = new Window(1366, 768, "window");

    Random rn = new Random();
    Camera camera = new Camera();
    Projection projection = new Projection(window.getWidth(), window.getHeight());

    Sphere turret, leftMissile, rightMissile, temp;

    ArrayList<Sphere> spheres = new ArrayList<>();
    ArrayList<Sphere> environment = new ArrayList<>();
    ArrayList<Sphere> missileTrail = new ArrayList<>();

    ArrayList<Vector3f> path, leftPath, rightPath;

    float tempX, tempZ;
    int objectChoice, smokeCounter, gunCounter, tankCounter = 0;
    boolean turretLaunch, missileLaunch, leftMissileLaunch, rightMissileLaunch, rotateMode = false;

    public static void main(String[] args)
    {
        new Main().run();
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
        camera.setPosition(0f, 2.3f, -2.5f);
        camera.setRotation((float) Math.toRadians(0f), (float) Math.toRadians(180f));

        //VINCENTIUS I. TIRO C14210047
        {
            //NOSE (PARENT)
            {
                spheres.add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.784f, 0.831f, 0.831f, 1.0f), 0.1, 0.1, 0.1, 0f, 0, 0, 8
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
                                new Vector4f(0.62f, 0.68f, 0.67f, 1.0f), 0.1, 0.1, 0.2, 0f, 0, 0, 3
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
                                new Vector4f(0.372f, 0.46f, 0.5f, 1.0f), 0.1, 0.1, 0.5, 0f, 0, 0, 2
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
                                new Vector4f(0.372f, 0.46f, 0.5f, 1.0f), 0.1, 0.1, 0.5, 0f, 0, 0, 2
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
                                new Vector4f(0.372f, 0.46f, 0.5f, 1.0f), 0.322, 0.1, 0.5, 0f, 0, 0, 2
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
                                new Vector4f(0.839f, 0.227f, 0.274f, 1.0f), 0.5, 0.01, 0.55, 0f, 0, 0, 10
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
                                new Vector4f(0.62f, 0.68f, 0.67f, 1.0f), 0.5, 0.01, 0.55, 0f, 0, 0, 10
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
                                new Vector4f(0.584f, 0.631f, 0.631f, 1.0f), 0.1, 0.1, 0.2, 0f, 0, 0, 11
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
                                new Vector4f(0.584f, 0.631f, 0.631f, 1.0f), 0.1, 0.1, 0.2, 0f, 0, 0, 11
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
                                new Vector4f(0.48f, 0.549f, 0.584f, 1.0f), 0.1, 0.1, 0.2, 0f, 0, 0, 8
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
                                new Vector4f(0.48f, 0.549f, 0.584f, 1.0f), 0.1, 0.1, 0.2, 0f, 0, 0, 8
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
                                new Vector4f(0.62f, 0.68f, 0.67f, 1.0f), 0.01, 0.09, 0.017, 0f, 0, 0, 12
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
                                new Vector4f(0.62f, 0.68f, 0.67f, 1.0f), 0.01, 0.09, 0.017, 0f, 0, 0, 12
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
                                new Vector4f(0.466f, 0.07f, 0.11f, 1.0f), 0.2, 0.005, 0.2, 0f, 0, 0, 13
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
                                new Vector4f(0.466f, 0.07f, 0.11f, 1.0f), 0.2, 0.005, 0.2, 0f, 0, 0, 13
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
                                new Vector4f(0.48f, 0.549f, 0.584f, 1.0f), 0.23, 0.005, 0.23, 0f, 0, 0, 14
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
                                new Vector4f(0.48f, 0.549f, 0.584f, 1.0f), 0.23, 0.005, 0.23, 0f, 0, 0, 14
                        )
                );
                spheres.get(0).getChildObjects().get(16).translateObject(0.16f, 0.06f, 1.45f);
            }


            //PYLON KIRI
            {
                spheres.get(0).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.784f, 0.831f, 0.831f, 1.0f), 0.002, 0.04, 0.2, 0f, 0, 0, 2
                        )
                );
                spheres.get(0).getChildObjects().get(17).translateObject(-0.3f, 0.027f, 0.83f);


                spheres.get(0).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.784f, 0.831f, 0.831f, 1.0f), 0.003, 0.05, 0.2, 0f, 0, 0, 2
                        )
                );
                spheres.get(0).getChildObjects().get(18).rotateObject(90f, 0, 0, 1);
                spheres.get(0).getChildObjects().get(18).translateObject(-0.3f, 0.007f, 0.83f);
            }


            //PYLON KANAN
            {
                spheres.get(0).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.784f, 0.831f, 0.831f, 1.0f), 0.002, 0.04, 0.2, 0f, 0, 0, 2
                        )
                );
                spheres.get(0).getChildObjects().get(19).translateObject(0.3f, 0.027f, 0.83f);


                spheres.get(0).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.784f, 0.831f, 0.831f, 1.0f), 0.003, 0.05, 0.2, 0f, 0, 0, 2
                        )
                );
                spheres.get(0).getChildObjects().get(20).rotateObject(90f, 0, 0, 1);
                spheres.get(0).getChildObjects().get(20).translateObject(0.3f, 0.007f, 0.83f);
            }


            //MISIL KIRI
            {
                spheres.get(0).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 0.1, 0.1, 0.1, 0, 0, 0, 15
                        )
                );
                spheres.get(0).getChildObjects().get(21).scaleObject(0.1f, 0.1f, 0.1f);
                spheres.get(0).getChildObjects().get(21).rotateObject(45f, 0, 0, 1);
                spheres.get(0).getChildObjects().get(21).translateObject(-0.335f, 0.007f, 0.73f);
            }


            //MISIL KANAN
            {
                spheres.get(0).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 0.1, 0.1, 0.1, 0, 0, 0, 15
                        )
                );
                spheres.get(0).getChildObjects().get(22).scaleObject(0.1f, 0.1f, 0.1f);
                spheres.get(0).getChildObjects().get(22).rotateObject(45f, 0, 0, 1);
                spheres.get(0).getChildObjects().get(22).translateObject(0.335f, 0.007f, 0.73f);
            }

            //CANNON KANAN
            {
                spheres.get(0).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 0.005, 0.005, 0.1, 0, 0, 0, 17
                        )
                );
                spheres.get(0).getChildObjects().get(23).translateObject(0.154f, 0.054f, 0.41f);
            }
            }
        spheres.get(0).translateObject(0f, 4.5f, 10f);


        //FABIAN OKKY D. S. C14210196
        {
            //bikin badan
            {
                spheres.add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 1.0f, 0.0f, 1.0f), 0.1, 0.1, 0.1, 0f, 0, 0, 2
                        )
                );
                spheres.get(1).scaleObject(4f, 1f, 2f);

            }

            //bikin roda kanan bagian kanan
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 1.0f, 1.0f, 1.0f), 0.1, 0.1, 0.13, 0f, 0, 0, 21
                        )
                );
                spheres.get(1).getChildObjects().get(0).scaleObject(0.5f, 0.5f, 0.07f);
                spheres.get(1).getChildObjects().get(0).translateObject(0.06f, -0.05f, 0.135f);
            }

            //bikin roda kanan bagian kiri
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 1.0f, 1.0f, 1.0f), 0.1, 0.1, 0.13, 0f, 0, 0, 21
                        )
                );
                spheres.get(1).getChildObjects().get(1).scaleObject(0.5f, 0.5f, 0.07f);
                spheres.get(1).getChildObjects().get(1).translateObject(-0.06f, -0.05f, 0.135f);
            }

            //roda kecil kanan bagian depan
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.13, 0f, 0, 0, 21
                        )
                );
                spheres.get(1).getChildObjects().get(2).scaleObject(0.3f, 0.3f, 0.07f);
                spheres.get(1).getChildObjects().get(2).translateObject(-0.15f, -0.03f, 0.135f);
            }

            //roda kecil kanan bagian belakang
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.13, 0f, 0, 0, 21
                        )
                );
                spheres.get(1).getChildObjects().get(3).scaleObject(-0.3f, 0.3f, 0.07f);
                spheres.get(1).getChildObjects().get(3).translateObject(0.15f, -0.03f, 0.135f);
            }

            //rantai ban kanan
            {

                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.4, 0.085, 0.25, 0f, 0, 0, 20
                        )
                );
                spheres.get(1).getChildObjects().get(4).scaleObject(0.76f, 0.75f, 0.1f);
                spheres.get(1).getChildObjects().get(4).translateObject(0f, -0.032f, 0.114f);
            }

            //ban kiri besar depan
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 1.0f, 1.0f, 1.0f), 0.1, 0.1, 0.13, 0f, 0, 0, 21
                        )
                );
                spheres.get(1).getChildObjects().get(5).scaleObject(0.5f, 0.5f, 0.07f);
                spheres.get(1).getChildObjects().get(5).translateObject(0.06f, -0.05f, -0.132f);
            }
            //ban kiri besar belakang
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 1.0f, 1.0f, 1.0f), 0.1, 0.1, 0.13, 0f, 0, 0, 21
                        )
                );
                spheres.get(1).getChildObjects().get(6).scaleObject(0.5f, 0.5f, 0.07f);
                spheres.get(1).getChildObjects().get(6).translateObject(-0.06f, -0.05f, -0.132f);
            }
            //ban kecil kiri besar depan
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.13, 0f, 0, 0, 21
                        )
                );
                spheres.get(1).getChildObjects().get(7).scaleObject(-0.3f, 0.3f, 0.07f);
                spheres.get(1).getChildObjects().get(7).translateObject(-0.15f, -0.03f, -0.132f);
            }
            //ban kecil kiri besar belakang
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.13, 0f, 0, 0, 21
                        )
                );
                spheres.get(1).getChildObjects().get(8).scaleObject(-0.3f, 0.3f, 0.07f);
                spheres.get(1).getChildObjects().get(8).translateObject(0.15f, -0.03f, -0.132f);
            }

            //rantai ban kiri
            {

                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.4, 0.085, 0.25, 0f, 0, 0, 20
                        )
                );
                spheres.get(1).getChildObjects().get(9).scaleObject(0.76f, 0.75f, 0.1f);
                spheres.get(1).getChildObjects().get(9).translateObject(0f, -0.032f, -0.118f);
            }

            //kepala
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.937f, 0.741f, 0.662f, 1.0f),0.1, 0.1, 0.1, 0f, 0, 0, 2
                        )
                );
                spheres.get(1).getChildObjects().get(10).scaleObject(1.5f, 1f, 1f);
                spheres.get(1).getChildObjects().get(10).translateObject(0f, 0.1f, 0f);
            }

            //corong
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 0.0f, 1.0f, 1.0f),0.01, 0.01, 0.35, 0f, 0, 0, 22
                        )
                );
                spheres.get(1).getChildObjects().get(11).scaleObject(1.f, 1f, 1f);
                spheres.get(1).getChildObjects().get(11).translateObject(-0.075f, 0.1f, 0f);
            }

            //cover ban depan 1
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1, 0f, 0, 0, 3
                        )
                );
                spheres.get(1).getChildObjects().get(12).scaleObject(0.3f, 0.3f, 0.25f);
                spheres.get(1).getChildObjects().get(12).translateObject(-0.151f, -0.03f, 0.127f);
            }

            //cover ban depan 2
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1, 0f, 0, 0, 3
                        )
                );
                spheres.get(1).getChildObjects().get(13).scaleObject(0.3f, 0.3f, 0.25f);
                spheres.get(1).getChildObjects().get(13).translateObject(-0.151f, -0.03f, -0.105f);
            }

            //cover ban belakang 1
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1, 0f, 0, 0, 3
                        )
                );
                spheres.get(1).getChildObjects().get(14).scaleObject(0.3f, 0.3f, 0.25f);
                spheres.get(1).getChildObjects().get(14).translateObject(0.151f, -0.03f, 0.127f);
            }

            //cover ban belakang 2
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1, 0f, 0, 0, 3
                        )
                );
                spheres.get(1).getChildObjects().get(15).scaleObject(0.3f, 0.3f, 0.25f);
                spheres.get(1).getChildObjects().get(15).translateObject(0.151f, -0.03f, -0.105f);
            }

            //bola meriam
            {
                spheres.get(1).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 0.0f, 1.0f, 1.0f),0.01, 0.01, 0.01, 0f, 0, 0, 1
                        )
                );
                spheres.get(1).getChildObjects().get(16).scaleObject(0.1f, 0.1f, 0.1f);
                spheres.get(1).getChildObjects().get(16).translateObject(-0.4f, 0.1f, 0);
            }
        }
        spheres.get(1).translateObject(0f, 0.6f, 0f);


        //CLEMENT GUNADI C14210183
        {
            //BAGIAN BAWAH MOBIL
            {
                spheres.add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.78f, 0.596f, 0.29f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).scaleObject(4.25f,0.3f,2f);
            }

            //BAGIAN ATAS BELAKANG MOBIL
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(0).scaleObject(2.8f, 0.5f, 2f);
                spheres.get(2).getChildObjects().get(0).translateObject(0.07f, 0.04f, 0f);
            }

            //BAGIAN TRAPESIUM ATAS
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.898f, 0.717f, 0.384f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 41
                        )
                );
                spheres.get(2).getChildObjects().get(1).scaleObject(1.8f, 0.4f, 1.5f);
                spheres.get(2).getChildObjects().get(1).translateObject(0.05f, 0.086f, 0f);
            }

            //BAGIAN TRAPESIUM DEPAN
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 42
                        )
                );
                spheres.get(2).getChildObjects().get(2).scaleObject(1f, 0.5f, 2f);
                spheres.get(2).getChildObjects().get(2).translateObject(-0.12f, 0.041f, 0f);
            }

            //BAGIAN KOTAK BELAKANG
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.898f, 0.717f, 0.384f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(3).scaleObject(0.65f, 0.6f, 2f);
                spheres.get(2).getChildObjects().get(3).translateObject(0.178f, 0.096f, 0f);
            }

            //BAGIAN TOPI ATAS TRAPESIUM
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.898f, 0.717f, 0.384f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(4).scaleObject(1.9f, 0.05f, 1.5f);
                spheres.get(2).getChildObjects().get(4).translateObject(0.046f, 0.11f, 0f);
            }

            //BAGIAN ATAS TRAPESIUM BOLONG
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.44f, 0.31f, 0.22f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(5).scaleObject(0.9f, 0.01f, 1.2f);
                spheres.get(2).getChildObjects().get(5).translateObject(0.06f, 0.113f, 0f);
            }

            //Anthena
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.949f, 0.811f, 0.584f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(6).scaleObject(0.4f, 1.2f, 0.2f);
                spheres.get(2).getChildObjects().get(6).translateObject(0.18f, 0.186f, 0f);

                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.03f, 0.474f, 0.411f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 7
                        )
                );
                spheres.get(2).getChildObjects().get(7).scaleObject(0.1f, 0.1f, 0.08f);
                spheres.get(2).getChildObjects().get(7).rotateObject(-105f,1.0f, 0.0f, 0.0f);
                spheres.get(2).getChildObjects().get(7).translateObject(0.18f, 0.246f, 0f);
            }

            //KACA SAMPING
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 44
                        )
                );
                spheres.get(2).getChildObjects().get(8).scaleObject(0.23f, 0.25f, 0.1f);
                spheres.get(2).getChildObjects().get(8).rotateObject(180f,0f,1f,0f);
                spheres.get(2).getChildObjects().get(8).rotateObject(-32f,1f,0f,0f);
                spheres.get(2).getChildObjects().get(8).translateObject(-0.007f, 0.0796f, 0.085f);

                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 44
                        )
                );

                spheres.get(2).getChildObjects().get(9).scaleObject(0.23f, 0.25f, 0.1f);
                spheres.get(2).getChildObjects().get(9).rotateObject(180f,0f,1f,0f);
                spheres.get(2).getChildObjects().get(9).rotateObject(32f,1f,0f,0f);
                spheres.get(2).getChildObjects().get(9).translateObject(-0.007f, 0.085f, -0.095f);
            }

            //KACA DEPAN
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0,
                                0, 0, 0, 46
                        )
                );
                spheres.get(2).getChildObjects().get(10).scaleObject(1.15f, 0.32f, 0f);
                spheres.get(2).getChildObjects().get(10).rotateObject(90f,0f, 1f, 0f);
                spheres.get(2).getChildObjects().get(10).rotateObject(-32f,0f, 0f, 1f);
                spheres.get(2).getChildObjects().get(10).translateObject(-0.057f, 0.085f, -0.002f);
            }

            //PENYANGGA BAN KIRI DEPAN
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 43
                        )
                );
                spheres.get(2).getChildObjects().get(11).scaleObject(0.15f, 0.4f, 0.3f);
                spheres.get(2).getChildObjects().get(11).translateObject(-0.138f, 0.035f, 0.108f);
            }

            //BAN KIRI DEPAN
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.235f, 0.145f, 0.113f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0,3
                        )
                );
                spheres.get(2).getChildObjects().get(12).scaleObject(0.45f, 0.5f, 0.22f);
                spheres.get(2).getChildObjects().get(12).translateObject(-0.138f, -0.018f, 0.121f);
            }

            //PENYANGGA BAN KIRI BELAKANG
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 43
                        )
                );
                spheres.get(2).getChildObjects().get(13).scaleObject(0.15f, 0.4f, 0.3f);
                spheres.get(2).getChildObjects().get(13).translateObject(0.168f, 0.035f, 0.108f);
            }

            //BAN KIRI BELAKANG
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.235f, 0.145f, 0.113f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0,3
                        )
                );
                spheres.get(2).getChildObjects().get(14).scaleObject(0.45f, 0.5f, 0.22f);
                spheres.get(2).getChildObjects().get(14).translateObject(0.168f, -0.018f, 0.121f);
            }

            //PENYANGGA BAN KANAN DEPAN
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 43
                        )
                );
                spheres.get(2).getChildObjects().get(15).scaleObject(0.15f, 0.4f, 0.3f);
                spheres.get(2).getChildObjects().get(15).translateObject(-0.138f, 0.035f, -0.11f);
            }

            //BAN KANAN DEPAN
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.235f, 0.145f, 0.113f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0,3
                        )
                );
                spheres.get(2).getChildObjects().get(16).scaleObject(0.45f, 0.5f, 0.22f);
                spheres.get(2).getChildObjects().get(16).translateObject(-0.138f, -0.018f, -0.1f);
            }

            //PENYANGGA BAN KANAN BELAKANG
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 43
                        )
                );
                spheres.get(2).getChildObjects().get(17).scaleObject(0.15f, 0.4f, 0.3f);
                spheres.get(2).getChildObjects().get(17).translateObject(0.168f, 0.033f, -0.11f);
            }

            //BAN KANAN BELAKANG
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.235f, 0.145f, 0.113f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0,3
                        )
                );

                spheres.get(2).getChildObjects().get(18).scaleObject(0.45f, 0.5f, 0.22f);
                spheres.get(2).getChildObjects().get(18).translateObject(0.164f, -0.018f, -0.1f);

            }

            //BAGIAN SAMPING PIJAKAN
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.505f, 0.435f, 0.317f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0,2
                        )
                );
                spheres.get(2).getChildObjects().get(19).scaleObject(1f, 0.2f, 0.12f);
                spheres.get(2).getChildObjects().get(19).translateObject(-0.01f, -0.011f, -0.107f);

                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.505f, 0.435f, 0.317f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0,2
                        )
                );
                spheres.get(2).getChildObjects().get(20).scaleObject(1f, 0.2f, 0.12f);
                spheres.get(2).getChildObjects().get(20).translateObject(-0.01f, -0.011f, 0.1f);
            }

            //BAWAH LINGKARAN
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.78f, 0.596f, 0.27f, 1.0f),0.1, 0.1, 0,
                                0, 0, 0, 1
                        )
                );
                spheres.get(2).getChildObjects().get(21).scaleObject(0.3f, 0.3f, 0f);
                spheres.get(2).getChildObjects().get(21).rotateObject(-90f,1f,0f,0f);
                spheres.get(2).getChildObjects().get(21).translateObject(0.06f, 0.115f, 0f);
            }

            //TEMPAT TURRET -> SEBAGAI ANAK DAN CUCUNYA DI SPHERE
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.78f, 0.596f, 0.29f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(22).scaleObject(0.4f, 0.4f, 0.7f);
                spheres.get(2).getChildObjects().get(22).translateObject(0.04f, 0.135f, 0f);

                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.427f, 0.176f, 0.09f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 45
                        )
                );
                spheres.get(2).getChildObjects().get(23).scaleObject(0.6f, 0.15f, 0.2f);
                spheres.get(2).getChildObjects().get(23).translateObject(0.03f, 0.163f, 0f);
            }

            //LAMPU
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.62f, 0.59f, 0.52f, 1.0f),0.1, 0.1, 0,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(24).scaleObject(0.3f, 0.3f, 0f);
                spheres.get(2).getChildObjects().get(24).rotateObject(90f,0f,1f,0f);
                spheres.get(2).getChildObjects().get(24).rotateObject(-35f,0f,0f,1f);
                spheres.get(2).getChildObjects().get(24).translateObject(-0.189f, 0.046f, 0.068f);

                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.62f, 0.59f, 0.52f, 1.0f),0.1, 0.1, 0.2,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(25).scaleObject(0.3f, 0.3f, 0f);
                spheres.get(2).getChildObjects().get(25).rotateObject(90f,0f,1f,0f);
                spheres.get(2).getChildObjects().get(25).rotateObject(-35f,0f,0f,1f);
                spheres.get(2).getChildObjects().get(25).translateObject(-0.189f, 0.046f, -0.068f);
            }

            //TOMBOL TURRET
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(26).scaleObject(0.05f, 0.05f, 0.05f);
                spheres.get(2).getChildObjects().get(26).translateObject(0.06f, 0.135f, -0.01f);

                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(27).scaleObject(0.05f, 0.05f, 0.05f);
                spheres.get(2).getChildObjects().get(27).translateObject(0.06f, 0.135f, 0.01f);

            }

            //TRAPESIUM KAP MOBIL
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 41
                        )
                );
                spheres.get(2).getChildObjects().get(28).scaleObject(0.2f, 0.1f, 0.4f);
                spheres.get(2).getChildObjects().get(28).translateObject(-0.14f, 0.0715f, 0f);
            }

            //PINTU SAMPING ATAS KIRI
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.807f, 0.615f, 0.317f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 44
                        )
                );
                spheres.get(2).getChildObjects().get(29).scaleObject(0.45f, 0.4f, 0.1f);
                spheres.get(2).getChildObjects().get(29).rotateObject(180f,0f,1f,0f);
                spheres.get(2).getChildObjects().get(29).rotateObject(-32f,1f,0f,0f);
                spheres.get(2).getChildObjects().get(29).translateObject(0.004f, 0.079f, 0.085f);
            }

            //PINTU SAMPING BAWAH KIRI
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.807f, 0.615f, 0.317f, 1.0f),0.1, 0.1, 0,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(30).scaleObject(0.78f, 0.5f, 0f);
                spheres.get(2).getChildObjects().get(30).translateObject(-0.012f, 0.042f, 0.1f);
            }

            //GANGGANG PINTU KIRI
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(31).scaleObject(0.2f, 0.1f, 0f);
                spheres.get(2).getChildObjects().get(31).translateObject(0.015f, 0.048f, 0.101f);
            }

            //PINTU SAMPING ATAS KANAN
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.807f, 0.615f, 0.317f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 44
                        )
                );
                spheres.get(2).getChildObjects().get(32).scaleObject(0.45f, 0.4f, 0.1f);
                spheres.get(2).getChildObjects().get(32).rotateObject(180f,0f,1f,0f);
                spheres.get(2).getChildObjects().get(32).rotateObject(32f,1f,0f,0f);
                spheres.get(2).getChildObjects().get(32).translateObject(0.004f, 0.086f, -0.094f);
            }

            //PINTU SAMPING BAWAH KANAN
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.807f, 0.615f, 0.317f, 1.0f),0.1, 0.1, 0,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(33).scaleObject(0.78f, 0.5f, 0f);
                spheres.get(2).getChildObjects().get(33).translateObject(-0.012f, 0.042f, -0.1001f);
            }

            //GANGGANG PINTU KANAN
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0,
                                0, 0, 0, 2
                        )
                );
                spheres.get(2).getChildObjects().get(34).scaleObject(0.2f, 0.1f, 0f);
                spheres.get(2).getChildObjects().get(34).translateObject(0.015f, 0.048f, -0.101f);
            }

            //KNALPOT MOBIL
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 8
                        )
                );
                spheres.get(2).getChildObjects().get(35).scaleObject(0.03f, 0.02f, 0.01f);
                spheres.get(2).getChildObjects().get(35).rotateObject(90f,0f, 1f, 0f);
                spheres.get(2).getChildObjects().get(35).translateObject(0.189f, 0.025f, -0.078f);

                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 8
                        )
                );
                spheres.get(2).getChildObjects().get(36).scaleObject(0.03f, 0.02f, 0.01f);
                spheres.get(2).getChildObjects().get(36).rotateObject(90f,0f, 1f, 0f);
                spheres.get(2).getChildObjects().get(36).translateObject(0.189f, 0.025f, 0.078f);
            }

            //BAN SEREP
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.235f, 0.145f, 0.113f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 3
                        )
                );
                spheres.get(2).getChildObjects().get(37).scaleObject(0.45f, 0.45f, 0.22f);
                spheres.get(2).getChildObjects().get(37).rotateObject(90f, 0f, 1f, 0f);
                spheres.get(2).getChildObjects().get(37).translateObject(0.23f, 0.07f, 0.0f);
            }

            //RODA DALAM
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0,2
                        )
                );
                spheres.get(2).getChildObjects().get(38).scaleObject(0.2f, 0.2f, 0.1f);
                spheres.get(2).getChildObjects().get(38).translateObject(-0.138f, -0.018f, -0.117f);

                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0,2
                        )
                );
                spheres.get(2).getChildObjects().get(39).scaleObject(0.2f, 0.2f, 0.1f);
                spheres.get(2).getChildObjects().get(39).translateObject(0.164f, -0.018f, -0.117f);

                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0,2
                        )
                );
                spheres.get(2).getChildObjects().get(40).scaleObject(0.2f, 0.2f, 0.1f);
                spheres.get(2).getChildObjects().get(40).translateObject(-0.138f, -0.018f, 0.126f);

                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0,2
                        )
                );
                spheres.get(2).getChildObjects().get(41).scaleObject(0.2f, 0.2f, 0.1f);
                spheres.get(2).getChildObjects().get(41).translateObject(0.164f, -0.018f, 0.126f);

                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0,2
                        )
                );
                spheres.get(2).getChildObjects().get(42).scaleObject(0.2f, 0.2f, 0.1f);
                spheres.get(2).getChildObjects().get(42).rotateObject(90f,0f,1f,0f);
                spheres.get(2).getChildObjects().get(42).translateObject(0.235f, 0.07f, 0.0f);
            }

            //PELURU
            {
                spheres.get(2).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                                0, 0, 0, 4
                        )
                );
                spheres.get(2).getChildObjects().get(43).scaleObject(0.05f, 0.05f, 0.05f);
                spheres.get(2).getChildObjects().get(43).translateObject(0f, 0.163f, 0f);
            }
        }
        spheres.get(2).translateObject(-2f, 0.5651f, 1f);


        //ENVIRONMENT
        {
            //TANAH
            {
                environment.add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.7f, 1f, 0.3f, 1.0f), 40, 1, 40, 0f, 0, 0, 2
                        )
                );
            }


            //HANGAR KIRI
            /*{
                environment.add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0f, 0f, 1f, 1.0f), 0.02, 1, 2, 0f, 0, 0, 16
                        )
                );
                environment.get(1).rotateObject(30f, 0, 1 , 0);
                environment.get(1).translateObject(3f, 1, 2.5f);
            }*/


            //HANGAR KANAN
            /*{
                environment.add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0f, 0f, 1f, 1.0f), 0.02, 1, 2, 0f, 0, 0, 16
                        )
                );
                environment.get(2).rotateObject(-30f, 0, 1 , 0);
                environment.get(2).translateObject(-3f, 1, 2.5f);
            }*/

            //MATAHARI
            {
                environment.add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(0.94f, 1f, 0.26f, 1.0f), 3, 3, 3, 0f, 0, 0, 1
                        )
                );
                environment.get(1).translateObject(50, 50, 50);
            }

            //WARNA 1 HIJAU         0.26f, 0.47f, 0.13f
            //WARNA 2 AGAK KUNING   0.78f, 0.87f, 0.55f
            //LOOP UNTUK RANDOM PEMBUATAN TREE SEBANYAK I KALI
            for (int i = 0; i < 150; i++)
            {
                tempX = rn.nextFloat(-7, 7);
                tempZ = rn.nextFloat(-7, 7);
                if(i % 2 == 1)
                {
                    environment.add(new Sphere
                            (
                                    Arrays.asList
                                            (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                    new ArrayList<>(),
                                    new Vector4f(0.26f, 0.47f, 0.13f, 1.0f), 1, 1, 1, 0f, 0, 0, 18
                            )
                    );
                }
                else
                {
                    environment.add(new Sphere
                            (
                                    Arrays.asList
                                            (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                    new ArrayList<>(),
                                    new Vector4f(0.78f, 0.87f, 0.55f, 1.0f), 1, 1, 1, 0f, 0, 0, 18
                            )
                    );
                }
                environment.get(i+2).translateObject(tempX, 0.64f, tempZ);
                environment.get(i+2).scaleObject(2.5f, 2.5f, 2.5f);
            }
        }
    }

    public void input()
    {
        //ANIMASI
        {
            //VINCENT
            if(objectChoice == 0)
            {
                {
                    //scale matahari
                    if(window.isKeyPressed(GLFW_KEY_V))
                        environment.get(1).scaleObject(1.001f, 1.001f, 1.001f);

                    //luncurkan misil kiri
                    {
                        if(window.isKeyPressed(GLFW_KEY_V))
                        {
                            //if buat misilnya tidak jalan otomatis, kalo ini nyala, moveToNextPoint di if yg bawah harus dimatikan
//                    if(leftMissileLaunch)
//                    {
//                        leftMissileLaunch = leftMissile.moveToNextPoint(leftPath);
//                    }
                            if(!leftMissileLaunch)
                            {
                                leftMissile = ((Sphere) spheres.get(0).getChildObjects().get(21));
                                leftPath = leftMissile.generateBezierPoints(leftMissile.getCpx(), leftMissile.getCpy(), leftMissile.getCpz(),
                                        leftMissile.getCpx(), leftMissile.getCpy(), leftMissile.getCpz()-2,
                                        spheres.get(2).getCpx(), spheres.get(2).getCpy(), spheres.get(2).getCpz());
                                leftMissileLaunch = true;
                            }
                        }
                        if(leftMissileLaunch)
                        {
                            //add smoke kalo masih ada titik di path
                            if(leftPath.size() > 0)
                            {
                                missileTrail.add(new Sphere
                                        (
                                                Arrays.asList
                                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                                new ArrayList<>(),
                                                new Vector4f(0.35f, 0.35f, 0.35f, 1.0f), 0.03, 0.03, 0.03, 0, 0, 0, 1
                                        )
                                );
                                missileTrail.get(missileTrail.size()-1).translateObject(leftPath.get(0).x, leftPath.get(0).y, leftPath.get(0).z + 0.2f);
                            }

                            leftMissileLaunch = leftMissile.moveToNextPoint(leftPath);
                        }
                    }

                    //luncurkan misil kanan
                    {
                        if(window.isKeyPressed(GLFW_KEY_B))
                        {
                            //if buat misilnya tidak ajlan otomatis, kalo ini nyala, moveToNextPoint di if yg bawah harus dimatikan
//                    if(rightMissileLaunch)
//                    {
//                        rightMissileLaunch = rightMissile.moveToNextPoint(rightPath);
//                    }
                            if(!rightMissileLaunch)
                            {
                                rightMissile = ((Sphere) spheres.get(0).getChildObjects().get(22));
                                rightPath = rightMissile.generateBezierPoints(rightMissile.getCpx(), rightMissile.getCpy(), rightMissile.getCpz(),
                                        rightMissile.getCpx(), rightMissile.getCpy(), rightMissile.getCpz()-5,
                                        spheres.get(1).getCpx(), spheres.get(1).getCpy(), spheres.get(1).getCpz());
                                rightMissileLaunch = true;
                            }
                        }
                        if(rightMissileLaunch)
                        {
                            //add smoke kalo masih ada titik di path
                            if(rightPath.size() > 0)
                            {
                                missileTrail.add(new Sphere
                                        (
                                                Arrays.asList
                                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                                new ArrayList<>(),
                                                new Vector4f(0.35f, 0.35f, 0.35f, 1.0f), 0.03, 0.03, 0.03, 0, 0, 0, 1
                                        )
                                );
                                missileTrail.get(missileTrail.size()-1).translateObject(rightPath.get(0).x, rightPath.get(0).y, rightPath.get(0).z + 0.2f);
                            }

                            rightMissileLaunch = rightMissile.moveToNextPoint(rightPath);
                        }
                    }

                    //PELURU
                    {
                        //bakal nembak peluru kalo counter sudah 8 supaya tidak terlalu otomatis gun nya
                        if(window.isKeyPressed(GLFW_KEY_C) && gunCounter == 8)
                        {
                            gunCounter = 0;
                            spheres.get(0).getChildObjects().get(23).getChildObjects().add(new Sphere
                                    (
                                            Arrays.asList
                                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                            new ArrayList<>(),
                                            new Vector4f(1.0f, 0, 0, 1.0f), 0.005, 0.005, 0.01, 0, 0, 0, 1
                                    )
                            );

                            temp = ((Sphere)spheres.get(0).getChildObjects().get(23).getChildObjects().get(spheres.get(0).getChildObjects().get(23).getChildObjects().size()-1));
                            temp.translateObject(((Sphere)spheres.get(0).getChildObjects().get(23)).getCpx(), ((Sphere)spheres.get(0).getChildObjects().get(23)).getCpy(), ((Sphere)spheres.get(0).getChildObjects().get(23)).getCpz()- 1);
                            temp.generateBezierPoints(((Sphere)spheres.get(0).getChildObjects().get(23)).getCpx(), ((Sphere)spheres.get(0).getChildObjects().get(23)).getCpy(), ((Sphere)spheres.get(0).getChildObjects().get(23)).getCpz(),
                                    ((Sphere)spheres.get(0).getChildObjects().get(23)).getCpx(), ((Sphere)spheres.get(0).getChildObjects().get(23)).getCpy(), ((Sphere)spheres.get(0).getChildObjects().get(23)).getCpz() - 2,
                                    ((Sphere)spheres.get(0).getChildObjects().get(23)).getCpx(), ((Sphere)spheres.get(0).getChildObjects().get(23)).getCpy(), ((Sphere)spheres.get(0).getChildObjects().get(23)).getCpz() - 10);
                        }
                        else if(window.isKeyPressed(GLFW_KEY_C))
                        {
                            gunCounter++;
                        }
                        //gerakkan tiap peluru yg sudah ada
                        for (Objects i : spheres.get(0).getChildObjects().get(23).getChildObjects())
                        {
                            ((Sphere)i).moveToNextPoint(((Sphere) i).getPath());
                        }
                    }

                    //hapus smoke yg sudah lama (pelan-pelan hilang)
                    if(missileTrail.size() > 0)
                    {
                        if(smokeCounter > 0)
                        {
                            smokeCounter = 0;
                            missileTrail.remove(0);
                        }
                        else
                        {
                            smokeCounter++;
                        }
                    }

                    //sayap belakang keatas
                    if(window.isKeyPressed(GLFW_KEY_N))
                    {
                        Sphere i = ((Sphere) spheres.get(0).getChildObjects().get(15));
                        Sphere j = ((Sphere) spheres.get(0).getChildObjects().get(16));

                        if(i.getTotalRotateX() > -i.getRotationLimit() && i.getTotalRotateX() < i.getRotationLimit())
                        {
                            i.rotateObjectOnPoint(1f, 1, 0,0, i.getCpx(), i.getCpy(), i.getCpz());
                            j.rotateObjectOnPoint(1f, 1, 0,0, j.getCpx(), j.getCpy(), j.getCpz());
                        }
                        else
                        {
                            i.rotateObjectOnPoint(-(float) (i.getTotalRotateX()-i.getRotationLimit() + 1), 1, 0,0, i.getCpx(), i.getCpy(), i.getCpz());
                            j.rotateObjectOnPoint(-(float) (j.getTotalRotateX()-j.getRotationLimit() + 1), 1, 0,0, j.getCpx(), j.getCpy(), j.getCpz());
                        }
                    }

                    //sayap belakang ke bawah
                    if(window.isKeyPressed(GLFW_KEY_M))
                    {
                        Sphere i = ((Sphere) spheres.get(0).getChildObjects().get(15));
                        Sphere j = ((Sphere) spheres.get(0).getChildObjects().get(16));

                        if(i.getTotalRotateX() > -i.getRotationLimit() && i.getTotalRotateX() < i.getRotationLimit())
                        {
                            i.rotateObjectOnPoint(-1f, 1, 0,0, i.getCpx(), i.getCpy(), i.getCpz());
                            j.rotateObjectOnPoint(-1f, 1, 0,0, j.getCpx(), j.getCpy(), j.getCpz());
                        }
                        else
                        {
                            i.rotateObjectOnPoint((float) (i.getTotalRotateX()+i.getRotationLimit() + 1), 1, 0,0, i.getCpx(), i.getCpy(), i.getCpz());
                            j.rotateObjectOnPoint((float) (j.getTotalRotateX()+j.getRotationLimit() + 1), 1, 0,0, j.getCpx(), j.getCpy(), j.getCpz());
                        }
                    }
                }
            }

            //OKKY
            if(objectChoice == 1)
            {
                if(window.isKeyPressed(GLFW_KEY_SPACE))
                {
                    ((Sphere)spheres.get(1).getChildObjects().get(10)).rotateObjectOnPoint(-1,0,1,0,spheres.get(1).getChildObjects().get(10).getCenterPoint().x, spheres.get(1).getChildObjects().get(10).getCenterPoint().y, spheres.get(1).getChildObjects().get(10).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(11)).rotateObjectOnPoint(-1,0,1,0,spheres.get(1).getChildObjects().get(10).getCenterPoint().x, spheres.get(1).getChildObjects().get(10).getCenterPoint().y, spheres.get(1).getChildObjects().get(10).getCenterPoint().z);
                    if(!missileLaunch)
                    {
                        ((Sphere)spheres.get(1).getChildObjects().get(16)).rotateObjectOnPoint(-1,0,1,0,spheres.get(1).getChildObjects().get(10).getCenterPoint().x, spheres.get(1).getChildObjects().get(10).getCenterPoint().y, spheres.get(1).getChildObjects().get(10).getCenterPoint().z);
                    }
                }

                if (window.isKeyPressed(GLFW_KEY_C) && tankCounter == 20)
                {
                    System.out.println("fire");
                    tankCounter = 0;
                    spheres.get(1).getChildObjects().get(16).getChildObjects().add(new Sphere
                            (
                                    Arrays.asList
                                            (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                    new ArrayList<>(),
                                    new Vector4f(1.0f, 0, 0, 1.0f), 0.005, 0.005, 0.01, 0, 0, 0, 1
                            )
                    );


                    temp = ((Sphere) spheres.get(1).getChildObjects().get(16).getChildObjects().get(spheres.get(1).getChildObjects().get(16).getChildObjects().size() - 1));
                    temp.translateObject(((Sphere) spheres.get(1).getChildObjects().get(16)).getCpx(), ((Sphere) spheres.get(1).getChildObjects().get(16)).getCpy(), ((Sphere) spheres.get(1).getChildObjects().get(16)).getCpz());
                    float x, y, z, x1, x2, z1, z2;
                    x = ((Sphere) spheres.get(1).getChildObjects().get(16)).getCpx();
                    y = 0.7f;
                    z = ((Sphere) spheres.get(1).getChildObjects().get(16)).getCpz();

                    //temp.translateObject(((Sphere)spheres.get(1).getChildObjects().get(16)).getCpx(), ((Sphere)spheres.get(1).getChildObjects().get(16)).getCpy()-0f, ((Sphere)spheres.get(1).getChildObjects().get(16)).getCpz());

                    if (x >= spheres.get(1).getCpx())
                    {
                        x1 = x + 2;
                        x2 = x + 5;
                    }
                    else
                    {
                        x1 = x - 2;
                        x2 = x - 5;
                    }
                    if (z >= spheres.get(1).getCpz())
                    {
                        z1 = z * (float) Math.sqrt((3 * 3) - (x1 * x1));
                    }
                    else
                    {
                        z1 = z+(float) Math.sqrt((3 * 3) - (x1 * x1));
                    }
                    z1 += 1.9f;
                    z2 = z1;

                    temp.generateBezierPoints(x, y, z,
                            x1, y, z1,
                            x2, -1, z2);
                }
                else if (window.isKeyPressed(GLFW_KEY_C)) {
                    tankCounter++;
                }

                for (Objects i : spheres.get(1).getChildObjects().get(16).getChildObjects()) {
                    ((Sphere) i).moveToNextPoint(((Sphere) i).getPath());
                }
            }

            //CLEMENT
            if(objectChoice == 2)
            {
                if(window.isKeyPressed(GLFW_KEY_SPACE))
                {
                    Sphere a = ((Sphere)spheres.get(2).getChildObjects().get(7));
                    a.rotateObjectOnPoint(
                            1f,0f, 1f, 0f, a.getCpx(), a.getCpy(), a.getCpz());
                }

                if(window.isKeyPressed(GLFW_KEY_M)) {
                    Sphere a = ((Sphere)spheres.get(2).getChildObjects().get(23));

                    if(a.getTotalRotateY() > -a.getRotationLimit() && a.getTotalRotateY() < a.getRotationLimit())
                    {
                        a.rotateObjectOnPoint(-1f, 0f, 1f,0f, a.getCpx(), a.getCpy(), a.getCpz());
                    }
                    else
                    {
                        a.rotateObjectOnPoint(2f, 0f, 1f,0f, a.getCpx(), a.getCpy(), a.getCpz());
                    }
                }

                if(window.isKeyPressed(GLFW_KEY_N)) {
                    Sphere a = ((Sphere)spheres.get(2).getChildObjects().get(23));

                    if(a.getTotalRotateY() > -a.getRotationLimit() && a.getTotalRotateY() < a.getRotationLimit())
                    {
                        a.rotateObjectOnPoint(1f, 0f, 1f,0f, a.getCpx(), a.getCpy(), a.getCpz());
                    }
                    else
                    {
                        a.rotateObjectOnPoint(-2f, 0f, 1f,0f, a.getCpx(), a.getCpy(), a.getCpz());
                    }
                }

                if(window.isKeyPressed(GLFW_KEY_V)) {
                    if (turretLaunch) {
                        turret.moveToNextPoint(path);
                    }
                    else {
                        turret = ((Sphere) spheres.get(2).getChildObjects().get(43));
                        path = turret.generateBezierPoints(turret.getCpx(), turret.getCpy(), turret.getCpz(),
                                turret.getCpx()-2, turret.getCpy()-0.3f, turret.getCpz(),
                                turret.getCpx()-4, turret.getCpy()-0.5f, turret.getCpz());
                        turretLaunch = true;
                    }
                }
            }
        }

        //================================================================================

        //WASDQE BUAT ROTATE ATAU TRANSLATE CAMERA
        {
            if(window.isKeyPressed(GLFW_KEY_Q))
            {
                if(rotateMode)
                {
                    spheres.get(objectChoice).rotateObjectOnPoint(1f, 0, 0, 1, spheres.get(objectChoice).getCpx(), spheres.get(objectChoice).getCpy(), spheres.get(objectChoice).getCpz());
                }
                else
                {
                    camera.moveForward(0.03f);
                }

            }

            if(window.isKeyPressed(GLFW_KEY_E))
            {

                if(rotateMode)
                {
                    spheres.get(objectChoice).rotateObjectOnPoint(-1f, 0, 0, 1, spheres.get(objectChoice).getCpx(), spheres.get(objectChoice).getCpy(), spheres.get(objectChoice).getCpz());
                }
                else
                {
                    camera.moveBackwards(0.03f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_W))
            {

                if(rotateMode)
                {
                    spheres.get(objectChoice).rotateObjectOnPoint(1f, 1, 0, 0, spheres.get(objectChoice).getCpx(), spheres.get(objectChoice).getCpy(), spheres.get(objectChoice).getCpz());
                }
                else
                {
                    camera.moveUp(0.03f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_S))
            {
                if(rotateMode)
                {
                    spheres.get(objectChoice).rotateObjectOnPoint(-1f, 1, 0, 0, spheres.get(objectChoice).getCpx(), spheres.get(objectChoice).getCpy(), spheres.get(objectChoice).getCpz());
                }
                else
                {
                    camera.moveDown(0.03f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_A))
            {
                if(rotateMode)
                {
                    spheres.get(objectChoice).rotateObjectOnPoint(1f, 0, 1, 0, spheres.get(objectChoice).getCpx(), spheres.get(objectChoice).getCpy(), spheres.get(objectChoice).getCpz());
                }
                else
                {
                    camera.moveLeft(0.03f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_D))
            {
                if(rotateMode)
                {
                    spheres.get(objectChoice).rotateObjectOnPoint(-1f, 0, 1, 0, spheres.get(objectChoice).getCpx(), spheres.get(objectChoice).getCpy(), spheres.get(objectChoice).getCpz());
                }
                else
                {
                    camera.moveRight(0.03f);
                }
            }
        }

        //================================================================================

        //ARROWS BUAT ROTATE CAMERA
        {
            if(window.isKeyPressed(GLFW_KEY_UP))
            {
                camera.addRotation(((float) Math.toRadians(-1)), 0);
            }

            if(window.isKeyPressed(GLFW_KEY_DOWN))
            {
                camera.addRotation(((float) Math.toRadians(1)), 0);
            }

            if(window.isKeyPressed(GLFW_KEY_LEFT))
            {
                camera.addRotation(0f, ((float) Math.toRadians(-1)));
            }

            if(window.isKeyPressed(GLFW_KEY_RIGHT))
            {
                camera.addRotation(0f, ((float) Math.toRadians(1)));
            }
        }

        //================================================================================

        //IJKLUO BUAT TRANSLATE OBJECT
        {
            if(window.isKeyPressed(GLFW_KEY_U))
            {
                spheres.get(objectChoice).translateObject(0f, 0, 0.1f);
            }

            if(window.isKeyPressed(GLFW_KEY_O))
            {
                spheres.get(objectChoice).translateObject(0f, 0, -0.1f);
            }

            if(window.isKeyPressed(GLFW_KEY_I))
            {
                spheres.get(objectChoice).translateObject(0f, 0.01f, 0f);
            }

            if(window.isKeyPressed(GLFW_KEY_K))
            {
                spheres.get(objectChoice).translateObject(0f, -0.01f, 0f);
            }

            if(window.isKeyPressed(GLFW_KEY_J))
            {
                spheres.get(objectChoice).translateObject(-0.01f, 0f, 0f);
                if(objectChoice == 1)
                {
                    ((Sphere)spheres.get(1).getChildObjects().get(0)).rotateObjectOnPoint(1,0,0,1,spheres.get(1).getChildObjects().get(0).getCenterPoint().x, spheres.get(1).getChildObjects().get(0).getCenterPoint().y, spheres.get(1).getChildObjects().get(0).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(1)).rotateObjectOnPoint(1,0,0,1,spheres.get(1).getChildObjects().get(1).getCenterPoint().x, spheres.get(1).getChildObjects().get(1).getCenterPoint().y, spheres.get(1).getChildObjects().get(1).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(2)).rotateObjectOnPoint(1,0,0,1,spheres.get(1).getChildObjects().get(2).getCenterPoint().x, spheres.get(1).getChildObjects().get(2).getCenterPoint().y, spheres.get(1).getChildObjects().get(2).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(3)).rotateObjectOnPoint(1,0,0,1,spheres.get(1).getChildObjects().get(3).getCenterPoint().x, spheres.get(1).getChildObjects().get(3).getCenterPoint().y, spheres.get(1).getChildObjects().get(3).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(5)).rotateObjectOnPoint(1,0,0,1,spheres.get(1).getChildObjects().get(5).getCenterPoint().x, spheres.get(1).getChildObjects().get(5).getCenterPoint().y, spheres.get(1).getChildObjects().get(5).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(6)).rotateObjectOnPoint(1,0,0,1,spheres.get(1).getChildObjects().get(6).getCenterPoint().x, spheres.get(1).getChildObjects().get(6).getCenterPoint().y, spheres.get(1).getChildObjects().get(6).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(7)).rotateObjectOnPoint(1,0,0,1,spheres.get(1).getChildObjects().get(7).getCenterPoint().x, spheres.get(1).getChildObjects().get(7).getCenterPoint().y, spheres.get(1).getChildObjects().get(7).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(8)).rotateObjectOnPoint(1,0,0,1,spheres.get(1).getChildObjects().get(8).getCenterPoint().x, spheres.get(1).getChildObjects().get(8).getCenterPoint().y, spheres.get(1).getChildObjects().get(8).getCenterPoint().z);
                }
                else if(objectChoice == 2)
                {
                    Sphere a = ((Sphere)spheres.get(2).getChildObjects().get(38));
                    Sphere b = ((Sphere)spheres.get(2).getChildObjects().get(39));
                    Sphere c = ((Sphere)spheres.get(2).getChildObjects().get(40));
                    Sphere d = ((Sphere)spheres.get(2).getChildObjects().get(41));

                    a.rotateObjectOnPoint(2f, 0f, 0f,1f,
                            a.getCpx(), a.getCpy(), a.getCpz());
                    b.rotateObjectOnPoint(2f, 0f, 0f,1f,
                            b.getCpx(), b.getCpy(), b.getCpz());
                    c.rotateObjectOnPoint(2f, 0f, 0f,1f,
                            c.getCpx(), c.getCpy(), c.getCpz());
                    d.rotateObjectOnPoint(2f, 0f, 0f,1f,
                            d.getCpx(), d.getCpy(), d.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_L))
            {
                spheres.get(objectChoice).translateObject(0.01f, 0f, 0f);
                if(objectChoice == 1)
                {
                    ((Sphere)spheres.get(1).getChildObjects().get(0)).rotateObjectOnPoint(-1,0,0,1,spheres.get(1).getChildObjects().get(0).getCenterPoint().x, spheres.get(1).getChildObjects().get(0).getCenterPoint().y, spheres.get(1).getChildObjects().get(0).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(1)).rotateObjectOnPoint(-1,0,0,1,spheres.get(1).getChildObjects().get(1).getCenterPoint().x, spheres.get(1).getChildObjects().get(1).getCenterPoint().y, spheres.get(1).getChildObjects().get(1).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(2)).rotateObjectOnPoint(-1,0,0,1,spheres.get(1).getChildObjects().get(2).getCenterPoint().x, spheres.get(1).getChildObjects().get(2).getCenterPoint().y, spheres.get(1).getChildObjects().get(2).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(3)).rotateObjectOnPoint(-1,0,0,1,spheres.get(1).getChildObjects().get(3).getCenterPoint().x, spheres.get(1).getChildObjects().get(3).getCenterPoint().y, spheres.get(1).getChildObjects().get(3).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(5)).rotateObjectOnPoint(-1,0,0,1,spheres.get(1).getChildObjects().get(5).getCenterPoint().x, spheres.get(1).getChildObjects().get(5).getCenterPoint().y, spheres.get(1).getChildObjects().get(5).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(6)).rotateObjectOnPoint(-1,0,0,1,spheres.get(1).getChildObjects().get(6).getCenterPoint().x, spheres.get(1).getChildObjects().get(6).getCenterPoint().y, spheres.get(1).getChildObjects().get(6).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(7)).rotateObjectOnPoint(-1,0,0,1,spheres.get(1).getChildObjects().get(7).getCenterPoint().x, spheres.get(1).getChildObjects().get(7).getCenterPoint().y, spheres.get(1).getChildObjects().get(7).getCenterPoint().z);
                    ((Sphere)spheres.get(1).getChildObjects().get(8)).rotateObjectOnPoint(-1,0,0,1,spheres.get(1).getChildObjects().get(8).getCenterPoint().x, spheres.get(1).getChildObjects().get(8).getCenterPoint().y, spheres.get(1).getChildObjects().get(8).getCenterPoint().z);
                }
                else if(objectChoice == 2)
                {
                    Sphere a = ((Sphere)spheres.get(2).getChildObjects().get(38));
                    Sphere b = ((Sphere)spheres.get(2).getChildObjects().get(39));
                    Sphere c = ((Sphere)spheres.get(2).getChildObjects().get(40));
                    Sphere d = ((Sphere)spheres.get(2).getChildObjects().get(41));

                    a.rotateObjectOnPoint(-2f, 0f, 0f,1f,
                            a.getCpx(), a.getCpy(), a.getCpz());
                    b.rotateObjectOnPoint(-2f, 0f, 0f,1f,
                            b.getCpx(), b.getCpy(), b.getCpz());
                    c.rotateObjectOnPoint(-2f, 0f, 0f,1f,
                            c.getCpx(), c.getCpy(), c.getCpz());
                    d.rotateObjectOnPoint(-2f, 0f, 0f,1f,
                            d.getCpx(), d.getCpy(), d.getCpz());
                }
            }
        }

        //================================================================================

        //EXTRAS
        {
            //ZOOM IN
            if(window.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
            {
                camera.moveForward(0.02f);
            }

            //ZOOM OUT
            if(window.isKeyPressed(GLFW_KEY_LEFT_CONTROL))
            {
                camera.moveBackwards(0.02f);
            }

            //GANTI KE MODE ROTATE OBJECT
            if(window.isKeyPressed(GLFW_KEY_R))
            {
                System.out.println("Rotate Mode");
                rotateMode = true;
            }

            //GANTI KE MODE TRANSLATE CAMERA
            if(window.isKeyPressed(GLFW_KEY_T))
            {
                System.out.println("Camera Mode");
                rotateMode = false;
            }

            //PILIH PESAWAT
            if(window.isKeyPressed(GLFW_KEY_0))
            {
                objectChoice = 0;
            }

            //PILIH TANK
            if(window.isKeyPressed(GLFW_KEY_1))
            {
                objectChoice = 1;
            }

            //PILIH MOBIL
            if(window.isKeyPressed(GLFW_KEY_2))
            {
                objectChoice = 2;
            }
        }
    }

    public void loop()
    {
        while (window.isOpen())
        {
            //Restore State
            window.update();
            glClearColor(0.65f, 0.79f, 0.93f, 0.0f);
            GL.createCapabilities();

            //Code
            for (Sphere objects : this.spheres)
            {
                //gambar sekalian child
                //tambahkan "true" untuk gambar pakai LINE_LOOP
                //objects.draw(camera, projection, true);
                objects.draw(camera, projection);
            }

            for (Sphere objects : this.environment)
            {
                objects.draw(camera, projection);
            }

            for (Sphere objects : this.missileTrail)
            {
                objects.draw(camera, projection);
            }

            //Poll for window event
            glDisableVertexAttribArray(0);
            glfwPollEvents();

            input();
        }
    }
}