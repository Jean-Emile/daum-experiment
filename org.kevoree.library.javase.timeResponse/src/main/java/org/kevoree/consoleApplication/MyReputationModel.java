package org.kevoree.consoleApplication;

import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Library;
import org.kevoree.reputationAPI.ClaimInfo;
import org.kevoree.reputationAPI.ReputationManager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 14/11/13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
@ComponentType
@Library(name="Application")
public final class MyReputationModel extends ReputationManager {

    @Override
    public String compute(String context, String idTarget) {
        List<ClaimInfo> claims = sendClaims(context, "cleanWords", idTarget);
        float res = 0.0f;

        if (claims != null) {
            if( claims.size() == 0 )
            {
                return "1"; //Initial reputation
            }
            for (ClaimInfo c : claims) {
                res += Float.parseFloat(c.getValue());
            }
        } else {
            System.out.println("claims is null...");
        }
        System.out.println("Calculated reputation of " + idTarget + ". Value " + res);
        return String.valueOf(res);
    }
}
