package org.kevoree.trustAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 15/10/13
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */

/* This class represents a trust entity, which can be a trustor or a trustee, or both*/
@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext",optional = false),
        @DictionaryAttribute(name = "defaultTrustValue", defaultValue ="0", optional = false),
        @DictionaryAttribute(name = "role", defaultValue = "trustor", vals= {"trustor", "trustee", "both"}, optional = false),
        @DictionaryAttribute(name = "group", optional = true)
})

//Up to now, the trustor manages metrics variables
/*@Provides({
        //@ProvidedPort(name = "serviceAddVariable", type = PortType.SERVICE, className = ITrustEntity.class),
        @ProvidedPort(name = "factorManagement", type = PortType.SERVICE, className = ITrustEntity.class)
})*/
@Requires({
        @RequiredPort(name = "trustRelationUpdate", type = PortType.MESSAGE, optional = true, needCheckDependency = true),
        @RequiredPort(name = "factorAddition", type = PortType.MESSAGE, optional = true, needCheckDependency = true),
        @RequiredPort(name = "trustManagement", type = PortType.SERVICE, className = ITrustModel.class, optional = true, needCheckDependency = true),
        @RequiredPort(name = "compute", type = PortType.SERVICE, className = ITrustMetric.class, optional = true, needCheckDependency = true)
})
@Provides({
        @ProvidedPort(name = "trustEntityNotification", type = PortType.MESSAGE)
})
@Library(name = "Trust")
@ComponentFragment
public abstract class TrustEntity extends AbstractComponentType implements ITrustEntity, ITrustValueChangeEvent {

    @Start
    public void start() throws TrustException {

        System.out.println("Trust Entity " + getModelElement().getName() + " started");
        System.out.println("Checking whether " + getModelElement().getName() + " is a trustor");
        if (getDictionary().get("role").equals("trustor") ||
                getDictionary().get("role").equals("both")) {

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

    @Port(name="trustEntityNotification")
    public abstract void onTrustValueChange(Object tInfo);

    //It updates the trust value of a trust relationship with a trustee with id 'idtrustee' and in the context
    //'context'
    public final void updateTrustRelationship(String idTrustee) {

        if (getDictionary().get("role").equals("trustor")) {

            System.out.println(getModelElement().getName() + " is gonna call updateTrustRelationship, and this relationship" +
                    "will update with value " + (String) computeTrust(idTrustee));

            //It communicates the update information, through a message port, to the trust model
            getPortByName("trustRelationUpdate", MessagePort.class).process(new TrustRelationInfo(
                    getContext(),
                    getModelElement().getName(),
                    idTrustee,
                    (String) computeTrust(idTrustee))
            );
            System.out.println(getModelElement().getName() + " is back from updateTrustRelationship");

        }
    }

    //It gets the trust value of the current trust entity with the trustee 'idTrustee' in the context
    public final String getTrustValue(String idTrustee) {
        return getPortByName("trustManagement", ITrustModel.class).getTrustValue(getContext(), getModelElement().getName(), idTrustee);
    }

    //@Port(method = "addSubjectiveFactor",name = "factorManagement")
    //It inserts a factor that it's subjective (belongs to the trust entity). Factor producers only can generate and insert
    //objective trust factors about other entities
    public final void addSubjectiveFactor(String name, String value) {
        System.out.println("In " + getModelElement().getName() + ", adding subjective factor " + getContext() + name + " with value " + value);
        getPortByName("factorAddition", MessagePort.class).process( new FactorInfo
                                                                (getModelElement().getName(),
                                                                getContext(),
                                                                name,
                                                                value )
                                                                    );
    }

    public final void addSubjectiveFactor(String name, String value, String target)
    {
        System.out.println("In " + getModelElement().getName() + ", adding subjective factor " +
                getContext() + name + " with value " + value + " about " + target);
        getPortByName("factorAddition", MessagePort.class).process( new FactorInfo
                                                            (getModelElement().getName(),
                                                             getContext(),
                                                             name,
                                                             value,
                                                             target)
                                                            );
    }

    //It computes the trust value between the current entity and a target trustee
    public final Object computeTrust(String idTrustee) {
        System.out.println("I'm " + getModelElement().getName() + " and I'm gonna compute trust");
        return getPortByName("compute", ITrustMetric.class).compute( getModelElement().getName(), idTrustee );

    }

    //It returns whether a given entity with id potentialTrusteeName is a trustee of the current entity
    public final boolean isTrustee(String potentialTrusteeName) {
        return getPortByName("trustManagement", ITrustModel.class).isTrustee(potentialTrusteeName);
    }

    public final String getContext()
    {
        return (String) getDictionary().get("trustContext");
    }

}
