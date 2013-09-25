package org.kevoree.trustAPI;

import org.kevoree.annotation.*;
import org.kevoree.reconfigurationAPI.AdaptationManager;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/09/13
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext",optional = false),
        @DictionaryAttribute(name = "role", defaultValue ="", vals = {"Trustor","Trustee"}),
        @DictionaryAttribute(name = "metric", defaultValue ="MyTrustEngine", optional = false)
}     )

//Up to now, the trustor manages metrics variables
@Provides({
        @ProvidedPort(name = "serviceAddVariable", type = PortType.SERVICE, className = VariableProducer.class) ,
        @ProvidedPort(name = "serviceGetVariable", type = PortType.SERVICE, className = Metric.class)
})
@Requires({
        @RequiredPort(name = "reconfigure", type = PortType.SERVICE, className = AdaptationManager.class)
})
@Library(name = "Trust")
@ComponentType
public interface TrustEntity {
}
