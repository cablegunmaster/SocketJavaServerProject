package Java.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Jasper Lankhorst on 17-11-2016.
 */
public class View {

    private JFrame mainFrame;
    private JScrollPane jscrollpane;

    //textareas
    private JTextArea loggedInUsersTextArea;
    private JTextArea logtextArea;
    private JTextArea matchTextArea;

    private JPanel middleContainer;
    private JMenuItem stopMenuItem;
    private JMenuItem restartMenuItem;

    public View() {
        prepareGUI();
    }

    public void prepareGUI() {
        mainFrame = new JFrame("Main Server");
        mainFrame.setMinimumSize(new Dimension(200, 270));
        mainFrame.setSize(100, 100);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        }); //Exit on click.

        mainFrame.setJMenuBar(this.NorthPanel());
        mainFrame.add(this.MiddleContainer(), BorderLayout.NORTH);

        mainFrame.pack(); //resorts the area to show everything.
        mainFrame.setVisible(true);
    }

    public void destroyGUI() {
        if (mainFrame != null) {
            mainFrame.removeAll();
            mainFrame.setVisible(false); //Is this enough to clear out memory?
        }
    }

    /**
     * Prepares toolbar on top.
     */
    public JMenuBar NorthPanel() {

        //the top toolbar.
        JMenuBar menuBar = new JMenuBar();
        //the first menu item.
        JMenu menu = new JMenu("File");
        //Item in options.
        restartMenuItem = new JMenuItem("Reboot server");
        stopMenuItem = new JMenuItem("Quit server");

        menu.add(restartMenuItem);
        menu.addSeparator();
        menu.add(stopMenuItem);

        menuBar.add(menu);

        return menuBar;
    }

    public JMenuItem getRestartMenuItem() {
        return restartMenuItem;
    }

    public JMenuItem getstopMenuItem() {
        return stopMenuItem;
    }

    /**
     * Used for the GUI in the middle.
     *
     * @return JPanel containing the middle screen.
     */
    public JPanel MiddleContainer() {

        //New Panel.
        middleContainer = new JPanel();
        middleContainer.setLayout(new GridLayout(0, 2));

        //retrieve the textareaScrollPane
        JScrollPane logScrollPane = setLogTextArea();
        JLabel serverLogLabel = new JLabel("Server Log");
        serverLogLabel.setPreferredSize(new Dimension(125, 100));

        //Add the logLabel and LogPanel
        middleContainer.add(serverLogLabel, BorderLayout.CENTER);
        middleContainer.add(logScrollPane, BorderLayout.CENTER);

        //retrieve the loggedInTextPane
        JScrollPane jscrollpane = setLoggedInTextArea();
        JLabel connectedUserLabel = new JLabel("Connected Users:");
        connectedUserLabel.setPreferredSize(new Dimension(125, 100));

        //adds the Scrollpanel to the panel.
        middleContainer.add(connectedUserLabel, BorderLayout.CENTER);
        middleContainer.add(jscrollpane, BorderLayout.CENTER);

        //Retrieve the match Label.
        JLabel matchLabel = new JLabel("Matches:");
        JScrollPane jScrollPane = setMatchTextArea();

        //adds the Scrollpanel to the panel.
        middleContainer.add(matchLabel, BorderLayout.CENTER);
        middleContainer.add(jScrollPane, BorderLayout.CENTER);

        return middleContainer;
    }

    /**
     * Set the Log textarea combining the JScrollpane with the textarea.
     *
     * @return JScrollPane containing a new textarea 300x300 size.
     */
    public JScrollPane setMatchTextArea() {
        matchTextArea = new JTextArea();
        matchTextArea.setLineWrap(true); //makes sure no
        matchTextArea.setWrapStyleWord(true);

        //SHOW The scroll panel on the textArea.
        JScrollPane jscrollpane = new JScrollPane(matchTextArea);
        jscrollpane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jscrollpane.setPreferredSize(new Dimension(300, 300));

        return jscrollpane;
    }

    public JScrollPane setLoggedInTextArea() {
        loggedInUsersTextArea = new JTextArea();
        loggedInUsersTextArea.setLineWrap(false); //makes sure no line gets wrapped.
        loggedInUsersTextArea.setWrapStyleWord(false);
        loggedInUsersTextArea.setEditable(false);

        jscrollpane = new JScrollPane(loggedInUsersTextArea);
        jscrollpane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jscrollpane.setPreferredSize(new Dimension(300, 100));
        return jscrollpane;
    }

    /**
     * Set the Log textarea combining the JScrollpane with the textarea.
     *
     * @return JScrollPane containing a new textarea 300x300 size.
     */
    public JScrollPane setLogTextArea() {
        logtextArea = new JTextArea();
        logtextArea.setLineWrap(true); //makes sure no
        logtextArea.setWrapStyleWord(true);

        //SHOW The scroll panel on the textArea.
        jscrollpane = new JScrollPane(logtextArea);
        jscrollpane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jscrollpane.setPreferredSize(new Dimension(300, 300));

        return jscrollpane;
    }

    /**
     * Function: returning the mainview Log TextArea for appending a String.
     *
     * @return JTextArea for appending a String
     */
    public JTextArea getLogTextArea() {
        return logtextArea;
    }

    /**
     * Function: returning the mainview USER TextArea for appending a String.
     *
     * @return JTextArea for appending a String
     */
    public JTextArea getLoggedInUsersTextArea() {
        return loggedInUsersTextArea;
    }

    /**
     * Function get the matchTextArea.
     * @return JTextaread for appending a String
     */
    public JTextArea getMatchTextArea() {
        return matchTextArea;
    }


    /**
     * Refresh the view van de maininterface.
     */
    public void refresh() {
        mainFrame.doLayout();
        mainFrame.revalidate();
    }

}
