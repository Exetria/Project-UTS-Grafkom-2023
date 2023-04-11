package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Star extends Rectangle
{
    double x, y, r, cpx, cpy;
    int ibo;
    public Star(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, List<Integer> index, double r, double cpx, double cpy)
    {
        super(shaderModuleDataList, vertices, color, index);
        this.r = r;
        this.cpx = cpx;
        this.cpy = cpy;

        createStar();

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.listoInt(index), GL_STATIC_DRAW);

    }

    public void createStar()
    {
        //clear vertices
        vertices.clear();

        for (double i = 36; i < 396; i+=72)
        {
            x = cpx + r * (float)Math.cos(Math.toRadians(i));
            y = cpy + r * (float)Math.sin(Math.toRadians(i));

            vertices.add(new Vector3f((float)x, (float)y, 0.0f));
        }
        setupVAOVBO();
    }

    @Override
    public void draw()
    {
//        drawSetup();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_LINES, index.size(), GL_UNSIGNED_INT, 0);
    }
}
