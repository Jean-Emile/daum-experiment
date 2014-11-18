package org.kevoree.reputationAPI;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 07/11/14
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class ReputationStatementInfo {
    private static int id = 0;
    private String context;
    private String source;
    private ClaimInfo claim;
    private String target;
    private String timeStamp;


    public ReputationStatementInfo( String ctx, String source, ClaimInfo claim, String target, String timestamp )
    {
        ++id;
        this.context = ctx;
        this.source = source;
        this.claim = claim;
        this.target = target;
        this.timeStamp = timestamp;
    }

    public int getId()
    {
        return id;
    }

    public String getContext()
    {
        return context;
    }

    public String getSource()
    {
        return source;
    }

    public ClaimInfo getClaim()
    {
       return claim;
    }

    public String getTarget()
    {
        return target;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }
}
