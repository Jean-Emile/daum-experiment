package org.kevoree.trustframework;


import org.kevoree.Trust.TrustRoot;
import org.kevoree.Trust.Trustee;
import org.kevoree.Trust.Value;
import org.kevoree.Trust.impl.DefaultTrustFactory;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.trustframework.api.ITrustor;
import org.kevoree.trustframework.api.IVariableProducer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 19/03/13
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "",optional = false)
}     )

@Provides({
        @ProvidedPort(name = "service", type = PortType.SERVICE, className = IVariableProducer.class)
})
@Library(name = "Trust")
@ComponentType
public abstract class Trustor extends AbstractComponentType  implements ITrustor {

    public DefaultTrustFactory factory =null;
    public TrustRoot model;

    @Start
    public void start(){
        factory = new DefaultTrustFactory();
        model = factory.createTrustRoot();

    }

    public  abstract void compute();

    @Port(method = "addVariable",name = "service")
    public void addVariable(String trustorquery,String trusteequery,Value value)
    {
        org.kevoree.Trust.Trustor trustor=null;
        Trustee trustee=null;
        trustor  =model.findTrustorsByID(trustorquery);
        if(trustor == null){
            trustor = factory.createTrustor();
            trustor.setQueryTrustor(trustorquery);
        }
        trustee = trustor.findTrusteesByID(trusteequery);
        if(trustee ==null){
            trustee = factory.createTrustee();
            trustee.setQueryTrustee(trusteequery);
        }

        System.out.println("ADD "+trustor.getQueryTrustor()+" "+trustee.getQueryTrustee()+" "+value.getValue());
        trustee.addValues(value);
    }

}
