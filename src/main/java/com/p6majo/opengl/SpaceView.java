package com.p6majo.opengl;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Particle;
import com.p6majo.octtree.Vector3D;
import com.p6majo.particleSimulation3D.SpaceSimulation;
import com.p6majo.utils.Boundary;
import com.p6majo.utils.Distance;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;


/**
 * The class provides a JPanel with an OpenGl view included
 * <br>
 * with the option to send text to all four corners
 *<br>
 * @author p6majo
 * @version 2019-04-24
 */
public class SpaceView extends JPanel implements GLEventListener, MouseMotionListener, MouseListener {



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
    private final SpaceSimulation simulation;

    private GLU glu;


    //for mouse control
    private boolean dragging;  // is a drag operation in progress?
    private int startX, startY;  // starting location of mouse during drag
    private int previousX, previousY;    // previous location of mouse during drag
    private float xRot=0,yRot=0,zRot=0;

    private int windowWidth, windowHeight;
    private double zoom=85;
    private double cameraOutPos=0;
    private double nearShift=3000;

    private boolean debug = true;


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
    public SpaceView(int width, int height, SpaceSimulation simulation)
    {
        super(new BorderLayout());

        this.windowHeight = height;
        this.windowWidth =width;
        this.simulation = simulation;

        //getting the capabilities object of GL2 profile
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // The canvas
        canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(this);
        setSize(width,height);

        // Uncomment the next two lines to enable keyboard event handling
        requestFocusInWindow();

        // Uncomment the next one or two lines to enable mouse event handling
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);

        this.add(canvas, BorderLayout.CENTER);

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


    public void setNearShift(double nearShift){
        this.nearShift = nearShift;
    }

    public void setZoom(double zoom){
        this.zoom = zoom;
    }

    public void setCameraOutPos(double cameraOutPos){
        this.cameraOutPos = cameraOutPos;
    }
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



    private void setCamera(GL2 gl)
    {

        gl.glViewport(0, 0, windowWidth, windowHeight);


        //setup ModelView (camera position)
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        Cuboid container = simulation.getContainer();
        Vector3D low = container.getLow();
        Vector3D high = container.getHigh();
        Vector3D middle = container.getMiddle();

        //Setup Projection
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(zoom, windowWidth/Math.max(1,windowHeight),0.1,3*container.getWidth());


        //Vector3D eye = new Vector3D(x, y,0);
        //Vector3D eye = new Vector3D(-1000, -1000,-1000);

        if (debug){
            debug = false;
            System.out.println("Mid-Point: "+middle);
           // System.out.println("Eye-Point: "+eye);

        }

         //eyepoint (x,y,z),lookatpoint (x,y,z), updirection (x,y,z)
        //move eyepoint 50 percent of the total depth before the box
      // glu.gluLookAt(eye.getX(),eye.getY(),eye.getZ(), middle.getX(), middle.getY(), middle.getZ(), 0, 0, 1);

        glu.gluLookAt(low.getX()-cameraOutPos/10, 0, 0,
                middle.getX(),middle.getY(),middle.getZ(),
                0,  1,0);



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
      Vector3D low = simulation.getContainer().getLow();
      Vector3D high = simulation.getContainer().getHigh();
        
        gl.glColor3f(1.0f, 0.0f, 0.0f);

        //  __
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) low.getX(), (float) low.getY(), (float) low.getZ());
        gl.glVertex3f((float) high.getX(), (float) low.getY(), (float) low.getZ());
        gl.glEnd();

        //  __|
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) low.getX(), (float) low.getY(), (float) low.getZ());
        gl.glVertex3f((float) low.getX(), (float) high.getY(), (float) low.getZ());
        gl.glEnd();

        //  __|/
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) low.getX(), (float) low.getY(), (float) low.getZ());
        gl.glVertex3f((float) low.getX(), (float) low.getY(), (float) high.getZ());
        gl.glEnd();

        // --
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) high.getX(), (float) high.getY(), (float) high.getZ());
        gl.glVertex3f((float) low.getX(), (float) high.getY(), (float) high.getZ());
        gl.glEnd();

        // |--
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) high.getX(), (float) high.getY(), (float) high.getZ());
        gl.glVertex3f((float) high.getX(), (float) low.getY(), (float) high.getZ());
        gl.glEnd();

        // /|--
        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) high.getX(), (float) high.getY(), (float) high.getZ());
        gl.glVertex3f((float) high.getX(), (float) high.getY(), (float) low.getZ());
        gl.glEnd();


        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) high.getX(), (float) low.getY(), (float) low.getZ());
        gl.glVertex3f((float) high.getX(), (float) high.getY(), (float) low.getZ());
        gl.glEnd();


        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) low.getX(), (float) high.getY(), (float) low.getZ());
        gl.glVertex3f((float) high.getX(), (float) high.getY(), (float) low.getZ());
        gl.glEnd();


        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) low.getX(), (float) low.getY(), (float) high.getZ());
        gl.glVertex3f((float) low.getX(), (float) high.getY(), (float) high.getZ());
        gl.glEnd();


        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) high.getX(), (float) low.getY(), (float) low.getZ());
        gl.glVertex3f((float) high.getX(), (float) low.getY(), (float) high.getZ());
        gl.glEnd();

        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) low.getX(), (float) high.getY(), (float) low.getZ());
        gl.glVertex3f((float) low.getX(), (float) high.getY(), (float) high.getZ());
        gl.glEnd();

        gl.glBegin (GL2.GL_LINES);
        gl.glVertex3f((float) low.getX(), (float) low.getY(), (float) high.getZ());
        gl.glVertex3f((float) high.getX(), (float) low.getY(), (float) high.getZ());
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

    /**
     * Called back immediately after the OpenGL context is initialized. Can be used to perform one-time initialization. Run only once.
     *
     * @param drawable
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2(); //get the OpenGL graphics context
        glu = new GLU(); //get GL Utilities
       // glu = GLU.createGLU();

        //light sources

        float light_diffuse0[] = {1.0f, 1.0f, 1.0f, 0.1f};  // Red diffuse light.
        float light_diffuse1[] = {0.0f, 1.0f,0.0f,1.0f};  // green diffuse light.
        float light_diffuse2[] = {0.0f, 0.0f,1.0f,1.0f};  // blue diffuse light.

        float light_position0[] = {1.0f, 1.0f, 1.0f, 0.0f};  // Infinite light location.
        float light_position1[] = {1.0f, 1.0f, 1.0f, 1.0f};  // Infinite light location.
        float light_position2[] = {1.0f, 0.0f, 1.0f, 0.0f};  // Infinite light location.

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

        //Enable smooth shading, which blends colors nicely, and smoothes out lighting
        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);  //set background (clear) color

        gl.glEnable(GL.GL_CULL_FACE);

        gl.glClearDepth(1.0f); //clear z-buffer to the farthest
        gl.glEnable(GL.GL_DEPTH_TEST);//make sure that farther objects are hidden by closer ones
        gl.glDepthFunc(GL.GL_LEQUAL);

        //Do the best perspective correction
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT,GL.GL_NICEST);




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

    /**
     * Called back by the animator to perform rendering
     * @param drawable
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2(); //get the OpenGL 2 graphics context
         setCamera(gl);

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); //clear color and depth buffers

        //start

        gl.glTranslated(0, 0, 0);
        gl.glRotatef(xRot, 1.f, 0.f, 0.f);
        gl.glRotatef(yRot, 0.f, 1.f, 0.f);
        gl.glRotatef(zRot, 0.f, 0.f, 1.f);

        GLUquadric qobj = glu.gluNewQuadric();

        // spotlight as it moves with the scene
        float spot_position[] = {-400.0f, 500.0f, 250.f, 1.0f};
        float spot_direction[] = {1.0f, 0.0f, .0f};
        float spot_angle = 20.0f;
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, spot_position, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, spot_direction, 0);
        gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, (float) spot_angle);
        gl.glLighti(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, 10);

        // spotlight as it moves with the scene
        float spot_position2[] = {-400.0f, 500.0f, 750.f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, spot_position2, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, spot_direction, 0);
        gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_CUTOFF, (float) spot_angle);
        gl.glLighti(GL2.GL_LIGHT2, GL2.GL_SPOT_EXPONENT, 10);

        drawBox(gl);

       // drawSphere(gl, glu, 1000, 100, 100);

        gl.glColor3f(0.0f, 0.0f, 1.0f);


        for (Particle particle : simulation.getParticles()) {


            double x = particle.getPosition().getX();
            double y = particle.getPosition().getY();
            double z = particle.getPosition().getZ();

            gl.glColor3f(0f, (float) z / 1000, (float) (1 - z / 1000));


            gl.glPushMatrix();
            gl.glTranslated(x, y, z);
            glu.gluQuadricDrawStyle(qobj, GLU.GLU_FILL);
            glu.gluQuadricNormals(qobj, GLU.GLU_SMOOTH);

            glu.gluSphere(qobj, Math.pow(particle.getMass(),0.33)*10, 10, 10);
            gl.glPopMatrix();
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
        orderRenderer.draw(String.format("Time: %.2f",simulation.getTime()), 100,70);
        // ... more draw commands, color changes, etc.
        orderRenderer.endRendering();

        gl.glFlush();


    }

    /**
     * Call-back handler for window re-size event. Also called when the drawable is first set to visible.
     *
     * @param drawable
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        /*
        GL2 gl = drawable.getGL().getGL2(); //get the OpenGL 2 graphics context
        if(height==0) height=1;//prevent divide by zero
        float aspect = (float) width/height;

        //Set the view port (display area) to cover the entire window
        gl.glViewport(0,0,width,height);

        //setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION); //choose projection matrix
        gl.glLoadIdentity(); //reset projection matrix
        glu.gluPerspective(85.0,aspect,900,2000);

        //enable the model-view transformation
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); //reset
         */
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
