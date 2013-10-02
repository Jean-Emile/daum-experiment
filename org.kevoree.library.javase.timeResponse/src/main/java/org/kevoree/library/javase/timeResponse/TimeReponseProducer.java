package org.kevoree.library.javase.timeResponse;

/*
import org.kevoree.ContainerNode;
import org.kevoree.annotation.*;

import org.kevoree.Trust.TrustValue;
import org.kevoree.trustframework.VariableProducer;
import org.kevoree.trustframework.api.IVariableProducer;

import org.kevoree.trustAPI.VariableProducer;
import org.kevoree.trustAPI.Trustor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
*/

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 31/01/13
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */

/*
@DictionaryType({
        @DictionaryAttribute(name = "refresh",defaultValue = "1000", optional =false)
})
@Library(name = "JavaSE")
@ComponentType
public class TimeReponseProducer extends VariableProducer implements Runnable {

    private boolean alive=true;
    private Thread t=null;
    private ConcurrentHashMap<String,Integer>  map_time = new ConcurrentHashMap<String, Integer>();
    private ExecutorService executorService = Executors.newFixedThreadPool(20);


    @Start
    public void start(){
        t = new Thread(this);
        alive = true;
        t.start();

    }

    @Stop
    public void stop(){
        alive = false;
    }

    @Override
    public void run() {


        while (alive)
        {
            try
            {
                for(ContainerNode node : getModelService().getLastModel().getNodes())
                {
                    if(!node.getName().equals(getNodeName())){
                        executorService.execute(new PingService(map_time,node.getName(),getModelService().getLastModel()));
                    }
                }
                executorService.awaitTermination(2, TimeUnit.SECONDS);

                // results
                for(String key : map_time.keySet())
                {
                    getPortByName("service", Trustor.class).addVariable("myContext", "ttr", getModelElement().getName(),
                           key, map_time.get(key).toString());
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try
            {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public void addVariable(String name, String context, Object value, String source, String target) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
*/
