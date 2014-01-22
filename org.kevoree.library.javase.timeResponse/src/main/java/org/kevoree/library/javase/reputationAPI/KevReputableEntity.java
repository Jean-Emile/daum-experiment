package org.kevoree.library.javase.reputationAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

import org.kevoree.reputationmetamodel.*;
import org.kevoree.reputationmetamodel.impl.DefaultReputationmetamodelFactory;
import org.kevoree.trustAPI.TrustException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/10/13
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */

@ComponentFragment
public abstract class KevReputableEntity extends AbstractComponentType implements IClaimSource {

    protected ReputationmetamodelFactory repFactory = null;
    protected ReputationRoot repRoot = null;

    @Start
    public void start() throws TrustException {
        repFactory = new DefaultReputationmetamodelFactory();
        repRoot = repFactory.createReputationRoot();
    }

}
