package org.kevoree.trustAPI;

import org.kevoree.library.javase.timeResponse.MyTrustEngine;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 28/03/13
 * Time: 11:09
 * To change this template use File | Settings | File Templates.
 */
public class MetricFactory {

    //Could be better implemented using enums and switch statement
    static Metric createMetricInstance(String metricName) {
        Metric m = null;
        if(metricName.equals("MyTrustEngine")) {
            m = new MyTrustEngine();
        }
        return m;
    }
}
