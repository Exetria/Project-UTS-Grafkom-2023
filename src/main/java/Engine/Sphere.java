package Engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class Sphere extends Circle
{
    List<Integer> index;

    int ibo, stackCount, sectorCount;
    double cpz;
    float radiusX, radiusY, radiusZ, totalRotateX, totalRotateY, totalRotateZ;
    public double rotationLimit;

    public Sphere(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, double rx, double ry, double rz, double cpx, double cpy, double cpz, int option)
    {
        super(shaderModuleDataList, vertices, color, rx, cpx, cpy);
        this.cpz = cpz;

        this.radiusX = (float) rx;
        this.radiusY = (float) ry;
        this.radiusZ = (float) rz;

        this.stackCount = 18;
        this.sectorCount = 36;

        if(option == 1)
        {
            createSphere();
        }
        else if (option == 2)
        {
            createBox();
        }
        else if (option == 3)
        {
            createCylinder();
        }
        else if (option == 4)
        {
            createEllipsoid();
        }
        else if (option == 5)
        {
            create1SideHyperboloid();
        }
        else if (option == 6)
        {
            create2SideHyperboloid();
        }
        else if (option == 7)
        {
            createEllipticCone();
        }
        else if (option == 8)
        {
            createEllipticParaboloid();
        }
        else if (option == 9)
        {
            createHyperboloidParaboloid();
        }
        else if(option == 10)
        {
            createWing();
        }
        else if(option == 11)
        {
            createFrontIntake();
        }
        else if(option == 12)
        {
            createTrapezoid();
        }
        else if(option == 13)
        {
            createTail();
        }
        else if(option == 14)
        {
            createEmpennage();
        }
        else if(option == 15)
        {
            createMissile();
        }
        else if(option == 16)
        {
            createHangar();
        }
        else if(option == 20)
        {
            createTrapesium();
        }
        else if(option == 21)
        {
            createRoda();
        }
        else if(option == 22)
        {
            createCorong();
        }
        else if (option == 41)
        {
            createTrapezoidwithSquare();
        }
        else if (option == 42)
        {
            createTrapezoidwithLine();
        }
        else if (option == 43)
        {
            createTireSupport();
        }
        else if (option == 44)
        {
            createGlass();
        }
        else if (option == 45)
        {
            createKepalaTembakan();
        }

        setupVAOVBO();
    }

    public void translateObject(float offsetX, float offsetY, float offsetZ)
    {
        model = new Matrix4f().translate(offsetX, offsetY, offsetZ).mul(new Matrix4f(model));
        cpx += offsetX;
        cpy += offsetY;
        cpz += offsetZ;

        for (Objects i: childObjects) {
            i.translateObject(offsetX, offsetY, offsetZ);
        }
    }

    public void rotateObject(float degree, float offsetX, float offsetY, float offsetZ)
    {
        //offset x, y, sama z itu maksudnya rotasi terhadap sumbunya misal z=1 berarti rotasi thd sb z
        model = new Matrix4f().rotate((float)(Math.toRadians(degree)), offsetX, offsetY, offsetZ).mul(new Matrix4f(model));

        float newcpx =(float) (cpx * Math.cos(degree) - cpy * Math.sin(degree));
        float newcpy =(float) (cpx * Math.sin(degree) + cpy * Math.cos(degree));

        cpx = newcpx;
        cpy = newcpy;

        for (Objects i: childObjects)
        {
            i.rotateObject(degree, offsetX, offsetY, offsetZ);
//            ((Sphere)i).rotateObjectOnPoint(degree, offsetX, offsetY, offsetZ, (float)((Sphere) i).getCpx(), (float)((Sphere) i).getCpy());

        }
    }

    public void rotateObjectOnPoint(float degree, float offsetX, float offsetY, float offsetZ, float rotateX, float rotateY, float rotateZ)
    {
        translateObject(-rotateX, -rotateY, -rotateZ);

        model = new Matrix4f().rotate((float)(Math.toRadians(degree)), offsetX, offsetY, offsetZ).mul(new Matrix4f(model));

        if(offsetX == 1f)
        {
            this.totalRotateX += degree;
            this.totalRotateX %= 360;
        }
        else if (offsetY == 1f)
        {
            this.totalRotateY += degree;
            this.totalRotateY %= 360;
        }
        else if (offsetZ == 1f)
        {
            this.totalRotateZ += degree;
            this.totalRotateZ %= 360;
        }

        float newcpx =(float) (cpx * Math.cos((float)(Math.toRadians(degree))) - cpy * Math.sin((float)(Math.toRadians(degree))));
        float newcpy =(float) (cpx * Math.sin((float)(Math.toRadians(degree))) + cpy * Math.cos((float)(Math.toRadians(degree))));

        cpx = newcpx;
        cpy = newcpy;

        translateObject(rotateX, rotateY, rotateZ);

        for (Objects i: childObjects)
        {
            ((Sphere)i).rotateObjectOnPoint(degree, offsetX, offsetY, offsetZ, rotateX, rotateY, rotateZ);
        }
    }

    public void experimentRotate(float degree, float offsetX, float offsetY, float offsetZ, float rotateX, float rotateY, float rotateZ)
    {
        translateObject(-rotateX, -rotateY, -rotateZ);

        model = new Matrix4f().rotate((float)(Math.toRadians(-this.totalRotateX)), 1, 0, 0).mul(new Matrix4f(model));
        model = new Matrix4f().rotate((float)(Math.toRadians(-this.totalRotateY)), 0, 1, 0).mul(new Matrix4f(model));
        model = new Matrix4f().rotate((float)(Math.toRadians(-this.totalRotateZ)), 0, 0, 1).mul(new Matrix4f(model));

        model = new Matrix4f().rotate((float)(Math.toRadians(degree)), offsetX, offsetY, offsetZ).mul(new Matrix4f(model));

        model = new Matrix4f().rotate((float)(Math.toRadians(this.totalRotateX)), 1, 0, 0).mul(new Matrix4f(model));
        model = new Matrix4f().rotate((float)(Math.toRadians(this.totalRotateY)), 0, 1, 0).mul(new Matrix4f(model));
        model = new Matrix4f().rotate((float)(Math.toRadians(this.totalRotateZ)), 0, 0, 1).mul(new Matrix4f(model));

        float newcpx =(float) (cpx * Math.cos((float)(Math.toRadians(degree))) - cpy * Math.sin((float)(Math.toRadians(degree))));
        float newcpy =(float) (cpx * Math.sin((float)(Math.toRadians(degree))) + cpy * Math.cos((float)(Math.toRadians(degree))));

        cpx = newcpx;
        cpy = newcpy;

        translateObject(rotateX, rotateY, rotateZ);

        for (Objects i: childObjects)
        {
            ((Sphere)i).rotateObjectOnPoint(degree, offsetX, offsetY, offsetZ, rotateX, rotateY, rotateZ);
        }
    }

    public boolean moveToNextPoint(ArrayList<Vector3f> path)
    {
        if(path.size() != 0)
        {
            float diffX, diffY, diffZ;
            diffX = (float) (path.get(0).x - cpx);
            diffY = (float) (path.get(0).y - cpy);
            diffZ = (float) (path.get(0).z - cpz);
            translateObject(diffX, diffY, diffZ);
            path.remove(0);
            return true;
        }
        else
        {
            vertices.clear();
            childObjects.clear();
            return false;
        }
    }

    public ArrayList<Vector3f> generateBezierPoints(float firstX, float firstY, float firstZ, float secondX, float secondY, float secondZ, float thirdX, float thirdY, float thirdZ)
    {
        ArrayList<Vector3f> result = new ArrayList<>();
        float newX, newY, newZ;
        for(double i = 0; i <=1; i+= 0.01)
        {
            newX = (float) ((Math.pow((1-i), 2) * firstX) + (2 * (1-i) * i * secondX) + (Math.pow(i, 2) * thirdX));
            newY = (float) ((Math.pow((1-i), 2) * firstY) + (2 * (1-i) * i * secondY) + (Math.pow(i, 2) * thirdY));
            newZ = (float) ((Math.pow((1-i), 2) * firstZ) + (2 * (1-i) * i * secondZ) + (Math.pow(i, 2) * thirdZ));
            result.add(new Vector3f(newX, newY, newZ));
        }

        childObjects.add(new Objects(
                Arrays.asList
                        (
                                new ShaderProgram.ShaderModuleData
                                        ("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                                new ShaderProgram.ShaderModuleData
                                        ("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                        ),
                new ArrayList<>(),
                new Vector4f(0.0f, 1.0f, 0.0f, 1.0f)    //color
        )
        );
//        childObjects.get(5).setVertices(result);

        return result;
    }

    //coba return ke normal state
    public void normalize()
    {
        float x = getCpx();
        float y = getCpy();
        float z = getCpz();
        rotateObjectOnPoint(-totalRotateX, 1f, 0, 0, x, y, z);
        rotateObjectOnPoint(-totalRotateY, 0, 1f, 0, x, y, z);
        rotateObjectOnPoint(-totalRotateZ, 0, 0, 1f, x, y, z);
        totalRotateX = 0;
        totalRotateY = 0;
        totalRotateZ = 0;
    }

    public void createSphere()
    {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for(double v = -Math.PI/2; v<= Math.PI/2; v+=Math.PI/60){
            for(double u = -Math.PI; u<= Math.PI; u+=Math.PI/60){
                float x = radiusX * (float)(Math.cos(v) * Math.cos(u));
                float y = radiusY * (float)(Math.cos(v) * Math.sin(u));
                float z = radiusZ * (float)(Math.sin(v));
                temp.add(new Vector3f(x,y,z));
            }
        }
        vertices = temp;
    }

    public void createBox()
    {
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();

        //titik 1 kiri atas belakang
        temp.x = (float)cpx - radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 2 kiri bawah belakang
        temp.x = (float)cpx - radiusX/2;
        temp.y = (float)cpy - radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 3 kanan bawah belakang
        temp.x = (float)cpx + radiusX/2;
        temp.y = (float)cpy - radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 4 kanan atas belakang
        temp.x = (float)cpx + radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 5 kiri atas depan
        temp.x = (float)cpx - radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 6 kiri bawah depan
        temp.x = (float)cpx - radiusX/2;
        temp.y = (float)cpy - radiusY/2;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 7 kanan bawah depan
        temp.x = (float)cpx + radiusX/2;
        temp.y = (float)cpy - radiusY/2;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 8 kanan atas depan
        temp.x = (float)cpx + radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);

        vertices.clear();
        {
            //kotak yg sisi belakang
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(0));

            //kotak yg sisi depan
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(4));

            //sisi kiri
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(1));

            //sisi kanan
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(3));
        }
    }

    public void createCylinder()
    {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for (double i = 0; i < 360; i+=0.1)
        {
            x = cpx + radiusX * (float)Math.cos(Math.toRadians(i));
            y = cpy + radiusY * (float)Math.sin(Math.toRadians(i));

            temp.add(new Vector3f((float)x, (float)y, 0.0f));
            temp.add(new Vector3f((float)x, (float)y, -radiusZ));
        }

        vertices = temp;
    }

    public void createEllipsoid()
    {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for(double v = -Math.PI/2; v<= Math.PI/2; v+=Math.PI/60){
            for(double u = -Math.PI; u<= Math.PI; u+=Math.PI/60){
                float x = radiusX * (float)(Math.cos(v) * Math.cos(u));
                float y = radiusY * (float)(Math.cos(v) * Math.sin(u) / 2);
                float z = radiusZ * (float)(Math.sin(v) / 2);
                temp.add(new Vector3f(x,y,z));
            }
        }
        vertices = temp;
    }

    public void create1SideHyperboloid()
    {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for(double v = -Math.PI/2; v<= Math.PI/2; v+=Math.PI/60){
            for(double u = -Math.PI/2; u<= Math.PI/2; u+=Math.PI/60){
                float x = radiusX * (float)((1/Math.cos(v)) * Math.cos(u));
                float y = radiusY * (float)((1/Math.cos(v)) * Math.sin(u));
                float z = radiusZ * (float)(Math.tan(v));
                temp.add(new Vector3f(x,y,z));
            }
        }
        vertices = temp;
    }

    public void create2SideHyperboloid()
    {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for(double v = -Math.PI/2; v<= Math.PI/2; v+=Math.PI/60){
            for(double u = -Math.PI/2; u<= Math.PI/2; u+=Math.PI/60){
                float x = radiusX * (float)(Math.tan(v) * Math.cos(u));
                float y = radiusY * (float)(Math.tan(v) * Math.sin(u));
                float z = radiusZ * (float)(1/Math.cos(v));
                temp.add(new Vector3f(x,y,z));
            }

            for(double u = Math.PI/2; u<= 3 * Math.PI / 2; u+=Math.PI/60){
                float x = -radiusX * (float)(Math.tan(v) * Math.cos(u));
                float y = -radiusY * (float)(Math.tan(v) * Math.sin(u));
                float z = -radiusZ * (float)(1/Math.cos(v));
                temp.add(new Vector3f(x,y,z));
            }
        }
        vertices = temp;
    }

    public void createEllipticCone()
    {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for(double v = 0; v<= 2 * Math.PI; v+=Math.PI/60){
            for(double u = -Math.PI; u<= Math.PI; u+=Math.PI/60){
                float x = (float) (radiusX * v * Math.cos(u));
                float y = (float) (radiusY * v * Math.sin(u));
                float z = (float) (radiusZ * v);
                temp.add(new Vector3f(x,y,z));
            }
        }
        vertices = temp;
    }

    public void createEllipticParaboloid()
    {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for(double v = 0; v<= 2 * Math.PI; v+=Math.PI/60){
            for(double u = -Math.PI; u<= Math.PI; u+=Math.PI/60){
                float x = (float) (radiusX * v * Math.cos(u));
                float y = (float) (radiusY * v * Math.sin(u));
                float z = (float) (Math.pow(v, 2)/11);
                temp.add(new Vector3f(x,y,z));
            }
        }
        vertices = temp;
    }

    public void createHyperboloidParaboloid()
    {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for(double v = 0; v<= 2 * Math.PI; v+=Math.PI/60){
            for(double u = -Math.PI; u<= Math.PI; u+=Math.PI/60){
                float x = radiusX * (float) (v * Math.tan(u));
                float y = radiusY * (float) (v * 1/Math.cos(u));
                float z = radiusZ * (float)(Math.pow(v, 2));
                temp.add(new Vector3f(x,y,z));
            }
        }
        vertices = temp;
    }

    //sayap (kanan)
    public void createWing()
    {
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();

        //titik 1 depan sayap (0, 1)
        temp.x = 0;
        temp.y = 0;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = 0;
        temp.y = -radiusY;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 2 belakang sayap (2, 3)
        temp.x = 0;
        temp.y = 0;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = 0;
        temp.y = -radiusY;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 3 ujung paling jauh sayap (4, 5)
        temp.x = (float)cpx + radiusX * 1.15f;
        temp.y = 0;
        temp.z = (float)cpz + radiusZ/3;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = (float)cpx + radiusX * 1.15f;
        temp.y = -radiusY;
        temp.z = (float)cpz + radiusZ/3;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 4 ujung belakang sayap yg agak kelebihan (6, 7)
        temp.x = (float)cpx + radiusX;
        temp.y = 0;
        temp.z = (float)cpz + radiusZ * 0.6f;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = (float)cpx + radiusX;
        temp.y = -radiusY;
        temp.z = (float)cpz + radiusZ * 0.6f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 5 ujung belakang sayap yg rata (8, 9)
        temp.x = (float)cpx + radiusX/2;
        temp.y = 0;
        temp.z = (float)cpz + radiusZ * 0.5f;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = (float)cpx + radiusX/2;
        temp.y = -radiusY;
        temp.z = (float)cpz + radiusZ * 0.5f;
        tempVertices.add(temp);

        vertices.clear();
        {
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(0));

            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(1));

            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(6));

            vertices.add(tempVertices.get(8));
            vertices.add(tempVertices.get(9));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(9));

            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(8));
        }
    }

    public void createFrontIntake()
    {
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();

        //titik 2 atas intake (0, 1)
        temp.x = -0.5f * radiusX;
        temp.y = 0.5f * radiusY;
        temp.z = 0;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = 0.5f * radiusX;
        temp.y = 0.5f * radiusY;
        temp.z = 0;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 2 depan intake (2, 3)
        temp.x = -0.5f * radiusX;
        temp.y = 0.5f * radiusY * 0.7f;
        temp.z = -radiusZ;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = 0.5f * radiusX;
        temp.y = 0.5f * radiusY * 0.7f;
        temp.z = -radiusZ;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 2 bawah intake (4, 5)
        temp.x = -0.5f * radiusX;
        temp.y = -0.5f * radiusY;
        temp.z = 0;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = 0.5f * radiusX;
        temp.y = -0.5f * radiusY;
        temp.z = 0;
        tempVertices.add(temp);

        vertices.clear();
        {
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(0));

            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(1));

            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(1));
        }
    }

    //trapesium secara horizontal (berdiri biasa) dan 1 sisinya rata
    public void createTrapezoid()
    {
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();

        //atas kiri depan
        temp.x = (float) cpx - radiusX / 2;
        temp.y = (float) cpy + radiusY / 2;
        temp.z = (float) cpz + radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //atas kanan depan
        temp.x = (float) cpx + radiusX / 2;
        temp.y = (float) cpy + radiusY / 2;
        temp.z = (float) cpz + radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //atas kiri belakang
        temp.x = (float) cpx - radiusX / 2;
        temp.y = (float) cpy + radiusY / 2;
        temp.z = (float) cpz - radiusZ;
        tempVertices.add(temp);
        temp = new Vector3f();

        //atas kanan belakang
        temp.x = (float) cpx + radiusX / 2;
        temp.y = (float) cpy + radiusY / 2;
        temp.z = (float) cpz - radiusZ;
        tempVertices.add(temp);
        temp = new Vector3f();

        //bawah kiri depan
        temp.x = (float) cpx - radiusX * 0.9f;
        temp.y = (float) cpy - radiusY / 2;
        temp.z = (float) cpz + radiusZ;
        tempVertices.add(temp);
        temp = new Vector3f();

        //bawah kanan depan
        temp.x = (float) cpx + radiusX * 0.9f;
        temp.y = (float) cpy - radiusY / 2;
        temp.z = (float) cpz + radiusZ;
        tempVertices.add(temp);
        temp = new Vector3f();

        //bawah kiri belakang
        temp.x = (float) cpx - radiusX * 0.9f;
        temp.y = (float) cpy - radiusY / 2;
        temp.z = (float) cpz - radiusZ;
        tempVertices.add(temp);
        temp = new Vector3f();

        //bawah kanan belakang
        temp.x = (float) cpx + radiusX * 0.9f;
        temp.y = (float) cpy - radiusY / 2;
        temp.z = (float) cpz - radiusZ;
        tempVertices.add(temp);

        vertices.clear();
        {
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(0));

            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(4));

            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(1));

            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(6));

            vertices.add(tempVertices.get(2));
        }
    }

    public void createTail()
    {
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();

        //titik 1 depan bawah ekor (0, 1)
        temp.x = 0;
        temp.y = 0;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = 0;
        temp.y = -radiusY;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 2 belakang bahah ekor (2, 3)
        temp.x = 0;
        temp.y = 0;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = 0;
        temp.y = -radiusY;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 3 ujung atas ekor (4, 5)
        temp.x = (float)cpx + radiusX;
        temp.y = 0;
        temp.z = (float)cpz + radiusZ/4;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = (float)cpx + radiusX;
        temp.y = -radiusY;
        temp.z = (float)cpz + radiusZ/4;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 4 ujung belakang atas ekor (6, 7)
        temp.x = (float)cpx + radiusX;
        temp.y = 0;
        temp.z = (float)cpz + radiusZ * 0.55f;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = (float)cpx + radiusX;
        temp.y = -radiusY;
        temp.z = (float)cpz + radiusZ * 0.55f;
        tempVertices.add(temp);

        vertices.clear();
        {
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(0));

            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(1));

            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(6));

            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(6));

            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(6));
        }
        rotateObject(90f, 0, 0, 1f);
    }

    //empennage = sayap belakang
    public void createEmpennage()
    {
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();

        this.rotationLimit = 45;

        //titik 1 depan sayap (0, 1)
        temp.x = 0;
        temp.y = 0;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = 0;
        temp.y = -radiusY;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 2 dalam tengah sayap (2, 3)
        temp.x = (float) cpx + radiusX * 0.4f;
        temp.y = 0;
        temp.z = (float)cpz - radiusZ * 0.17f;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = (float) cpx + radiusX * 0.4f;
        temp.y = -radiusY;
        temp.z = (float)cpz - radiusZ * 0.17f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 3 depan tengah sayap (4, 5)
        temp.x = (float) cpx + radiusX * 0.4f;
        temp.y = 0;
        temp.z = (float)cpz - radiusZ * 0.27f;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = (float) cpx + radiusX * 0.4f;
        temp.y = -radiusY;
        temp.z = (float)cpz - radiusZ * 0.27f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 4 ujung luar sayap (6, 7)
        temp.x = (float) cpx + radiusX;
        temp.y = 0;
        temp.z = (float) cpz + radiusZ * 0.2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = (float) cpx + radiusX;
        temp.y = -radiusY;
        temp.z = (float) cpz + radiusZ * 0.2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 5 ujung luar belakang sayap (8, 9)
        temp.x = (float)cpx + radiusX * 0.9f;
        temp.y = 0;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = (float)cpx + radiusX * 0.9f;
        temp.y = -radiusY;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 6 ujung belakang sayap (10, 11)
        temp.x = (float) cpx;
        temp.y = 0;
        temp.z = (float)cpz + radiusZ * 0.4f;
        tempVertices.add(temp);
        temp = new Vector3f();

        temp.x = (float) cpx;
        temp.y = -radiusY;
        temp.z = (float)cpz + radiusZ * 0.4f;
        tempVertices.add(temp);

        vertices.clear();
        {
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(0));

            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(5));

            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(6));

            vertices.add(tempVertices.get(8));
            vertices.add(tempVertices.get(9));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(9));

            vertices.add(tempVertices.get(11));
            vertices.add(tempVertices.get(10));
            vertices.add(tempVertices.get(8));
            vertices.add(tempVertices.get(10));

            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(11));
        }
    }

    public void createMissile()
    {
        //KEPALA MISIL (PARENT)
        createSphere();

        //BODY MISIL
        childObjects.add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), radiusX, radiusY, 20 * radiusZ, 0, 0, 0, 3
                )
        );
        childObjects.get(0).rotateObject(180f, 1, 0, 0);

        //SAYAP KIRI
        childObjects.add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), radiusX*2f, radiusY/20f, radiusZ*2f, 0f, 0, 0, 13                    )
        );
        childObjects.get(1).translateObject(0f, radiusY/2f, 19 * radiusZ);
        childObjects.get(1).rotateObject(90f, 0, 0, 1);

        //SAYAP ATAS
        childObjects.add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), radiusX*2f, radiusY/20f, radiusZ*2f, 0f, 0, 0, 13                    )
        );
        childObjects.get(2).translateObject(0f, radiusY/2f, 19 * radiusZ);

        //SAYAP KANAN
        childObjects.add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), radiusX*2f, radiusY/20f, radiusZ*2f, 0f, 0, 0, 13                    )
        );
        childObjects.get(3).translateObject(0f, radiusY/2f, 19 * radiusZ);
        childObjects.get(3).rotateObject(-90f, 0, 0, 1);

        //SAYAP BAWAH
        childObjects.add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), radiusX*2f, radiusY/20f, radiusZ*2f, 0f, 0, 0, 13                    )
        );
        childObjects.get(4).translateObject(0f, radiusY/2f, 19 * radiusZ);
        childObjects.get(4).rotateObject(180f, 0, 0, 1);
    }

    public void createHangar()
    {
        //TEMBOK KIRI (PARENT)
        createBox();
        translateObject(-1f, 0, 0);

        //TEMBOK KANAN
        childObjects.add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(0f, 0f, 1f, 1.0f), radiusX, radiusY, radiusZ, 0, 0, 0, 2
                )
        );
        childObjects.get(0).translateObject(1f, 0, 0);

        //TEMBOK BELAKANG
        childObjects.add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), radiusX, radiusY, radiusZ, 0, 0, 0, 2
                )
        );
        childObjects.get(1).rotateObject(90f, 0, 1, 0);
        childObjects.get(1).translateObject(0, 0, 1);

        //TEMBOK BELAKANG
        childObjects.add(new Sphere
                (
                        Arrays.asList
                                (new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER), new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)),
                        new ArrayList<>(),
                        new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), radiusX, 2 * radiusY, radiusZ, 0, 0, 0, 2
                )
        );
        childObjects.get(2).rotateObject(90f, 0, 0, 1);
        childObjects.get(2).translateObject(0, 0.5f, 0);

    }

    public void createTrapezoidwithSquare()
    {
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();

        //titik 1 kiri atas belakang
        temp.x = (float)cpx - radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 2 kiri bawah belakang
        temp.x = (float)cpx - radiusX/1.5f;
        temp.y = (float)cpy - radiusY/1.95f;
        temp.z = (float)cpz - radiusZ/1.5f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 3 kanan bawah belakang
        temp.x = (float)cpx + radiusX/1.9f;
        temp.y = (float)cpy - radiusY/1.95f;
        temp.z = (float)cpz - radiusZ/1.5f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 4 kanan atas belakang
        temp.x = (float)cpx + radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 5 kiri atas depan
        temp.x = (float)cpx - radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 6 kiri bawah depan
        temp.x = (float)cpx - radiusX/1.5f;
        temp.y = (float)cpy - radiusY/1.95f;
        temp.z = (float)cpz + radiusZ/1.5f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 7 kanan bawah depan
        temp.x = (float)cpx + radiusX/1.9f;
        temp.y = (float)cpy - radiusY/1.95f;
        temp.z = (float)cpz + radiusZ/1.5f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 8 kanan atas depan
        temp.x = (float)cpx + radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);

        vertices.clear();
        {
            //BAGIAN ATAS KOTAK
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(0));

            //TITIK MIRING KANAN DEPAN DAN TITIK MIRING KIRI DEPAN SERTA SAMBUNGKAN DEPAN
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(1));

            //TITIK MIRING KANAN BELAKANG DAN SAMBUNGKAN KANAN
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(1));

            //TITIK MIRING KIRI BELAKANG DAN SAMBUNGKAN BELAKANG
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(2));

            //SAMBUNGKAN KIRI
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(5));
        }

    }

    public void createTrapezoidwithLine()
    {
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();

        //titik 1 kiri atas belakang
        temp.x = (float)cpx - radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 2 kiri bawah belakang
        temp.x = (float)cpx - radiusX/1.08f;
        temp.y = (float)cpy - radiusY/1.95f;
        temp.z = (float)cpz - radiusZ/2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 3 kanan bawah belakang
        temp.x = (float)cpx + radiusX/2f;
        temp.y = (float)cpy - radiusY/2f;
        temp.z = (float)cpz - radiusZ/2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 4 kanan atas belakang
        temp.x = (float)cpx + radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 5 kiri atas depan
        temp.x = (float)cpx - radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 6 kiri bawah depan
        temp.x = (float)cpx - radiusX/1.08f;
        temp.y = (float)cpy - radiusY/1.95f;
        temp.z = (float)cpz + radiusZ/2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 7 kanan bawah depan
        temp.x = (float)cpx + radiusX/2f;
        temp.y = (float)cpy - radiusY/2f;
        temp.z = (float)cpz + radiusZ/2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 8 kanan atas depan
        temp.x = (float)cpx + radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);

        vertices.clear();
        {
            //BAGIAN ATAS KOTAK
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(0));

            //TITIK MIRING KANAN DEPAN DAN TITIK MIRING KIRI DEPAN SERTA SAMBUNGKAN DEPAN
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(1));

            //TITIK MIRING KANAN BELAKANG DAN SAMBUNGKAN KANAN
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(1));

            //TITIK MIRING KIRI BELAKANG DAN SAMBUNGKAN BELAKANG
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(2));

            //SAMBUNGKAN KIRI
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(5));
        }
    }

    public void createTireSupport()
    {
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();

        //titik 1 kiri atas belakang
        temp.x = (float)cpx - radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 2 kiri bawah belakang
        temp.x = (float)cpx - radiusX/0.35f;
        temp.y = (float)cpy - radiusY/2f;
        temp.z = (float)cpz - radiusZ/2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 3 kanan bawah belakang
        temp.x = (float)cpx + radiusX/0.4f;
        temp.y = (float)cpy - radiusY/2f;
        temp.z = (float)cpz - radiusZ/2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 4 kanan atas belakang
        temp.x = (float)cpx + radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 5 kiri atas depan
        temp.x = (float)cpx - radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 6 kiri bawah depan
        temp.x = (float)cpx - radiusX/0.35f;
        temp.y = (float)cpy - radiusY/2f;
        temp.z = (float)cpz + radiusZ/2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 7 kanan bawah depan
        temp.x = (float)cpx + radiusX/0.4f;
        temp.y = (float)cpy - radiusY/2f;
        temp.z = (float)cpz + radiusZ/2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 8 kanan atas depan
        temp.x = (float)cpx + radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz + radiusZ/2;
        tempVertices.add(temp);

        vertices.clear();
        {
            //BAGIAN ATAS
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(0));

            //BAGIAN KIRI MELENGKUNG
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(1));

            //BAGIAN KANAN MELENGKUNG
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(2));

        }
    }

    public void createCorong()
    {
        this.vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for(double i = 0.0; i < 360.0; i += 0.1) {
            this.x = this.cpx - this.r * (double)((float)Math.sin(Math.toRadians(i)));
            this.y = this.cpy - this.r * (double)((float)Math.cos(Math.toRadians(i)));
            temp.add(new Vector3f(0.0F, (float)this.x, (float)this.y));
            temp.add(new Vector3f(-this.radiusZ, (float)this.x, (float)this.y));
        }

        this.vertices = temp;
    }

    public void createRoda()
    {
        this.vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for(double i = 0.0; i < 360.0; i += 0.1) {
            if (i >= 85.0 && i <= 95.0) {
                this.x = this.cpx + (this.r - 0.03) * (double)((float)Math.cos(Math.toRadians(i)));
                this.y = this.cpy + (this.r - 0.03) * (double)((float)Math.sin(Math.toRadians(i)));
            } else if (i >= 175.0 && i <= 185.0) {
                this.x = this.cpx + (this.r - 0.03) * (double)((float)Math.cos(Math.toRadians(i)));
                this.y = this.cpy + (this.r - 0.03) * (double)((float)Math.sin(Math.toRadians(i)));
            } else if (i >= 265.0 && i <= 275.0) {
                this.x = this.cpx + (this.r - 0.03) * (double)((float)Math.cos(Math.toRadians(i)));
                this.y = this.cpy + (this.r - 0.03) * (double)((float)Math.sin(Math.toRadians(i)));
            } else if ((!(i >= 0.0) || !(i <= 5.0)) && (!(i >= 355.0) || !(i < 360.0))) {
                this.x = this.cpx + this.r * (double)((float)Math.cos(Math.toRadians(i)));
                this.y = this.cpy + this.r * (double)((float)Math.sin(Math.toRadians(i)));
            } else {
                this.x = this.cpx + (this.r - 0.03) * (double)((float)Math.cos(Math.toRadians(i)));
                this.y = this.cpy + (this.r - 0.03) * (double)((float)Math.sin(Math.toRadians(i)));
            }

            temp.add(new Vector3f((float)this.x, (float)this.y, 0.0F));
            temp.add(new Vector3f((float)this.x, (float)this.y, -this.radiusZ));
        }

        this.vertices = temp;
    }

    public void createTrapesium()
    {
        float a = 0.23F;
        float b = 0.08F;
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();
        temp.x = (float)this.cpx - this.radiusX / 2.0F;
        temp.y = (float)this.cpy + this.radiusY / 2.0F;
        temp.z = (float)this.cpz - this.radiusZ / 2.0F;
        tempVertices.add(temp);
        temp = new Vector3f();
        temp.x = (float)this.cpx - this.radiusX / 2.0F;
        temp.y = (float)this.cpy - this.radiusY / 2.0F;
        temp.z = (float)this.cpz - this.radiusZ / 2.0F;
        tempVertices.add(temp);
        temp = new Vector3f();
        temp.x = (float)this.cpx + this.radiusX / 2.0F;
        temp.y = (float)this.cpy - this.radiusY / 2.0F;
        temp.z = (float)this.cpz - this.radiusZ / 2.0F;
        tempVertices.add(temp);
        temp = new Vector3f();
        temp.x = (float)this.cpx + this.radiusX / 2.0F;
        temp.y = (float)this.cpy + this.radiusY / 2.0F;
        temp.z = (float)this.cpz - this.radiusZ / 2.0F;
        tempVertices.add(temp);
        temp = new Vector3f();
        temp.x = (float)this.cpx - this.radiusX / 2.0F;
        temp.y = (float)this.cpy + this.radiusY / 2.0F;
        temp.z = (float)this.cpz + this.radiusZ / 2.0F;
        tempVertices.add(temp);
        temp = new Vector3f();
        temp.x = (float)this.cpx - this.radiusX / 2.0F;
        temp.y = (float)this.cpy - this.radiusY / 2.0F;
        temp.z = (float)this.cpz + this.radiusZ / 2.0F;
        tempVertices.add(temp);
        temp = new Vector3f();
        temp.x = (float)this.cpx + this.radiusX / 2.0F;
        temp.y = (float)this.cpy - this.radiusY / 2.0F;
        temp.z = (float)this.cpz + this.radiusZ / 2.0F;
        tempVertices.add(temp);
        temp = new Vector3f();
        temp.x = (float)this.cpx + this.radiusX / 2.0F;
        temp.y = (float)this.cpy + this.radiusY / 2.0F;
        temp.z = (float)this.cpz + this.radiusZ / 2.0F;
        tempVertices.add(temp);
        temp = new Vector3f();
        temp.x = (float)this.cpx - (this.radiusX - a) / 2.0F;
        temp.y = (float)this.cpy - (this.radiusY + b) / 2.0F;
        temp.z = (float)this.cpz - this.radiusZ / 2.0F;
        tempVertices.add(temp);
        temp = new Vector3f();
        temp.x = (float)this.cpx + (this.radiusX - a) / 2.0F;
        temp.y = (float)this.cpy - (this.radiusY + b) / 2.0F;
        temp.z = (float)this.cpz - this.radiusZ / 2.0F;
        tempVertices.add(temp);
        temp = new Vector3f();
        temp.x = (float)this.cpx + (this.radiusX - a) / 2.0F;
        temp.y = (float)this.cpy - (this.radiusY + b) / 2.0F;
        temp.z = (float)this.cpz + this.radiusZ / 2.0F;
        tempVertices.add(temp);
        temp = new Vector3f();
        temp.x = (float)this.cpx - (this.radiusX - a) / 2.0F;
        temp.y = (float)this.cpy - (this.radiusY + b) / 2.0F;
        temp.z = (float)this.cpz + this.radiusZ / 2.0F;
        tempVertices.add(temp);
        this.vertices.clear();
        this.vertices.add(tempVertices.get(0));
        this.vertices.add(tempVertices.get(1));
        this.vertices.add(tempVertices.get(2));
        this.vertices.add(tempVertices.get(3));
        this.vertices.add(tempVertices.get(4));
        this.vertices.add(tempVertices.get(5));
        this.vertices.add(tempVertices.get(6));
        this.vertices.add(tempVertices.get(7));
        this.vertices.add(tempVertices.get(0));
        this.vertices.add(tempVertices.get(4));
        this.vertices.add(tempVertices.get(7));
        this.vertices.add(tempVertices.get(3));
        this.vertices.add(tempVertices.get(1));
        this.vertices.add(tempVertices.get(5));
        this.vertices.add(tempVertices.get(6));
        this.vertices.add(tempVertices.get(2));
        this.vertices.add(tempVertices.get(0));
        this.vertices.add(tempVertices.get(1));
        this.vertices.add(tempVertices.get(5));
        this.vertices.add(tempVertices.get(4));
        this.vertices.add(tempVertices.get(3));
        this.vertices.add(tempVertices.get(2));
        this.vertices.add(tempVertices.get(7));
        this.vertices.add(tempVertices.get(6));
        this.vertices.add(tempVertices.get(2));
        this.vertices.add(tempVertices.get(9));
        this.vertices.add(tempVertices.get(10));
        this.vertices.add(tempVertices.get(6));
        this.vertices.add(tempVertices.get(8));
        this.vertices.add(tempVertices.get(1));
        this.vertices.add(tempVertices.get(5));
        this.vertices.add(tempVertices.get(11));
        this.vertices.add(tempVertices.get(8));
        this.vertices.add(tempVertices.get(9));
        this.vertices.add(tempVertices.get(10));
        this.vertices.add(tempVertices.get(11));
    }

    public void createGlass()
    {
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();

        //titik 1 kiri atas belakang
        temp.x = (float)cpx - radiusX/2;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 2 kiri bawah belakang
        temp.x = (float)cpx - radiusX/2f;
        temp.y = (float)cpy - radiusY/2f;
        temp.z = (float)cpz - radiusZ/2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 3 kanan bawah belakang
        temp.x = (float)cpx + radiusX/0.8f;
        temp.y = (float)cpy - radiusY/2f;
        temp.z = (float)cpz - radiusZ/2f;
        tempVertices.add(temp);
        temp = new Vector3f();

        //titik 4 kanan atas belakang
        temp.x = (float)cpx + radiusX/2f;
        temp.y = (float)cpy + radiusY/2;
        temp.z = (float)cpz - radiusZ/2;
        tempVertices.add(temp);
        temp = new Vector3f();
        vertices.clear();
        {
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(0));
        }
    }

    public void createKepalaTembakan()
    {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for (double i = 0; i < 360; i+=0.1)
        {
            x = cpx + radiusX * (float)Math.cos(Math.toRadians(i));
            y = cpy + radiusY * (float)Math.sin(Math.toRadians(i));

            temp.add(new Vector3f((float)x, (float)y, 0.0f));
            temp.add(new Vector3f((float)x * 1.5f, (float)y * 1.5f, -radiusZ));
        }

        vertices = temp;
    }
    public float getCpz()
    {
        return (float) cpz;
    }

    public float getRadiusX()
    {
        return radiusX;
    }

    public float getRadiusY()
    {
        return radiusY;
    }

    public float getRadiusZ()
    {
        return radiusZ;
    }

    public float getTotalRotateX()
    {
        return totalRotateX;
    }

    public float getTotalRotateY()
    {
        return totalRotateY;
    }

    public float getTotalRotateZ()
    {
        return totalRotateZ;
    }

    public double getRotationLimit()
    {
        return rotationLimit;
    }
}