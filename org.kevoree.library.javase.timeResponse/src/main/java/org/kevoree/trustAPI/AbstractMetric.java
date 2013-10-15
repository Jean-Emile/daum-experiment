package org.kevoree.trustAPI;


import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.trustmetamodel.Variable;

import java.util.List;

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
@Provides({
        @ProvidedPort(name = "instance", type = PortType.SERVICE, className = ITrustMetric.class)
})
@ComponentFragment
public abstract class AbstractMetric extends AbstractComponentType implements ITrustMetric, TrustEventListener {

    //A list of trustor instances using the metric
    //List<TrustEntity> trustors;
    public boolean started = false;

    @Start
    public void start() {
        started = true;
        //System.out.println("Abstract Metric started");
        GetHelper.getComponentBindedToPort(getModelService().getLastModel(), "factorManagement", getModelElement().getName());

        System.out.println("Registering metric for trust events notifications");

        //At start time, we register the metric with the trustor
        //getPortByName("factorManagement", ITrustEntity.class).registerMetric("myContext");
        System.out.println("Metric registration successful");
    }

    @Stop
    public void stop() {
        //System.out.println("Abstract Metric stopped");
    }

    @Update
    public void update() {
        //System.out.println("Abstract Metric updated");
    }

    public Variable getVariable(String context, String name) {
        //Access trust model and return variable set by source
        //Now we can only access the trust model through the trustor's service
        //This limitation must be overcome by making the trust model accessible to all components
        //System.out.println("Returning variable " + name);
        //System.out.println(getPortByName("factorManagement", ITrustEntity.class).getVariable(context, name));
        //return null;

        return getPortByName("factorManagement", ITrustEntity.class).getVariable(context, name);
    }

    @Port(name="instance", method="getInstance")
    @Override
    public AbstractMetric getInstance(TrustEntity te) {
        System.out.println("A trustor called getInstance of the metric");
        //trustors.add(te);
        return this;
    }

    //public List<TrustEntity> getTrustors() {
        //return trustors;
    //}

    //This method must be overridden by trust engines extending this class
    public abstract Object compute();

    public String toString() {
        return "This is an AbstractMetric";
    }
}
