package org.kevoree.trustAPI;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 18/10/13
 * Time: 14:24
 * To change this template use File | Settings | File Templates.
 */
public class TrustValueInfo {

    String idSender = null;
    String context = null;
    String value = null;

    public TrustValueInfo() {}

    public TrustValueInfo(String id, String context, String value) {
        this.idSender = id;
        this.context = context;
        this.value = value;
    }

    public String getIdSender() {
        return idSender;
    }

    public String getContext() {
        return context;
    }

    public String getValue() {
        return value;
    }

    public void setIdSender(String id) {
        idSender = id;
    }

    public void setContext(String c) {
        context = c;
    }

    public void setValue(String v) {
        value = v;
    }
}
