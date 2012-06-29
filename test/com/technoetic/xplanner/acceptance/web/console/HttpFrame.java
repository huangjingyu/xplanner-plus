/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Aug 29, 2004
 * Time: 11:33:01 AM
 */
package com.technoetic.xplanner.acceptance.web.console;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import org.apache.batik.ext.swing.GridBagConstants;

class HttpFrame extends JFrame implements GridBagConstants {

    private JTextArea response;
    private JEditorPane htmlPane;
    private JTextField urlField;
    private JTextField instructionField;
    private JTextField statusField;

    public JEditorPane getHtmlPane() {
        return htmlPane;
    }

    public HttpFrame(ActionListener stepAction, ActionListener continueAction, ActionListener pauseAction) {
        JSplitPane responsePanel = createResponsePanel();
        JPanel toolbar = createToolbar(stepAction, continueAction, pauseAction, response);
        JPanel panInput = createInputPanel();

        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = HORIZONTAL;
        c.weightx = 1;
        this.getContentPane().add(panInput, c);
        c.gridy = 1;
        this.getContentPane().add(toolbar, c);
        c.gridy = 2;
        c.fill = BOTH;
        c.weighty = 1;
        this.getContentPane().add(responsePanel, c);
    }

    private JPanel createInputPanel() {
        JPanel panInput = new JPanel(new GridBagLayout());
        urlField = createTextField();
        instructionField = createTextField();
        statusField = createTextField();

        int row = 0;
        createLabeledField(panInput, "Instruction", instructionField, row++);
        createLabeledField(panInput, "Url", urlField, row++);
        createLabeledField(panInput, "Status", statusField, row++);

        return panInput;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
        return textField;
    }

    private void createLabeledField(JPanel panInput, String title, JTextField field, int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.fill = NONE;
        c.anchor = EAST;
        c.weightx = 0;
        panInput.add(new JLabel(title + ":"),c);

        c.gridx = 1;
        c.gridwidth = REMAINDER;
        c.fill = HORIZONTAL;
        c.weightx = 1;
        panInput.add(field,c);
    }

    private JPanel createToolbar(ActionListener stepAction,
                                 ActionListener continueAction,
                                 ActionListener pauseAction,
                                 JTextArea textAreaToSearch) {
        final JButton stepBtn = new JButton("Step");
        stepBtn.addActionListener(stepAction);
        final JButton continueBtn = new JButton("Continue");
        continueBtn.addActionListener(continueAction);
        final JButton pauseBtn = new JButton("Pause");
        pauseBtn.addActionListener(pauseAction);
        JPanel searchField = new SearchPanel(textAreaToSearch);

        JPanel toolbar = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 1;
        c.fill = NONE;
        c.weightx = 0;

        toolbar.add(stepBtn, c);

        c.gridx++;
        toolbar.add(continueBtn, c);

        c.gridx++;
        toolbar.add(pauseBtn, c);

        c.gridx++;
        c.insets = new Insets(0, 5, 0, 0);
        c.fill = HORIZONTAL;
        c.weightx = 1;
        toolbar.add(searchField,c);

        return toolbar;
    }

    private JSplitPane createResponsePanel() {
        response = new JTextArea();
        response.setEditable(false);
        response.setCaretPosition(0);

        htmlPane = new JEditorPane();
        htmlPane.setContentType("text/html");
        htmlPane.setEditable(false);

        JSplitPane splitResponsePane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(response),
            new JScrollPane(htmlPane)
        );
        splitResponsePane.setOneTouchExpandable(false);
        splitResponsePane.setResizeWeight(0.5);
        return splitResponsePane;
    }

    private URL getCSSUrl() {
        URL url = null;
        try {
            url = new URL("file:///c:/dev/xplanner/xplanner-sf/xplanner/war/default.css");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public void setInstruction(String instruction) {
        instructionField.setText(instruction);
    }

    public void setException(Throwable e) {
        if (e == null) {
            statusField.setText("Ok");
            statusField.setForeground(Color.BLACK);
        } else {
            statusField.setText("Exception thrown: " + e.getMessage());
            statusField.setForeground(Color.RED);
        }
    }

    public void setDocumentContent(String content) {

        HTMLDocument doc = new HTMLDocument();
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
//        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

        try {
            htmlPane.read(new ByteArrayInputStream(content.getBytes()), doc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        htmlPane.setDocument(doc);
        htmlPane.setCaretPosition(0);

        response.setText(content);
        response.setCaretPosition(0);
        response.requestFocus();
    }


    public void setURL(String url) {
        urlField.setText(url);
    }

}