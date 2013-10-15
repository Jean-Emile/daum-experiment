package org.kevoree.trustAPI;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.trustmetamodel.*;
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
 * Date: 15/10/13
 * Time: 14:08
 * To change this template use File | Settings | File Templates.
 */

@Provides({
        @ProvidedPort(name = "trustManagement", type = PortType.SERVICE, className = ITrustModel.class)
})
@Library(name = "Trust")
@ComponentType
public final class TrustModel extends AbstractComponentType implements ITrustModel {

    private TrustmetamodelFactory factory = null;
    private TrustRoot trustModel = null;

    @Start
    public void start() throws TrustException {

        System.out.println("Trust Model Manager started");
        factory = new DefaultTrustmetamodelFactory();
        trustModel = factory.createTrustRoot();

        if (factory == null || trustModel == null) {
            throw new TrustException("Initialization error");
        }
    }

    @Stop
    public void stop() {

    }

    @Update
    public void update() {

    }

    @Override
    @Port(name="trustManagement", method="initializeTrustRelationships")
    public final boolean initializeTrustRelationships(String ctx, String trustor, AbstractMetric am) {
        System.out.println("Initializing trust metamodel");
        String context = ctx;
        String idTrustor = trustor;
        AbstractMetric m = am;

        //Get all the trustees to this trustor
        //trustees store: [Node 0, (CompInstance1, CompInstance2, etc)]
        //                [Node 1, (CompInstance34, CompInstance50, etc)]
        //We need this becuase the same instance name can be used in different nodes, but for the trust model
        //we need a unique identifier for trustees. In this case, "nodeName + instanceName"
        HashMap<String, List<String>> trustees = getTrusteesInstanceName(getModelService().getLastModel(), context);
        List<String> idTrustee = new ArrayList<String>();

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

        return true;
    }

    @Override
    @Port(name="trustManagement", method="getMetric")
    public final AbstractMetric getMetric(String context, String idTrustor) {

        AbstractMetric am = null;

        System.out.println("Looking for metric for context " + context + " and for entity " + idTrustor);
        org.kevoree.trustmetamodel.Metric met = trustModel.findMetricsByID(context + getModelElement().getName());
        if (met != null) {
            System.out.println("I found the metric for " + getModelElement().getName() + " and I returned it");
            am = (AbstractMetric) met.getEngine();
        } else {
            am = null;
        }

        return am;
    }

    @Override
    @Port(name="trustManagement", method="updateTrustRelationship")
    public final void updateTrustRelationship(String context, String idTrustor, String idTrustee) {

        System.out.println("Starting the update of trust relationship with id " + context + idTrustor + idTrustee);
        TrustRelationship tr = trustModel.findTRelationshipsByID(context + idTrustor + idTrustee);
        TrustValue value = factory.createTrustValue();
        AbstractMetric m = (AbstractMetric) trustModel.findMetricsByID(context + idTrustor).getEngine();

            if (m == null) {
                System.out.println("Metric is null");
            }else {
                System.out.println("Metric got!");
            }

        String timeStamp = new SimpleDateFormat("dd/MM/yyy HH:mm").format(new Timestamp(new Date().getTime()));
        value.setTimeStamp(timeStamp);
        value.setValue(String.valueOf(m.compute()));
        tr.setTrustValue(value);
        System.out.println("Update finished with value " + m.compute());

    }

    @Override
    @Port(name="trustManagement", method="getTrustValue")
    public final String getTrustValue(String context, String idTrustor, String idTrustee) {
        return trustModel.findTRelationshipsByID(context + idTrustor + idTrustee).getTrustValue().getValue();
    }

    @Override
    @Port(name="trustManagement", method="addSubjectiveFactor")
    public final void addSubjectiveFactor(String context, String name, String value) {

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
        trustModel.addVariables(var);
        System.out.println("I created variable " + context + name);

    }

    @Override
    @Port(name="trustManagement", method="isTrustee")
    public final boolean isTrustee(String potentialTrusteeName) {
        boolean res = false;
        if (trustModel.findTrusteesByID(potentialTrusteeName) != null) {
            res = true;
        }
        return res;
    }

    @Override
    @Port(name="trustManagement", method="getVariable")
    public final Variable getVariable(String context, String name) {
        System.out.println("the metric is looking for variable " + context + name);
        return trustModel.findVariablesByID(context + name);
    }


    private void addTrustRelationship(AbstractMetric metric, String context, String idTrustor, String idTrustee) {

        org.kevoree.trustmetamodel.Trustor trustor = trustModel.findTrustorsByID(idTrustor);
        org.kevoree.trustmetamodel.Trustee trustee = trustModel.findTrusteesByID(idTrustee);
        org.kevoree.trustmetamodel.Metric m = trustModel.findMetricsByID(context + trustor);
        org.kevoree.trustmetamodel.TrustRelationship tr = trustModel.findTRelationshipsByID(context + idTrustor + idTrustee);

        //We create the metric
        if (m == null) {
            m = factory.createMetric();
        }
        m.setContext(context);
        m.setIdMetric(context + idTrustor);
        m.setEngine(metric);
        trustModel.addMetrics(m);

        //We create the trustor
        if (trustor == null) {
            trustor = factory.createTrustor();

        }
        trustor.setIdTrustor(idTrustor);
        trustor.addTrustorMetrics(m);
        trustModel.addTrustors(trustor);

        //We create the trustee
        if (trustee == null)  {
            trustee = factory.createTrustee();
        }
        trustee.setIdTrustee(idTrustee);
        trustModel.addTrustees(trustee);
        trustor.addTrustorTrustees(trustee);

        //We create the trust relationships
        if (tr == null) {
            tr = factory.createTrustRelationship();

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
        trustModel.addTRelationships(tr);


        System.out.println("trust relationship with id " + context + idTrustor + idTrustee + " initialized");
    }


}



