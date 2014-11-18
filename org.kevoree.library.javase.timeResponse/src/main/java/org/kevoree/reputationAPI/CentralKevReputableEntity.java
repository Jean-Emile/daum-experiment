package org.kevoree.reputationAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.kevoree.trustAPI.TrustException;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/10/13
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
@DictionaryType({
        @DictionaryAttribute(name = "trustContext", optional = false),
        @DictionaryAttribute(name = "group", optional = true)
})

@Requires({
        @RequiredPort(name = "retrieveReputation", type = PortType.SERVICE, className = IReputationEngine.class, optional = true),
        @RequiredPort(name = "receiveClaim", type = PortType.MESSAGE, optional = true)
})

@Library(name = "Reputation")
@ComponentType
public class CentralKevReputableEntity extends AbstractComponentType implements IClaimSource {

    @Start
    public void start() throws TrustException {
        System.out.println("initializing central reputable entity");
        if (!isPortBinded("retrieveReputation")) {
            throw new TrustException("Port retrieveReputation must be binded for centralized reputation entity");
        }
    }

    /******************** METHODS ASSOCIATED TO PORTS **************************/
    //Centralized reputable entity must ask for reputation to the reputation manager
    public final String getReputation( String target ) {
        System.out.println( getModelElement().getName() + " is retrieving reputation of " + target);
        return (String) getPortByName("retrieveReputation", IReputationEngine.class).
                                computeReputation(getContext(), target);
    }

    //Centralized reputable entity must send the claim to the reputation manager
    private void sendClaim( ClaimInfo claim, String idTarget ) {
        ReputationStatementInfo rs = new ReputationStatementInfo( getContext(),
                                                                  getModelElement().getName(),
                                                                  claim,
                                                                  idTarget,
                                                                  "any date"
                                                                );
        getPortByName("receiveClaim", MessagePort.class).process( rs );
    }
    /***************************************************************************/

    /****** AUXILIARY METHODS *****/
    public final void makeClaim(String name, String value, String target)
    {
        sendClaim( new ClaimInfo( name, value ), target );
    }

    public final String getBelongingGroup()
    {
        return ( String ) getDictionary().get("group");
    }

    public final String getContext()
    {
        return ( String ) getDictionary().get("trustContext");
    }
    /*****************************/



}
