package org.kevoree.library.javase.fakeDataProvider;

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
 * User: franciscomoyanolara
 * Date: 16/01/13
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "JavaSE")
@ComponentType
public class FakeReasoner2 extends AbstractComponentType implements ModelListener, Runnable {

    boolean computerDataProviderExists;
    Thread t;

    @Start
    public void start() {
        computerDataProviderExists = false;
        getModelService().registerModelListener(this);
        t = new Thread(this);
        t.start();
    }

    @Stop
    public void stop() {
       t.interrupt();
       t = null;
    }

    @Override
    public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean afterLocalUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void modelUpdated() {
        ContainerRoot root = getModelService().getLastModel();
        for (ContainerNode node:root.getNodesForJ()) {
            if(!computerDataProviderExists && node.getName().equals(getNodeName())) {
                for(ComponentInstance component:node.getComponentsForJ()) {
                    if(component.getTypeDefinition().getName().equals("ComputerDataProvider")) {
                        computerDataProviderExists = true;
                        break;
                    }
                }
            }

            t.start();
        }
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

        if(!computerDataProviderExists) {
           ContainerRoot root = getModelService().getLastModel();
           KevScriptEngine engine = getKevScriptEngineFactory().createKevScriptEngine();
           engine.addVariable("nodeName", getNodeName());
           engine.append("addComponent {nodeName}CPD@{nodeName}:ComputerDataProvider");
           try{
            engine.interpretDeploy();
           }catch(KevScriptEngineException e) {
               e.printStackTrace();
           }
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
