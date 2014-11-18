package org.kevoree.trustAPI;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 17/10/13
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public final class TrustEventInfo implements Serializable {

    private TrustEventType type = null;
    private FactorInfo fi = null;
    private TrustValueInfo tvi = null;

    public TrustEventInfo() {
        fi = new FactorInfo();
        tvi = new TrustValueInfo();
    }

    public TrustEventInfo(TrustEventType type, String context, String idSender, String factorName, String value) {
        this.type = type;
        fi = new FactorInfo(idSender, context, factorName, value);
    }

    public TrustEventInfo(TrustEventType type, String context, String idSender, String factorName, String value, String target) {
        this.type = type;
        fi = new FactorInfo(idSender, context, factorName, value, target);
    }

    public TrustEventInfo(TrustEventType type, String context, String idSender) {
        this.type = type;
        tvi = new TrustValueInfo(idSender, context);
    }

    public TrustEventType getType() {
        return type;
    }

    public FactorInfo getFactorInfo() {
        return fi;
    }

    public TrustValueInfo getTrustValueInfo() {
        return tvi;
    }

    public void setType(TrustEventType t) {
        type = t;
    }

}
