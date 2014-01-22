package org.kevoree.library.javase.reputationAPI;

import org.kevoree.annotation.*;
import org.kevoree.reputationmetamodel.*;
import org.kevoree.trustAPI.TrustException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/10/13
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
@Provides({
        @ProvidedPort(name = "retrieveClaims", type = PortType.SERVICE, className = IClaimContainer.class)
})

@Requires({
        @RequiredPort(name = "provideClaims", type = PortType.SERVICE, className = IClaimContainer.class, optional = true)

})
@Library(name = "Reputation")
@ComponentType
public abstract class DistKevReputableEntity extends KevReputableEntity implements IClaimContainer {

    @Start
    public void start() throws TrustException {
        super.start();
    }

    //A distributed reputable entity can provide the claims stored by itself
    //The method retrieves the claims for a target in a context
    @Override
    @Port(name = "retrieveClaims", method = "getClaims")
    public List<Claim> getClaims(String context, String target) {

        List<Claim> res = new ArrayList<Claim>();
        for (ReputationStatement rs : repRoot.getStatements()) {
            if (rs.getTarget().equals(target)) {
                res.addAll(rs.getClaim());
            }
        }
        return res;
    }


    //A distributed reputable entity can request claims to other entities
    @Override
    public List<Claim> requestClaims(String context, String target) {
        return getPortByName("provideClaims", IClaimContainer.class).getClaims( context, target );
    }

    //A distributed reputable entity must store its own claims
    @Override
    public void makeClaim(String context, String value, String target) {

        Claim c = repFactory.createClaim();
        ClaimValue cv = repFactory.createClaimValue();

        //We create the source
        Source source = repFactory.createSource();
        source.setIdSource(getModelElement().getName());
        repRoot.addSources(source);

        //We create the claim
        c.setIdClaim(context + getModelElement().getName() + target);
        c.setIsCompounded(false);
        cv.setValue(value);
        cv.setTimeStamp("invented date");
        repRoot.addClaims(c);

        //We create the target
        Target t = repFactory.createTarget();
        t.setIdTarget(target);
        repRoot.addTargets(t);

        //We create the reputation statement
        ReputationStatement rs = repFactory.createReputationStatement();
        rs.setIdRepStatement(context + getModelElement().getName() + target);
        rs.setContext(context);
        rs.setSource(source);
        rs.setTarget(t);
        rs.addClaim(c);
        repRoot.addStatements(rs);

    }

    //A distributed reputable entity is in charge itself to compute a reputation score
    public abstract Object compute();


}
