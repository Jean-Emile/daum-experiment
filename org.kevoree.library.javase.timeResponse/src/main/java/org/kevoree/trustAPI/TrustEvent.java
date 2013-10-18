package org.kevoree.trustAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 14/10/13
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */

@Provides({
        @ProvidedPort(name = "trustEvent", type = PortType.MESSAGE)
})
@Requires({
        @RequiredPort(name = "trustMetricNotification", type = PortType.MESSAGE, optional = false, needCheckDependency = true),
        @RequiredPort(name = "trustEntityNotification", type = PortType.MESSAGE, optional = false, needCheckDependency = true)
})
@Library(name = "Trust")
@ComponentType
public final class TrustEvent extends AbstractComponentType implements ITrustEvent {

    @Override
    @Port(name="trustEvent")
    public void TrustModelChanged(Object ti) {

        TrustEventType type = null;
        if (ti instanceof  TrustEventInfo) {
            type = ((TrustEventInfo) ti).getType();
            if (type == TrustEventType.FACTORUPDATE || type == TrustEventType.NEWFACTOR) {
                getPortByName("trustMetricNotification", MessagePort.class).process( ti );
            } else if (type == TrustEventType.NEWTRUSTVALUEAVAILABLE) {
                getPortByName("trustEntityNotification", MessagePort.class).process( ti );
            }
        }
    }
}
