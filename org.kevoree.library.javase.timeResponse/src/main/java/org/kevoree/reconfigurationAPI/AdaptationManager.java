package org.kevoree.reconfigurationAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.trustAPI.Metric;
import org.kevoree.trustAPI.VariableProducer;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 03/04/13
 * Time: 17:53
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "JavaSE")
@ComponentType
@DictionaryType({
        @DictionaryAttribute(name = "configFileName" , defaultValue = "config.xml")
})
@Provides({
        @ProvidedPort(name = "reconfigure", type = PortType.SERVICE, className = AdaptationManager.class)
})
public class AdaptationManager extends AbstractComponentType {

    private String fileName = null;

    @Start
    public void start() {
        fileName = this.getDictionary().get("configFileName").toString();
        //read reconfigaration file
    }

    @Port(method = "reconfigure", name = "reconfigure")
    public void reconfigure() {

        //Launch kevscript according to the rules read from fileName

    }
}
