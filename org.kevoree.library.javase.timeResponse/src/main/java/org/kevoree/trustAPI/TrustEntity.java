package org.kevoree.trustAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.kevoree.library.javase.timeResponse.MyTrustEngine;
import org.kevoree.trustmetamodel.Variable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 15/10/13
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */

@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext",optional = false),
        @DictionaryAttribute(name = "defaultTrustValue", defaultValue ="0", optional = false),
        @DictionaryAttribute(name = "role", defaultValue = "trustor", vals= {"trustor", "trustee", "both"}, optional = false)
})

//Up to now, the trustor manages metrics variables
/*@Provides({
        //@ProvidedPort(name = "serviceAddVariable", type = PortType.SERVICE, className = ITrustEntity.class),
        @ProvidedPort(name = "factorManagement", type = PortType.SERVICE, className = ITrustEntity.class)
})*/
@Requires({
       // @RequiredPort(name = "trustRelationInit", type = PortType.MESSAGE, optional = true, needCheckDependency = true),
        @RequiredPort(name = "trustRelationUpdate", type = PortType.MESSAGE, optional = true, needCheckDependency = true),
        @RequiredPort(name = "factorAddition", type = PortType.MESSAGE, optional = true, needCheckDependency = true),
        @RequiredPort(name = "trustManagement", type = PortType.SERVICE, className = ITrustModel.class, optional = true, needCheckDependency = true),
        @RequiredPort(name = "compute", type = PortType.SERVICE, className = ITrustMetric.class, optional = true, needCheckDependency = true)
})
@Library(name = "Trust")
@ComponentType
public class TrustEntity extends AbstractComponentType {

    @Start
    public void start() throws TrustException {

        System.out.println("Trust Entity " + getModelElement().getName() + " started");
        System.out.println("Checking whether " + getModelElement().getName() + " is a trustor");
        if (getDictionary().get("role").equals("trustor")) {

            if (!isPortBinded("compute")) {
                throw new TrustException("A trustor must be binded to a trust engine");
            }

            System.out.println("... and it is indeed. Therefore, we initialize its trust relationships");
            //We send this information to the trust model
            getPortByName("trustManagement", ITrustModel.class).initializeTrustRelationships(
                                                                        getDictionary().get("trustContext").toString(),
                                                                        getModelElement().getName(),
                                                                        getDictionary().get("defaultTrustValue").toString()
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
    public final AbstractMetric getMetric(String context) {
        return getPortByName("trustManagement", ITrustModel.class).getMetric(context, getModelElement().getName());
    }

    public final void updateTrustRelationship(String context, String idTrustee, String newValue) {

        if (getDictionary().get("role").equals("trustor")) {

            System.out.println(getModelElement().getName() + " is gonna call updateTrustRelationship");
            getPortByName("trustRelationUpdate", MessagePort.class).process(new TrustModelInfo(
                                                                                    context,
                                                                                    getModelElement().getName(),
                                                                                    idTrustee,
                                                                                    newValue)
                                                                            );
            System.out.println(getModelElement().getName() + " is back from updateTrustRelationship");

        }
    }

    public final String getTrustValue(String context, String idTrustee) {
        return getPortByName("trustManagement", ITrustModel.class).getTrustValue(context, getModelElement().getName(), idTrustee);
    }

    //@Port(method = "addSubjectiveFactor",name = "factorManagement")
    public final void addSubjectiveFactor(String context, String name, String value) {
       //getPortByName("trustManagement", ITrustModel.class).addSubjectiveFactor(context, name, value);
       System.out.println("In " + getModelElement().getName() + ", adding subjective factor " + context + name + " with value " + value);
       getPortByName("factorAddition", MessagePort.class).process( new FactorInfo( context, name, value ) );
    }

    public final Object computeTrust() {
        System.out.println("I'm " + getModelElement().getName() + " and I'm gonna compute trust");
        return getPortByName("compute", ITrustMetric.class).compute();

    }

    public final boolean isTrustee(String potentialTrusteeName) {
        return getPortByName("trustManagement", ITrustModel.class).isTrustee(potentialTrusteeName);
    }


}
