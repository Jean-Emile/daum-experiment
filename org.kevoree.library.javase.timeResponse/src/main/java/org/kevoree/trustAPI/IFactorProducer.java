package org.kevoree.trustAPI;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 24/10/13
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */
public interface IFactorProducer {

    public void addFactor(String context, String name, String value, String target);
}
