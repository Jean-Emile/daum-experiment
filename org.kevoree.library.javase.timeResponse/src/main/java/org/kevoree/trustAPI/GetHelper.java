package org.kevoree.trustAPI;

import org.kevoree.*;
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
                    if (trusteeContext != null) {   //The component has a property "trustContext"
                        String role = propertyHelper.getProperty(component, "role", false, node.getName());
                        if (role != null) {  //The component has a property "role"
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

        return components;
    }

    //It returns the names of the components instances that have a port with name portName
    //The third argument indicates the name of the component that called this method. This is to ensure
    //that we don't consider it
    public static List<String> componentOnPort(ContainerRoot model, String portName, String componentName) {

        List<String> componentsList = new ArrayList<String>();
        //System.out.println("Looking for all the ports of component " + componentName);
        for (ContainerNode node : model.getNodes()) {
            for (ComponentInstance component : node.getComponents()) {
                for (Port port : component.getProvided()) {
                    if (portName.equals (port.getPortTypeRef().getName()) &&
                            !component.getName().equals(componentName)) {
                        System.out.println("Added component " + component.getName());
                        componentsList.add(component.getName());
                    }
                }
                for (Port port : component.getRequired()) {
                    if (portName.equals (port.getPortTypeRef().getName()) &&
                            !component.getName().equals(componentName)) {
                        System.out.println("Added component " + component.getName());
                        componentsList.add(component.getName());
                    }
                }
            }

        }
        return componentsList;
    }


    //It returns the name of the binding of the port "portName" of component instance "componentName"
    private static String getBindingName(ContainerRoot model, String portName, String componentName) {
        String res = null;

        //System.out.println("Looking for all the bindings of " + portName + " of component " + componentName);
        for (ContainerNode node : model.getNodes()) {
            for (ComponentInstance component : node.getComponents()) {
                if (component.getName().equals(componentName)) {
                    for (Port port : component.getProvided()) {
                        //System.out.println("Provided ports for " + component.getName() + ": " + port.getPortTypeRef().getName());
                        if (port.getPortTypeRef().getName().equals(portName)) {
                            for (MBinding binding : port.getBindings()) {
                                //System.out.println("Binding: " + binding.getHub().getName());
                                //First we look for the binding of "text entered"
                                res = binding.getHub().getName();
                            }
                        }
                    }

                    for (Port port : component.getRequired()) {
                        //System.out.println("Required ports for " + component.getName() + ": " + port.getPortTypeRef().getName());

                        if (port.getPortTypeRef().getName().equals(portName)) {
                            for (MBinding binding : port.getBindings()) {
                                //System.out.println("Binding: " + binding.getHub().getName());

                                //First we look for the binding of "text entered"
                                res = binding.getHub().getName();
                            }
                        }
                    }

                }

            }
        }

        return res;

    }


    //It returns the name of the component instance that is bounded to the port with name portName, and ignoring the
    //component instance with name componentName
    public static String getComponentBindedToPort(ContainerRoot model, String portName, String componentName) {

        String componentRes = null;

        //We get the name of the binding of the port for component
        String sourceBinding = getBindingName(model, portName, componentName);

        //Now, we search for any binding that is equal to the aforementioned binding (but which is not from componentName)
        //System.out.println("Looking for all the bindings of other components with name " + sourceBinding);
        for (ContainerNode node : model.getNodes()) {
            for (ComponentInstance component : node.getComponents()) {
                if (!component.getName().equals(componentName)) {
                    for (Port port : component.getProvided()) {
                        for (MBinding binding : port.getBindings()) {
                              if (binding.getHub().getName().equals(sourceBinding)) {
                                  componentRes = component.getName();
                               }
                        }
                    }
                    for (Port port : component.getRequired()) {
                        for (MBinding binding : port.getBindings()) {
                            if (binding.getHub().getName().equals(sourceBinding)) {
                                componentRes = component.getName();
                            }
                        }
                    }
                }
            }
        }

        //System.out.println("The component connected to " + componentName + " though port " + portName + " is " + componentRes);

        return componentRes;
    }

    /*public static List<String> componentOnPortAndBinding(ContainerRoot model, String componentName) { //String portName, String componentName) {

        List<String> componentsList = new ArrayList<String>();
        System.out.println("Looking for all the ports of component " + componentName);
        for (ContainerNode node : model.getNodes()) {
            for (ComponentInstance component : node.getComponents()) {
                for (Port port : component.getProvided()) {
                    System.out.println("Bindings for port provided " + port.getPortTypeRef().getName());
                    for (MBinding binding : port.getBindings()) {
                        System.out.println(binding.getHub().getName());
                    }
                }
                for (Port port : component.getRequired()) {
                    System.out.println("Bindings for port required " + port.getPortTypeRef().getName());
                    for (MBinding binding : port.getBindings()) {
                        System.out.println(binding.getHub().getName());
                    }
                }
            }

        }
        return componentsList;
    }      */
}
