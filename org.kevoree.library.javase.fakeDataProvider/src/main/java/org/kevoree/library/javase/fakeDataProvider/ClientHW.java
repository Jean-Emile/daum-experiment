package org.kevoree.library.javase.fakeDataProvider;

import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 15/01/13
 * Time: 17:25
 * To change this template use File | Settings | File Templates.
 */


@ComponentType
@Requires({
        @RequiredPort(name = "cpu", type = PortType.SERVICE, className = Ihardware.class, optional = true, needCheckDependency = true)
})
@Library(name="JavaSE")
public class ClientHW extends AbstractComponentType {

    @Start
    public void start() {

        getModelService().registerModelListener(new ModelListener() {
            @Override
            public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean afterLocalUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void modelUpdated() {

                System.out.println("Average CPU Load: " + getPortByName("cpu",Ihardware.class).getCpu());
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void preRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void postRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

    }

    @Stop
    public void stop() {

    }


}
