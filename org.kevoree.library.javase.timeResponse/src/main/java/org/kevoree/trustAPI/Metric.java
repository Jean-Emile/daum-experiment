package org.kevoree.trustAPI;

import org.kevoree.annotation.PortType;
import org.kevoree.annotation.RequiredPort;
import org.kevoree.annotation.Requires;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.trustframework.api.IVariableProducer;
import org.kevoree.trustmetamodel.Variable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 24/03/13
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
@Requires({
        @RequiredPort(name = "serviceGetVariable", type = PortType.SERVICE, className = Trustor.class)
})
public abstract class Metric extends AbstractComponentType {

    public Variable getVariable(String context, String name) {
        //Access trust model and return variable set by source
        //Now we can only access the trust model through the trustor's service
        //This limitation must be overcome by making the trust model accessible to all components
        return getPortByName("serviceGetVariable", Trustor.class).
                getVariable("myContext", "ttr");
    }

    //This method must be overridden by trust engines extending this class
    public abstract Object compute();
}
