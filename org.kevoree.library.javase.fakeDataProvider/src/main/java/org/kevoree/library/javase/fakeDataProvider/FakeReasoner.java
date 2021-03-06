package org.kevoree.library;

import org.kevoree.ComponentInstance;
import org.kevoree.ContainerNode;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Library;
import org.kevoree.annotation.Start;
import org.kevoree.annotation.Stop;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.api.service.core.script.KevScriptEngine;
import org.kevoree.api.service.core.script.KevScriptEngineException;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 04/06/12
 * Time: 01:14
 */
@Library(name = "JavaSE")
@ComponentType
public class FakeReasoner extends AbstractComponentType implements ModelListener, Runnable {

    private Thread dummyReason = null;

    @Start
    public void start() {
        getModelService().registerModelListener(this);
        dummyReason = new Thread(this);
        dummyReason.start();
    }

    @Stop
    public void stop() {
        getModelService().unregisterModelListener(this);
        dummyReason.interrupt();
        dummyReason = null;
    }

    @Override
    public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
        return true;
    }

    @Override
    public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
        return true;
    }

    @Override
    public boolean afterLocalUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void modelUpdated() {

    }

    @Override
    public void preRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void postRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void run() {
        while (true) {
            ContainerRoot root = getModelService().getLastModel();
            KevScriptEngine engine = getKevScriptEngineFactory().createKevScriptEngine();
            Boolean globalExecute = false;
            for (ContainerNode node : root.getNodesForJ()) {
                    engine.addVariable("nodeName", node.getName());
                    engine.append("addComponent {nodeName}Console@{nodeName} : FakeConsole");
            }
            try {
                engine.interpretDeploy();
            } catch (KevScriptEngineException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
