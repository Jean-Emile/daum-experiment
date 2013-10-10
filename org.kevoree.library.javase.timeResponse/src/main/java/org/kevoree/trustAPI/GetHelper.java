package org.kevoree.trustAPI;

import org.kevoree.ComponentInstance;
import org.kevoree.ContainerNode;
import org.kevoree.ContainerRoot;
import org.kevoree.framework.KevoreePropertyHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 28/03/13
 * Time: 11:16
 * To change this template use File | Settings | File Templates.
 */

public final class GetHelper {

    //It returns a list with the names of all component instances (of a componentType)
    //running on a node with name nodeName
    static List<String> getComponentInstanceName(ContainerRoot model, String componentType, String nodeName) {

        List<String> components = new ArrayList<String>();

        for (ContainerNode node : model.getNodes()) {
            if(node.getName().equals(nodeName)) {
                for(ComponentInstance component:node.getComponents()) {
                    if(component.getTypeDefinition().getName().equals(componentType)) {
                        components.add(component.getName());
                    }
                }
            }
        }

        return components;
    }

    //It returns a list with the names of all component instances running on every node in the model
    static List<String> getComponentInstanceName(ContainerRoot model, String componentType) {

        List<String> components = new ArrayList<String>();

        for (ContainerNode node : model.getNodes()) {
            components.addAll(getComponentInstanceName(model, componentType, node.getName()));
        }
        return components;
    }

    //It returns a list with the names of all trustee instances with context "context" and
    //running on a node with name nodeName
    static List<String> getTrusteeInstanceName(ContainerRoot model, String context, String nodeName) {

        KevoreePropertyHelper propertyHelper = KevoreePropertyHelper.instance$;

        List<String> components = new ArrayList<String>();
        for (ContainerNode node : model.getNodes()) {
            if(node.getName().equals(nodeName)) {
                for(ComponentInstance component:node.getComponents()) {
                    //We obtain the context of the component and if it's a trustee
                    String trusteeContext = propertyHelper.getProperty(component, "trustContext", false, node.getName());
                    boolean isTrustee = propertyHelper.getProperty(component, "role", false, node.getName()).equals("trustee");

                    //System.out.println("For component " + component.getName() + "...");
                    //System.out.println("trusteeContext is " +  trusteeContext);
                    //System.out.println("is trustee is " +  isTrustee);
                    if(isTrustee && trusteeContext.equals(context)) {
                        //System.out.println("Therefore, I add component " + component.getName() + "to the list");
                        components.add(component.getName());
                    }

                }

            }
        }

        return components;
    }

    //It returns a list with the names of all trustee instances running on every node in the model
    //The trustee instances must have the same context as "context"
    // Node 0 - compInst1, compInst2, ...
    // Node 1 - compInst45, compInst46
    static HashMap<String, List<String>> getTrusteesInstanceName(ContainerRoot model, String context) {

        HashMap<String, List<String>> components = new HashMap<String, List<String>>();
        List<String> componentsOnNode = new ArrayList<String>();

        for (ContainerNode node : model.getNodes()) {
            componentsOnNode.clear();
            componentsOnNode = getTrusteeInstanceName(model, context, node.getName());
            if (componentsOnNode.size() > 0)  {
                components.put(node.getName(), new ArrayList<String>(componentsOnNode));
            }
        }
        /*
        for (String n: components.keySet()) {
            System.out.println("Node " + n + " is running the following trustees:");
            for (String c : components.get(n)) {
                System.out.println(c);
            }
        } */

        return components;
    }
}
