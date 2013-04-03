package org.kevoree.library.javase.timeResponse;

/*import org.kevoree.Trust.TrustValue;
import org.kevoree.Trust.Trustee;
import org.kevoree.trustframework.Trustor;*/
import org.kevoree.reconfigurationAPI.AdaptationManager;
import org.kevoree.trustAPI.Metric;
import org.kevoree.trustAPI.Trustor;
import org.kevoree.annotation.*;
import org.kevoree.framework.MessagePort;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import org.kevoree.library.javase.webserver.ServerBootstrap;

import java.util.concurrent.TimeUnit;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 18/03/13
 * Time: 10:40
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "JavaSE")
@ComponentType
@DictionaryType({
        @DictionaryAttribute(name = "port" , defaultValue = "8080"),
        @DictionaryAttribute(name = "timeout" , defaultValue = "5000", optional = true)
})
@Requires({
        @RequiredPort(name = "handler", type = PortType.MESSAGE)
})
@Provides({
        @ProvidedPort(name = "response", type = PortType.MESSAGE)
})
public class WebServerTrustor2 extends Trustor implements Runnable
{

    public ServerBootstrap bootstrap = null;

    @Start
    public void start() {
        //start should receive the name of this trustor to initialize its relationships
        super.start(getModelElement().getName());
        bootstrap = new ServerBootstrap(this.getPortByName("handler", MessagePort.class),this);
        bootstrap.startServer(Integer.parseInt(this.getDictionary().get("port").toString()),
                Long.parseLong(this.getDictionary().get("timeout").toString())
        );
        //Need to specify the metric to use with each potential trustee
        //This will also create a trust relationship. This can be in a dictionary
        //so the developer can change it at runtime in the editor or kevscript
        //setMetric(new MyTrustEngine(), "mycontext", "trustor", "trustee");
    }

    @Stop
    public void stop() {
        super.stop();
        if (bootstrap != null) {
            bootstrap.stop();
        }
        alive = false;
    }

    @Update
    public void update() {
        super.update();
        stop();
        start();
    }

    @Port(name = "response")
    public void responseHandler(Object param) {
        if (bootstrap != null) {
            bootstrap.responseHandler(param);


            if(param instanceof KevoreeHttpResponse)
            {
                KevoreeHttpResponse r = (KevoreeHttpResponse)param;
            }
        }
    }

    //Will there be problems because the parent class also implements Runnable?
    //Here is where the reconfiguration code will be. Every x seconds, this component
    //will analyze its trust relationships and will change the system architecture

    //For separation of concerns, this should be done by another component. This component
    //should simply access the service offered by this other reconfiguration component.
    //The reconfiguration component would access a self-adaptation.xml file where there are rules
    //to change the architecture according to certain trust conditions
    //e.g. if (trustrelationship(trustor, trustee) < threshold)
                   //look up another potential trustee
                   //change binding to this trustee

    //Implementing LISTENERS would be more elegant (When the trustor's metric has a new value for some
    //relationship, reconfigure is called. In turn, metric implements onVariableChanged listener)
    //public void onMetricValueChanged()
    public void run() {
        //change for alive (but another alive, not trustor's alive)
        while (true)
        {
            getPortByName("reconfiguration", AdaptationManager.class).reconfigure();

            try
            {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
