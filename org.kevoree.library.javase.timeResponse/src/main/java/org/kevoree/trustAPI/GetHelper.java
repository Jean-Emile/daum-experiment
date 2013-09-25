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
public class GetHelper {

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

    //It returns a list with the names of all trustee instances of "componentType", with context "context" and
    //running on a node with name nodeName
    static List<String> getTrusteeInstanceName(ContainerRoot model, String context, String componentType, String nodeName) {

        KevoreePropertyHelper propertyHelper = new KevoreePropertyHelper();

        List<String> components = new ArrayList<String>();
        for (ContainerNode node : model.getNodes()) {
            if(node.getName().equals(nodeName)) {
                for(ComponentInstance component:node.getComponents()) {
                    //We obtain the context of the component
                    String trusteeContext = propertyHelper.getProperty(component, "context", false, node.getName());
                    boolean isTrustee = propertyHelper.getProperty(component, "role", false, node.getName()).equals("trustee");
                    if(isTrustee &&
                       component.getTypeDefinition().getName().equals(componentType) &&
                       trusteeContext.equals(context)) {
                        components.add(component.getName());
                    }

                }

            }
        }

        return components;
    }

    //It returns a list with the names of all trustee instances running on every node in the model
    //The trustee instances must have the same context as "context" and must be of "componentType"
    // Node 0 - compInst1, compInst2, ...
    // Node 1 - compInst45, compInst46
    static HashMap<String, List<String>> getTrusteesInstanceName(ContainerRoot model, String context, String componentType) {

        HashMap<String, List<String>> components = new HashMap<String, List<String>>();
        List<String> componentsOnNode = new ArrayList<String>();

        for (ContainerNode node : model.getNodes()) {
            componentsOnNode = getTrusteeInstanceName(model, context, componentType, node.getName());
            if (componentsOnNode.size() > 0)  {
                components.put(node.getName(), componentsOnNode);
            }
        }

        return components;
    }
}
