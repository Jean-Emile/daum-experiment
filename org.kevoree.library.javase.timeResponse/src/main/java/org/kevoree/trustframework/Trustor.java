package org.kevoree.trustframework;

/*
import org.kevoree.Trust.TrustRoot;
import org.kevoree.Trust.TrustValue;
import org.kevoree.Trust.Trustee;
import org.kevoree.Trust.impl.DefaultTrustFactory;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.trustframework.api.ITrustor;
import org.kevoree.trustframework.api.IVariableProducer;

import java.util.ArrayList;
import java.util.List;
*/

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 19/03/13
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
/*
@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "",optional = false)
}     )

@Provides({
        @ProvidedPort(name = "service", type = PortType.SERVICE, className = IVariableProducer.class)
})
@Library(name = "Trust")
@ComponentType
public abstract class Trustor extends AbstractComponentType implements ITrustor {

    public DefaultTrustFactory factory =null;
    public TrustRoot model;

    @Start
    public void start(){
        factory = new DefaultTrustFactory();
        model = factory.createTrustRoot();
    }

    public  abstract void compute();

    @Port(method = "addVariable",name = "service")
    public void addVariable(String idTrustor,String idTrustee,TrustValue value)
    {
        org.kevoree.Trust.Trustor trustor=null;
        Trustee trustee=null;
        trustor  =model.findTrustorsByID(idTrustor);
        if(trustor == null){
            trustor = factory.createTrustor();
            trustor.setQueryTrustor(idTrustor);
        }
        trustee = trustor.findTrusteesByID(idTrustee);
        if(trustee ==null){
            trustee = factory.createTrustee();
            trustee.setQueryTrustee(idTrustee);
        }
       // System.out.println("ADD "+trustor.getQueryTrustor()+" "+trustee.getQueryTrustee()+" "+value.getValue());
        trustee.addValues(value);
    }

}
*/
