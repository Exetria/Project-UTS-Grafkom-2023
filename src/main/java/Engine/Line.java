package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Line extends Objects
{

    public Line(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color) {
        super(shaderModuleDataList, vertices, color);
    }

    public Line(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, List<Vector3f> verticesColor) {
        super(shaderModuleDataList, vertices, verticesColor);
    }

    @Override
    public void draw(Camera camera, Projection projection)
    {
        drawSetup(camera, projection);
        glLineWidth(1);
        glPointSize(0);
        glDrawArrays(GL_LINE_STRIP, 0, vertices.size());
        for (Objects i: childObjects)
        {
            i.draw(camera, projection);
        }
    }
}
