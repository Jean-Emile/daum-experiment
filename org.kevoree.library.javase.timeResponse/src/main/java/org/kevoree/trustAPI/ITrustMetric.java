package org.kevoree.trustAPI;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 14/10/13
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */

/* This interface, implemented by AbstractMetric,
allows a trustor with id idTrustor to compute trust
in a given trustee with id idTrustee */
public interface ITrustMetric {
    public Object compute(String idTrustor, String idTrustee);
}
