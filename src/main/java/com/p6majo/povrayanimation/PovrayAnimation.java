package com.p6majo.povrayanimation;

import com.p6majo.dynamicpolyhedra.SimulationView;
import com.p6majo.linalg.Vector3D;
import com.p6majo.logger.Logger;
import com.p6majo.octtree.Particle;
import com.p6majo.povrayanimation.camera.PovRayCamera;
import com.p6majo.povrayanimation.lights.PovRayLight;
import com.p6majo.povrayanimation.objects.PovrayObject;
import com.p6majo.utils.OSValidator;
import javafx.scene.media.Media;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static java.awt.Color.WHITE;

/**
 * The class PovrayAnimation
 *
 * @author p6majo
 * @version 2021-02-06
 */
public abstract class PovrayAnimation implements SimulationView {

    /*
     *********************************************
     ***           Statics              **********
     *********************************************
     */

    private static String PATH = "povrayanimations";
    private static String TMPPATH = PATH+"/tmp";

    public static String generateWindowsPath(URL url){
        String path = url.getPath();
        StringTokenizer tokenizer = new StringTokenizer(path,"/");
        String windowsPath = "";
        while(tokenizer.hasMoreTokens()){
            String token = tokenizer.nextToken();
            if (token.length()>0)
                windowsPath+=token+"\\";
        }
        return windowsPath;
    }

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private String name;
    private int duration;
    private int fps;

    private int width;
    private int height;
    private double dim;

    private List<PovRayLight> lights;

    private PovRayCamera camera;

    private Icon animation;
    private Media animationMedia;

    private int maxdigits; //flag needed for windows to combine frames to movie

    private boolean redraw=false;

    private int frameCounter = 0;
    private int frames;

    private int shiftX=0;
    private int shiftY=0;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    /**
     * The Animation is defined by its name, which is the keyword for the file
     * and the frames that are generated.
     * @param name characterization of the animation
     * @param duration duration of the animation in seconds
     * @param fps frames per second in Hz
     * @param width
     * @param height
     * @param dim
     */
    public PovrayAnimation(String name, int duration, int fps, int width, int height, double dim) {
        this.name = name;
        this.duration = duration;
        this.fps = fps;
        this.frames = duration*fps;

        //width and height have to be even to work with the codec h264, which is compatible with javafx
        this.width = width%2==0?width:width-1;
        this.height = height%2==0?height:height-1;

        this.dim = dim;

        //camera = new CameraDefault(dim,(double) width/height,3);
        lights = new ArrayList<>();

    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void setStandardLight(){
        this.addLight(new PovRayLight(new Vector3D(2*dim,2*dim,2*dim),WHITE));
        this.addLight(new PovRayLight(new Vector3D(-2*dim,2*dim,2*dim),WHITE));
        this.addLight(new PovRayLight(new Vector3D(-2*dim,-2*dim,2*dim),WHITE));
        this.addLight(new PovRayLight(new Vector3D(2*dim,-2*dim,2*dim),WHITE));
    }

    public void setSingleLight(){
        this.addLight(new PovRayLight(new Vector3D(2*dim,2*dim,2*dim),WHITE));
        this.addLight(new PovRayLight(new Vector3D(-2*dim,2*dim,2*dim),WHITE));
        this.addLight(new PovRayLight(new Vector3D(-1.1*dim,-1.1*dim,1.1*dim),WHITE));
        this.addLight(new PovRayLight(new Vector3D(2*dim,-2*dim,2*dim),WHITE));
    }

    public void addLight(PovRayLight light){
        this.lights.add(light);
    }

    public void setCamera(PovRayCamera camera){
        this.camera = camera;
    }

    public void setShiftX(int shiftX){ this.shiftX=shiftX; }
    public void setShiftY(int shiftY){ this.shiftY=shiftY; }
    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */


    /**
     *
     * Show the animation that belongs to this PovrayAnimation
     * If it is created already it is just loaded and returned
     *<br>
     * otherwise it is created
     *
     * @return the animated gif-file as Icon

    public Icon showAnimation(){
    if (!redraw){
    if (animation!=null)
    return animation;
    else {
    animation = load();
    if (animation != null)
    return animation;
    }
    }
    //redraw
    return generateImages();

    }
     */

    /**
     * Show the animation that belongs to this PovrayAnimation
     * If it is created already it is just loaded and returned
     *<br>
     * otherwise null is returned
     *
     * @return the movie media file
     */
    public Media getMovie(){
        if (!redraw){
            if (animationMedia!=null)
                return animationMedia;
            else {
                animationMedia = loadMedia();
                if (animationMedia != null)
                    return animationMedia;
            }
        }
        //redraw
        // generateImages();
        //animationMedia=loadMedia();


        return animationMedia;
    }

    /**
     * Enforces the recreation of the PovrayAnimation, even if the file is found inside the resources.
     * This should be used, if changes are made during the construction process.
     *
     */
    public void redraw() {
        this.animation = null;
        this.redraw = true;
    }

    //special callback for linux
    public void renderCallback(){
        redraw = false;
        generateAnimation();
    }

    /**
     * Called once povray has finished its business
     * This call can be used to make after povray modifications
     * The generation of the animation is triggered from here
     * @param frame
     */
    public void renderCallback(int frame, String path, String name){

        frameCounter++;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(path+"/"+name+".png");

        /*
        try {
            BufferedImage image = ImageIO.read(url);
            for (AnimatedObject animatedObject : animatedObjects) {
                animatedObject.setInterpolator((double) frame/(frames-1)); //make sure it goes from 0 to 1
                animatedObject.draw();
            }

            Canvas canvas = tutorial.getCanvas();
            tutorial.getCanvas().picture(canvas.getWidth()/2+shiftX,canvas.getHeight()/2+shiftY,image);
            canvas.update();
            canvas.save(url.getPath());

        } catch (IOException e) {
            e.printStackTrace();
            Logger.logging(Logger.Level.error,"Povray image could not be loaded "+name,this);
        }
    */

        if (frameCounter==frames) {
            redraw = false;
            generateAnimation();
            // cleanup();
        }
    }


    /**
     *
     */
    public void generateFrame(List<PovrayObject> objects) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(TMPPATH);

        //generate all frames
        int frames = duration*fps;
        double ds = 1./(frames-1);

        FileWriter commandFile = null;
        PrintWriter commandWriter=null;
        try {
            commandFile = new FileWriter(url.getPath()+"/run.sh",true);
            commandWriter=new PrintWriter(commandFile);
            if (frameCounter==0){
                commandWriter.write("#!/bin/bash\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.logging(Logger.Level.error,e.getMessage(),this.getClass());
        }

        String nameString = createPaddedName(frameCounter, frames);

        File fileChecker = new File(url.getPath()+"/"+nameString+".pov");
        if (!fileChecker.exists()) {

            //build povray file
            StringBuilder povFileBuilder = new StringBuilder();
            povFileBuilder.append(createHeader());
           // this.camera.setState(ds * frameCounter); //rotation of the camera is unaffected by the interpolation function
           // povFileBuilder.append(this.camera.draw());

            for (PovRayLight light : lights) {
                povFileBuilder.append(light.draw());
            }

            for (PovrayObject object : objects) {
                povFileBuilder.append(object.draw());
            }
           // povFileBuilder.append(new CoordinateSystem(100).draw());
            povFileBuilder.append(createFooter());

            //try to save it
            File file = new File(url.getPath() + "/" + nameString + ".pov");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.logging(Logger.Level.error, e.getMessage(), this);
            }

            PrintWriter writer = null;

            try {
                writer = new PrintWriter(file, "UTF-8");
                writer.write(povFileBuilder.toString());
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Logger.logging(Logger.Level.error, e.getMessage(), this);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Logger.logging(Logger.Level.error, e.getMessage(), this);
            }

        }

        fileChecker = new File(url.getPath() + "/" + nameString + ".png");
        if (!fileChecker.exists()) {
            commandWriter.append("povray -Dx +W" + this.width+" +A +H"+this.height+" "+url.getPath() + "/" + nameString + ".pov >/dev/null 2>1\n");//redirect output to /dev/null see also https://stackoverflow.com/questions/3285408/java-processbuilder-resultant-process-hangs
        } else {
            System.out.println(frameCounter + " already created.");
            if (frameCounter == frames - 1) {
                generateAnimation();
            }
        }

        commandWriter.close();
        frameCounter++;
    }

    public void finish(){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(TMPPATH);

        PovrayRenderingThread thread = new PovrayRenderingThread(url.getPath(),this);
        try {
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logging(Logger.Level.error,e.getMessage(),this.getClass());
        }
    }

    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */


    private void copyfromPrevious(int frame,int frames){
        String newFileName = createPaddedName(frame,frames);
        String oldFileName = createPaddedName(frame-1,frames);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(TMPPATH);

        File newFile = new File(url.getPath()+"/"+newFileName+".png");
        File oldFile =new File(url.getPath()+"/"+oldFileName+".png");

        if (oldFile.exists()){
            try {
                Files.copy(oldFile.toPath(),newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.logging(Logger.Level.warning,e.getMessage(),this);
            }
        }

    }

    private String createPaddedName(int frame, int frames){
        //prepare padding of zeros
        String framesString = frames+"";
        maxdigits = framesString.length();
        String frameString = frame+"";
        int digits = frameString.length();
        String padding = "";
        for (int i = digits; i < maxdigits; i++)
            padding+="0";

        String nameString ="";
        if (OSValidator.isWindows()) {
            nameString = padding+frameString;
        }
        else if (OSValidator.isUnix()) {
            nameString = name+"_"+padding+frameString;
        }
        return nameString;
    }


    /*
    private Icon load(){
        Icon icon = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(PATH+"/"+name+".gif");
        if (url!=null)
            return new ImageIcon(url);
        else
            return null;
    }
     */

    private Media loadMedia(){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(PATH+"/"+name+".mp4");
        if (url!=null) {
            Media media = null;
            if (OSValidator.isWindows())
                media = new Media(url.getFile().substring(1));
            else
                media =  new Media(url.toString());

/*
            File video = new File(url.getPath());
            try {
                Desktop.getDesktop().open(video);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.logging(Logger.Level.error, e.getMessage(), this);
            }
*/
            return media;
        }
        else
            return null;
    }

    private String createHeader( ){
        StringBuilder header=new StringBuilder();
        header.append("//This is an automatically generated povray script\n")
                .append("//Generated by the PovRayCanvas.\n")
                .append("#version 3.6\n")
                .append("#include \"colors.inc\"\n")
                .append("#include \"textures.inc\"\n")
                .append("\n")
                .append("global_settings {assumed_gamma 1.0}\n")
                .append("#default{ finish{ambient 0.1 diffuse 0.9}}\n\n")
                .append("camera{ location  <0.0 , 100.0 ,-600.0>\n")//<0.0 , 100.0 ,-600.0> //close up <0,20,-600> oder <0,0,-600>
                .append("\tlook_at   <0.0 , 75.0 , 0.0>\n")  //standard 0,75,0  //close up <0,70,0> oder <0,5,0>
                .append("\tright x*image_width/image_height\n")
                .append("\tangle 25\n")//standard 25 //closup 10
                .append("}\n\n")
                .append("light_source{<1500,3000,-2500> color White}\n")
                .append("plane{ <0,1,0>,1 hollow\n")
                .append("\ttexture{\n")
                .append("\t\tpigment{ bozo turbulence 0.92\n")
                .append("\t\tcolor_map{\n")
                .append("\t\t\t[0.00 rgb<0.05,0.15,0.45>]\n")
                .append("\t\t\t[0.50 rgb<0.05,0.15,0.45>]\n")
                .append("\t\t\t[0.70 rgb<1,1,1>        ]\n")
                .append("\t\t\t[0.85 rgb<0.2,0.2,0.2>  ]\n")
                .append("\t\t\t[1.00 rgb<0.5,0.5,0.5>  ]\n")
                .append("\t\t} //\n")
                .append("\t\tscale<1,1,1.5>*10\n")
                .append("\t\ttranslate<0,0,0>\n")
                .append("\t} // end of pigment\n")
                .append("\tfinish {ambient 1 diffuse 0}\n")
                .append("\t} // end of texture\n")
                .append("\tscale 10000}\n")
                .append("// fog on the ground -----------------------\n")
                .append("        fog { fog_type   2\n")
                .append("            distance   50\n")
                .append("            color      rgb<1,1,1>*0.8\n")
                .append("            fog_offset 0.1\n")
                .append("            fog_alt    1.5\n")
                .append("            turbulence 1.8\n")
                .append("        } //\n")
                .append("// ground ----------------------------------\n")
                .append("        plane{ <0,1,0>, 0\n")
                .append("            texture{\n")
                .append("                pigment{ color rgb<0.22,0.45,0>}\n")
                .append("                normal { bumps 0.75 scale 0.015 }\n")
                .append("                finish { phong 0.1 }\n")
                .append("            } // end of texture\n")
                .append("        } // end of plane\n");
        return header.toString();
    }

    private String createFooter(){
        String footer="";
        return footer;
    }

    public void generateAnimation(){
        VideoGenerationThread thread =new VideoGenerationThread(PATH,TMPPATH,name,fps,maxdigits);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.logging(Logger.Level.error,e.getMessage(),this);
        }
    }

    private void cleanup(){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(TMPPATH);
        File folder = new File(url.getPath());
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.getName().endsWith("pov")||file.getName().endsWith("png"))
                file.delete();
        }

    }



    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return "Animation of "+duration+" ms "+" and size "+width+"x"+height;
    }


    @Override
    public abstract void draw(double time, List<Particle> particles);
}

