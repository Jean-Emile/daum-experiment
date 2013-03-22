package org.kevoree.trustframework.api;




import org.kevoree.Trust.TrustValue;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 19/03/13
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
public interface IVariableProducer
{
    public void addVariable(String trustorquery,String trusteequery,TrustValue value);
}
