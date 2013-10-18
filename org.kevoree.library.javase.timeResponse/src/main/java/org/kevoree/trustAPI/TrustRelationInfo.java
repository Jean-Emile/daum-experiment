package org.kevoree.trustAPI;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 16/10/13
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public final class TrustRelationInfo implements Serializable {

    private String context = null;
    private String idTrustor = null;
    private String idTrustee = null;
    private String newValue = null;

    public TrustRelationInfo(String ctx, String idTrustor) {
        context = ctx;
        this.idTrustor = idTrustor;
    }

    public TrustRelationInfo(String ctx, String idTrustor, String idTrustee, String newValue) {
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

    public String getNewValue() {
        return newValue;
    }

    /*public AbstractMetric getMetric() {
        return am;
    } */

}
