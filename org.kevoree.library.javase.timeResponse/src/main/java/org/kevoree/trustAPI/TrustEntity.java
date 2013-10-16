package org.kevoree.trustAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.trustmetamodel.*;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 15/10/13
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */

@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext",optional = false),
        @DictionaryAttribute(name = "metric", defaultValue ="MyTrustEngine", optional = false),
        @DictionaryAttribute(name = "role", defaultValue = "trustor", vals= {"trustor", "trustee"}, optional = false)
})

//Up to now, the trustor manages metrics variables
@Provides({
        //@ProvidedPort(name = "serviceAddVariable", type = PortType.SERVICE, className = ITrustEntity.class),
        @ProvidedPort(name = "factorManagement", type = PortType.SERVICE, className = ITrustEntity.class)
})
@Requires({
        @RequiredPort(name = "trustManagement", type = PortType.SERVICE, className = ITrustModel.class, optional = true, needCheckDependency = true),
        @RequiredPort(name = "instance", type = PortType.MESSAGE, className = ITrustMetric.class, optional = true, needCheckDependency = true)
})
@Library(name = "Trust")
@ComponentType
public class TrustEntity extends AbstractComponentType implements ITrustEntity {

    @Start
    public void start() throws TrustException {

        System.out.println("Trust Entity " + getModelElement().getName() + " started");
        System.out.println("Checking whether " + getModelElement().getName() + " is a trustor");
        if (getDictionary().get("role").equals("trustor")) {

            if (!isPortBinded("instance")) {
                throw new TrustException("A trustor must be binded to a trust engine");
            }

            System.out.println("... and it is indeed");
            getPortByName("trustManagement", ITrustModel.class).
                    initializeTrustRelationships(getDictionary().get("trustContext").toString(),
                            getModelElement().getName(),
                            getPortByName("instance", ITrustMetric.class).getInstance(this)
                    );

        } else if (!getDictionary().get("role").equals("trustee")) {
            throw new TrustException("Wrong role for " + getModelElement().getName());
        }
    }

    @Stop
    public void stop() {

    }

    @Update
    public void update() {

    }

    //This method retrieves the trustor's metric for a specific context
    public AbstractMetric getMetric(String context) {
        return getPortByName("trustManagement", ITrustModel.class).getMetric(context, getModelElement().getName());
    }

    //For simplicity, we assume that the trust relationship already exists and that therefore we only update the trust value
    public void updateTrustRelationship(String context, String idTrustee) {

        if (getDictionary().get("role").equals("trustor")) {

            System.out.println(getModelElement().getName() + " is gonna call updateTrustRelationship");
            getPortByName("trustManagement", ITrustModel.class).updateTrustRelationship(context, getModelElement().getName(), idTrustee);
            System.out.println(getModelElement().getName() + " is back from updateTrustRelationship");

        }
    }

    public String getTrustValue(String context, String idTrustee) {
        return getPortByName("trustManagement", ITrustModel.class).getTrustValue(context, getModelElement().getName(), idTrustee);
    }

    //@Port(method = "addSubjectiveFactor",name = "factorManagement")
    public void addSubjectiveFactor(String context, String name, String value) {
       getPortByName("trustManagement", ITrustModel.class).addSubjectiveFactor(context, name, value);
    }

    //@Port(name="factorManagement", method="isTrustee")
    public boolean isTrustee(String potentialTrusteeName) {
        return getPortByName("trustManagement", ITrustModel.class).isTrustee(potentialTrusteeName);
    }

    @Override
    @Port(name="factorManagement", method="getVariable")
    public Variable getVariable(String context, String name) {
        System.out.println(getModelElement().getName() + " is calling getVariable " + context + name);
        System.out.println("C'monnn, what's going on??");
        getPortByName("trustManagement", MessagePort)
        new Thread(new Runnable() {

            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        }) ;
        return getPortByName("trustManagement", ITrustModel.class).getVariable(context, name);
        //getPortByName("trustManagement", ITrustModel.class).addSubjectiveFactor("myContext", "prueba", "value1");
        //System.out.println("This is reachead");
        return null;
    }

    /*
    @Override
    @Port(name="factorManagement", method="registerMetric")
    public void registerMetric(String context) {
        System.out.println("Metric with id " + context + getModelElement().getName() + " registered");
        registeredMetrics.add(context + getModelElement().getName());
    }


    private void notifyMetrics(String ctx, String factorName, String newValue) {

        System.out.println("Notifying all the metrics about a change in factor " + factorName);

        for (org.kevoree.trustmetamodel.Metric m : trustModel.getMetrics()) {
            if (registeredMetrics.contains(m.getIdMetric())) {
                AbstractMetric am = (AbstractMetric) m.getEngine();
                am.onFactorValueChange(ctx, factorName, getModelElement().getName(), newValue);
                am.onNewFactor(ctx, factorName, getModelElement().getName(), newValue);
            }
        }
    }   */


}
