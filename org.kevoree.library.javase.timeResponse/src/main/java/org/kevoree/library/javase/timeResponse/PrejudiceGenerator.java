package org.kevoree.library.javase.timeResponse;

import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Library;
import org.kevoree.annotation.Start;
import org.kevoree.annotation.Stop;
import org.kevoree.trustAPI.FactorProducer;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 18/10/13
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
@ComponentType
@Library(name = "Trust")
public class PrejudiceGenerator extends FactorProducer implements Runnable {

    private Thread thread = null;
    private boolean alive = false;

    @Start
    public void start() {
        System.out.println("PrejudiceGenerator started");
        thread = new Thread(this);
        alive = true;
        thread.start();

    }

    @Stop
    public void stop() {
        alive = false;
    }

    @Override
    public void run() {


        while (alive)
        {
            try
            {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double r = 1 + (Math.random() * ((10 - 1) + 1));

            System.out.println("New random value for prejudice generated: " + r);
            addFactor("myContext", "prejudice", String.valueOf(r), null);

            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

