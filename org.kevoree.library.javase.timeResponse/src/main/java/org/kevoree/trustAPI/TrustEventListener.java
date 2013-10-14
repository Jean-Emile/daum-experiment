package org.kevoree.trustAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 14/10/13
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
public interface TrustEventListener {

    public void onNewFactor(String context, String factorName, String idTrustor, String value);
    public void onFactorValueChange(String context, String factorName, String idTrustor, String value);

}
