package Engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Sphere extends Circle
{
    List<Integer> index;

    int ibo, stackCount, sectorCount;
    double cpz;
    float radiusX, radiusY, radiusZ;

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
        model = new Matrix4f().rotate(degree, offsetX, offsetY, offsetZ).mul(new Matrix4f(model));

        float newcpx =(float) (cpx * Math.cos((double) degree) - cpy * Math.sin((double) degree));
        float newcpy =(float) (cpx * Math.sin((double) degree) + cpy * Math.cos((double) degree));

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

        model = new Matrix4f().rotate(degree, offsetX, offsetY, offsetZ).mul(new Matrix4f(model));

        float newcpx =(float) (cpx * Math.cos((double) degree) - cpy * Math.sin((double) degree));
        float newcpy =(float) (cpx * Math.sin((double) degree) + cpy * Math.cos((double) degree));

        cpx = newcpx;
        cpy = newcpy;

        translateObject(rotateX, rotateY, rotateZ);

        for (Objects i: childObjects)
        {
            ((Sphere)i).rotateObjectOnPoint(degree, offsetX, offsetY, offsetZ, rotateX, rotateY, rotateZ);
        }
    }

    public void centralize()
    {
        createSphere();
        setupVAOVBO();
    }

    public void returnPosition()
    {
        translateObject((float) cpx, (float) cpy, (float) cpz);
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

            //kotak yg sisi depan
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(7));

            //kotak yg sisi kiri
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(4));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(3));

            //kotak yg sisi kanan
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(6));
            vertices.add(tempVertices.get(2));

            //kotak yg sisi atas
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(5));
            vertices.add(tempVertices.get(4));

            //kotak yg sisi bawah
            vertices.add(tempVertices.get(3));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(7));
            vertices.add(tempVertices.get(6));
        }
    }

    public void createCylinder()
    {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for (double i = 0; i < 360; i+=0.1)
        {
            x = cpx + r * (float)Math.cos(Math.toRadians(i));
            y = cpy + r * (float)Math.sin(Math.toRadians(i));

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
                float x = (float) (0.5 * v * Math.cos(u));
                float y = (float) (0.5 * v * Math.sin(u));
                float z = (float) (0.5f * v);
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
                float x = (float) (0.5 * v * Math.cos(u));
                float y = (float) (0.5 * v * Math.sin(u));
                float z = (float) (Math.pow(v, 2));
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
                float x = 0.5f * (float) (v * Math.tan(u));
                float y = 0.5f * (float) (v * 1/Math.cos(u));
                float z = 0.5f * (float)(Math.pow(v, 2));
                temp.add(new Vector3f(x,y,z));
            }
        }
        vertices = temp;
    }

    public float getCpz()
    {
        return (float) cpz;
    }

    public float getRadiusX() {
        return radiusX;
    }

    public float getRadiusY() {
        return radiusY;
    }

    public float getRadiusZ() {
        return radiusZ;
    }
}