package org.kevoree.reputationAPI;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 25/10/13
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public final class ClaimInfo {

    //private String context;
    private String name;
    private String value;
    //private String timeStamp;
    //private String target;
    //private String source;


    /*public ClaimInfo(String ctx, String value, String target, String timeStamp) {
        id++;
        context = ctx;
        this.value = value;
        this.timeStamp = timeStamp;
        this.target = target;
    }    */

    /*public ClaimInfo(String ctx, String name, String value, String source, String target, String timeStamp) {
        id++;
        this.context = ctx;
        this.name = name;
        this.value = value;
        this.timeStamp = timeStamp;
        this.target = target;
        this.source = source;
    } */

    public ClaimInfo( String name, String value )
    {
        this.name = name;
        this.value = value;
    }

    /*public int getId() {
        return id;
    }            */

    public String getName()
    {
        return name;
    }

    public String getValue() {
        return value;
    }

    /*public String getContext() {
        return context;
    }



    public String getTimeStamp() {
        return timeStamp;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
    public void setContext(String ctx) {
        context = ctx;
    }

    public void setValue(String v) {
        value = v;
    }

    public void setTimeStamp(String ts) {
        timeStamp = ts;
    }    */

}
