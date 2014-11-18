package org.kevoree.reputationAPI;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/10/13
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public interface IClaimSource {
    public void makeClaim(String name, String value, String target);
}
