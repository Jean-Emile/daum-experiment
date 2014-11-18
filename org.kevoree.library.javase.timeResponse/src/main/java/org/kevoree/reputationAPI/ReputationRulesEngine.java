package org.kevoree.reputationAPI;

import org.kevoree.ContainerRoot;
import org.kevoree.api.service.core.script.KevScriptEngine;
import org.kevoree.kevReflection.GetHelper;
import org.kevoree.kevReflection.ScriptEngine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by franciscomoyanolara on 17/11/14.
 */
public class ReputationRulesEngine {

    private FileReader fi;
    private Path path;
    private ScriptEngine se;

    public ReputationRulesEngine( String fileName, ScriptEngine se ) {
        try {
            fi = new FileReader( fileName );
            path = Paths.get( fileName );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.se = se;
    }

    public void executeRules( ContainerRoot model, String idTarget, String reputationValue )
    {
        List<String> rules = new ArrayList<String>();
        try {
            rules = Files.readAllLines( path, StandardCharsets.UTF_8 );
        } catch (IOException e) {
            e.printStackTrace();
        }
        for( String rule : rules )
        {
            processRule( rule, model, idTarget, reputationValue );
        }
    }

    private void processRule( String rule, ContainerRoot model, String idTarget, String reputationValue )
    {
        String componentType = GetHelper.getComponentTypeFromInstance( model, idTarget );
        StringTokenizer st = new StringTokenizer( rule );
        String element = st.nextToken();
        if( element.equals( componentType ))
        {
            String condition = st.nextToken();
            char booleanValue = condition.charAt(0);
            String value = condition.substring(1);
            if( conditionSatisfied( booleanValue, value, reputationValue ))
            {
                String action = st.nextToken();
                List<String> args = new ArrayList<String>();
                while ( st.hasMoreTokens() )
                {
                    args.add( st.nextToken() );
                }
                se.execute( action, idTarget, args );
                System.out.println("Script engine called with action " + action + " and args ");
                for ( String arg : args )
                {
                    System.out.print( arg );
                }
            }
        }
    }

    private boolean conditionSatisfied( char booleanValue, String value, String reputationValue )
    {
        float val = Float.parseFloat( value );
        float repVal = Float.parseFloat( reputationValue );
        if( '<' == booleanValue )
        {
            return repVal < val;
        }
        else if ( '>' == booleanValue )
        {
            return repVal > val;
        }
        else
        {
            return repVal == val;
        }

    }
}
