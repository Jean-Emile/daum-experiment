package org.kevoree.trustAPI;


import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.kevoree.trustmetamodel.Factor;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 24/03/13
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Trust")
@Requires({
        @RequiredPort(name = "trustManagement", type = PortType.SERVICE, className = ITrustModel.class, needCheckDependency = true),
        @RequiredPort(name = "trustEvent", type = PortType.MESSAGE, optional = false)
})
@Provides({
        @ProvidedPort(name = "compute", type = PortType.SERVICE, className = ITrustMetric.class),
        @ProvidedPort(name = "trustMetricNotification", type = PortType.MESSAGE)
})
@ComponentFragment
public abstract class AbstractMetric extends AbstractComponentType implements IFactorChangeEvent {

    public boolean started = false;
    private Object lastValueComputed = null;

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

    public final Factor getFactor(String context, String name) {
        return getPortByName("trustManagement", ITrustModel.class).getFactor(context, name);
    }

    public final void notifyTrustEntities() {
        TrustEventInfo tInfo = new TrustEventInfo( TrustEventType.NEWTRUSTVALUEAVAILABLE,
                                                        "myContext",
                                                         getModelElement().getName(),
                                                         String.valueOf(lastValueComputed)
                                                  );
        getPortByName("trustEvent", MessagePort.class).process( tInfo );
    }

    public final void setLastValueComputed(Object v) {
        lastValueComputed = v;
    }

    public final Object getLastValueComputed() {
        return lastValueComputed;
    }

    public String toString() {
        return "This is an AbstractMetric";
    }

    /******************** ABSTRACT METHODS ***********************/
    //These methods must be overridden by trust engines extending this class
    @Port(name="compute", method="compute")
    public abstract Object compute();

    @Port(name="trustMetricNotification")
    public abstract void onFactorChange(Object tInfo);

}
