package com.p6majo.povrayanimation;

import com.p6majo.logger.Logger;
import com.p6majo.utils.OSValidator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

/**
 * The class PovrayGenerationThread
 *
 * @author com.p6majo
 * @version 2020-05-29
 */
public class VideoGenerationThread extends Thread {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */


    private String imgPath;
    private String videoPath;
    private String name;
    private int fps;
    private int maxdigits;
    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */


    public VideoGenerationThread(String videoPath, String imgPath, String name, int fps, int maxdigits){
        super();
        this.videoPath = videoPath;
        this.imgPath = imgPath;
        this.name = name;
        this.fps = fps;
        this.maxdigits = maxdigits;

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


    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */


    @Override
    public void start(){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(this.imgPath);


        //remove existing file
        File file = new File(url.getPath()+"/"+name+".*");
        if (file!=null)
            file.delete();

        //generate command
        String command = "";
        String[] params=null ;
        if (OSValidator.isWindows()) {
            command = "ffmpeg";
            String winPath = PovrayAnimation.generateWindowsPath(url);
          //  params = new String[]{command,"-framerate",fps+"","-i", winPath+"%0"+maxdigits+"d.png","-pix_fmt","yuv420p",winPath + name+".mp4"};
            params = new String[]{command,"-i", winPath+"%0"+maxdigits+"d.png","-pix_fmt","yuv420p",winPath + name+".mp4"};
        }
        else if (OSValidator.isUnix()) {
            command = "ffmpeg";
            //params = new String[]{command,"-framerate",fps+"","-pattern_type","glob","-i",url.getPath()+"/"+name+"*.png",url.getPath()+"/"+name+".gif"};
            params = new String[]{command,"-f","image2","-i",url.getPath()+"/"+name+"_%0"+maxdigits+"d.png","-framerate",fps+"","-vcodec", "h264","-pix_fmt","yuv420p","-f","mp4",url.getPath()+"/"+name+".mp4"};
        }

        boolean result = false;

        try {
            StringBuffer response = new StringBuffer();
            StringBuffer errorStr = new StringBuffer();

            ProcessBuilder processBuilder = new ProcessBuilder(params);//.redirectErrorStream(true);
            Logger.logging(Logger.Level.info,"Staring process for the video generation",this);
            Process p = processBuilder.start();


            try {
                while(p.isAlive()){
                    Thread.sleep(100);//for some reason ffmpeg does not finish, so the process is interrupted manually
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Logger.logging(Logger.Level.warning, ex.getMessage(), this);
            }

        }catch (IOException e) {
            e.printStackTrace();
            Logger.logging(Logger.Level.error,e.getMessage(),this);
        }

        URL url2 = loader.getResource(this.videoPath);

        try {
            Files.copy(new File(url.getPath()+"/"+name+".mp4").toPath(),new File(url2.getPath()+"/"+name+".mp4").toPath());
        } catch (IOException e) {
            e.printStackTrace();
            Logger.logging(Logger.Level.warning,"Copying of video file failed.");
        }

        File video = new File(url2.getPath() + "/" + name + ".mp4");
            try {
                Desktop.getDesktop().open(video);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.logging(Logger.Level.error, e.getMessage(), this);
            }

    }



    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return "Animation thread";
    }



}
