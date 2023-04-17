package Engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Objects extends ShaderProgram
{
    List<Vector3f> vertices;
    List<Objects> childObjects;

    int vao, vbo, vboColor;
    Vector4f color;
    List<Vector3f> verticesColor;
    UniformsMap uniformsMap;
    Matrix4f model;

    //constructor 1 warna
    public Objects(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color)
    {
        super(shaderModuleDataList);
        this.vertices = vertices;
        setupVAOVBO();

        this.color = color;
        uniformsMap = new UniformsMap(getProgramId());
        uniformsMap.createUniform("uni_color");     //bikin uniform buat ditangkap shader
        uniformsMap.createUniform("model");     //bikin uniform model buat shader
        uniformsMap.createUniform("view");
        uniformsMap.createUniform("projection");

        model = new Matrix4f();         //default const. matrix4f itu identitas
        childObjects = new ArrayList<>();
    }

    //constructor pake multi warna
    public Objects(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, List<Vector3f> verticesColor)
    {
        super(shaderModuleDataList);
        this.vertices = vertices;
        this.verticesColor = verticesColor;
        setupVAOVBOWithVerticesColor();
    }

    public void setupVAOVBO()
    {
        //setup vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        //setup vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Utils.listoFloat(vertices), GL_STATIC_DRAW);
    }

    public void setupVAOVBOWithVerticesColor()
    {
        //setup vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        //setup vbocolor
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Utils.listoFloat(vertices), GL_STATIC_DRAW);

        //setup vbocolor
        vboColor = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        glBufferData(GL_ARRAY_BUFFER, Utils.listoFloat(verticesColor), GL_STATIC_DRAW);
    }

    //draw setup tanpa kamera
    public void drawSetup()
    {
        bind();
        //isi uniform dengan variabel dari objek
        uniformsMap.setUniform("uni_color", color);
        uniformsMap.setUniform("model", model);

        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }

    //draw setup pake kamera
    public void drawSetup(Camera camera, Projection projection)
    {
        bind();
        //isi uniform dengan variabel dari objek
        uniformsMap.setUniform("uni_color", color);
        uniformsMap.setUniform("model", model);
        uniformsMap.setUniform("view", camera.getViewMatrix());
        uniformsMap.setUniform("projection", projection.getProjMatrix());

        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }

    //drawsetup pake warna vertices
    public void drawSetupWithVerticesColor()
    {
        bind();
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
    }

    //tambah vertices ke ArrayList
    public void addVertices(Vector3f newVector)
    {
        vertices.add(newVector);
        setupVAOVBO();
    }

    //bikin ArrayList baru isinya 3 vertices untuk garis bezier
    public void addVertices(double firstX, double firstY, double secondX, double secondY, double thirdX, double thirdY)
    {
        vertices.clear();
        vertices.add(new Vector3f((float)firstX, (float)firstY, 0));
        double newX, newY;
        for(double i = 0; i <=1; i+= 0.01)
        {
            //p(t) = ((1-t) * v1) + (t * v2)
            //p(t) = ((1-t)^2 * v1) + (2(1-t) * t * v2) + (t^2 * v3)
//            newX = ((1-i) * prevX) + (i * nextX);
//            newY = ((1-i) * prevY) + (i * nextY);
            newX = (Math.pow((1-i), 2) * firstX) + (2 * (1-i) * i * secondX) + (Math.pow(i, 2) * thirdX);
            newY = (Math.pow((1-i), 2) * firstY) + (2 * (1-i) * i * secondY) + (Math.pow(i, 2) * thirdY);
//            System.out.println(newX + "                " + newY);
            vertices.add(new Vector3f((float)newX, (float)newY, 0));
        }
        vertices.add(new Vector3f((float)thirdX, (float)thirdY, 0));
        setupVAOVBO();
    }

    public void setVertices(ArrayList<Vector3f> newVertices)
    {
        vertices = newVertices;
    }

    //update vertices dalam ArrayList index ke x
    public void updateVertice(int index, Vector3f value)
    {
        vertices.set(index, value);
        setupVAOVBO();
    }

    //get child
    public List<Objects> getChildObjects()
    {
        return childObjects;
    }

    //set child
    public void setChildObjects(List<Objects> childObjects)
    {
        this.childObjects = childObjects;
    }

    //getCenterPoint dari objek (jangan dipake, belum jadi pasti)
    public Vector3f getCenterPoint()
    {
        Vector3f centerTemp = new Vector3f();
        model.transformPosition(0, 0, 0, centerTemp);           //kalo dikali 0 semua nnti dapat posisi sekarang
        return centerTemp;
    }

    //translate objek + childnya
    public void translateObject(float offsetX, float offsetY, float offsetZ)
    {
        model = new Matrix4f().translate(offsetX, offsetY, offsetZ).mul(new Matrix4f(model));
        for (Objects i: childObjects)
        {
            i.translateObject(offsetX, offsetY, offsetZ);
        }
    }

    //rotate objek + childnya
    public void rotateObject(float degree, float offsetX, float offsetY, float offsetZ)
    {
        //offset x, y, sama z itu maksudnya rotasi terhadap sumbunya misal z=1 berarti rotasi thd sb z
        model = new Matrix4f().rotate((float)(Math.toRadians(degree)), offsetX, offsetY, offsetZ).mul(new Matrix4f(model));
        for (Objects i: childObjects) {
            i.rotateObject(degree, offsetX, offsetY, offsetZ);
        }
    }

    //scale object + childnya
    public void scaleObject(float x, float y, float z)
    {
        model = new Matrix4f().scale(x, y, z).mul(new Matrix4f(model));
        for (Objects i: childObjects) {
            i.scaleObject(x, y, z);
        }
    }

    //clear semua vertices di ArrayList
    public void clearVertices()
    {
        vertices.clear();
    }

    //draw standar tanpa
    public void draw()
    {
        drawSetup();
        glLineWidth(1);
        glPointSize(0);
        glDrawArrays(GL_POLYGON, 0, vertices.size());
        for (Objects i: childObjects)
        {
            i.draw();
        }
    }

    //draw pake kamera + child
    public void draw(Camera camera, Projection projection)
    {
        drawSetup(camera, projection);
        glLineWidth(1);
        glPointSize(0);
        glDrawArrays(GL_POLYGON, 0, vertices.size());
        for (Objects i: childObjects)
        {
            i.draw(camera, projection);
        }
    }

    //method draw pake kamera + child tapi gambarnya pake garis
    public void draw(Camera camera, Projection projection, boolean garis)
    {
        drawSetup(camera, projection);
        glLineWidth(1);
        glPointSize(0);
        glDrawArrays(GL_LINE_STRIP, 0, vertices.size());
        for (Objects i: childObjects)
        {
            i.draw(camera, projection, true);
        }
    }

    //draw pake warna banyak
    public void drawWithVerticesColor()
    {
        drawSetupWithVerticesColor();
        glLineWidth(1);
        glPointSize(0);
        glDrawArrays(GL_TRIANGLES, 0, vertices.size());
    }

    //draw garis
    public void drawLine()
    {
        drawSetup();
        glLineWidth(1);
        glPointSize(0);
        glDrawArrays(GL_LINE_STRIP, 0, vertices.size());
    }
}
