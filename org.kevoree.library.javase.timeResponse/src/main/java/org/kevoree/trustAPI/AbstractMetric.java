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
        @RequiredPort(name = "trustManagement", type = PortType.SERVICE, className = ITrustModel.class, needCheckDependency = true)
})
@Provides({
        @ProvidedPort(name = "compute", type = PortType.SERVICE, className = ITrustMetric.class)
})
@ComponentFragment
public abstract class AbstractMetric extends AbstractComponentType {

    public boolean started = false;

    @Start
    public void start() {
        started = true;
        //System.out.println("Abstract Metric started");
        //GetHelper.getComponentBindedToPort(getModelService().getLastModel(), "factorManagement", getModelElement().getName());

        //At start time, we register the metric with the trustor
        //getPortByName("factorManagement", ITrustEntity.class).registerMetric("myContext");
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
        return getPortByName("trustManagement", ITrustModel.class).getVariable(context, name);
    }

    //This method must be overridden by trust engines extending this class
    @Port(name="compute", method="compute")
    public abstract Object compute();

    public String toString() {
        return "This is an AbstractMetric";
    }
}
