package net.codentonic.gradientlabyrinthgl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

    private Circle circle;
    public boolean objectsReady = false;
    public Circle getCircle() {
        return circle;
    }

    private Circle[] holes = new Circle[3];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.9f, 0.9f,0.9f,1f);
        try{
            circle = new Circle(0,0, 0.1f, 55);
            float min = -1;
            float max = 1;

            Random rnd = new Random();
            holes[0] = new Circle(
                    min + rnd.nextFloat() * (max - min),
                    min + rnd.nextFloat() * (max - min),
                    0.1f, 55);
            holes[1] = new Circle(min + rnd.nextFloat() * (max - min),
                    min + rnd.nextFloat() * (max - min),
                    0.12f, 55);
            holes[2] = new Circle(min + rnd.nextFloat() * (max - min),
                    min + rnd.nextFloat() * (max - min),
                    0.14f, 55);

            objectsReady = true;
        }catch (Exception e){
           System.out.println(e.getMessage());
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        holes[0].draw(new float[]{0.9f, 0.3f, 0.1f, 1f});
        holes[1].draw(new float[]{0.7f, 0.4f, 0.1f, 1f});
        holes[2].draw(new float[]{0.5f, 0.5f, 0.1f, 1f});
        circle.draw();
    }
}
