package org.kevoree.trustAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
//import org.kevoree.reconfigurationAPI.AdaptationManager;
import org.kevoree.trustmetamodel.TrustRoot;
import org.kevoree.trustmetamodel.TrustmetamodelFactory;
import org.kevoree.trustmetamodel.Variable;
import org.kevoree.trustmetamodel.VariableValue;
import org.kevoree.trustmetamodel.impl.DefaultTrustmetamodelFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.kevoree.trustAPI.GetHelper.getTrusteesInstanceName;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 26/09/13
 * Time: 12:17
 * To change this template use File | Settings | File Templates.
 */

@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext",optional = false),
        @DictionaryAttribute(name = "metric", defaultValue ="MyTrustEngine", optional = false)
})

//Up to now, the trustor manages metrics variables
@Provides({
        //@ProvidedPort(name = "serviceAddVariable", type = PortType.SERVICE, className = VariableProducer.class) ,
        //@ProvidedPort(name = "serviceGetVariable", type = PortType.SERVICE, className = Metric.class)
})
/*@Requires({
        @RequiredPort(name = "reconfigure", type = PortType.SERVICE, className = AdaptationManager.class)
})*/
@Library(name = "Trust")
@ComponentType
public class TrustEntity extends AbstractComponentType implements Runnable {

    public TrustmetamodelFactory factory = null;
    public TrustRoot trustModel = null;
    public boolean alive = false;
    public Thread thread = null;
    //private  Thread reconfiguration_thread = null;

    @Start
    public void start() throws TrustException {

        factory = new DefaultTrustmetamodelFactory();
        trustModel = factory.createTrustRoot();
        thread = new Thread(this);
        alive = true;

        //If it's a trustor
        if (getDictionary().get("role").equals("trustor")) {
            initializeTrustRelationships();
            //If it's not a trustor or a trustee
        } else if (!getDictionary().get("role").equals("trustee")) {
            throw new TrustException("Wrong role for " + getModelElement().getName());
        }
        //For now, we lay reconfiguration aside
        /*reconfiguration_thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (alive)
                {
                    getPortByName("reconfiguration", AdaptationManager.class).reconfigure();

                    try
                    {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        });
        reconfiguration_thread.start();       */
    }

    @Update
    public void update() {
        initializeTrustRelationships();
    }

    @Stop
    public void stop() {
        alive = false;
        //reconfiguration_thread.interrupt();
    }

    //Set all the trust relationships
    //We have to read from the dictionary
    //We will set trust relationships between the trustor and all its trustees
    private void initializeTrustRelationships()
    {
        String context = getDictionary().get("trustContext").toString();
        String idTrustor = getModelElement().getName();
        String trusteeType = getDictionary().get("trusteeType").toString();
        String nameMetric = getDictionary().get("metric").toString();
        //Metric m = MetricFactory.createMetricInstance(nameMetric);

        //Get all the trustees to this trustor
        //trustees store: [Node 0, (CompInstance1, CompInstance2, etc)]
        //                [Node 1, (CompInstance34, CompInstance50, etc)]
        //We need this becuase the same instance name can be used in different nodes, but for the trust model
        //we need a unique identifier for trustees. In this case, "nodeName + instanceName"
        HashMap<String, List<String>> trustees = getTrusteesInstanceName(getModelService().getLastModel(),
                context, trusteeType);

        //For each node, we see their instances and we change their names to "nodeName + instanceName"
        //We accumulate all the instances in the idTrustee list
        List<String> idTrustee = new ArrayList<String>();
        List<String> temp, temp2 = new ArrayList<String>();
        String currentName;
        for (String nodeName: trustees.keySet()) {
            //Get the list of trustees running on this node
            temp = trustees.get(nodeName);
            for (int i = 0; i < temp.size(); i++) {
                currentName = temp.get(i);
                temp2.set(i, nodeName + currentName);
            }
            idTrustee.addAll(temp2);
        }
        //Now, we have all the information regarding the new trust relationship
        //We know: trustor, trustee, context and metric
        //We have to create all the necessary entities in the trust metamodel
        for (String t : idTrustee) {
            //addTrustRelationship(m, context, idTrustor, t);
        }
    }

    //This method stores in the trust model the metric to be used by a trustor for a trustee type under
    //a context. It also creates a trust relationship, and adds trustor and trustee
    /*public void addTrustRelationship(Metric metric, String context, String idTrustor, String idTrustee)
    {
        org.kevoree.trustmetamodel.Trustor trustor = trustModel.findTrustorsByID(idTrustor);
        org.kevoree.trustmetamodel.Trustee trustee = trustModel.findTrusteesByID(idTrustee);
        org.kevoree.trustmetamodel.Metric m = trustModel.findMetricsByID(context + trustor + trustee);
        if (m == null) {
            m = factory.createMetric();
            m.setContext(context);
            m.setIdMetric(context + idTrustor + idTrustee);
            m.setEngine(metric);
        }
        if (trustor == null) {
            trustor = factory.createTrustor();
            trustor.setIdTrustor(idTrustor);
            trustor.addTrustorMetrics(m);
        }
        if (trustee == null)  {
            trustee = factory.createTrustee();
            trustee.setIdTrustee(idTrustee);
        }
        org.kevoree.trustmetamodel.TrustRelationship tr = trustModel.findTRelationshipsByID(context + idTrustor + idTrustee);
        if (tr == null) {
            tr = factory.createTrustRelationship();
            tr.setIdTRelationship(context + idTrustor + idTrustee);
            tr.setContext(context);
            tr.setTrustor(trustor);
            tr.setTrustee(trustee);
            org.kevoree.trustmetamodel.TrustValue tv = factory.createTrustValue();
            tv.setValue("Default value");
            String timeStamp = new SimpleDateFormat("dd/MM/yyy HH:mm").format(new Timestamp(new Date().getTime()));
            tv.setTimeStamp(timeStamp);
            tr.setTrustValue(tv);
        }
    }      */

    //For now, we are going to store the trust model in the Trustor component
    //This will change in the future (it will be shared by all components)
    //This is why the trustor implements the method addVariable, which should be implemented by
    //the VariableProducer
    //@Port(method = "addVariable",name = "serviceAddVariable")
    public void addVariable(String context, String name, String idSource, String idTarget, String value)
    {
        Variable var = trustModel.findVariablesByID(context + name);
        if (var == null) {
            var = factory.createVariable();
        }
        var.setContext(context);
        var.setIdVariable(context + name);
        var.setIdSource(idSource);
        var.setIdTarget(idTarget);
        VariableValue varVal = factory.createVariableValue();
        varVal.setValue(value);
        var.setValue(varVal);
    }

    //@Port(method = "getVariable",name = "serviceGetVariable")
    public Variable getVariable(String context, String name)
    {
        return trustModel.findVariablesByID(context + name);

    }

    /*
    //This method retrieves a metric set by a trustor
    public Metric getMetric(String context, String trustor, String trustee) {
        for (org.kevoree.trustmetamodel.Metric m : trustModel.getMetrics()) {
            if(m.getIdMetric().equals(context+trustor+trustee)) {
                return (Metric) m.getEngine();
            }
        }
        return null;
    }

    public Object compute(String context, String idTrustor, String idTrustee) {
        Metric m = (Metric) trustModel.findMetricsByID(context + idTrustor + idTrustee).getEngine();
        return m.compute();
    }

    public void updateTrustRelationship(String context, String idTrustor, String idTrustee) {
        //Update trust relationship in the model (add timestamp)
        TrustRelationship tr = trustModel.findTRelationshipsByID(context + idTrustor + idTrustee);
        if (tr.getTrustValue().getValue() == null) {
            TrustValue value = factory.createTrustValue();
            Metric m = (Metric) trustModel.findMetricsByID(context + idTrustor + idTrustee).getEngine();
            java.util.Date date= new java.util.Date();
            value.setTimeStamp(String.valueOf(date.getTime()));
            value.setValue(m.compute().toString()); //This may fail
        }
    }

    public void updateAllTrustRelationships(String context, String idTrustor) {
        org.kevoree.trustmetamodel.Trustor trustor = trustModel.findTrustorsByID(idTrustor);
        if (trustor != null) {
            for (org.kevoree.trustmetamodel.Trustee trustee : trustor.getTrustorTrustees()) {
                updateTrustRelationship(context, idTrustor, trustee.getIdTrustee());
            }
        }
    }

    public List<org.kevoree.trustmetamodel.Trustee> getTrustees() {
        return trustModel.getTrustees();
    }
    */
    @Override
    public void run() {
        while (alive)
        {
            //getModelElement().getName() retrieves the trustor ID (instance name of the kevoree component)
            //updateAllTrustRelationships("context", getModelElement().getName());

            try
            {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}

