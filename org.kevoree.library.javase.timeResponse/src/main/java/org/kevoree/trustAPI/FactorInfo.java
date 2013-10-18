package org.kevoree.trustAPI;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 16/10/13
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public final class FactorInfo implements Serializable {

    private String idSender = null;
    private String name = null;
    private String context = null;
    private String value = null;
    private String target = null; //The entity that refers the factor to

    public FactorInfo() {}

    public FactorInfo(String id, String c, String n, String v) {
        idSender = id;
        context = c;
        name = n;
        value = v;
        target = null;
    }

    public FactorInfo(String id, String c, String n, String v, String t) {
        idSender = id;
        context = c;
        name = n;
        value = v;
        target = t;
    }

    public String getIdSender() {
        return idSender;
    }

    public String getContext() {
        return context;
    }

    public String getFactorName() {
        return name;
    }

    public String getFactorValue() {
        return value;
    }

    public String getTarget() {
        return target;
    }

    public void setFactorName(String n) {
        name = n;
    }

    public void setIdSender(String id) {
        idSender = id;
    }

    public void setContext(String c) {
        context = c;
    }

    public void setFactorValue(String v) {
        value = v;
    }

    public void setTarget(String t) {
        target = t;
    }
}
