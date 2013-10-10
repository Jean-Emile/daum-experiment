package org.kevoree.trustAPI;


import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.trustmetamodel.Variable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 24/03/13
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Trust")
@Requires({
        @RequiredPort(name = "factorManagement", type = PortType.SERVICE, className = ITrustEntity.class)
})
@ComponentType
public abstract class AbstractMetric extends AbstractComponentType {

    public Variable getVariable(String context, String name) {
        //Access trust model and return variable set by source
        //Now we can only access the trust model through the trustor's service
        //This limitation must be overcome by making the trust model accessible to all components
        System.out.println("Returning variable " + name);
        return getPortByName("factorManagement", ITrustEntity.class).
                getVariable(context, name);
    }

    //This method must be overridden by trust engines extending this class
    public abstract Object compute();
}
