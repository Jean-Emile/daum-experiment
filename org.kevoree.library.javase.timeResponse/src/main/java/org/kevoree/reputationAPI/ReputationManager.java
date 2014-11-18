package org.kevoree.reputationAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.kevReflection.ScriptEngine;
import org.kevoree.reputationmetamodel.*;
import org.kevoree.reputationmetamodel.impl.DefaultReputationmetamodelFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 24/10/13
 * Time: 11:54
 * To change this template use File | Settings | File Templates.
 */
@Provides({
        @ProvidedPort(name = "sendReputation", type = PortType.SERVICE, className = IReputationEngine.class),
        @ProvidedPort(name = "receiveClaim", type = PortType.MESSAGE)
})
@Library(name = "Reputation")
@ComponentFragment
public class ReputationManager extends AbstractComponentType implements IReputationEngine, IClaimContainer {

    private ReputationmetamodelFactory repFactory = null;
    private ReputationRoot repRoot = null;
    private ReputationRulesEngine rre = null;
    private ScriptEngine se = null;

    @Start
    public void start() {
        System.out.println("initializing reputation manager");
        repFactory = new DefaultReputationmetamodelFactory();
        repRoot = repFactory.createReputationRoot();
        se = new ScriptEngine( getKevScriptEngineFactory().createKevScriptEngine() );
        rre = new ReputationRulesEngine( "RepRules.policy", se );
    }

    @Stop
    public void stop() {

    }

    @Update
    public void update() {

    }

    /****************************METHODS ASSOCIATED TO PORTS******************************************/
    //It receives a claim from an external entity and stores it
    @Port(name = "receiveClaim")
    public final void receiveClaim(Object rs) {

        String context = null, source = null, target = null, timeStamp = null;
        ClaimInfo claim = null;
        int id = -1;
        if (rs instanceof ReputationStatementInfo) {
            context = ((ReputationStatementInfo) rs).getContext();
            source =  ((ReputationStatementInfo) rs).getSource();
            target =  ((ReputationStatementInfo) rs).getTarget();
            claim = ((ReputationStatementInfo) rs).getClaim();
            timeStamp = ((ReputationStatementInfo) rs).getTimeStamp();
            id = ((ReputationStatementInfo) rs).getId();
        }

        /*System.out.println("Reputation manager receives claim from: "
                            + source + " about " + claim.getName() + " of " + target +
                            " with value " + claim.getValue()
                            );        */
        //We always want to create a new claim (with a new value)
        Claim c = repFactory.createClaim();
        ClaimValue cv = repFactory.createClaimValue();

        //We create the source if it does not exist yet
        Source s = repRoot.findSourcesByID(source);
        if( s == null )
        {
            s = repFactory.createSource();
            s.setIdSource(source);
            repRoot.addSources(s);
        }

        //We fill in the claim
        //The id of a claim is the name of the claim, the target, and the time stamp
        c.setIdClaim( claim.getName() + target + timeStamp );
        c.setName( claim.getName() );
        cv.setValue( claim.getValue() );
        cv.setTimeStamp( timeStamp );
        c.setClaimValue( cv );
        repRoot.addClaims(c);

        //We create the target if it does not exist yet
        Target t = repRoot.findTargetsByID(target);
        if ( t == null )
        {
            t = repFactory.createTarget();
            t.setIdTarget(target);
            repRoot.addTargets(t);
        }

        //We create the reputation statement
        ReputationStatement rstatement = repFactory.createReputationStatement();
        rstatement.setIdRepStatement( String.valueOf( id ) );
        rstatement.setContext(context);
        rstatement.setSource(s);
        rstatement.setTarget(t);
        rstatement.addClaim(c);
        repRoot.addStatements(rstatement);
    }

    //ReputationManager should be extended to implement the concrete reputation engine
    @Port(name = "sendReputation", method = "computeReputation")
    public final String computeReputation(String context, String idTarget)
    {
        String res = compute(context, idTarget);
        rre.executeRules( getModelService().getLastModel(), idTarget, res );
        return res;
    }

    public String compute(String context, String idTarget)
    {
        return null;
    }
    /**********************************************************************/

    /**** AUXILIARY METHODS ***/
    //It gets all the claims about an issue of the target (given by the name of the claim) in a context
    //This method may be used by the computeReputation method implemented by the developer
    public final List<ClaimInfo> sendClaims(String context, String name, String target) {

        List<ClaimInfo> claims = new ArrayList<ClaimInfo>();
        for ( ReputationStatement rs : repRoot.getStatements() )
        {
            if ( rs.getContext().equals( context ) &&
                    rs.getTarget().getIdTarget().equals( target ))
            {
                for( Claim claim : rs.getClaim() )
                {
                    if( claim.getName().equals( name ))
                    {
                        ClaimInfo ci = new ClaimInfo( claim.getName(), claim.getClaimValue().getValue() );
                        claims.add( ci );
                    }
                }
            }
        }
        return claims;
    }
}
