package org.kevoree.consoleApplication;


import org.kevoree.annotation.*;
import org.kevoree.framework.KevoreeMessage;
import org.kevoree.framework.MessagePort;

import org.kevoree.kevReflection.GetHelper;
import org.kevoree.kevReflection.ScriptEngine;
import org.kevoree.reputationAPI.CentralKevReputableEntity;
import org.kevoree.library.ui.layout.KevoreeLayout;
import org.kevoree.trustAPI.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: franciscomoyanolara
 * Date: 26/09/13
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
@Provides({
        @ProvidedPort(name = "showText", type = PortType.MESSAGE)
})
@Requires({
        @RequiredPort(name = "textEntered", type = PortType.MESSAGE, optional = true)
})
@DictionaryType({
        @DictionaryAttribute(name = "singleFrame", defaultValue = "true", optional = true)
})
@ComponentType
@Library(name = "Application")
public class ReputationAwareFakeConsole extends CentralKevReputableEntity { // implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TrustAwareFakeConsole.class);
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 600;
    private MyFrame frame = null;
    private JFrame localFrame = null;

    @Override
    public void start() throws TrustException {

        super.start();

        System.out.println("Reputation console " + getName() + " started");

        frame = new MyFrame();
        // frame.setTitle(getName() + "@@@" + getNodeName());
        // frame.setVisible(true);
        frame.appendSystem("/***** CONSOLE INITIALIZED  ********/ ");
        if(Boolean.valueOf((String)getDictionary().get("singleFrame"))) {
            KevoreeLayout.getInstance().displayTab((JPanel)frame, getName());
        } else {
            localFrame = new JFrame(getName() + "@@@" + getNodeName());
            localFrame.setContentPane(frame);
            localFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            localFrame.pack();
            localFrame.setVisible(true);
        }
    }

    public void stop() {

        //super.stop();

        if(Boolean.valueOf((String)getDictionary().get("singleFrame"))) {
            KevoreeLayout.getInstance().releaseTab(getName());
        } else {
            if(localFrame != null) {
                localFrame.dispose();
            }
        }
        frame = null;
        localFrame = null;
    }

    @Update
    public void update() {

        //super.update();

        if(Boolean.valueOf((String)getDictionary().get("singleFrame"))) {
            if(localFrame != null) {
                KevoreeLayout.getInstance().displayTab((JPanel)frame,getName());
                localFrame.dispose();
                localFrame = null;
            }
        } else {
            if(localFrame == null) {
                KevoreeLayout.getInstance().releaseTab(getName());
                localFrame = new JFrame(getName() + "@@@" + getNodeName());
                localFrame.setContentPane(frame);
                localFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                localFrame.pack();
                localFrame.setVisible(true);
            }
        }
    }

    public void appendSystem(String text) {
        frame.appendSystem(text);
    }

    public void appendOutgoing(String text) {

        frame.appendOutgoing(text);
        if (isPortBinded("textEntered")) {
            getPortByName("textEntered", MessagePort.class).process( getName() + "." + text );

            //We search for the component instance connected with the current component through the port "textEntered"
            /*Set<String> idTargets = GetHelper.getComponentBindedToPort(getModelService().getLastModel(),
                    "textEntered", getModelElement().getName());

            boolean ok = true;
            String idT = null;
            for( String idTarget : idTargets )
            {
                float reputation = Float.parseFloat( getReputation( idTarget ));
                ok = ok && ( reputation > 0 );
                System.out.println( "Reputation of " + idTarget + " is " + reputation );
                idT = idTarget;
            }
            if ( ok ) //change by ok
            {
                getPortByName("textEntered", MessagePort.class).process(text);
            }
            else
            {
                ScriptEngine.substituteComponent( getKevScriptEngineFactory().createKevScriptEngine(),
                                                  "Reputatio801", "ReputationAwareFilteredFakeConsole" );
            }    */
            //float rep = Float.parseFloat(getReputation(idTarget));
            //System.out.println("The reputation of " + idTarget + " is " + rep);

            //if ( rep > 4 ) {

            //System.out.println("Reputation ok! I'll send the message");

           // } else {

               // System.out.println("Sorry, reputation of " + idTarget + " too low");
            //}
        }
    }

    @Port(name = "showText")
    public void appendIncoming(Object text) {
        if (text != null) {
            if (text instanceof KevoreeMessage) {
                KevoreeMessage kmsg = (KevoreeMessage) text;
                frame.appendIncomming("->");
                for (String key : kmsg.getKeys()) {
                    //before, instead of toString, there was a get
                    frame.appendIncomming(key + "=" + kmsg.getValue(key).toString());
                }
            } else {
                /*Set<String> idTargets = GetHelper.getComponentBindedToPort(getModelService().getLastModel(),
                        "textEntered", getModelElement().getName()); */
                StringTokenizer st = new StringTokenizer( text.toString(), "." );
                String idTarget = st.nextToken();
                String message = st.nextToken();
                if( message.equals("shit\n") || message.equals("fuck\n"))
                {
                    makeClaim( "cleanWords", "-1", idTarget );
                }
                else
                {
                    makeClaim( "cleanWords", "1", idTarget );
                    frame.appendIncomming( "->" + message );
                }

                float reputation = Float.valueOf( getReputation( idTarget ));
                if ( reputation <= 0 )
                {
                    /*ScriptEngine.substituteComponent(
                            getKevScriptEngineFactory().createKevScriptEngine(),
                            idTarget,
                            "ReputationAwareFakeConsole"
                            );  */

                }

            }
        }
    }


    private class MyFrame extends JPanel {

        private JTextPane screen;
        private JTextArea inputTextField;
        private JButton send;

        public MyFrame() {
            setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
            setLayout(new BorderLayout());
            send = new JButton("Send");
            send.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (inputTextField.getText().length() > 1) {
                        ReputationAwareFakeConsole.this.appendOutgoing(inputTextField.getText());
                    }

                }
            });

            screen = new JTextPane();
            screen.setFocusable(false);
            screen.setEditable(false);
            StyledDocument doc = screen.getStyledDocument();
            Style def = StyleContext.getDefaultStyleContext().
                    getStyle(StyleContext.DEFAULT_STYLE);
            Style system = doc.addStyle("system", def);
            StyleConstants.setForeground(system, Color.GRAY);

            Style incoming = doc.addStyle("incoming", def);
            StyleConstants.setForeground(incoming, Color.BLUE);

            Style outgoing = doc.addStyle("outgoing", def);
            StyleConstants.setForeground(outgoing, Color.GREEN);


            final String INITIAL_MESSAGE = "Type your text here";
            inputTextField = new JTextArea();
            inputTextField.setText(INITIAL_MESSAGE);
            inputTextField.setFocusable(true);
            inputTextField.setRequestFocusEnabled(true);
            inputTextField.requestFocus();
            inputTextField.setCaretPosition(0);
            inputTextField.setSelectionStart(0);
            inputTextField.setSelectionEnd(INITIAL_MESSAGE.length());

            inputTextField.addKeyListener(new KeyAdapter() {

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
                        if (e.isControlDown()) {
                            inputTextField.append("\n");
                        } else {
                            if (inputTextField.getText().length() > 1) {
                                ReputationAwareFakeConsole.this.appendOutgoing(inputTextField.getText());
                            }
                            inputTextField.setText("");
                        }
                    }
                }
            });

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new BorderLayout());
            bottomPanel.add(inputTextField, BorderLayout.CENTER);
            bottomPanel.add(send, BorderLayout.EAST);

            add(new JScrollPane(screen), BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);
            setVisible(true);
        }

        public void appendSystem(String text) {
            try {
                StyledDocument doc = screen.getStyledDocument();
                doc.insertString(doc.getLength(), formatForPrint(text), doc.getStyle("system"));
            } catch (BadLocationException ex) {
                ex.printStackTrace();
                logger.error("Error while trying to append system message in the " + this.getName(), ex);
            }
        }

        public void appendIncomming(String text) {
            try {
                StyledDocument doc = screen.getStyledDocument();
                doc.insertString(doc.getLength(), formatForPrint(text), doc.getStyle("incoming"));
                screen.setCaretPosition(doc.getLength());
            } catch (BadLocationException ex) {
                ex.printStackTrace();
                logger.error("Error while trying to append incoming message in the " + this.getName(), ex);
                //getLoggerLocal().error(ex.getClass().getSimpleName() + " occured while trying to append text in the terminal.", ex);
            }
        }

        public void appendOutgoing(String text) {
            try {
                StyledDocument doc = screen.getStyledDocument();
                doc.insertString(doc.getLength(), ">" + formatForPrint(text), doc.getStyle("outgoing"));
            } catch (BadLocationException ex) {
                ex.printStackTrace();
                logger.error("Error while trying to append local message in the " + this.getName(), ex);
                //getLoggerLocal().error(ex.getClass().getSimpleName() + " occured while trying to append text in the terminal.", ex);
            }
        }

        private String formatForPrint(String text) {
            return (text.endsWith("\n") ? text : text + "\n");
        }
    }


    /*public static void main(String[] args) {
        FakeConsole console = null;
        try {
            console = new FakeConsole();
            console.start();
            console.appendSystem("Ceci est un append system");
            Thread.sleep(2 * 1000);
            console.appendIncoming("Ceci est un message entrant");
            Thread.sleep(2 * 1000);
            console.appendOutgoing("Ceci est un message sortant");
            Thread.sleep(1 * 60 * 1000);
            console.stop();
        } catch (InterruptedException ex) {
            if (console != null) {
                //console.getLoggerLocal().error(ex.getClass().getSimpleName(), ex);
            } else {
                System.out.println(ex.toString());
            }
        }

    }*/
}