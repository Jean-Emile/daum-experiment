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

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 16/01/13
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */

@ComponentType
@DictionaryType({
        @DictionaryAttribute(name="refreshSpeed", defaultValue ="5000", optional=true)
})
@Library(name = "JavaSE")
public class TrustManager extends AbstractComponentType implements Runnable {

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

    @Override
    public void run() {

        //Recorre todos los nodos que tienen un FakeTrustServer y se queda con el HelloWorldPage cuyo
        //TrustServer asociado tiene el máximo valor de confianza

        while (alive) {

            ContainerRoot root = getModelService().getLastModel();
            float maxTrust = -100.0f;
            float componentTrust = -100.0f;
            ComponentInstance componentChosen = null;

            //Choose the "best" trust server
            for (ContainerNode node:root.getNodesForJ()) {

                //Potential best Hello World Page
                ComponentInstance potentialBestHWP = null;
                boolean fakeTrustFoundInNode = false;
                boolean betterFakeTrustFoundInNode = false;

                for (ComponentInstance component:node.getComponentsForJ()) {

                    if(component.getTypeDefinition().getName().equals("FakeTrustServer")) {
                        fakeTrustFoundInNode = true;
                        componentTrust = Float.parseFloat(KevoreePropertyHelper.getProperty(component, "trustValue", false, node.getName()).get());
                        if (componentTrust >= maxTrust) {
                            betterFakeTrustFoundInNode = true;
                            if (potentialBestHWP != null) {
                                componentChosen = potentialBestHWP;
                                System.out.println("Component chosen: " + componentChosen.getName());

                            }
                            System.out.println("Component's trust: " + componentTrust);
                            maxTrust = componentTrust;
                        }
                    } else if(component.getTypeDefinition().getName().equals("HelloWorldPage"))  {
                        if (betterFakeTrustFoundInNode) {
                            componentChosen = component;
                            System.out.println("Component chosen: " + componentChosen.getName());
                        } else if (!fakeTrustFoundInNode) {
                             potentialBestHWP = component;
                        }
                    }
                }
            }

            //Retrieve the name of the channel used by the KTinyWebServer
            //(For simplicity, we assume that it only uses one, and the same
            // for the required and provided ports)
            String channel = retrieveChannel("KTinyWebServer");

            //Check which HelloWorldPage component instance is using the same channel
            ComponentInstance componentUsingChannel = retrieveInstanceUsingChannel("HelloWorldPage",
                                                                                    channel);

            //Retrieve the node where the component using the channel instance is running
            ContainerNode executingOnNode = retrieveExecutingOnNode(componentUsingChannel);

            //Retrieve the node where the component chosen is running
            ContainerNode chosenExecutingOnNode = retrieveExecutingOnNode(componentChosen);

            //If the component is different from componentChosen
            //- unbind component to the channel
            //- bind componentChosen to the channel
            if (!componentUsingChannel.getName().equals(componentChosen.getName())) {
                KevScriptEngine engine = getKevScriptEngineFactory().createKevScriptEngine();

                //This bunch of code would unbind and then rebind another component to the channel

                engine.addVariable("nameComponentUsingChannel", componentUsingChannel.getName());
                engine.addVariable("nodeOfOldComponent", executingOnNode.getName());
                engine.addVariable("nameComponentChosen", componentChosen.getName());
                engine.addVariable("nodeOfNewComponent", chosenExecutingOnNode.getName());
                engine.addVariable("channel", channel);

                //For debugging...
                System.out.println("KEVOREE SCRIPT");
                System.out.println("unbind " + componentUsingChannel.getName() + ".request@" + executingOnNode.getName() + "=>" + channel);
                System.out.println("unbind " + componentUsingChannel.getName() + ".content@" + executingOnNode.getName() + "=>" + channel);
                System.out.println("bind " + componentChosen.getName() + ".request@" + chosenExecutingOnNode.getName() + "=>" + channel);
                System.out.println("bind " + componentChosen.getName() + ".content@" + chosenExecutingOnNode.getName() + "=>" + channel);

                engine.append("unbind {nameComponentUsingChannel}.request@{nodeOfOldComponent}=>{channel}");
                engine.append("unbind {nameComponentUsingChannel}.content@{nodeOfOldComponent}=>{channel}");
                engine.append("bind {nameComponentChosen}.request@{nodeOfNewComponent}=>{channel}");
                engine.append("bind {nameComponentChosen}.content@{nodeOfNewComponent}=>{channel}");


                try {
                    engine.interpretDeploy();
                } catch(KevScriptEngineException e) {
                    e.printStackTrace();
                }
                System.out.println("A binding change occurred towards Node " + chosenExecutingOnNode.getName());
            }
            try {
                Thread.sleep(refreshSpeed);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private ContainerNode retrieveExecutingOnNode(ComponentInstance component) {

        ContainerNode nodeRes = null;
        ContainerRoot root = getModelService().getLastModel();

        for (ContainerNode node:root.getNodesForJ())  {
           for (ComponentInstance componentNode:node.getComponentsForJ()) {
               if (componentNode.getName().equals(component.getName())) {
                   nodeRes =  node;
                   break;
               }
           }
        }
        return nodeRes;

    }


    private String retrieveChannel(String s) {

        String channel="";
        ContainerRoot root = getModelService().getLastModel();

        for (ContainerNode node:root.getNodesForJ()) {
            if (node.getName().equals(getNodeName())) {
                for(ComponentInstance component:node.getComponentsForJ()) {
                    if(component.getTypeDefinition().getName().equals(s)) {
                        for (Port p:component.getRequiredForJ()) {
                            for (MBinding binding:p.getBindingsForJ()) {
                                channel = binding.getHub().getName();
                            }
                        }

                    }
                }
            }
        }
        return channel;
    }

    private ComponentInstance retrieveInstanceUsingChannel(String componentType, String channel) {

        boolean usedByComponent = false;
        ContainerRoot root = getModelService().getLastModel();
        ComponentInstance res = null;

        for (ContainerNode node:root.getNodesForJ()) {
            for(ComponentInstance component:node.getComponentsForJ()) {
                if(component.getTypeDefinition().getName().equals(componentType)) {
                    for (Port p:component.getRequiredForJ()) {
                        for (MBinding binding:p.getBindingsForJ()) {
                            if (binding.getHub().getName().equals(channel)) {
                                res = component;
                                break;
                            }
                        }
                    }
                    for (Port p:component.getProvidedForJ()) {
                        for(MBinding binding:p.getBindingsForJ()) {
                            if (binding.getHub().getName().equals(channel)) {
                                res = component;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return res;
    }
}
