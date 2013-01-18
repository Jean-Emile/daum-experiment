package org.kevoree.library.javase.fakeDataProvider;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;

import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 15/01/13
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
@ComponentType
@DictionaryType({
        @DictionaryAttribute(name = "refresh_speed", defaultValue = "2000", optional = true)
})
@Requires({
        @RequiredPort(name = "ram", type = PortType.MESSAGE, optional = true)
})
@Provides({
        @ProvidedPort(name="cpu", type=PortType.SERVICE, className=Ihardware.class)
})
@Library(name="JavaSE")
public class ComputerDataProvider extends AbstractComponentType implements Runnable,Ihardware {

    Thread t = null;
    boolean alive;

    @Start
    public void start() throws IOException {
        alive = true;
        t = new Thread(this);
        t.start();

    }

    @Port(name="cpu", method="getCpu")
    public Object getCpu(){
        com.sun.management.OperatingSystemMXBean operatingSystemMXBean =
                (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return operatingSystemMXBean.getSystemLoadAverage();
    }

    @Stop
    public void stop() {
        alive = false;
    }

    @Update
    public void update() {

    }

    @Override
    public void run() {
            com.sun.management.OperatingSystemMXBean operatingSystemMXBean =
                    (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            while (alive) {
                MessagePort portRAM = (MessagePort) this.getPortByName("ram");
                portRAM.process("Free Memory = " + operatingSystemMXBean.getFreePhysicalMemorySize());
                try {
                    Thread.sleep(Integer.parseInt(getDictionary().get("refresh_speed").toString()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
             }
   }
}
