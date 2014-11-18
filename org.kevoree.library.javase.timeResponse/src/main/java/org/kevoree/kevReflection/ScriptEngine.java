package org.kevoree.kevReflection;

import org.kevoree.api.service.core.script.KevScriptEngine;

import java.util.List;

/**
 * Created by franciscomoyanolara on 12/11/14.
 */
public final class ScriptEngine {

    private KevScriptEngine engine;

    public ScriptEngine( KevScriptEngine engine )
    {
         this.engine = engine;
    }

    public void execute( String script )
    {

    }

    public void substituteComponent( String oldInstanceName, String newTypeName )
    {
        System.out.println("Substituting component");
        /*System.out.println( "Trying to substitute component " + oldInstanceName );
        engine.clearScript();
        engine.addVariable("oldComponent", oldInstanceName);
        engine.addVariable( "newComponentType", newTypeName);
        engine.append( "unbind {oldComponent}.textEntered@node0=>defMSG602");
        engine.append( "unbind {oldComponent}.showText@node0=>defMSG217" );
        engine.append( "unbind {oldComponent}.retrieveReputation@node0=>defSERVIC460" );
        engine.append( "unbind {oldComponent}.receiveClaim@node0=>defMSG856");
        engine.append( "removeComponent {oldComponent}@node0");
        engine.append( "addComponent Reputatio42@node0:{newComponentType}");
        engine.append( "bind Reputatio42.receiveClaim@node0=>defMSG856");
        engine.append( "bind Reputatio42.retrieveReputation@node0=>defSERVIC460" );
        engine.append( "bind Reputatio42.textEntered@node0=>defMSG602");
        engine.append( "bind Reputatio42.showText@node0=>defMSG217" );
        System.out.println( "Script: " + engine.getScript() );
        try
        {
            engine.interpretDeploy();
        } catch(Exception e )
        {
            e.printStackTrace();
        }   */
    }

    public void removeComponent( String oldInstanceName )
    {
        System.out.println("Removing component");
    }

    public void execute( String action, String idTarget, List<String> args )
    {
        if( "substitute".equals( action ))
        {
            substituteComponent( idTarget, args.get(0) );
        }
        else if( "remove".equals( action ))
        {
            removeComponent( idTarget );
        }
    }
}
