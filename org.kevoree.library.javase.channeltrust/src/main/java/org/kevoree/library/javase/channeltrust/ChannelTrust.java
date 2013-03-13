package org.kevoree.library.javase.channeltrust;


import org.kevoree.ContainerNode;
import org.kevoree.annotation.*;
import org.kevoree.annotation.ChannelTypeFragment;
import org.kevoree.annotation.ComponentType;
import org.kevoree.framework.*;
import org.kevoree.framework.Constants;
import org.kevoree.framework.KevoreePlatformHelper;
import org.kevoree.framework.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 31/01/13
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "JavaSE")
@ChannelTypeFragment(theadStrategy = ThreadStrategy.THREAD_QUEUE)
public class ChannelTrust extends   AbstractChannelFragment {

    private Logger logger = LoggerFactory.getLogger(ChannelTrust.class);

    @Override
    public Object dispatch(Message msg)
    {
        if (getBindedPorts().isEmpty() && getOtherFragments().isEmpty()) {
            logger.debug("No consumer, msg lost");
        }
        for (org.kevoree.framework.KevoreePort p : getBindedPorts()) {
            forward(p, msg);
        }
        for (KevoreeChannelFragment cf : getOtherFragments()) {
            if (!msg.getPassedNodes().contains(cf.getNodeName())) {
                forward(cf, msg);
            }
        }
        return null;
    }

    @Start
    public void startHello() {
        logger.debug("Hello Channel");
    }

    @Stop
    public void stopHello() {
        logger.debug("Bye Channel");
    }

    @Update
    public void updateHello() {
        for (String s : this.getDictionary().keySet()) {
            logger.debug("Dic => " + s + " - " + this.getDictionary().get(s));
        }
    }

    @Override
    public ChannelFragmentSender createSender(String remoteNodeName, String remoteChannelName)
    {
        return new NoopChannelFragmentSender();
    }

}
