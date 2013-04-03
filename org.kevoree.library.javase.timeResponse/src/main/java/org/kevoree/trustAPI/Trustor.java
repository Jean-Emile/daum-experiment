package org.kevoree.trustAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.trustframework.api.IVariableProducer;

import java.sql.Timestamp;
import java.util.List;

import org.kevoree.trustmetamodel.*;
import org.kevoree.trustmetamodel.impl.DefaultTrustmetamodelFactory;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 24/03/13
 * Time: 16:46
 * To change this template use File | Settings | File Templates.
 */
@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext",optional = false),
        @DictionaryAttribute(name = "trusteeType", defaultValue ="", optional = false),
        //@DictionaryAttribute(name = "nodesTrustee", defaultValue ="", optional = false),
        @DictionaryAttribute(name = "metric", defaultValue ="MyTrustEngine", optional = false)
}     )

@Provides({
        @ProvidedPort(name = "serviceAddVariable", type = PortType.SERVICE, className = VariableProducer.class) ,
        @ProvidedPort(name = "serviceGetVariable", type = PortType.SERVICE, className = Metric.class)
})
@Library(name = "Trust")
@ComponentType
public class Trustor extends AbstractComponentType implements Runnable {

    public TrustmetamodelFactory factory =null;
    public TrustRoot trustModel;
    public boolean alive = false;
    public Thread thread = null;

    @Start
    public void start(String idTrustorChild){
        factory = new DefaultTrustmetamodelFactory();
        trustModel = factory.createTrustRoot();
        thread = new Thread(this);
        alive = true;
        initializeTrustRelationships(idTrustorChild);
    }

    @Update
    public void update() {
        //Reset all the trust relationships

    }

    @Stop
    public void stop() {
        alive = false;
    }

    //Set all the trust relationships
    //We have to read from the dictionary
    //We will set trust relationships between the trustor and all the trustee with type
    //trusteeType and hosted in the node X
    //For now, let's assume one only trust relationship (i.e. one trustee) for each trustor
    private void initializeTrustRelationships(String idTrustorChild) {
        String context = getDictionary().get("trustContext").toString();
        String idTrustor = idTrustorChild;
        String trusteeType = getDictionary().get("trusteeType").toString();
        String nameMetric = getDictionary().get("metric").toString();
        Metric m = MetricFactory.createMetricInstance(nameMetric);
        //Get the name of all instances that are trustees to this trustor (according to the dictionary)
        List<String> idTrustee = GetHelper.getComponentInstanceName(getModelService().getLastModel(), trusteeType);
        for (int i = 0; i < idTrustee.size(); i++) {
            addTrustRelationship(m, context, idTrustor, idTrustee.get(i));
        }
    }

    //For now, we are going to store the trust model in the Trustor component
    //This will change in the future (it will be shared by all components)
    //This is why the trustor implements the method addVariable, which should be implemented by
    //the VariableProducer
    @Port(method = "addVariable",name = "serviceAddVariable")
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

    @Port(method = "getVariable",name = "serviceGetVariable")
    public Variable getVariable(String context, String name)
    {
        return trustModel.findVariablesByID(context + name);

    }

    //This method retrieves a metric set by a trustor
    public Metric getMetric(String context, String trustor, String trustee) {
        for (org.kevoree.trustmetamodel.Metric m : trustModel.getMetrics()) {
            if(m.getIdMetric().equals(context+trustor+trustee)) {
                return (Metric) m.getEngine();
            }
        }
        return null;
    }

    public void addTrustRelationship(Metric metric, String context, String idTrustor, String idTrustee) {
        //This method stores in the trust model the metric to be used by a trustor for a trustee type under
        //a context. It also creates a trust relationship, and adds trustor and trustee
        org.kevoree.trustmetamodel.Trustor trustor = trustModel.findTrustorsByID(idTrustor);
        org.kevoree.trustmetamodel.Trustee trustee = trustModel.findTrusteesByID(idTrustee);
        org.kevoree.trustmetamodel.Metric m = trustModel.findMetricsByID(context + trustor + trustee);
        if (m == null) {
            m = factory.createMetric();
            m.setContext(context);
            m.setIdMetric(context + idTrustor + idTrustee);
            m.setEngine((Metric)metric);
        }
        if (trustor == null) {
            trustor = factory.createTrustor();
            trustor.addTrustorMetrics(m);
            trustor.setIdTrustor(idTrustor);
        }
        if (trustee == null)  {
            trustee = factory.createTrustee();
            trustee.setIdTrustee(idTrustee);
        }
        TrustRelationship tr = trustModel.findTRelationshipsByID(context + idTrustor + idTrustee);
        if (tr == null) {
            tr = factory.createTrustRelationship();
            tr.setContext(context);
            tr.setIdTRelationship(context + idTrustor + idTrustee);
            tr.setTrustor(trustor);
            tr.setTrustee(trustee);
        }
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

    @Override
    public void run() {
        while (alive)
        {
            //getModelElement().getName() retrieves the trustor ID (instance name of the kevoree component)
            updateAllTrustRelationships("context", getModelElement().getName());

            try
            {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}
