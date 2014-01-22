package org.kevoree.library.javase.reputationAPI;

import org.kevoree.reputationmetamodel.Claim;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/10/13
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public interface IClaimContainer {
    public List<Claim> getClaims(String context, String target);
    public List<Claim> requestClaims(String context, String target);
}
