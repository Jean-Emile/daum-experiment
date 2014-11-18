package org.kevoree.trustAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 24/03/13
 * Time: 16:50
 * To change this template use File | Settings | File Templates.
 */

@DictionaryType({
        @DictionaryAttribute(name = "trustContext", optional = false),
        @DictionaryAttribute(name = "node", optional = true),
        @DictionaryAttribute(name = "component", optional = true)
        //TODO name="how" vals={assigned, monitored} -> Esto tiene más sentido en Claim
})

@Requires({
        @RequiredPort(name = "factorAddition", type = PortType.MESSAGE, optional = false, needCheckDependency = true)
})
@ComponentType
@Library(name = "Trust")
public class FactorProducer extends AbstractComponentType implements IFactorProducer {

   //This method adds a factor to the trust model
   public final void addFactor(String name, String value) {
        System.out.println("In " + getModelElement().getName() +
                ", adding factor " + getContext() + name + getTarget() + " with value " + value);

        if (getPortByName("factorAddition") == null) {
            System.out.println("Model not ready yet to accept connections. Please retry later");

        } else {
            getPortByName("factorAddition", MessagePort.class).process(
                    new FactorInfo( getModelElement().getName(),
                                    getContext(), name, value, getTarget() ));
        }
    }

    //This method gets the target of the factor (when the factor corresponds to a target component)
    public final String getTarget()
    {
        System.out.println("Accessing from " + getModelElement().getName() + " to target entity: " +
                (String) getDictionary().get("node") + (String) getDictionary().get("component"));
        //TODO: components in different nodes can have the same name
        return ( String ) getDictionary().get("node") + ( String ) getDictionary().get("component");
    }

    public final String getContext()
    {
        return ( String ) getDictionary().get( "trustContext" );

    }

}
