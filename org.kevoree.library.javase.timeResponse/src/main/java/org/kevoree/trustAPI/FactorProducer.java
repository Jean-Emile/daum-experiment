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

@Requires({
        @RequiredPort(name = "factorAddition", type = PortType.MESSAGE, optional = false, needCheckDependency = true)
})
@ComponentType
@Library(name = "Trust")
public class FactorProducer extends AbstractComponentType implements IFactorProducer {


   public final void addFactor(String context, String name, String value, String target) {
        System.out.println("In " + getModelElement().getName() + ", adding subjective factor " + context + name + " with value " + value);

        if (getPortByName("factorAddition") == null) {
            System.out.println("Model not ready yet to accept connections. Please retry later");

        } else {
            getPortByName("factorAddition", MessagePort.class).process( new FactorInfo( getModelElement().getName(),
                                                                                        context, name, value, target ) );
        }
    }

}
