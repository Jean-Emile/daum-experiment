package org.kevoree.trustAPI;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 18/10/13
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public interface ITrustValueChangeEvent {
    public void onNewTrustValueAvailable(TrustEventInfo tInfo);
}
