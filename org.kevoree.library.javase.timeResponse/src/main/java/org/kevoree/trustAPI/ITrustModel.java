package org.kevoree.trustAPI;

import org.kevoree.trustmetamodel.Variable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 15/10/13
 * Time: 14:00
 * To change this template use File | Settings | File Templates.
 */
public interface ITrustModel {

    //public void addSubjectiveFactor(String context, String name, String value);
    //public void updateTrustRelationship(String context, String idTrustor, String idTrustee);
    public boolean initializeTrustRelationships(String context, String trustor, String defaultValue);
    public AbstractMetric getMetric(String context, String idTrustor);
    public String getTrustValue(String context, String idTrustor, String idTrustee);
    public boolean isTrustee(String potentialTrusteeName);
    public Variable getVariable(String context, String name);
}
