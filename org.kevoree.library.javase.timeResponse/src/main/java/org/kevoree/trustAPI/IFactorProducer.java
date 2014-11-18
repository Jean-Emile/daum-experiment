package org.kevoree.trustAPI;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 24/10/13
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */

//This interface must be implemented by FactorProducer. It simply adds a factor to the trust model
public interface IFactorProducer {
    public void addFactor(String name, String value);
}
