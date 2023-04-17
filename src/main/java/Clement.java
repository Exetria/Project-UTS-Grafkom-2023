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

    Sphere turret;

    ArrayList<Vector3f> path;

    boolean turretLaunch = false;

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
        glEnable(GL_DEPTH_TEST);
        camera.setPosition(0, 0,  0.5f);
        camera.setRotation((float) Math.toRadians(0f),  (float) Math.toRadians(0f));

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
            spheres.get(0).scaleObject(4.25f,0.3f,2f);
        }

        //BAGIAN ATAS BELAKANG MOBIL
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(0).scaleObject(2.8f, 0.5f, 2f);
            spheres.get(0).getChildObjects().get(0).translateObject(0.07f, 0.04f, 0f);
        }

        //BAGIAN TRAPESIUM ATAS
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.898f, 0.717f, 0.384f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 41
                    )
            );
            spheres.get(0).getChildObjects().get(1).scaleObject(1.8f, 0.4f, 1.5f);
            spheres.get(0).getChildObjects().get(1).translateObject(0.05f, 0.086f, 0f);
        }

        //BAGIAN TRAPESIUM DEPAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 42
                    )
            );
            spheres.get(0).getChildObjects().get(2).scaleObject(1f, 0.5f, 2f);
            spheres.get(0).getChildObjects().get(2).translateObject(-0.12f, 0.041f, 0f);
        }

        //BAGIAN KOTAK BELAKANG
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.898f, 0.717f, 0.384f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(3).scaleObject(0.65f, 0.6f, 2f);
            spheres.get(0).getChildObjects().get(3).translateObject(0.178f, 0.096f, 0f);
        }

        //BAGIAN TOPI ATAS TRAPESIUM
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.898f, 0.717f, 0.384f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(4).scaleObject(1.9f, 0.05f, 1.5f);
            spheres.get(0).getChildObjects().get(4).translateObject(0.046f, 0.11f, 0f);
        }

        //BAGIAN ATAS TRAPESIUM BOLONG
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.44f, 0.31f, 0.22f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(5).scaleObject(0.9f, 0.01f, 1.2f);
            spheres.get(0).getChildObjects().get(5).translateObject(0.06f, 0.113f, 0f);
        }

        //Anthena
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.949f, 0.811f, 0.584f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(6).scaleObject(0.4f, 1.2f, 0.2f);
            spheres.get(0).getChildObjects().get(6).translateObject(0.18f, 0.186f, 0f);

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.03f, 0.474f, 0.411f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 7
                    )
            );
            spheres.get(0).getChildObjects().get(7).scaleObject(0.1f, 0.1f, 0.08f);
            spheres.get(0).getChildObjects().get(7).rotateObject(-105f,1.0f, 0.0f, 0.0f);
            spheres.get(0).getChildObjects().get(7).translateObject(0.18f, 0.246f, 0f);
        }

        //KACA SAMPING
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 44
                    )
            );
            spheres.get(0).getChildObjects().get(8).scaleObject(0.23f, 0.25f, 0.1f);
            spheres.get(0).getChildObjects().get(8).rotateObject(180f,0f,1f,0f);
            spheres.get(0).getChildObjects().get(8).rotateObject(-32f,1f,0f,0f);
            spheres.get(0).getChildObjects().get(8).translateObject(-0.007f, 0.0796f, 0.085f);

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 44
                    )
            );

            spheres.get(0).getChildObjects().get(9).scaleObject(0.23f, 0.25f, 0.1f);
            spheres.get(0).getChildObjects().get(9).rotateObject(180f,0f,1f,0f);
            spheres.get(0).getChildObjects().get(9).rotateObject(32f,1f,0f,0f);
            spheres.get(0).getChildObjects().get(9).translateObject(-0.007f, 0.085f, -0.095f);
        }

        //KACA DEPAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0,
                            0, 0, 0, 46
                    )
            );
            spheres.get(0).getChildObjects().get(10).scaleObject(1.15f, 0.32f, 0f);
            spheres.get(0).getChildObjects().get(10).rotateObject(90f,0f, 1f, 0f);
            spheres.get(0).getChildObjects().get(10).rotateObject(-32f,0f, 0f, 1f);
            spheres.get(0).getChildObjects().get(10).translateObject(-0.057f, 0.085f, -0.002f);
        }

        //PENYANGGA BAN KIRI DEPAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 43
                    )
            );
            spheres.get(0).getChildObjects().get(11).scaleObject(0.15f, 0.4f, 0.3f);
            spheres.get(0).getChildObjects().get(11).translateObject(-0.138f, 0.035f, 0.108f);
        }

        //BAN KIRI DEPAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.235f, 0.145f, 0.113f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0,3
                    )
            );
            spheres.get(0).getChildObjects().get(12).scaleObject(0.45f, 0.5f, 0.22f);
            spheres.get(0).getChildObjects().get(12).translateObject(-0.138f, -0.018f, 0.121f);
        }

        //PENYANGGA BAN KIRI BELAKANG
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 43
                    )
            );
            spheres.get(0).getChildObjects().get(13).scaleObject(0.15f, 0.4f, 0.3f);
            spheres.get(0).getChildObjects().get(13).translateObject(0.168f, 0.035f, 0.108f);
        }

        //BAN KIRI BELAKANG
        {
            spheres.get(0).getChildObjects().add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(0.235f, 0.145f, 0.113f, 1.0f),0.1, 0.1, 0.1,
                        0, 0, 0,3
                )
            );
            spheres.get(0).getChildObjects().get(14).scaleObject(0.45f, 0.5f, 0.22f);
            spheres.get(0).getChildObjects().get(14).translateObject(0.168f, -0.018f, 0.121f);
        }

        //PENYANGGA BAN KANAN DEPAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 43
                    )
            );
            spheres.get(0).getChildObjects().get(15).scaleObject(0.15f, 0.4f, 0.3f);
            spheres.get(0).getChildObjects().get(15).translateObject(-0.138f, 0.035f, -0.11f);
        }

        //BAN KANAN DEPAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.235f, 0.145f, 0.113f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0,3
                    )
            );
            spheres.get(0).getChildObjects().get(16).scaleObject(0.45f, 0.5f, 0.22f);
            spheres.get(0).getChildObjects().get(16).translateObject(-0.138f, -0.018f, -0.1f);
        }

        //PENYANGGA BAN KANAN BELAKANG
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.976f, 0.792f, 0.439f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 43
                    )
            );
            spheres.get(0).getChildObjects().get(17).scaleObject(0.15f, 0.4f, 0.3f);
            spheres.get(0).getChildObjects().get(17).translateObject(0.168f, 0.033f, -0.11f);
        }

        //BAN KANAN BELAKANG
        {
            spheres.get(0).getChildObjects().add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(0.235f, 0.145f, 0.113f, 1.0f),0.1, 0.1, 0.1,
                        0, 0, 0,3
                )
        );

            spheres.get(0).getChildObjects().get(18).scaleObject(0.45f, 0.5f, 0.22f);
            spheres.get(0).getChildObjects().get(18).translateObject(0.164f, -0.018f, -0.1f);

        }

        //BAGIAN SAMPING PIJAKAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.505f, 0.435f, 0.317f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0,2
                    )
            );
            spheres.get(0).getChildObjects().get(19).scaleObject(1f, 0.2f, 0.12f);
            spheres.get(0).getChildObjects().get(19).translateObject(-0.01f, -0.011f, -0.107f);

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.505f, 0.435f, 0.317f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0,2
                    )
            );
            spheres.get(0).getChildObjects().get(20).scaleObject(1f, 0.2f, 0.12f);
            spheres.get(0).getChildObjects().get(20).translateObject(-0.01f, -0.011f, 0.1f);
        }

        //BAWAH LINGKARAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.78f, 0.596f, 0.27f, 1.0f),0.1, 0.1, 0,
                            0, 0, 0, 1
                    )
            );
            spheres.get(0).getChildObjects().get(21).scaleObject(0.3f, 0.3f, 0f);
            spheres.get(0).getChildObjects().get(21).rotateObject(-90f,1f,0f,0f);
            spheres.get(0).getChildObjects().get(21).translateObject(0.06f, 0.115f, 0f);
        }

        //TEMPAT TURRET -> SEBAGAI ANAK DAN CUCUNYA DI SPHERE
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.78f, 0.596f, 0.29f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(22).scaleObject(0.4f, 0.4f, 0.7f);
            spheres.get(0).getChildObjects().get(22).translateObject(0.04f, 0.135f, 0f);

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.427f, 0.176f, 0.09f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 45
                    )
            );
            spheres.get(0).getChildObjects().get(23).scaleObject(0.6f, 0.15f, 0.2f);
            spheres.get(0).getChildObjects().get(23).translateObject(0.03f, 0.163f, 0f);
        }

        //LAMPU
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.62f, 0.59f, 0.52f, 1.0f),0.1, 0.1, 0,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(24).scaleObject(0.3f, 0.3f, 0f);
            spheres.get(0).getChildObjects().get(24).rotateObject(90f,0f,1f,0f);
            spheres.get(0).getChildObjects().get(24).rotateObject(-35f,0f,0f,1f);
            spheres.get(0).getChildObjects().get(24).translateObject(-0.189f, 0.046f, 0.068f);

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.62f, 0.59f, 0.52f, 1.0f),0.1, 0.1, 0.2,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(25).scaleObject(0.3f, 0.3f, 0f);
            spheres.get(0).getChildObjects().get(25).rotateObject(90f,0f,1f,0f);
            spheres.get(0).getChildObjects().get(25).rotateObject(-35f,0f,0f,1f);
            spheres.get(0).getChildObjects().get(25).translateObject(-0.189f, 0.046f, -0.068f);
        }

        //TOMBOL TURRET
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(26).scaleObject(0.05f, 0.05f, 0.05f);
            spheres.get(0).getChildObjects().get(26).translateObject(0.06f, 0.135f, -0.01f);

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(27).scaleObject(0.05f, 0.05f, 0.05f);
            spheres.get(0).getChildObjects().get(27).translateObject(0.06f, 0.135f, 0.01f);

        }

        //TRAPESIUM KAP MOBIL
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 41
                    )
            );
            spheres.get(0).getChildObjects().get(28).scaleObject(0.2f, 0.1f, 0.4f);
            spheres.get(0).getChildObjects().get(28).translateObject(-0.14f, 0.0715f, 0f);
        }

        //PINTU SAMPING ATAS KIRI
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.807f, 0.615f, 0.317f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 44
                    )
            );
            spheres.get(0).getChildObjects().get(29).scaleObject(0.45f, 0.4f, 0.1f);
            spheres.get(0).getChildObjects().get(29).rotateObject(180f,0f,1f,0f);
            spheres.get(0).getChildObjects().get(29).rotateObject(-32f,1f,0f,0f);
            spheres.get(0).getChildObjects().get(29).translateObject(0.004f, 0.079f, 0.085f);
        }

        //PINTU SAMPING BAWAH KIRI
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.807f, 0.615f, 0.317f, 1.0f),0.1, 0.1, 0,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(30).scaleObject(0.78f, 0.5f, 0f);
            spheres.get(0).getChildObjects().get(30).translateObject(-0.012f, 0.042f, 0.1f);
        }

        //GANGGANG PINTU KIRI
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(31).scaleObject(0.2f, 0.1f, 0f);
            spheres.get(0).getChildObjects().get(31).translateObject(0.015f, 0.048f, 0.101f);
        }

        //PINTU SAMPING ATAS KANAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(0.807f, 0.615f, 0.317f, 1.0f),0.1, 0.1, 0.1,
                        0, 0, 0, 44
                )
        );
            spheres.get(0).getChildObjects().get(32).scaleObject(0.45f, 0.4f, 0.1f);
            spheres.get(0).getChildObjects().get(32).rotateObject(180f,0f,1f,0f);
            spheres.get(0).getChildObjects().get(32).rotateObject(32f,1f,0f,0f);
            spheres.get(0).getChildObjects().get(32).translateObject(0.004f, 0.086f, -0.094f);
        }

        //PINTU SAMPING BAWAH KANAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.807f, 0.615f, 0.317f, 1.0f),0.1, 0.1, 0,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(33).scaleObject(0.78f, 0.5f, 0f);
            spheres.get(0).getChildObjects().get(33).translateObject(-0.012f, 0.042f, -0.1001f);
        }

        //GANGGANG PINTU KANAN
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0,
                            0, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(34).scaleObject(0.2f, 0.1f, 0f);
            spheres.get(0).getChildObjects().get(34).translateObject(0.015f, 0.048f, -0.101f);
        }

        //KNALPOT MOBIL
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 8
                    )
            );
            spheres.get(0).getChildObjects().get(35).scaleObject(0.03f, 0.02f, 0.01f);
            spheres.get(0).getChildObjects().get(35).rotateObject(90f,0f, 1f, 0f);
            spheres.get(0).getChildObjects().get(35).translateObject(0.189f, 0.025f, -0.078f);

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 8
                    )
            );
            spheres.get(0).getChildObjects().get(36).scaleObject(0.03f, 0.02f, 0.01f);
            spheres.get(0).getChildObjects().get(36).rotateObject(90f,0f, 1f, 0f);
            spheres.get(0).getChildObjects().get(36).translateObject(0.189f, 0.025f, 0.078f);
        }

        //BAN SEREP
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.235f, 0.145f, 0.113f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0, 3
                    )
            );
            spheres.get(0).getChildObjects().get(37).scaleObject(0.45f, 0.45f, 0.22f);
            spheres.get(0).getChildObjects().get(37).rotateObject(90f, 0f, 1f, 0f);
            spheres.get(0).getChildObjects().get(37).translateObject(0.23f, 0.07f, 0.0f);
        }

        //RODA DALAM
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0,2
                    )
            );
            spheres.get(0).getChildObjects().get(38).scaleObject(0.2f, 0.2f, 0.1f);
            spheres.get(0).getChildObjects().get(38).translateObject(-0.138f, -0.018f, -0.117f);

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0,2
                    )
            );
            spheres.get(0).getChildObjects().get(39).scaleObject(0.2f, 0.2f, 0.1f);
            spheres.get(0).getChildObjects().get(39).translateObject(0.164f, -0.018f, -0.117f);

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0,2
                    )
            );
            spheres.get(0).getChildObjects().get(40).scaleObject(0.2f, 0.2f, 0.1f);
            spheres.get(0).getChildObjects().get(40).translateObject(-0.138f, -0.018f, 0.126f);

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0,2
                    )
            );
            spheres.get(0).getChildObjects().get(41).scaleObject(0.2f, 0.2f, 0.1f);
            spheres.get(0).getChildObjects().get(41).translateObject(0.164f, -0.018f, 0.126f);

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.1,
                            0, 0, 0,2
                    )
            );
            spheres.get(0).getChildObjects().get(42).scaleObject(0.2f, 0.2f, 0.1f);
            spheres.get(0).getChildObjects().get(42).rotateObject(90f,0f,1f,0f);
            spheres.get(0).getChildObjects().get(42).translateObject(0.235f, 0.07f, 0.0f);
        }

        //BEIZER
        {

        }
    }

    public void input()
    {
        {
            if(window.isKeyPressed(GLFW_KEY_M))
            {
                Sphere a = ((Sphere)spheres.get(0).getChildObjects().get(7));
                a.rotateObjectOnPoint(
                        1f,0f, 1f, 0f, a.getCpx(), a.getCpy(), a.getCpz());
            }

            if(window.isKeyPressed(GLFW_KEY_N)) {
                Sphere a = ((Sphere)spheres.get(0).getChildObjects().get(23));

                if(a.getTotalRotateY() > -a.getRotationLimit() && a.getTotalRotateY() < a.getRotationLimit())
                {
                    a.rotateObjectOnPoint(-1f, 0f, 1f,0f, a.getCpx(), a.getCpy(), a.getCpz());
                }
                else
                {
                    a.rotateObjectOnPoint(2f, 0f, 1f,0f, a.getCpx(), a.getCpy(), a.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_B)) {
                Sphere a = ((Sphere)spheres.get(0).getChildObjects().get(23));

                if(a.getTotalRotateY() > -a.getRotationLimit() && a.getTotalRotateY() < a.getRotationLimit())
                {
                    a.rotateObjectOnPoint(1f, 0f, 1f,0f, a.getCpx(), a.getCpy(), a.getCpz());
                }
                else
                {
                    a.rotateObjectOnPoint(-2f, 0f, 1f,0f, a.getCpx(), a.getCpy(), a.getCpz());
                }
            }

//            if(window.isKeyPressed(GLFW_KEY_V)) {
//                if (turretLaunch) {
//                    turret.moveToNextPoint(path);
//                }
//                else {
//                    turret = ((Sphere) spheres.get(0).getChildObjects().get(43));
//                    path = turret.generateBezierPoints(turret.getCpx(), turret.getCpy(), turret.getCpz(),
//                            turret.getCpx(), turret.getCpy(),
//                            -5,5, -5, -5);
//                    turretLaunch = true;
//                }
//            }

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
//                camera.addRotation(0.01f, 0);
                for (Sphere i: spheres)
                {
//                    i.rotateObject(1f, 1, 0, 0);

                    i.rotateObjectOnPoint(1f, 1, 0, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_S))
            {
//                camera.addRotation(-0.01f, 0);
                for (Sphere i: spheres)
                {
//                    i.rotateObject(-1f, 1, 0, 0);
                    i.rotateObjectOnPoint(-1f, 1, 0, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_A))
            {
//                camera.addRotation(0, 0.01f);
                for (Sphere i: spheres)
                {
//                    i.rotateObject(1f, 0, 1, 0);
                    i.rotateObjectOnPoint(1f, 0, 1, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_D))
            {
//                camera.addRotation(0, -0.01f);
                for (Sphere i: spheres)
                {
//                    i.rotateObject(-1f, 0, 1, 0);
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
                Sphere a = ((Sphere)spheres.get(0).getChildObjects().get(38));
                Sphere b = ((Sphere)spheres.get(0).getChildObjects().get(39));
                Sphere c = ((Sphere)spheres.get(0).getChildObjects().get(40));
                Sphere d = ((Sphere)spheres.get(0).getChildObjects().get(41));

                a.rotateObjectOnPoint(2f, 0f, 0f,1f,
                        a.getCpx(), a.getCpy(), a.getCpz());
                b.rotateObjectOnPoint(2f, 0f, 0f,1f,
                        b.getCpx(), b.getCpy(), b.getCpz());
                c.rotateObjectOnPoint(2f, 0f, 0f,1f,
                        c.getCpx(), c.getCpy(), c.getCpz());
                d.rotateObjectOnPoint(2f, 0f, 0f,1f,
                        d.getCpx(), d.getCpy(), d.getCpz());


//                Sphere e = ((Sphere)spheres.get(0).getChildObjects().get(12));
//                Sphere f = ((Sphere)spheres.get(0).getChildObjects().get(14));
//                Sphere g = ((Sphere)spheres.get(0).getChildObjects().get(16));
//                Sphere h = ((Sphere)spheres.get(0).getChildObjects().get(18));
//
//                e.rotateObjectOnPoint(-0.01f, 0f, 0f,1f,
//                        e.getCpx(), e.getCpy(), e.getCpz());
//                f.rotateObjectOnPoint(-0.01f, 0f, 0f,1f,
//                        f.getCpx(), f.getCpy(), f.getCpz());
//                g.rotateObjectOnPoint(-0.01f, 0f, 0f,1f,
//                        g.getCpx(), g.getCpy(), g.getCpz());
//                h.rotateObjectOnPoint(-0.01f, 0f, 0f,1f,
//                        h.getCpx(), h.getCpy(), h.getCpz());


                for (Sphere i: spheres)
                {
                    i.translateObject(-0.001f, 0f, 0f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_L))
            {
                Sphere a = ((Sphere)spheres.get(0).getChildObjects().get(38));
                Sphere b = ((Sphere)spheres.get(0).getChildObjects().get(39));
                Sphere c = ((Sphere)spheres.get(0).getChildObjects().get(40));
                Sphere d = ((Sphere)spheres.get(0).getChildObjects().get(41));

                a.rotateObjectOnPoint(-2f, 0f, 0f,1f,
                        a.getCpx(), a.getCpy(), a.getCpz());
                b.rotateObjectOnPoint(-2f, 0f, 0f,1f,
                        b.getCpx(), b.getCpy(), b.getCpz());
                c.rotateObjectOnPoint(-2f, 0f, 0f,1f,
                        c.getCpx(), c.getCpy(), c.getCpz());
                d.rotateObjectOnPoint(-2f, 0f, 0f,1f,
                        d.getCpx(), d.getCpy(), d.getCpz());

//                Sphere e = ((Sphere)spheres.get(0).getChildObjects().get(12));
//                Sphere f = ((Sphere)spheres.get(0).getChildObjects().get(14));
//                Sphere g = ((Sphere)spheres.get(0).getChildObjects().get(16));
//                Sphere h = ((Sphere)spheres.get(0).getChildObjects().get(18));
//
//                e.rotateObjectOnPoint(0.01f, 0f, 0f,1f,
//                        e.getCpx(), e.getCpy(), e.getCpz());
//                f.rotateObjectOnPoint(0.01f, 0f, 0f,1f,
//                        f.getCpx(), f.getCpy(), f.getCpz());
//                g.rotateObjectOnPoint(0.01f, 0f, 0f,1f,
//                        g.getCpx(), g.getCpy(), g.getCpz());
//                h.rotateObjectOnPoint(0.01f, 0f, 0f,1f,
//                        h.getCpx(), h.getCpy(), h.getCpz());

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
                objects.draw(camera, projection);
            }

            //Poll for window event
            glDisableVertexAttribArray(0);
            glfwPollEvents();

            input();
        }
    }
}