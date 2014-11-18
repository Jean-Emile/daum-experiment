package org.kevoree.consoleApplication;


import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.trustAPI.*;
import org.kevoree.trustmetamodel.Factor;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/03/13
 * Time: 09:51
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "Application")
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

        //getModelService().registerModelListener(this);
    }

    @Stop
    public void stop() {

    }

    @Update
    public void update() {

    }

    public Object compute(String idTrustor, String idTrustee) {

        //float res = -1.0f;
        String res = null;

        System.out.println("I'm a trust engine, and I'm gonna compute a new trust value");
        if (started) {
            //Each trust engine is associated to a unique trustor and trustee
            Factor x = getFactor( "prejudice", idTrustor );
            Factor y = getFactor( "timeToResponse", idTrustee );
            if (x != null && y != null) {
                //System.out.println("Variable obtained from metamodel: " + x);
                float aux1 = Float.parseFloat(x.getValue().getValue());
                float aux2 = Float.parseFloat(y.getValue().getValue());
                res = String.valueOf(aux1 * aux2);
            }

        } else {
            System.out.println("My Trust Engine is NOT alive");
        }

        return res;
        //System.out.println("I'm coming back from compute() with value " + res * 2);
        //return String.valueOf(res * 2);

    }

    //This can be left without implementation (and it will notify all trust entities always)
    //we allow overriding it for more elaborated logic
    @Override
    public void onFactorChange(Object tInfo) {

        System.out.println(getModelElement().getName() + " received an event notification");

        String context = null, factorName = null, value = null;
        FactorInfo fi = null;

        if (tInfo instanceof TrustEventInfo) {
            fi = ((TrustEventInfo) tInfo).getFactorInfo();
            context = fi.getContext();
            factorName = fi.getFactorName();
            //Not necessary value = fi.getFactorValue();

            //In this case we only want to notify trust entities if the context of the factor that changed
            //or the name has changed
            if (context.equals("myContext") &&
                (factorName.equals("prejudice") || factorName.equals("timeToResponse"))) {
                notifyTrustEntities( tInfo );
            }
        }
    }

    public String toString() {
        return "MyTrustEngine";
    }

}