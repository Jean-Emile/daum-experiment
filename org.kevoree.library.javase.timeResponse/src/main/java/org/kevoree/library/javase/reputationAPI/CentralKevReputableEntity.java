package org.kevoree.library.javase.reputationAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.MessagePort;
import org.kevoree.reputationmetamodel.Claim;
import org.kevoree.trustAPI.TrustException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/10/13
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
@Requires({
        @RequiredPort(name = "retrieveReputation", type = PortType.SERVICE, className = IReputationModel.class, optional = false, needCheckDependency = true),
        @RequiredPort(name = "sendClaim", type = PortType.MESSAGE)
})

@Library(name = "Reputation")
@ComponentType
public class CentralKevReputableEntity extends KevReputableEntity {

    @Start
    public void start() throws TrustException {
        if (!isPortBinded("retrieveReputation")) {
            throw new TrustException("Port retrieveReputation must be binded for centralized reputation entity");
        }
    }
    //Centralized reputable entity must ask for reputation to the reputation manager
    public String getReputation(String context, String target) {
        System.out.println("Retrieving reputation of " + target);
        return getPortByName("retrieveReputation", IReputationModel.class).getReputation(context, target);
    }

    //Centralized reputable entity must send the claim to the reputation manager
    @Override
    public void makeClaim(String context, String idTarget, String value) {
        ClaimInfo ci = new ClaimInfo(context, value, getModelElement().getName(), idTarget, "any date");
        System.out.println("sending the claim to the model");
        getPortByName("sendClaim", MessagePort.class).process( ci );
    }
}
