package org.kevoree.library.javase.trustStuff;
import org.kevoree.*;
import org.kevoree.Port;
import org.kevoree.annotation.*;
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.DictionaryAttribute;
import org.kevoree.annotation.DictionaryType;
import org.kevoree.api.service.core.script.KevScriptEngine;
import org.kevoree.api.service.core.script.KevScriptEngineException;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.KevoreePropertyHelper;
import scala.Option;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 17/01/13
 * Time: 16:49
 * To change this template use File | Settings | File Templates.
 */
@ComponentType
@DictionaryType({
        @DictionaryAttribute(name="refreshSpeed", defaultValue ="5000", optional=true)
})
@Library(name = "JavaSE")
public class TrustManagerMover extends AbstractComponentType implements Runnable {

    Thread t;
    boolean alive;
    int refreshSpeed;

    @Start
    public void start() throws IOException {
        t = new Thread(this);
        refreshSpeed = Integer.parseInt(getDictionary().get("refreshSpeed").toString());
        alive = true;
        System.out.println("Trust Manager is started");
        t.start();
    }

    @Stop
    public void stop() {
        alive = false;
        t.interrupt();
    }

    @Update
    public void update() {
        refreshSpeed = Integer.parseInt(getDictionary().get("refreshSpeed").toString());
    }


    public int parsePortNumber (String nodeName) {
        Option<Channel> channelOption = getModelService().getLastModel().findByQuery("hubs[" + getName() + "]", Channel.class);
        int port = 8000;
        if (channelOption.isDefined()) {
            Option<String> portOption = KevoreePropertyHelper.getProperty(channelOption.get(), "port", true, nodeName);
            if (portOption.isDefined()) {
                try {
                    port = Integer.parseInt(portOption.get());
                } catch (NumberFormatException e) {
        e.printStackTrace();
                }
            }
        }
        return port;
    }
    @Override
    public void run() {
        while (alive) {

            ContainerRoot root = getModelService().getLastModel();
            float maxTrust = -100.0f;
            float nodeTrust = -100.0f;
            ContainerNode nodeChosen = null;

            ContainerNode currentNodeOfComponent = executingComponentInstance("HelloWorldPage");
            System.out.println("Current node of component: " + currentNodeOfComponent.getName());
            //Choose the "best" node
            for (ContainerNode node:root.getNodesForJ()) {
                for (ComponentInstance component:node.getComponentsForJ()) {
                    if (component.getTypeDefinition().getName().equals("FakeTrustServer")) {
                        nodeTrust =  Float.parseFloat(KevoreePropertyHelper.getProperty(component, "trustValue", false, node.getName()).get());
                        System.out.println(node.getName()+" trust="+nodeTrust);
                        if (nodeTrust >= maxTrust) {
                            nodeChosen = node;
                            maxTrust = nodeTrust;
                        }
                    }
                }
            }

            System.out.println("Node chosen: " + nodeChosen.getName());
            //If a movement of component is required...
            if (nodeChosen != null && !nodeChosen.getName().equals(currentNodeOfComponent.getName())) {

                String instanceName = retrieveInstance("HelloWorldPage").getName();
                Channel channelInstance = retrieveChannelInstance("KTinyWebServer");
                System.out.println("Channel Instance: " + channelInstance.getName());

                KevScriptEngine engine = getKevScriptEngineFactory().createKevScriptEngine();

                //ADDING VARIABLES
                engine.addVariable("currentNode", currentNodeOfComponent.getName());
                engine.addVariable("newNode", nodeChosen.getName());
                engine.addVariable("nodeOfTrustManager", getNodeName());

                engine.addVariable("componentInstance", instanceName); //Assumes just one instance




                // TODO FIX parsePortNumber
                //you cannot take to postulate that ports will always start with 9000
                // it will only work with your assembly of the mode
                engine.addVariable("portNumberTrustManager", String.valueOf(9000 + Integer.parseInt(getNodeName().substring(getNodeName().length()-1))));
                engine.addVariable("portNumber1", String.valueOf(9000 + Integer.parseInt(currentNodeOfComponent.getName().substring(currentNodeOfComponent.getName().length()-1))));
                engine.addVariable("portNumber2", String.valueOf(9000 + Integer.parseInt(nodeChosen.getName().substring(nodeChosen.getName().length()-1))));

                engine.addVariable("channel", channelInstance.getName());

                //WRITING SCRIPT
                engine.append("moveComponent {componentInstance}@{currentNode}=>{newNode}");
                engine.append("updateDictionary {channel} {port=\"{portNumberTrustManager}\"}@{nodeOfTrustManager},{port=\"{portNumber2}\"}@{newNode}");

                System.out.println("Moving " + instanceName + " from " + currentNodeOfComponent.getName() + " to " + nodeChosen.getName());
                try {
                    engine.interpretDeploy();
                } catch(KevScriptEngineException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(refreshSpeed);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    //Given a componentType, it returns the channel instance it is using (assumes using only one channel instance through
    //a required port)
    private Channel retrieveChannelInstance(String s) {

        Channel channel = null;
        ContainerRoot root = getModelService().getLastModel();

        for (ContainerNode node:root.getNodesForJ()) {
            if (node.getName().equals(getNodeName())) {
                for(ComponentInstance component:node.getComponentsForJ()) {
                    if(component.getTypeDefinition().getName().equals(s)) {
                        for (Port p:component.getRequiredForJ()) {
                            for (MBinding binding:p.getBindingsForJ()) {
                                channel = binding.getHub();
                                return channel;
                            }
                        }

                    }
                }
            }
        }
        return channel;
    }

    //This method retrieves the first found component instance of type componentType
    private ComponentInstance retrieveInstance(String componentType) {

        ContainerRoot root = getModelService().getLastModel();
        ComponentInstance res = null;

        for (ContainerNode node:root.getNodesForJ()) {
            for (ComponentInstance component:node.getComponentsForJ())   {
                if(component.getTypeDefinition().getName().equals(componentType)) {
                    res = component;
                    return res;
                }
            }
        }
        return res;

    }

    // This method retrieves the node where at least one component instance of type componentType is running
    private ContainerNode executingComponentInstance(String componentType) {

        ContainerRoot root = getModelService().getLastModel();
        ContainerNode res = null;

        for (ContainerNode node:root.getNodesForJ()) {
            for (ComponentInstance component:node.getComponentsForJ())   {
                if(component.getTypeDefinition().getName().equals(componentType)) {
                    res = node;
                    return res;
                    }
                }
            }
        return res;
    }
}
