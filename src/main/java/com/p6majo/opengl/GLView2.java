package com.p6majo.opengl;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.p6majo.linalg.Vector;
import com.p6majo.physics.nbody.Particle;
import com.p6majo.physics.nbody.Simulation;
import com.p6majo.utils.Boundary;
import com.p6majo.utils.Distance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;


/**
 * The class provides a JPanel with an OpenGl view included
 * <br>
 * with the option to send text to all four corners
 *<br>
 * @author p6majo
 * @version 2019-04-24
 */
public class GLView2 extends JPanel implements GLEventListener, MouseMotionListener, MouseListener {



    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private static final Color DROP_SHADOW_COLOR = new Color(0, 0, 0, 0.5f);

    private TextRenderer upperLeftRenderer;
    private TextRenderer lowerLeftRenderer;
    private TextRenderer lowerRightRenderer;
    private TextRenderer upperRightRenderer;

    private TextRenderer orderRenderer;
    private TextRenderer infoRenderer;

    private final GLCanvas canvas;
    private final Simulation simulation;


    //for mouse control
    private boolean dragging;  // is a drag operation in progress?
    private int startX, startY;  // starting location of mouse during drag
    private int previousX, previousY;    // previous location of mouse during drag
    private float xRot=0,yRot=0,zRot=0;


    /*
     *********************************************
     ***           private classes      **********
     *********************************************
     */

    class CustomRenderDelegate implements TextRenderer.RenderDelegate {
        private int dropShadowDepth;
        private Color color;

        CustomRenderDelegate(int dropShadowDepth, Color color) {
            this.dropShadowDepth = dropShadowDepth;
            this.color = color;
        }

        public boolean intensityOnly() {
            return false;
        }

        public Rectangle2D getBounds(CharSequence str, Font font, FontRenderContext frc) {
            return getBounds(str.toString(), font, frc);
        }

        public Rectangle2D getBounds(String str, Font font, FontRenderContext frc) {
            return getBounds(font.createGlyphVector(frc, str), frc);
        }

        public Rectangle2D getBounds(GlyphVector gv, FontRenderContext frc) {
            Rectangle2D stringBounds = gv.getPixelBounds(frc, 0, 0);
            return new Rectangle2D.Double(stringBounds.getX(), stringBounds.getY(), stringBounds.getWidth()
                    + dropShadowDepth, stringBounds.getHeight() + dropShadowDepth);
        }

        public void drawGlyphVector(Graphics2D graphics, GlyphVector str, int x, int y) {
            graphics.setColor(DROP_SHADOW_COLOR);
            graphics.drawGlyphVector(str, x + dropShadowDepth, y + dropShadowDepth);
            graphics.setColor(color);
            graphics.drawGlyphVector(str, x, y);
        }

        public void draw(Graphics2D graphics, String str, int x, int y) {
            graphics.setColor(DROP_SHADOW_COLOR);
            graphics.drawString(str, x + dropShadowDepth, y + dropShadowDepth);
            graphics.setColor(color);
            graphics.drawString(str, x, y);
        }
    }


    private void renderOsd(GLAutoDrawable drawable) {

        int maxModeWidth=0,maxOsdHeight = 0;

        int width = drawable.getSurfaceWidth();
        int height = drawable.getSurfaceHeight();

        maxModeWidth = (int) Math.max(maxModeWidth, upperLeftRenderer.getBounds("\u25B8").getWidth());
        maxOsdHeight = (int) Math.max(maxOsdHeight, upperLeftRenderer.getBounds("\u25B8").getHeight());


        upperLeftRenderer.beginRendering(width, height);
        upperLeftRenderer.draw("\u2759", 10, height - maxOsdHeight - 2);
        int barWidth = (int) upperLeftRenderer.getBounds("\u2759").getWidth();
        upperLeftRenderer.draw("\u2759", 10 + barWidth - 3, height - maxOsdHeight - 2);
        upperLeftRenderer.flush();
        upperLeftRenderer.endRendering();

        upperRightRenderer.beginRendering(width, height);
        upperRightRenderer.draw("\u25B8", 10, height - maxOsdHeight - 5);
        upperRightRenderer.flush();
        upperRightRenderer.endRendering();


        int y = (int) ((maxOsdHeight + 10) -lowerLeftRenderer.getBounds("What's up").getHeight() / 2);
        lowerLeftRenderer.beginRendering(width, height);
        lowerLeftRenderer.draw("Hello world", maxModeWidth + 18, height - y - 2);
        lowerLeftRenderer.flush();
        lowerLeftRenderer.endRendering();


        lowerRightRenderer.beginRendering(width, height);
        lowerRightRenderer.draw("What's up", 18, height - y - maxOsdHeight + 15);
        lowerRightRenderer.flush();
        lowerRightRenderer.endRendering();


    }



    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */
    public GLView2(int width, int height, Simulation simulation)
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



        // TODO:  Uncomment the next two lines to enable keyboard event handling
        requestFocusInWindow();

        // TODO:  Uncomment the next one or two lines to enable mouse event handling
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);

        this.add(canvas, BorderLayout.CENTER);

/*
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

*/

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
        glu.gluPerspective(80.0, 1,0.1,3*( bc.getMaxSize(2)-bc.getMinSize(2)));

       gl.glTranslatef(0.5f*(float) bc.getMinSize(0),-0.5f*(float) bc.getMinSize(1),1.5f*(float) bc.getMinSize(2));
      // gl.glTranslatef(0.5f*(float) (bc.getMaxSize(0)+bc.getMinSize(0)),0.5f*(float)(bc.getMaxSize(1)+ bc.getMinSize(1)),(float) -(bc.getMaxSize(2)-bc.getMinSize(2)));

    }


    private void setXRotation(double rot){
        System.out.println("xrotation: " + rot);
    }
    private void setYRotation(double rot){
        System.out.println("zrotation: " + rot);
    }
    private void setZRotation(double rot){
        System.out.println("yrotation: " + rot);
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

    /**
     * Draw surface sphere, where all the charges are located on
     *
     * @param gl
     * @param glu
     * @param r
     * @param lats
     * @param longs
     */
    void drawSphere(GL2 gl,GLU glu, double r, int lats, int longs) {



        GLUquadric qobj = glu.gluNewQuadric();

        gl.glColor4f(0.0f, 0.0f, 1.0f,0.01f);
        glu.gluQuadricDrawStyle(qobj, GLU.GLU_LINE);
        glu.gluQuadricNormals(qobj, GLU.GLU_SMOOTH);

        glu.gluSphere(qobj, r, 70, 70);

        /*
        int i, j;
        for(i = 0; i <= lats; i++) {
            float lat0 = (float) (Math.PI * (-0.5 + (double) (i - 1) / lats));
            float z0  = (float) (Math.sin(lat0));
            float zr0 =  (float) (Math.cos(lat0));

            float lat1 = (float) (Math.PI * (-0.5 + (double) i / lats));
            float z1 = (float) (Math.sin(lat1));
            float zr1 = (float) (Math.expm1(lat1));

            gl.glBegin(GL_LINE_STRIP);
            gl.glColor3f(0.0f, 1.0f, 1.0f);
            for(j = 0; j <= longs; j++) {
                float lng = (float) (2. * Math.PI * (double) (j - 1) / longs);
                float x = (float) (r*Math.cos(lng));
                float y = (float) (r*Math.sin(lng));



                gl.glNormal3f(x * z0, y * z0,(float)r* zr0);
               gl.glVertex3f(x * z0, y * z0, (float) r*zr0);
               //gl.glNormal3f(x * zr1, y * zr1, (float) r*z1);
               //gl.glVertex3f(x * zr1, y * zr1, (float)r*z1);
            }
            gl.glEnd();
        }
        */
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


        //light sources

        float light_diffuse0[] = {1.0f, 1.0f, 1.0f, 0.1f};  /* Red diffuse light. */
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


        //text orderRenderer
        /*
        lowerLeftRenderer = new TextRenderer(new Font("Helvetica", Font.ITALIC, 16), true, true, new CustomRenderDelegate(0,
                Color.WHITE));
        lowerLeftRenderer.setSmoothing(true);

        upperLeftRenderer = new TextRenderer(new Font("Helvetica", Font.BOLD, 65), true, true, new CustomRenderDelegate(0,
                Color.WHITE));
        lowerRightRenderer = new TextRenderer(new Font("Helvetica", Font.BOLD, 50), true, true, new CustomRenderDelegate(0,
                Color.WHITE));

        upperRightRenderer = new TextRenderer(new Font("Helvetica", Font.PLAIN, 32), true, true, new CustomRenderDelegate(0,
                Color.WHITE));
        upperRightRenderer.setSmoothing(true);
        */


        orderRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36));
        infoRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 8));

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
        gl.glRotatef(xRot,1.f,0.f,0.f);
        gl.glRotatef(yRot,0.f,1.f,0.f);
        gl.glRotatef(zRot,0.f,0.f,1.f);

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

        //drawBox(gl);

       drawSphere(gl,glu,1000,100,100);

        gl.glColor3f(0.0f, 0.0f, 1.0f);


        Particle[] particles = simulation.getParticles();
        for (int i = 0; i < particles.length; i++) {

            double x =particles[i].getPosition().getValue(0);
            double y =particles[i].getPosition().getValue(1);
            double z =particles[i].getPosition().getValue(2);

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

        Distance[][] distances = simulation.getDistances();

        for (int i = 0; i < particles.length; i++) {
            for (int j = i+1; j < particles.length; j++) {
                gl.glPushMatrix();

                Vector direction = particles[j].getPosition().add(particles[i].getPosition().mul(-1));
                double length = Math.sqrt(direction.dot(direction));

                if (distances[i][j].visible) {
                    Vector pos = particles[i].getPosition();
                    gl.glTranslated(pos.getValue(0), pos.getValue(1), pos.getValue(2));
                    Vector zAxis = new Vector( 0., 0., 1.);
                    Vector rotAxis = zAxis.toVector3D().cross(direction.toVector3D());
                    rotAxis = rotAxis.normalize();

                    double angle = Math.acos(direction.dot(zAxis) / length);
                    gl.glRotated(angle * 180 / Math.PI, rotAxis.getValue(0), rotAxis.getValue(1), rotAxis.getValue(2));
                    glu.gluCylinder(qobj, 10, 10, length, 5, 5);

                    gl.glPopMatrix();
                }
            }
        }


        //draw cylinder from first particle to second



        //text

       // gl.glClear(GL.GL_COLOR_BUFFER_BIT);
       // gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //renderOsd(drawable);
       // gl.glFinish();

        int width = drawable.getSurfaceWidth();
        int height = drawable.getSurfaceHeight();

        orderRenderer.beginRendering(width, height);
        // optionally set the color
        orderRenderer.setColor(1.0f, 0.2f, 0.2f, 0.8f);
        orderRenderer.draw(simulation.toString(),100,100);
        orderRenderer.draw("Energy: "+simulation.getOrderParameter()+"", 100,70);
        // ... more draw commands, color changes, etc.
        orderRenderer.endRendering();

        infoRenderer.beginRendering(width, height);
        // optionally set the color
        infoRenderer.setColor(0.0f, 1.f, 0.f, 0.5f);
        String[] info = simulation.getInfo();
        for (int i = 0; i < info.length; i++)
            infoRenderer.draw(info[i], 1500,1300-8*i);
        // ... more draw commands, color changes, etc.
        infoRenderer.endRendering();

        gl.glFlush();


    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (dragging) {
            return;  // don't start a new drag while one is already in progress
        }
        int x = e.getX();
        int y = e.getY();
        // TODO: respond to mouse click at (x,y)
        dragging = true;  // might not always be correct!
        previousX = startX = x;
        previousY = startY = y;
        canvas.repaint();    //  only needed if display should change
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (! dragging) {
            return;
        }
        dragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if (! dragging) {
            return;
        }

        int dx = e.getX() - previousX;
        int dy = e.getY()-previousY;

        if (e.getModifiers() ==MouseEvent.BUTTON1_MASK) {
            xRot = xRot - dy;
            yRot = yRot - dx;
        }
        else if (e.getModifiers()==MouseEvent.BUTTON3_MASK) {
            xRot=xRot - dy;
            zRot = zRot - dx;
        }

        // TODO:  respond to mouse drag to new point (x,y)

        previousX = e.getX();
        previousY = e.getY();

        canvas.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

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
