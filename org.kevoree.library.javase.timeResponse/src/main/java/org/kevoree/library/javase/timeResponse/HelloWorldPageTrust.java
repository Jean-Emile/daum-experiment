package org.kevoree.library.javase.timeResponse;

import org.kevoree.annotation.DictionaryAttribute;
import org.kevoree.annotation.DictionaryType;
import org.kevoree.library.javase.webserver.AbstractPage;
import org.kevoree.library.javase.webserver.KevoreeHttpRequest;
import org.kevoree.library.javase.webserver.KevoreeHttpResponse;
import org.kevoree.trustAPI.Trustee;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 22/03/13
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */

//We should extend from Trustee, and Trustee should have this attribute, but
//I didn't know how to achieve that
@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext", optional = false)
}     )
public class HelloWorldPageTrust extends AbstractPage {

    public KevoreeHttpResponse process (KevoreeHttpRequest request, KevoreeHttpResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body>");
        builder.append("Hello from Kevoree from url " + request.getUrl() + " <br />");
        for (String key : request.getResolvedParams().keySet()) {
            builder.append(key + "->" + request.getResolvedParams().get(key) + "<br>");
        }

        builder.append("lastParam->" + getLastParam(request.getUrl()) + "<br>");
        builder.append("Served by node "+this.getNodeName()+"<br />");
        builder.append("</body></html>");
        response.setContent(builder.toString());
        return response;
    }

}
