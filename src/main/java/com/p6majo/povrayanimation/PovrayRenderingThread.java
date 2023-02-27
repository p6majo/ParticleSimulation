package com.p6majo.povrayanimation;

import com.p6majo.logger.Logger;
import java.io.IOException;
import java.net.URL;

import static com.p6majo.logger.Logger.Level.info;

/**
 * The class PovrayGenerationThread
 *
 * @author com.p6majo
 * @version 2020-05-29
 */
public class PovrayRenderingThread extends Thread {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */


    private String path;
    private PovrayAnimation caller;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */


    public PovrayRenderingThread(String path, PovrayAnimation caller){
        this.path = path;
        this.caller = caller;
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
    public void start()  {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(this.path);

        //generate command
        String command = "";
        String[] params = null;

        command = "chmod";
        params = new String[]{command,"+777",path+"/run.sh"};

        try {
            long start = System.currentTimeMillis();
            ProcessBuilder processBuilder = new ProcessBuilder(params);
            Process p =processBuilder.start();
            Logger.logging(info,"Change rights ... ",this.getClass());

            while (p.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Logger.logging(Logger.Level.warning, e.getMessage(), this);
                }
            }


            //generate command
            command = "";
            params = null;

            command = path+"/run.sh";
            params = new String[]{command};
            try {
                start = System.currentTimeMillis();
                processBuilder = new ProcessBuilder(params);
                p =processBuilder.start();
                Logger.logging(info,"Start povray... ",this.getClass());

                while (p.isAlive()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Logger.logging(Logger.Level.warning, e.getMessage(), this);
                    }
                }

                this.caller.renderCallback();
                System.out.println((System.currentTimeMillis() - start)+" ms.");
            } catch (IOException e) {
                e.printStackTrace();
                Logger.logging(Logger.Level.error,e.getMessage(),this.getClass());
            }
            this.caller.renderCallback();
            System.out.println((System.currentTimeMillis() - start)+" ms.");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.logging(Logger.Level.error,e.getMessage(),this.getClass());
        }
    }

    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return "Callable for to generate the povrayImages";
    }



}
