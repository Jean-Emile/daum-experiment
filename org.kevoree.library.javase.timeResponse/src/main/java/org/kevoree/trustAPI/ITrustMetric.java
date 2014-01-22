package org.kevoree.trustAPI;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 14/10/13
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */
public interface ITrustMetric {
    //Compute trust in a given trustee
    public Object compute(String idTrustee);
}
