package org.kevoree.trustAPI;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 16/10/13
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class TrustModelInfo implements Serializable {

    private String context = null;
    private String idTrustor = null;
    private String idTrustee = null;
    private String idMetric = null;
    private String newValue = null;
    //private AbstractMetric am = null;

    public TrustModelInfo(String ctx, String idTrustor) {
        context = ctx;
        this.idTrustor = idTrustor;
    }

    public TrustModelInfo(String ctx, String idTrustor, String idTrustee, String newValue) {
        context = ctx;
        this.idTrustor = idTrustor;
        this.idTrustee = idTrustee;
        this.newValue = newValue;
    }


    public String getContext() {
        return context;
    }

    public String getIdTrustor() {
        return idTrustor;
    }

    public String getIdTrustee() {
        return idTrustee;
    }

    public String getIdMetric() {
        return idMetric;
    }

    public String getNewValue() {
        return newValue;
    }

    /*public AbstractMetric getMetric() {
        return am;
    } */

}
