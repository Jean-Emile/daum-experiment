package org.kevoree.trustAPI;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 16/10/13
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class FactorInfo implements Serializable {

    private String name = null;
    private String context = null;
    private String value = null;

    public FactorInfo(String c, String n, String v) {
        context = c;
        name = n;
        value = v;
    }

    public String getContext() {
        return context;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
