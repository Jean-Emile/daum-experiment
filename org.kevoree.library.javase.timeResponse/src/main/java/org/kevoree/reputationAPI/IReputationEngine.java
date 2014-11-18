package org.kevoree.reputationAPI;

/**
 * Created by franciscomoyanolara on 02/07/14.
 */
public interface IReputationEngine {
    public Object computeReputation(String context, String target);
}
