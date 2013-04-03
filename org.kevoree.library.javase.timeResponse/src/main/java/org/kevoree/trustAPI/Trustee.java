package org.kevoree.trustAPI;

import org.kevoree.annotation.DictionaryAttribute;
import org.kevoree.annotation.DictionaryType;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 03/04/13
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext", optional = false)
}     )
public interface Trustee {
}
