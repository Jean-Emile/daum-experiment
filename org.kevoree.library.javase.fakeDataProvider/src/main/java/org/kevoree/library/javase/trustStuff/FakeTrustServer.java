package org.kevoree.library.javase.trustStuff;

import org.kevoree.ComponentInstance;
import org.kevoree.ContainerNode;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.script.KevScriptEngine;
import org.kevoree.api.service.core.script.KevScriptEngineException;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 16/01/13
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
@ComponentType
@DictionaryType({
        @DictionaryAttribute(name="refreshSpeed", defaultValue ="5000", optional=true),
        @DictionaryAttribute(name="minTrust", defaultValue="0.0", optional=true),
        @DictionaryAttribute(name="maxTrust", defaultValue="1.0", optional=true),
        @DictionaryAttribute(name="trustValue", defaultValue="0.5", optional=true)
})
@Library(name = "JavaSE")
public class FakeTrustServer extends AbstractComponentType implements Runnable {

    Thread t;
    boolean alive;
    float minTrust, maxTrust;
    int refreshSpeed;

    @Start
    public void start() throws IOException {
        updateTrustInfo();
        t = new Thread(this);
        alive = true;
        t.start();
    }

    @Stop
    public void stop() {
        alive = false;
        t.interrupt();
        t = null;
    }

    @Update
    public void update() {
        updateTrustInfo();
    }

    private ComponentInstance retrieveInstance(String nodename,String componentType)
    {
        ContainerRoot root = getModelService().getLastModel();
        ComponentInstance res = null;
        ContainerNode node = root.findByQuery("nodes["+nodename+"]",ContainerNode.class).get();
        for(ComponentInstance instance :     node.getComponentsForJ()){
            if(instance.getTypeDefinition().getName().equals(componentType)){
                return instance;
            }
        }
        return null;
    }

    @Override
    public void run() {
        java.util.Random rand = new java.util.Random();
        while(alive) {
            float randValue = minTrust + rand.nextFloat() * (maxTrust-minTrust);


            ContainerRoot model = getModelService().getLastModel();

            KevScriptEngine engine=    getKevScriptEngineFactory().createKevScriptEngine();

            ComponentInstance instance =  retrieveInstance(getNodeName(),"FakeTrustServer");
            engine.append("updateDictionary "+instance.getName()+"@"+getNodeName()+"  {maxTrust='"+getDictionary().get("maxTrust")+"',refreshSpeed='"+getDictionary().get("refreshSpeed")+"',minTrust='0.0',trustValue='"+randValue+"'}");

            try {
                engine.interpretDeploy();
            } catch (KevScriptEngineException e) {
                e.printStackTrace();
            }

            System.out.println("New trust value: " + String.valueOf(randValue));

            try {
                Thread.sleep(refreshSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTrustInfo() {
        maxTrust = Float.parseFloat(getDictionary().get("maxTrust").toString());
        minTrust = Float.parseFloat(getDictionary().get("minTrust").toString());
        refreshSpeed = Integer.parseInt(getDictionary().get("refreshSpeed").toString());
    }
}
