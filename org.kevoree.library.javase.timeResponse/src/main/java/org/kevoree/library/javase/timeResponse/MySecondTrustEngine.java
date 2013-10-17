package org.kevoree.library.javase.timeResponse;

import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.trustAPI.AbstractMetric;
import org.kevoree.trustAPI.ITrustEntity;
import org.kevoree.trustmetamodel.Variable;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 14/10/13
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Trust")
@ComponentType
public class MySecondTrustEngine extends AbstractMetric implements ModelListener {

    private boolean isAlive = false;

    @Override
    public boolean afterLocalUpdate(ContainerRoot cr, ContainerRoot cr1) {
        return true;
    }

    @Override
    public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void modelUpdated() {
        //compute();
    }

    @Override
    public void preRollback(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void postRollback(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Start
    public void start() {

        System.out.println("My Trust Engine started");
        isAlive = true;
        //System.out.println("My Trust Engine yields now "+ compute());
        //getModelService().registerModelListener(this);
    }

    @Stop
    public void stop() {

    }

    @Update
    public void update() {

    }

    public Object compute() {

        float res = 50f;

        if (isAlive) {
            System.out.println("My Trust Engine is alive");

            System.out.println("Calling getPortByName... ");
            ITrustEntity o = getPortByName("factorManagement", ITrustEntity.class);

            if (o == null) {
                System.out.println("...but it returns null");
            }

            Variable x = o.getVariable("myContext", "prejudice");
            //getVariable("myContext", "prejudice");
            if (x != null) {
                System.out.println("Variable obtained from metamodel: " + x);
                res = Float.parseFloat(x.getValue().getValue());
            }

        } else {
            System.out.println("My Trust Engine is NOT alive");
        }
        return res * 5;

    }

}
