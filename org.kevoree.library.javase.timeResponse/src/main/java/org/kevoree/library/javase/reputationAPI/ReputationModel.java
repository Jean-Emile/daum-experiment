package org.kevoree.library.javase.reputationAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
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
        @ProvidedPort(name = "reputation", type = PortType.SERVICE, className = IReputationModel.class),
        @ProvidedPort(name = "receiveClaim", type = PortType.MESSAGE)
})
@Library(name = "Reputation")
@ComponentFragment
public abstract class ReputationModel extends AbstractComponentType implements IReputationModel {


    private ReputationmetamodelFactory repFactory = null;
    private ReputationRoot repRoot = null;

    @Start
    public void start() {
        System.out.println("initializing reputation model");
        repFactory = new DefaultReputationmetamodelFactory();
        repRoot = repFactory.createReputationRoot();
    }

    @Stop
    public void stop() {

    }

    @Update
    public void update() {

    }

    @Port(name = "receiveClaim")
    public void receiveClaim(Object ci) {


        String context = null, source = null, target = null, value = null, timeStamp = null;
        int id = -1;
        if (ci instanceof ClaimInfo) {
            context = ((ClaimInfo) ci).getContext();
            source =  ((ClaimInfo) ci).getSource();
            target =  ((ClaimInfo) ci).getTarget();
            value = ((ClaimInfo) ci).getValue();
            timeStamp = ((ClaimInfo) ci).getTimeStamp();
            id = ((ClaimInfo) ci).getId();
        }

        System.out.println("Reputation model receives claim with value: " + value
               + " target: " + target);

        Claim c = repFactory.createClaim();
        ClaimValue cv = repFactory.createClaimValue();

        //We create the source
        Source s = repFactory.createSource();
        s.setIdSource(source);
        repRoot.addSources(s);

        //We create the claim
        c.setIdClaim(id + target);
        c.setIsCompounded(false);
        cv.setValue(value);
        cv.setTimeStamp(timeStamp);
        c.setClaimValue(cv);
        repRoot.addClaims(c);

        //We create the target
        Target t = repFactory.createTarget();
        t.setIdTarget(target);
        repRoot.addTargets(t);

        //We create the reputation statement
        ReputationStatement rs = repFactory.createReputationStatement();
        rs.setIdRepStatement(context + source + target);
        rs.setContext(context);
        rs.setSource(s);
        rs.setTarget(t);
        rs.addClaim(c);
        repRoot.addStatements(rs);
    }

    //Get all the claims of a target in a context
    public List<Claim> getClaims(String context, String target) {

        List<Claim> res = new ArrayList<Claim>();

        System.out.println("Looking for the claim in reputation statements");
        for (ReputationStatement rs : repRoot.getStatements()) {
            System.out.println("Reputation statement context " + rs.getContext() +
                                        " and target " + rs.getTarget().getIdTarget());
            if (rs.getContext().equals(context) && rs.getTarget().getIdTarget().equals(target)) {
                res.addAll(rs.getClaim());
            }
        }
        return res;
    }

    //Reputation model should be extended to implement the reputation metric
    @Port(name = "reputation", method = "getReputation")
    public abstract String getReputation(String context, String idTarget);

}
