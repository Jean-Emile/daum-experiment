package org.kevoree.library.javase.timeResponse;


import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.framework.MessagePort;
import org.kevoree.trustAPI.*;
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

    private boolean started = false;

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
        started = true;


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

        System.out.println("I'm a trust engine, and I'm gonna compute a new trust value");
        if (started) {
            //System.out.println("My Trust Engine is alive");

            //System.out.println("Calling getPortByName... ");
            ITrustModel o = getPortByName("trustManagement", ITrustModel.class);

            if (o == null) {
                System.out.println("...but it returns null");
            }

            System.out.println("At this point, me, the trust engine, it's trying to get the variable it needs");
            Variable x = o.getVariable("myContext", "prejudice");
                //getVariable("myContext", "prejudice");
            if (x != null) {
                //System.out.println("Variable obtained from metamodel: " + x);
                res = Float.parseFloat(x.getValue().getValue());
            }

        } else {
            System.out.println("My Trust Engine is NOT alive");
        }

        System.out.println("I'm coming back from compute() with value " + res * 2);

        setLastValueComputed(res * 2);
        return res * 2;

    }

    @Override
    public void onFactorChange(Object tInfo) {
        System.out.println(getModelElement().getName() + " received an event notification");

        String context = null, factorName = null, value = null;
        FactorInfo fi = null;

        if (tInfo instanceof TrustEventInfo) {
            fi = ((TrustEventInfo) tInfo).getFactorInfo();
            context = fi.getContext();
            factorName = fi.getFactorName();
            value = fi.getFactorValue();
        }

        if (context.equals("myContext") && factorName.equals("prejudice")) {
            System.out.println(getModelElement().getName() + " is gonna notify trust entities");
            setLastValueComputed(Float.parseFloat(value) * 2);
            //Notify the trustors
            notifyTrustEntities();
        }
    }

    public String toString() {
        return "MyTrustEngine";
    }

}