package org.kevoree.library.javase.timeResponse;

import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Library;
import org.kevoree.annotation.Start;
import org.kevoree.annotation.Stop;
import org.kevoree.trustAPI.FactorProducer;

/**
 * Created by franciscomoyanolara on 22/01/14.
 */
@ComponentType
@Library(name = "Trust")
public class TimeToResponseGenerator extends FactorProducer implements Runnable {

    private Thread thread = null;
    private boolean alive = false;

    @Start
    public void start() {
        System.out.println("TimeToResponseGenerator started");
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

            System.out.println("New random value for time to response generated: " + r);
            //This is an objective factor about a trustee
            addFactor( "timeToResponse", String.valueOf(r) );

            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
