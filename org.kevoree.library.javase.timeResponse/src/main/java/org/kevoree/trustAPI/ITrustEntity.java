package org.kevoree.trustAPI;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/10/13
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public interface ITrustEntity {

    public void updateTrustRelationship(String idTrustee);
    public String getTrustValue(String idTrustee);
    public void addSubjectiveFactor(String name, String value);
    public Object computeTrust(String idTrustee);
    public boolean isTrustee(String potentialTrusteeName);

}
