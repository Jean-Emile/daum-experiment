package org.kevoree.reputationAPI;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/10/13
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public interface IClaimContainer {
    //It sends all the claims that are of a particular context, and about a subject (name) of a target
    public List<ClaimInfo> sendClaims(String ctx, String name, String target);

}
