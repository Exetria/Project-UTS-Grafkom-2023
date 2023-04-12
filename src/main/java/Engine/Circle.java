package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Circle extends Objects
{
    double x, y, r, cpx, cpy;

    //constructor standar
    public Circle(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, double r, double cpx, double cpy)
    {
        super(shaderModuleDataList, vertices, color);
        this.r = r;
        this.cpx = cpx;
        this.cpy = cpy;
    }

    //constructor lingkaran(1), persegi(2), segitiga(3)
    public Circle(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, double r, double cpx, double cpy, int option)
    {
        super(shaderModuleDataList, vertices, color);
        this.r = r;
        this.cpx = cpx;
        this.cpy = cpy;

        //Setup VAO VBO sudah include dalam create()
        if(option == 1)
            createCircle();
        else if(option == 2)
            createRectangle();
        else if(option == 3)
            createTriangle();
    }

    //constructor khusus ellipse (1 horizontal, 2 vertical)
    public Circle(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, double r, double cpx, double cpy, double ratio, int option)
    {
        super(shaderModuleDataList, vertices, color);
        this.r = r;
        this.cpx = cpx;
        this.cpy = cpy;

        //Setup VAO VBO sudah include dalam create()
        if(option == 1)
            createHorizontalEllipse(ratio);
        else if(option == 2)
            createVerticalEllipse(ratio);
    }

    public void createCircle()
    {
        //clear vertices
        vertices.clear();

        for (double i = 0; i < 360; i+=0.01)
        {
            x = cpx + r * (float)Math.cos(Math.toRadians(i));
            y = cpy + r * (float)Math.sin(Math.toRadians(i));

            vertices.add(new Vector3f((float)x, (float)y, 0.0f));
        }
        setupVAOVBO();
    }

    public void createRectangle()
    {
        //clear vertices
        vertices.clear();

        for (double i = 45; i < 360; i+=90)
        {
            x = cpx + r * (float)Math.cos(Math.toRadians(i));
            y = cpy + r * (float)Math.sin(Math.toRadians(i));

            x = (double)Math.round(x*100)/100;
            y = (double)Math.round(y*100)/100;
            vertices.add(new Vector3f((float)x, (float)y, 0.0f));
        }
        setupVAOVBO();
    }

    public void createTriangle()
    {
        //clear vertices
        vertices.clear();

        for (double i = 90; i < 360; i+=120)
        {
            x = cpx + r * (float)Math.cos(Math.toRadians(i));
            y = cpy + r * (float)Math.sin(Math.toRadians(i));

            x = (double)Math.round(x*100)/100;
            y = (double)Math.round(y*100)/100;
            vertices.add(new Vector3f((float)x, (float)y, 0.0f));
        }
        setupVAOVBO();
    }

    public void createHorizontalEllipse(double yRatio)
    {
        vertices.clear();

        for (double i = 0; i < 360; i+=0.01)
        {
            x = cpx + r * (float)Math.cos(Math.toRadians(i));
            y = cpy + r/yRatio * (float)Math.sin(Math.toRadians(i));

            vertices.add(new Vector3f((float)x, (float)y, 0.0f));
        }
        setupVAOVBO();
    }

    public void createVerticalEllipse(double xRatio)
    {
        vertices.clear();

        for (double i = 0; i < 360; i+=0.01)
        {
            x = cpx + r/xRatio * (float)Math.cos(Math.toRadians(i));
            y = cpy + r * (float)Math.sin(Math.toRadians(i));

            vertices.add(new Vector3f((float)x, (float)y, 0.0f));
        }
        setupVAOVBO();
    }

    public float getCpx()
    {
        return (float) cpx;
    }

    public float getCpy()
    {
        return (float) cpy;
    }

    public void updateCP(double cpx, double cpy)
    {
        this.cpx = cpx;
        this.cpy = cpy;
        vertices.clear();
        createRectangle();
    }

    public void draw()
    {
        drawSetup();
        glLineWidth(1);
        glPointSize(0);
        glDrawArrays(GL_POLYGON, 0, vertices.size());
    }
}
