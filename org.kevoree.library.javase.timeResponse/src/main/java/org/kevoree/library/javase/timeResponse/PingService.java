package org.kevoree.library.javase.timeResponse;

import com.googlecode.jpingy.Ping;
import com.googlecode.jpingy.PingArguments;
import com.googlecode.jpingy.PingResult;
import org.kevoree.ContainerRoot;
import org.kevoree.framework.KevoreePlatformHelper;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 31/01/13
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */
public class PingService implements Runnable {

    private ConcurrentHashMap<String,Integer> map_time;
    private String nodename;
    private final int bad = 1500;
    private ContainerRoot model;

    private String adr = "";
    public PingService(ConcurrentHashMap<String,Integer> r, String nodename,ContainerRoot model){
        this.map_time = r;
        this.model = model;
        this.nodename = nodename;
        adr = getAddress(nodename);
    }



    public String getAddress(String remoteNodeName)
    {
        String ip = KevoreePlatformHelper.getProperty(model, remoteNodeName,
                org.kevoree.framework.Constants.KEVOREE_PLATFORM_REMOTE_NODE_IP());
        if (ip == null || ip.equals("")) {
            ip = "";
        }
        return ip;
    }

    @Override
    public void run() {
        if(adr.length() > 0){
            PingResult results=null;


            PingArguments arguments = new PingArguments.Builder().url(adr).timeout(5000).count(2).bytes(32).build();
            results = Ping.ping(arguments, Ping.Backend.UNIX);



            /// todo

            if(map_time.containsKey( nodename))
            {
                if(results == null)
                {
                    if(map_time.get(nodename) != Integer.MAX_VALUE)
                    {
                        map_time.put(nodename,(map_time.get(nodename) + bad));
                    }
                }else
                {
                    map_time.put(nodename,(map_time.get(nodename) +  results.ttl())/2);
                }



            }  else
            {
                if(results == null){
                    map_time.put(  nodename,bad);

                }else {
                    map_time.put( nodename,results.ttl());
                }

            }  }
    }
}
