package org.kevoree.library.javase.reputationAPI;

import org.kevoree.reputationmetamodel.Claim;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/10/13
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
public interface IReputationModel {
    public String getReputation(String context, String idTarget);
}
