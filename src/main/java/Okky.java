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

public class Okky
{
    private final Window window = new Window(1000, 1000, "window");
    Camera camera = new Camera();
    Projection projection = new Projection(window.getWidth(), window.getHeight());

    ArrayList<Objects> objects = new ArrayList<>();
    ArrayList<Sphere> spheres = new ArrayList<>();
    ArrayList<Objects> multipleColorObjects = new ArrayList<>();
    ArrayList<Objects> objectsPointControl =  new ArrayList<>();
    ArrayList<Vector3f> jalur;
    Sphere missile, temp;
    int smokeCounter, gunCounter = 0;

    boolean missileLaunch, rotateMode = false;
    ArrayList<Objects> titikBerzier = new ArrayList<>();

    public static void main(String[] args)
    {
        new Okky().run();
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

//
        //bezier
//        objectsPointControl.add(new Objects(Arrays.asList(
//                new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
//                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
//        ), new ArrayList<>(
////                List.of(
////                        new Vector3f(-1.0f, 0.0f, 0.0f),
////                        new Vector3f(0.0f,1.0f,0.0f),
////                        new Vector3f(1.0f, 0.0f, 0.0f)
////                )
//        ),
//                new Vector4f(0.0f, 1.0f, 0.0f, 1.0f) // ini untuk warna garisnya
//        ));
//
//        titikBerzier.add(new Objects(Arrays.asList(
//                new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
//                new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
//        ), new ArrayList<>(),
//                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f) // ini untuk warna garisnya
//        ));



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
            spheres.get(0).scaleObject(4f, 1f, 2f);

        }

        //bikin roda kanan bagian kanan
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f), 0.1, 0.1, 0.13, 0f, 0, 0, 21
                    )
            );
            spheres.get(0).getChildObjects().get(0).scaleObject(0.5f, 0.5f, 0.07f);
            spheres.get(0).getChildObjects().get(0).translateObject(0.06f, -0.05f, 0.135f);
        }

        //bikin roda kanan bagian kiri
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f), 0.1, 0.1, 0.13, 0f, 0, 0, 21
                    )
            );
            spheres.get(0).getChildObjects().get(1).scaleObject(0.5f, 0.5f, 0.07f);
            spheres.get(0).getChildObjects().get(1).translateObject(-0.06f, -0.05f, 0.135f);
        }

        //roda kecil kanan bagian depan
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.13, 0f, 0, 0, 21
                    )
            );
            spheres.get(0).getChildObjects().get(2).scaleObject(0.3f, 0.3f, 0.07f);
            spheres.get(0).getChildObjects().get(2).translateObject(-0.15f, -0.03f, 0.135f);
        }

        //roda kecil kanan bagian belakang
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.13, 0f, 0, 0, 21
                    )
            );
            spheres.get(0).getChildObjects().get(3).scaleObject(-0.3f, 0.3f, 0.07f);
            spheres.get(0).getChildObjects().get(3).translateObject(0.15f, -0.03f, 0.135f);
        }

        //rantai ban kanan
        {

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.4, 0.085, 0.25, 0f, 0, 0, 20
                    )
            );
            spheres.get(0).getChildObjects().get(4).scaleObject(0.76f, 0.75f, 0.1f);
            spheres.get(0).getChildObjects().get(4).translateObject(0f, -0.032f, 0.114f);
        }

        //ban kiri besar depan
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f), 0.1, 0.1, 0.13, 0f, 0, 0, 21
                    )
            );
            spheres.get(0).getChildObjects().get(5).scaleObject(0.5f, 0.5f, 0.07f);
            spheres.get(0).getChildObjects().get(5).translateObject(0.06f, -0.05f, -0.132f);
        }
        //ban kiri besar belakang
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f), 0.1, 0.1, 0.13, 0f, 0, 0, 21
                    )
            );
            spheres.get(0).getChildObjects().get(6).scaleObject(0.5f, 0.5f, 0.07f);
            spheres.get(0).getChildObjects().get(6).translateObject(-0.06f, -0.05f, -0.132f);
        }
        //ban kecil kiri besar depan
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.13, 0f, 0, 0, 21
                    )
            );
            spheres.get(0).getChildObjects().get(7).scaleObject(-0.3f, 0.3f, 0.07f);
            spheres.get(0).getChildObjects().get(7).translateObject(-0.15f, -0.03f, -0.132f);
        }
        //ban kecil kiri besar belakang
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 1.0f, 1.0f, 1.0f),0.1, 0.1, 0.13, 0f, 0, 0, 21
                    )
            );
            spheres.get(0).getChildObjects().get(8).scaleObject(-0.3f, 0.3f, 0.07f);
            spheres.get(0).getChildObjects().get(8).translateObject(0.15f, -0.03f, -0.132f);
        }

        //rantai ban kiri
        {

            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.4, 0.085, 0.25, 0f, 0, 0, 20
                    )
            );
            spheres.get(0).getChildObjects().get(9).scaleObject(0.76f, 0.75f, 0.1f);
            spheres.get(0).getChildObjects().get(9).translateObject(0f, -0.032f, -0.118f);
        }

        //kepala
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.5f, 0.5f, 0.5f, 1.0f),0.1, 0.1, 0.1, 0f, 0, 0, 2
                    )
            );
            spheres.get(0).getChildObjects().get(10).scaleObject(1.5f, 1f, 1f);
            spheres.get(0).getChildObjects().get(10).translateObject(0f, 0.1f, 0f);
        }

        //corong
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 1.0f, 1.0f),0.01, 0.01, 0.35, 0f, 0, 0, 22
                    )
            );
            spheres.get(0).getChildObjects().get(11).scaleObject(1.f, 1f, 1f);
            spheres.get(0).getChildObjects().get(11).translateObject(-0.075f, 0.1f, 0f);
        }

        //cover ban depan 1
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1, 0f, 0, 0, 3
                    )
            );
            spheres.get(0).getChildObjects().get(12).scaleObject(0.3f, 0.3f, 0.25f);
            spheres.get(0).getChildObjects().get(12).translateObject(-0.151f, -0.03f, 0.127f);
        }

        //cover ban depan 2
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1, 0f, 0, 0, 3
                    )
            );
            spheres.get(0).getChildObjects().get(13).scaleObject(0.3f, 0.3f, 0.25f);
            spheres.get(0).getChildObjects().get(13).translateObject(-0.151f, -0.03f, -0.105f);
        }

        //cover ban belakang 1
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1, 0f, 0, 0, 3
                    )
            );
            spheres.get(0).getChildObjects().get(14).scaleObject(0.3f, 0.3f, 0.25f);
            spheres.get(0).getChildObjects().get(14).translateObject(0.151f, -0.03f, 0.127f);
        }

        //cover ban belakang 2
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(1.0f, 0.0f, 0.0f, 1.0f),0.1, 0.1, 0.1, 0f, 0, 0, 3
                    )
            );
            spheres.get(0).getChildObjects().get(15).scaleObject(0.3f, 0.3f, 0.25f);
            spheres.get(0).getChildObjects().get(15).translateObject(0.151f, -0.03f, -0.105f);
        }

        //bola meriam
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.0f, 0.0f, 1.0f, 1.0f),0.01, 0.01, 0.01, 0f, 0, 0, 1
                    )
            );
            //spheres.get(0).getChildObjects().get(16).scaleObject(0.1f, 0.1f, 0.1f);
            spheres.get(0).getChildObjects().get(16).translateObject(-0.4f, 0.1f, 0);
        }

        //bola asap
        {
            spheres.get(0).getChildObjects().add(new Sphere
                    (
                            Arrays.asList
                                    (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                            new ArrayList<>(),
                            new Vector4f(0.81f, 0.81f, 0.81f, 1.0f),0.01, 0.01, 0.01, 0f, 0, 0, 1
                    )
            );
            //spheres.get(0).getChildObjects().get(16).scaleObject(0.1f, 0.1f, 0.1f);
            spheres.get(0).getChildObjects().get(17).translateObject(-0.4f, 0.1f, 0);
        }

    }

    public void input()
    {
        {
            if(window.isKeyPressed(GLFW_KEY_Q))
            {
                for (Sphere i: spheres)
                {
//                    i.rotateObject(1f, 0, 0, 1);
                    i.rotateObjectOnPoint(1f, 0, 0, 1, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_E))
            {
                for (Sphere i: spheres)
                {
//                    i.rotateObject(-1f, 0, 0, 1);
                    i.rotateObjectOnPoint(-1f, 0, 0, 1, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            /*
            if(window.isKeyPressed(GLFW_KEY_W))
            {
                camera.addRotation(0.01f, 0);
                for (Sphere i: spheres)
                {
//                    i.rotateObject(1f, 1, 0, 0);
                    //i.rotateObjectOnPoint(1f, 1, 0, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_S))
            {
                camera.addRotation(-0.01f, 0);
                for (Sphere i: spheres)
                {
//                    i.rotateObject(-1f, 1, 0, 0);
                    //i.rotateObjectOnPoint(-1f, 1, 0, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_A))
            {
                camera.addRotation(0, 0.01f);
                for (Sphere i: spheres)
                {
//                    i.rotateObject(1f, 0, 1, 0);
                    //i.rotateObjectOnPoint(1f, 0, 1, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }

            if(window.isKeyPressed(GLFW_KEY_D))
            {
                camera.addRotation(0, -0.01f);
                for (Sphere i: spheres)
                {
//                    i.rotateObject(-1f, 0, 1, 0);
                    //i.rotateObjectOnPoint(-1f, 0, 1, 0, i.getCpx(), i.getCpy(), i.getCpz());
                }
            }
            */

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



            if(window.isKeyPressed(GLFW_KEY_SPACE)) {
                ((Sphere) spheres.get(0).getChildObjects().get(10)).rotateObjectOnPoint(-1, 0, 1, 0, ((Sphere) spheres.get(0).getChildObjects().get(10)).getCenterPoint().x, ((Sphere) spheres.get(0).getChildObjects().get(10)).getCenterPoint().y, ((Sphere) spheres.get(0).getChildObjects().get(10)).getCenterPoint().z);
                ((Sphere) spheres.get(0).getChildObjects().get(11)).rotateObjectOnPoint(-1, 0, 1, 0, ((Sphere) spheres.get(0).getChildObjects().get(10)).getCenterPoint().x, ((Sphere) spheres.get(0).getChildObjects().get(10)).getCenterPoint().y, ((Sphere) spheres.get(0).getChildObjects().get(10)).getCenterPoint().z);
                ((Sphere) spheres.get(0).getChildObjects().get(16)).rotateObjectOnPoint(-1, 0, 1, 0, ((Sphere) spheres.get(0).getChildObjects().get(10)).getCenterPoint().x, ((Sphere) spheres.get(0).getChildObjects().get(10)).getCenterPoint().y, ((Sphere) spheres.get(0).getChildObjects().get(10)).getCenterPoint().z);
            }

            if (window.isKeyPressed(GLFW_KEY_C) && gunCounter == 300) {
                gunCounter = 0;
                spheres.get(0).getChildObjects().get(16).getChildObjects().add(new Sphere
                        (
                                Arrays.asList
                                        (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                                new ArrayList<>(),
                                new Vector4f(1.0f, 0, 0, 1.0f), 0.005, 0.005, 0.01, 0, 0, 0, 1
                        )
                );


                temp = ((Sphere) spheres.get(0).getChildObjects().get(16).getChildObjects().get(spheres.get(0).getChildObjects().get(16).getChildObjects().size() - 1));
                temp.translateObject(((Sphere) spheres.get(0).getChildObjects().get(16)).getCpx(), ((Sphere) spheres.get(0).getChildObjects().get(16)).getCpy(), ((Sphere) spheres.get(0).getChildObjects().get(16)).getCpz());
                float x, y, z, x1, x2, z1, z2;
                x = ((Sphere) spheres.get(0).getChildObjects().get(16)).getCpx();
                y = 0.1f;
                z = ((Sphere) spheres.get(0).getChildObjects().get(16)).getCpz();

                //temp.translateObject(((Sphere)spheres.get(0).getChildObjects().get(16)).getCpx(), ((Sphere)spheres.get(0).getChildObjects().get(16)).getCpy()-0f, ((Sphere)spheres.get(0).getChildObjects().get(16)).getCpz());

                if (x >= 0) {
                    x1 = x + 2;
                    x2 = x + 5;
                } else {
                    x1 = x - 2;
                    x2 = x - 5;
                }
                if (z >= 0) {
                    z1 = -1 * (float) Math.sqrt((3 * 3) - (x1 * x1));
                } else {
                    z1 = (float) Math.sqrt((3 * 3) - (x1 * x1));
                }
                z2 = z1;

                System.out.println(((Sphere) spheres.get(0).getChildObjects().get(16)).getCpy());
                System.out.println(y);
                temp.generateBezierPoints(x, y, z,
                        x1, y, z1,
                        x2, -2, z2);
            } else if (window.isKeyPressed(GLFW_KEY_C)) {
                gunCounter++;
            }

            for (Objects i : spheres.get(0).getChildObjects().get(16).getChildObjects()) {
                ((Sphere) i).moveToNextPoint(((Sphere) i).getPath());
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
                    i.translateObject(0f, 0.01f, 0f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_K))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0f, -0.01f, 0f);
                }
            }

            if(window.isKeyPressed(GLFW_KEY_J))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(-0.005f, 0f, 0f);
                    ((Sphere)spheres.get(0).getChildObjects().get(0)).rotateObjectOnPoint(1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(0)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(0)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(0)).getCenterPoint().z);
                    ((Sphere)spheres.get(0).getChildObjects().get(1)).rotateObjectOnPoint(1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(1)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(1)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(1)).getCenterPoint().z);
                    ((Sphere)spheres.get(0).getChildObjects().get(2)).rotateObjectOnPoint(1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(2)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(2)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(2)).getCenterPoint().z);
                    ((Sphere)spheres.get(0).getChildObjects().get(3)).rotateObjectOnPoint(1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(3)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(3)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(3)).getCenterPoint().z);
                    ((Sphere)spheres.get(0).getChildObjects().get(5)).rotateObjectOnPoint(1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(5)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(5)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(5)).getCenterPoint().z);
                    ((Sphere)spheres.get(0).getChildObjects().get(6)).rotateObjectOnPoint(1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(6)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(6)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(6)).getCenterPoint().z);
                    ((Sphere)spheres.get(0).getChildObjects().get(7)).rotateObjectOnPoint(1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(7)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(7)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(7)).getCenterPoint().z);
                    ((Sphere)spheres.get(0).getChildObjects().get(8)).rotateObjectOnPoint(1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(8)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(8)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(8)).getCenterPoint().z);
                }

            }

            if(window.isKeyPressed(GLFW_KEY_L))
            {
                for (Sphere i: spheres)
                {
                    i.translateObject(0.005f, 0f, 0f);

                }
                ((Sphere)spheres.get(0).getChildObjects().get(0)).rotateObjectOnPoint(-1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(0)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(0)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(0)).getCenterPoint().z);
                ((Sphere)spheres.get(0).getChildObjects().get(1)).rotateObjectOnPoint(-1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(1)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(1)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(1)).getCenterPoint().z);
                ((Sphere)spheres.get(0).getChildObjects().get(2)).rotateObjectOnPoint(-1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(2)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(2)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(2)).getCenterPoint().z);
                ((Sphere)spheres.get(0).getChildObjects().get(3)).rotateObjectOnPoint(-1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(3)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(3)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(3)).getCenterPoint().z);
                ((Sphere)spheres.get(0).getChildObjects().get(5)).rotateObjectOnPoint(-1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(5)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(5)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(5)).getCenterPoint().z);
                ((Sphere)spheres.get(0).getChildObjects().get(6)).rotateObjectOnPoint(-1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(6)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(6)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(6)).getCenterPoint().z);
                ((Sphere)spheres.get(0).getChildObjects().get(7)).rotateObjectOnPoint(-1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(7)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(7)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(7)).getCenterPoint().z);
                ((Sphere)spheres.get(0).getChildObjects().get(8)).rotateObjectOnPoint(-1,0,0,1,((Sphere)spheres.get(0).getChildObjects().get(8)).getCenterPoint().x, ((Sphere)spheres.get(0).getChildObjects().get(8)).getCenterPoint().y, ((Sphere)spheres.get(0).getChildObjects().get(8)).getCenterPoint().z);
            }
        }

        if(window.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
        {
            camera.moveForward(0.01f);
        }

        if(window.isKeyPressed(GLFW_KEY_LEFT_CONTROL))
        {
            camera.moveBackwards(0.01f);
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