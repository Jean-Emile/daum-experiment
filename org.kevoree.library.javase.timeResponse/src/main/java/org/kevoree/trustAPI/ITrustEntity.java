package org.kevoree.trustAPI;

/*
import org.kevoree.annotation.*;
import org.kevoree.reconfigurationAPI.AdaptationManager;
*/

import org.kevoree.trustmetamodel.Variable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/09/13
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
public interface ITrustEntity {
    public Variable getVariable(String context, String name);
}

