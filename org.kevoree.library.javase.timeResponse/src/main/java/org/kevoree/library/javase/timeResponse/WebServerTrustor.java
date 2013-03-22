package org.kevoree.library.javase.timeResponse;

import org.kevoree.annotation.*;
import org.kevoree.framework.MessagePort;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import org.kevoree.library.javase.webserver.ServerBootstrap;
import org.kevoree.trustframework.Trustor;

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
public class WebServerTrustor extends Trustor
{

    public ServerBootstrap bootstrap = null;

    @Start
    public void start() {
        super.start();
        bootstrap = new ServerBootstrap(this.getPortByName("handler", MessagePort.class),this);
        bootstrap.startServer(Integer.parseInt(this.getDictionary().get("port").toString()),
                Long.parseLong(this.getDictionary().get("timeout").toString())
        );
    }

    @Stop
    public void stop() {
        if (bootstrap != null) {
            bootstrap.stop();
        }
    }

    @Update
    public void update() {
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


    @Override
    public void compute() {



    }
}
