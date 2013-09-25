package org.kevoree.trustAPI;

import org.kevoree.annotation.DictionaryAttribute;
import org.kevoree.annotation.DictionaryType;
import org.kevoree.library.javase.webserver.AbstractPage;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 04/04/13
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "myContext", optional = false)
}     )
public abstract class TrusteePage extends AbstractPage {
}