package org.kevoree.library.javase.timeResponse;


import org.kevoree.annotation.*;
import org.kevoree.trustAPI.AbstractMetric;
import org.kevoree.trustAPI.ITrustEntity;
import org.kevoree.trustAPI.TrustEntity;
import org.kevoree.trustmetamodel.Variable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/03/13
 * Time: 09:51
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Trust")
@ComponentType
public class MyTrustEngine extends AbstractMetric {
    @Override
    public Object compute() {

        Variable x = getVariable("myContext", "prejudice");
        float res = -1.0f;
        if (x != null) {
            res = Float.parseFloat(x.getValue().getValue());
        }
        return res * 5;

    }
}