package org.kevoree.library.javase.trustStuff;

import org.kevoree.*;
import org.kevoree.framework.KevoreePropertyHelper;
import org.kevoree.framework.KevoreeXmiHelper;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 16/01/13
 * Time: 17:15
 * To change this template use File | Settings | File Templates.
 */
public class Tester {

    public static void main(String args[]) {
        ContainerRoot model = KevoreeXmiHelper.loadStream(Tester.class.getClassLoader().getResourceAsStream("model.kev"));
        String channel = "";
        for (ContainerNode node:model.getNodesForJ()) {
            for(ComponentInstance component:node.getComponentsForJ()) {
                if(component.getTypeDefinition().getName().equals("KTinyWebServer")) {
                    for (Port p:component.getRequiredForJ()) {
                        for (MBinding binding:p.getBindingsForJ()) {
                            //Print the port
                            //System.out.println(binding.getPort().getPortTypeRef().getName());
                            System.out.println(binding.getHub().getName());
                            channel = binding.getHub().getName();
                        }
                    }
                }
            }

        }
        //System.out.println(model.findByQuery("nodes[node0]",ContainerNode.class).get().getName());
        boolean usedByComponent = false;
        for (ContainerNode node:model.getNodesForJ()) {
            for(ComponentInstance component:node.getComponentsForJ()) {
                if(component.getTypeDefinition().getName().equals("HelloWorldPage")) {
                    for (Port p:component.getRequiredForJ()) {
                        for (MBinding binding:p.getBindingsForJ()) {
                            if (binding.getHub().getName().equals(channel)) {
                                usedByComponent = true;
                                System.out.println("Required port used");
                                System.out.println("Component: " + component.getName());

                            }
                        }
                    }
                    for (Port p:component.getProvidedForJ()) {
                        for(MBinding binding:p.getBindingsForJ()) {
                            if (binding.getHub().getName().equals(channel)) {
                                usedByComponent = true;
                                System.out.println("Provided port used");
                                System.out.println("Component: " + component.getName());
                            }
                        }
                    }
                }
            }

        }

        for (ContainerNode node:model.getNodesForJ())  {
            for (ComponentInstance componentNode:node.getComponentsForJ()) {
                if (componentNode.getName().equals("HelloWorl94")) {
                    System.out.println("Node where HelloWorl94 is executing: " + node.getName());
                    break;
                }
            }
        }

        //Prueba diccionario
        for (ContainerNode node:model.getNodesForJ()) {
            for (ComponentInstance component:node.getComponentsForJ()) {
                if(component.getTypeDefinition().getName().equals("HelloWorldPage")) {
                    System.out.println(KevoreePropertyHelper.getProperty(component, "urlpattern", false, node.getName()).get());

                }
            }
        }
        String prueba = "node0";
        System.out.println(prueba.substring(prueba.length()-1));

    }
}
