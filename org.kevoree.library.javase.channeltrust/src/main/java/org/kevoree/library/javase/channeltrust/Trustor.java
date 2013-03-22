package org.kevoree.library.javase.channeltrust;

import org.kevoree.annotation.DictionaryAttribute;
import org.kevoree.annotation.DictionaryType;
import org.kevoree.framework.AbstractComponentType;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 19/03/13
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
@DictionaryType({
        @DictionaryAttribute(name = "trustContext" , defaultValue = "",optional = false)
}     )
public abstract class Trustor extends AbstractComponentType {

    public  abstract void compute();


    public void addVariable(String val){

    }

}
