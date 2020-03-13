package com.p6majo.opengl;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.p6majo.physics.nbody.Particle;
import com.p6majo.physics.nbody.Simulation;
import com.p6majo.utils.Boundary;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

/**
 * The class provides a JPanel with an OpenGl view included
 *
 * @author p6majo
 * @version 2019-04-24
 */
public class GLView extends JPanel implements GLEventListener {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

private final GLCanvas canvas;
private final Simulation simulation;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */
    public GLView(int width, int height, Simulation simulation)
    {
        super(new BorderLayout());

        this.simulation = simulation;
        System.out.println(simulation.getBoundary());

        //getting the capabilities object of GL2 profile
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // The canvas
        canvas = new GLCanvas(capabilities);

       // Lektion3View view = new Lektion3View();
        canvas.addGLEventListener(this);
        setSize(width,height);

        this.add(canvas, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });


    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public GLCanvas getCanvas(){
        return canvas;
    }

    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */



    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */


    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */

    private void setCamera(GL2 gl, GLU glu)
    {
        Boundary bc = simulation.getBoundary();
        int w = (int) (bc.getMaxSize(0)-bc.getMinSize(0));
        int h = (int) (bc.getMaxSize(1)-bc.getMinSize(1));

        gl.glViewport(0, 0, w, h);


        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        //opening angle, ratio of height and width, near and far
        glu.gluPerspective(80.0, 1,0.1,2*( bc.getMaxSize(2)-bc.getMinSize(2)));

        gl.glTranslatef(0.5f*(float) bc.getMinSize(0),-0.5f*(float) bc.getMinSize(1),1.5f*(float) bc.getMinSize(2));
        //gl.glTranslatef(0.5f*(float) (bc.getMaxSize(0)+bc.getMinSize(0)),0.5f*(float)(bc.getMaxSize(1)+ bc.getMinSize(1)),(float) -(bc.getMaxSize(2)-bc.getMinSize(2)));

    }

    private void drawBox(GL2 gl){
        Boundary bd = simulation.getBoundary();
        gl.glColor3f(1.0f, 0.0f, 0.0f);

        //  __
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMinSize(1), (float) bd.getMinSize(2));
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMinSize(1), (float) bd.getMinSize(2));
        gl.glEnd();

        //  __|
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMinSize(1), (float) bd.getMinSize(2));
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMaxSize(1), (float) bd.getMinSize(2));
        gl.glEnd();

        //  __|/
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMinSize(1), (float) bd.getMinSize(2));
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMinSize(1), (float) bd.getMaxSize(2));
        gl.glEnd();

        // --
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMaxSize(1), (float) bd.getMaxSize(2));
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMaxSize(1), (float) bd.getMaxSize(2));
        gl.glEnd();

        // |--
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMaxSize(1), (float) bd.getMaxSize(2));
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMinSize(1), (float) bd.getMaxSize(2));
        gl.glEnd();

        // /|--
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMaxSize(1), (float) bd.getMaxSize(2));
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMaxSize(1), (float) bd.getMinSize(2));
    gl.glEnd();


        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMinSize(1), (float) bd.getMinSize(2));
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMaxSize(1), (float) bd.getMinSize(2));
        gl.glEnd();


        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMaxSize(1), (float) bd.getMinSize(2));
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMaxSize(1), (float) bd.getMinSize(2));
        gl.glEnd();


        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMinSize(1), (float) bd.getMaxSize(2));
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMaxSize(1), (float) bd.getMaxSize(2));
        gl.glEnd();


        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMinSize(1), (float) bd.getMinSize(2));
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMinSize(1), (float) bd.getMaxSize(2));
        gl.glEnd();

        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMaxSize(1), (float) bd.getMinSize(2));
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMaxSize(1), (float) bd.getMaxSize(2));
        gl.glEnd();

        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) bd.getMinSize(0), (float) bd.getMinSize(1), (float) bd.getMaxSize(2));
        gl.glVertex3f((float) bd.getMaxSize(0), (float) bd.getMinSize(1), (float) bd.getMaxSize(2));
        gl.glEnd();


    }

    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();

        float light_diffuse0[] = {1.0f, 0.0f, 0.0f, 0.1f};  /* Red diffuse light. */
        float light_diffuse1[] = {0.0f, 1.0f,0.0f,1.0f};  /* green diffuse light. */
        float light_diffuse2[] = {0.0f, 0.0f,1.0f,1.0f};  /* blue diffuse light. */

        float light_position0[] = {1.0f, 1.0f, 1.0f, 0.0f};  /* Infinite light location. */
        float light_position1[] = {1.0f, 1.0f, 1.0f, 1.0f};  /* Infinite light location. */
        float light_position2[] = {1.0f, 0.0f, 1.0f, 0.0f};  /* Infinite light location. */



        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glEnable(GL.GL_CULL_FACE);

        gl.glEnable(GL.GL_DEPTH_TEST);//make sure that farther objects are hidden by closer ones
        gl.glDepthFunc(GL.GL_LEQUAL);
        setCamera(gl, glu);

        gl.glMatrixMode(GL2.GL_MODELVIEW);

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_diffuse0,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position0,0);
        gl.glEnable(GL2.GL_LIGHT0);


        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, light_diffuse1,0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, light_position1,0);
        gl.glEnable(GL2.GL_LIGHT1);

        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, light_diffuse2,0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, light_position2,0);
        gl.glEnable(GL2.GL_LIGHT2);
        gl.glEnable(GL2.GL_LIGHTING);

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();

        //eyepoint (x,y,z),lookatpoint (x,y,z), updirection (x,y,z)
        glu.gluLookAt(500, 500, -700,
                500, 500, 1500,
                0, 1, 0);

        //start
        gl.glTranslated(0, 0, 0);


        GLUquadric qobj = glu.gluNewQuadric();

        // spotlight as it moves with the scene
        float spot_position[] =  {-400.0f,500.0f,250.f,1.0f};
        float spot_direction[] = {1.0f,0.0f,.0f};
        float spot_angle=20.0f;
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION,  spot_position,0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION,spot_direction,0);
        gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF,(float)spot_angle);
        gl.glLighti(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, 10);

        // spotlight as it moves with the scene
        float spot_position2[] =  {-400.0f,500.0f,750.f,1.0f};
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION,  spot_position2,0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION,spot_direction,0);
        gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_CUTOFF,(float)spot_angle);
        gl.glLighti(GL2.GL_LIGHT2, GL2.GL_SPOT_EXPONENT, 10);

       drawBox(gl);

        gl.glColor3f(0.0f, 0.0f, 1.0f);


        Particle[] particles = simulation.getParticles();
        for (int i = 0; i < particles.length; i++) {

            double x =particles[i].getPosition().get(0);
            double y =particles[i].getPosition().get(1);
            double z =particles[i].getPosition().get(2);

            if (particles[i].isMarked())
                    gl.glColor3f(1.f,0,0);
           // else if (particles[i].isApproaching())
           //     gl.glColor3f(1.f,1.f,0);
            else gl.glColor3f(0f,(float) z/1000,(float) (1-z/1000));


            gl.glPushMatrix();
            gl.glTranslated(x, y, z);
            glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL);
            glu.gluQuadricNormals(qobj, GLU.GLU_SMOOTH);

            glu.gluSphere(qobj, particles[i].getRadius(), 30, 30);
            gl.glPopMatrix();

        }


    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }


    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return "opengl capable view";
    }



}
