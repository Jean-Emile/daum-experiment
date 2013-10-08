package org.kevoree.library.javase.timeResponse;


import org.kevoree.trustAPI.Metric;
import org.kevoree.trustmetamodel.Variable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/03/13
 * Time: 09:51
 * To change this template use File | Settings | File Templates.
 */

public class MyTrustEngine extends Metric {
    @Override
    public Object compute() {
        Variable x = getVariable("mycontext", "ttr");
        Variable y = getVariable("mycontext", "ttr2");

        float ttr1 = Float.parseFloat(x.getValue().getValue());
        float ttr2 = Float.parseFloat(x.getValue().getValue());
        return ttr1 / ttr2;  //To change body of implemented methods use File | Settings | File Templates.
    }
}