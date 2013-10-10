package org.kevoree.trustAPI;

import jet.runtime.typeinfo.JetValueParameter;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
//import org.kevoree.reconfigurationAPI.AdaptationManager;
import org.kevoree.modeling.api.events.ModelEvent;
import org.kevoree.modeling.api.events.ModelTreeListener;
import org.kevoree.trustmetamodel.TrustRoot;
import org.kevoree.trustmetamodel.TrustmetamodelFactory;
import org.kevoree.trustmetamodel.Variable;
import org.kevoree.trustmetamodel.VariableValue;
import org.kevoree.trustmetamodel.impl.DefaultTrustmetamodelFactory;
import org.kevoree.trustmetamodel.TrustRelationship;
import org.kevoree.trustmetamodel.TrustValue;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.kevoree.trustAPI.GetHelper.getTrusteesInstanceName;

//import static org.kevoree.trustAPI.GetHelper.getTrusteesInstanceName;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 26/09/13
 * Time: 12:17
 * To change this template use File | Settings | File Templates.
 */

@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext",optional = false),
        @DictionaryAttribute(name = "metric", defaultValue ="MyTrustEngine", optional = false),
        @DictionaryAttribute(name = "role", defaultValue = "trustor", vals= {"trustor", "trustee"}, optional = false)
})

//Up to now, the trustor manages metrics variables
@Provides({
        //@ProvidedPort(name = "serviceAddVariable", type = PortType.SERVICE, className = ITrustEntity.class),
        @ProvidedPort(name = "factorManagement", type = PortType.SERVICE, className = ITrustEntity.class)
})
/*@Requires({
        @RequiredPort(name = "reconfigure", type = PortType.SERVICE, className = AdaptationManager.class)
})*/
@Library(name = "Trust")
public class TrustEntity extends AbstractComponentType implements ITrustEntity {

    public TrustmetamodelFactory factory = null;
    public TrustRoot trustModel = null;
    //public boolean alive = false;
    //public Thread thread = null;
    //private  Thread reconfiguration_thread = null;

    @Start
    public void start() throws TrustException {

        if (getDictionary().get("role").equals("trustor")) {

        factory = new DefaultTrustmetamodelFactory();
        trustModel = factory.createTrustRoot();
        //thread = new Thread(this);
        //alive = true;

        System.out.println("Address of trust model just after being init: " + trustModel);

        /*trustModel.addModelTreeListener(new ModelTreeListener() {
            @Override
            public void elementChanged(@JetValueParameter(name = "evt", type = "Lorg/kevoree/modeling/api/events/ModelEvent;") ModelEvent modelEvent) {
                System.out.println(modelEvent.getSourcePath()+modelEvent.getElementAttributeName());
            }
        });     */

        //If it's a trustor
            initializeTrustRelationships();
            //If it's not a trustor or a trustee, throw exception
        } else if (!getDictionary().get("role").equals("trustee")) {
            throw new TrustException("Wrong role for " + getModelElement().getName());
        }
    }

    @Update
    public void update() throws TrustException {
        if (getDictionary().get("role").equals("trustor")) {
            initializeTrustRelationships();
            //If it's not a trustor or a trustee
        } else if (!getDictionary().get("role").equals("trustee")) {
            throw new TrustException("Wrong role for " + getModelElement().getName());
        }
        //F();
    }

    @Stop
    public void stop() {
        //alive = false;
    }

    //Set all the trust relationships
    //We have to read from the dictionary
    //We will set trust relationships between the trustor and all its trustees
    private void initializeTrustRelationships()
    {
        String context = getDictionary().get("trustContext").toString();
        String idTrustor = getModelElement().getName();
        String nameMetric = getDictionary().get("metric").toString();
        String role = getDictionary().get("role").toString();
        AbstractMetric m = MetricFactory.createMetricInstance(nameMetric);

        //Get all the trustees to this trustor
        //trustees store: [Node 0, (CompInstance1, CompInstance2, etc)]
        //                [Node 1, (CompInstance34, CompInstance50, etc)]
        //We need this becuase the same instance name can be used in different nodes, but for the trust model
        //we need a unique identifier for trustees. In this case, "nodeName + instanceName"
        HashMap<String, List<String>> trustees = getTrusteesInstanceName(getModelService().getLastModel(), context);
        //For each node, we see their instances and we change their names to "nodeName + instanceName"
        //We accumulate all the instances in the idTrustee list
        List<String> idTrustee = new ArrayList<String>();
        //List<String> temp, temp2 = new ArrayList<String>();
        //String currentName;

        //For every node in the model...
        for (String nodeName: trustees.keySet()) {
            //...get the list of trustees running on that node
            idTrustee.addAll(trustees.get(nodeName));
        }

        //Now, we have all the information regarding the new trust relationship
        //We know: trustor, trustee, context and metric
        //We have to create all the necessary entities in the trust metamodel
        for (String t : idTrustee) {
            addTrustRelationship(m, context, idTrustor, t);
        }
    }

    //This method stores in the trust model the metric to be used by a trustor for a trustee type under
    //a context. It also creates a trust relationship, and adds trustor and trustee
    private void addTrustRelationship(AbstractMetric metric, String context, String idTrustor, String idTrustee)
    {

        System.out.println("IT'S ME: " + getModelElement().getName() + " ADDING TO THE TRUSTMODEL");
        org.kevoree.trustmetamodel.Trustor trustor = trustModel.findTrustorsByID(idTrustor);
        org.kevoree.trustmetamodel.Trustee trustee = trustModel.findTrusteesByID(idTrustee);
        org.kevoree.trustmetamodel.Metric m = trustModel.findMetricsByID(context + trustor);
        org.kevoree.trustmetamodel.TrustRelationship tr = trustModel.findTRelationshipsByID(context + idTrustor + idTrustee);

        if (m == null) {
            m = factory.createMetric();
            System.out.println("Metric created");
        }
        m.setContext(context);
        m.setIdMetric(context + idTrustor);
        m.setEngine(metric);
        System.out.println("Metric parameters are set");
        trustModel.addMetrics(m);

        for (org.kevoree.trustmetamodel.Metric m2 : trustModel.getMetrics()) {
            System.out.println("Metric found just after inserting: " + m2.getIdMetric());

        }
        System.out.println("Created metric with context " + context + " and entity " + idTrustor);

        if (trustor == null) {
            trustor = factory.createTrustor();
            System.out.println("Trustor created");

        }
        trustor.setIdTrustor(idTrustor);
        trustor.addTrustorMetrics(m);
        System.out.println("Trustor parameters are set");
        trustModel.addTrustors(trustor);

        if (trustee == null)  {
            trustee = factory.createTrustee();
            System.out.println("Trustee created");

        }
        trustee.setIdTrustee(idTrustee);
        System.out.println("Trustee parameters are set");
        trustModel.addTrustees(trustee);

        System.out.println("Looking for trustees just after inserting.");
        for (org.kevoree.trustmetamodel.Trustee t: trustModel.getTrustees()) {
            System.out.println("Trustee " + t.getIdTrustee());
        }

        if (tr == null) {
            tr = factory.createTrustRelationship();
            System.out.println("Trust relationship between " + idTrustor + " and " + idTrustee + " created");

        }
        tr.setIdTRelationship(context + idTrustor + idTrustee);
        tr.setContext(context);
        tr.setTrustor(trustor);
        tr.setTrustee(trustee);
        org.kevoree.trustmetamodel.TrustValue tv = factory.createTrustValue();
        tv.setValue("Default value");
        String timeStamp = new SimpleDateFormat("dd/MM/yyy HH:mm").format(new Timestamp(new Date().getTime()));
        tv.setTimeStamp(timeStamp);
        tr.setTrustValue(tv);
        System.out.println("Trust relationship parameters are set");
        trustModel.addTRelationships(tr);

        System.out.println("Everything seems to be ok!");
    }


    //This method retrieves the trustor's metric for a specific context
    public AbstractMetric getMetric(String context) {

        System.out.println("IT'S ME: " + getModelElement().getName() + " CALLING GETMETRIC");

        System.out.println("Address of trust model in getMetric: " + trustModel);
        System.out.println("I'm looking for a metric in the context " + context +
        " for entity " + getModelElement().getName());

        org.kevoree.trustmetamodel.Metric met = trustModel.findMetricsByID(context + getModelElement().getName());

        /*for (org.kevoree.trustmetamodel.Metric m : trustModel.getMetrics()) {
            System.out.println("Yepp, I found metric " + m.getIdMetric());
            if(m.getIdMetric().equals(context+getModelElement().getName())) {
                return (AbstractMetric) m.getEngine();
            }
        }*/
        if (met == null) {
            System.out.println("... but I didn't find it :(");
            return null;
        }
        System.out.println("... and I found it! :)");
        return (AbstractMetric) met.getEngine();
    }

    public Object compute(String context, String idTrustor, String idTrustee) {
        AbstractMetric m = (AbstractMetric) trustModel.findMetricsByID(context + idTrustor).getEngine();
        return m.compute();
    }

    public void updateTrustRelationship(String context, String idTrustor, String idTrustee) {
        //Update trust relationship in the model (add timestamp)
        TrustRelationship tr = trustModel.findTRelationshipsByID(context + idTrustor + idTrustee);
        if (tr.getTrustValue().getValue() == null) {
            TrustValue value = factory.createTrustValue();
            AbstractMetric m = (AbstractMetric) trustModel.findMetricsByID(context + idTrustor + idTrustee).getEngine();
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
        System.out.println("IT'S ME: " + getModelElement().getName() + " CALLING GETTRUSTEES");

        return trustModel.getTrustees();
    }

    /*@Override
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
    }*/


    @Override
    @Port(method = "addVariable",name = "factorManagement")
    public void addVariable(String context, String name, String idTarget, String value) {

        //For now, we are going to store the trust model in the Trustor component
        //This will change in the future (it will be shared by all components)
        //This is why the trustor implements the method addVariable, which should be implemented by
        //the VariableProducer
        Variable var = trustModel.findVariablesByID(context + name);
        if (var == null) {
            var = factory.createVariable();
        }
        var.setContext(context);
        var.setIdVariable(context + name);
        var.setIdSource(getModelElement().getName());
        var.setIdTarget(idTarget);
        VariableValue varVal = factory.createVariableValue();
        varVal.setValue(value);
        var.setValue(varVal);
    }

    @Override
    @Port(method = "getVariable",name = "factorManagement")
    public Variable getVariable(String context, String name) {
        return trustModel.findVariablesByID(context + name);
    }

    @Override
    @Port(method = "addSubjectiveFactor",name = "factorManagement")
    public void addSubjectiveFactor(String context, String name, String value) {
        Variable var = trustModel.findVariablesByID(context + name);
        if (var == null) {
            var = factory.createVariable();
        }
        var.setContext(context);
        var.setIdVariable(context + name);
        var.setIdSource(getModelElement().getName());
        var.setIdTarget(null);
        VariableValue varVal = factory.createVariableValue();
        varVal.setValue(value);
        var.setValue(varVal);
    }
}

