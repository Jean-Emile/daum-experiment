package org.kevoree.trustAPI;

/*
import org.kevoree.annotation.*;
import org.kevoree.reconfigurationAPI.AdaptationManager;
*/

import org.kevoree.trustmetamodel.Variable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/09/13
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
/*
@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext",optional = false),
        @DictionaryAttribute(name = "role", defaultValue ="", vals = {"Trustor","Trustee"}),
        @DictionaryAttribute(name = "metric", defaultValue ="MyTrustEngine", optional = false)
}     )

//Up to now, the trustor manages metrics variables
@Provides({
        @ProvidedPort(name = "serviceAddVariable", type = PortType.SERVICE, className = VariableProducer.class) ,
        //@ProvidedPort(name = "serviceGetVariable", type = PortType.SERVICE, className = AbstractMetric.class)
})
//@Requires({
        //@RequiredPort(name = "reconfigure", type = PortType.SERVICE, className = AdaptationManager.class)
//})
@Library(name = "Trust")
@ComponentType
*/
public interface ITrustEntity {

    public void addVariable(String context, String name, String idTarget, String value);
    public Variable getVariable(String context, String name);
    public void addSubjectiveFactor(String context, String name, String value);
    public boolean isTrustee(String potentialTrusteeName);
    public void registerMetric(AbstractMetric am);
    public void updateTrustRelationship(String context, String idTrustee)

}

