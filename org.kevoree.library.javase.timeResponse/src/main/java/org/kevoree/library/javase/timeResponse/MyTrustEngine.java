package org.kevoree.library.javase.timeResponse;


import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.trustAPI.AbstractMetric;
import org.kevoree.trustAPI.ITrustEntity;
import org.kevoree.trustAPI.ITrustMetric;
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
public class MyTrustEngine extends AbstractMetric implements ModelListener {//AbstractMetric implements ModelListener {

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

        super.start();
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

        float res = -1.0f;

        if (isAlive) {
            //System.out.println("My Trust Engine is alive");

            //System.out.println("Calling getPortByName... ");
            ITrustEntity o = getPortByName("factorManagement", ITrustEntity.class);

            if (o == null) {
                System.out.println("...but it returns null");
            }

            Variable x = o.getVariable("myContext", "prejudice");
                //getVariable("myContext", "prejudice");
            if (x != null) {
                //System.out.println("Variable obtained from metamodel: " + x);
                res = Float.parseFloat(x.getValue().getValue());
            }

        } else {
            System.out.println("My Trust Engine is NOT alive");
        }
        return res * 2;

    }

    @Override
    public void onNewFactor(String context, String factorName, String idTrustor, String value) {
        System.out.println("New factor added to the model: " + context + " " + factorName + " " + idTrustor + " " + value);
    }

    @Override
    public void onFactorValueChange(String context, String factorName, String idTrustor, String value) {
        System.out.println("Factor value changed: " + context + " " + factorName + " " + idTrustor + " " + value);

        //When we make sure that the factor is one that affects this metric, we re-compute and notify the trustors
        if (context.equals("myContext") && factorName.equals("prejudice")) {
            //compute();

        }
    }
}