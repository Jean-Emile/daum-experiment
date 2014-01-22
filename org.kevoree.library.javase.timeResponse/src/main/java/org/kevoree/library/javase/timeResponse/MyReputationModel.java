package org.kevoree.library.javase.timeResponse;

import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Library;
import org.kevoree.library.javase.reputationAPI.ReputationModel;
import org.kevoree.reputationmetamodel.Claim;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 14/11/13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
@ComponentType
@Library(name="Trust")
public final class MyReputationModel extends ReputationModel {

    @Override
    public String getReputation(String context, String idTarget) {

        List<Claim> claims = getClaims(context, idTarget);
        float res = -1.0f;
        int totalClaims = -1;

        if (claims != null) {
            System.out.println("Accessing all the claims of " + idTarget);
            totalClaims = claims.size();

            for (Claim c : claims) {
                System.out.println("Claim " + c.getIdClaim() + ": " + c.getClaimValue().getValue());
                res += Float.parseFloat(c.getClaimValue().getValue()) / totalClaims;
            }
        } else {
            System.out.println("Claims is null...");
        }

        System.out.println("Returning a reputation of " + res);
        return String.valueOf(res);
    }
}
