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

@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext", optional = false)
})

@Requires({
        @RequiredPort(name = "trustManagement", type = PortType.SERVICE, className = ITrustModel.class, needCheckDependency = true),
        @RequiredPort(name = "trustEvent", type = PortType.MESSAGE, optional = false)
})
@Provides({
        @ProvidedPort(name = "compute", type = PortType.SERVICE, className = ITrustMetric.class),
        @ProvidedPort(name = "trustMetricNotification", type = PortType.MESSAGE)
})
@ComponentFragment
@Library(name = "Trust")
public abstract class AbstractMetric extends AbstractComponentType implements IFactorChangeEvent, ITrustMetric {

    public boolean started = false;
    private Object lastValueComputed = null;

    //This method is automatically called when the runtime launches this component
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

    //This method retrieves a factor from the model, with id 'context+name+factorTarget'
    public final Factor getFactor(String name, String factorTarget) {
        System.out.println("The trust enigne is querying the factor "+ getContext()+name+factorTarget);
        return getPortByName("trustManagement", ITrustModel.class).getFactor(getContext(), name, factorTarget);
    }

    //This method notifies trust entities that a new trust value is available
    public final void notifyTrustEntities( Object eventInfo ) {
        String ctx = null;
        if ( eventInfo instanceof TrustEventInfo )
        {
            //Context where the factor has changed
            ctx = ((TrustEventInfo) eventInfo).getFactorInfo().getContext();
        }
        TrustEventInfo tInfo = new TrustEventInfo( TrustEventType.NEWTRUSTVALUEAVAILABLE,
                                                         ctx,
                                                         getModelElement().getName()
                                                  );
        getPortByName("trustEvent", MessagePort.class).process( tInfo );
    }

    //Store the last value computed (for caching purposes)
    public final void setLastValueComputed(Object v) {
        lastValueComputed = v;
    }

    //Get the last value computed (for caching purposes)
    public final Object getLastValueComputed() {
        return lastValueComputed;
    }

    public String toString() {
        return "This is an AbstractMetric";
    }

    /******************** ABSTRACT METHODS ***********************/
    //These methods can or must be overridden by trust engines extending this class

    //This method should implement the actual metric used by the trust model
    @Port(name="compute", method="compute")
    public abstract Object compute(String idTrustor, String idTrustee);

    //This method implements the action of the metric upon receival of a factor change
    //The default behaviour is notifying trust entities if the context of the factor is the same as the context of the metric
    //(because that means that the metric might use the factor)
    @Port(name="trustMetricNotification")
    public void onFactorChange(Object tInfo)
    {
        if ( tInfo instanceof TrustEventInfo )
        {
            String ctx = ((TrustEventInfo) tInfo).getFactorInfo().getContext();

            if ( ctx.equals( getDictionary().get( "trustContext" )))
            {
                notifyTrustEntities( tInfo );
            }
        }
    }

    public final String getContext()
    {
        return (String) getDictionary().get( "trustContext" );
    }

}
