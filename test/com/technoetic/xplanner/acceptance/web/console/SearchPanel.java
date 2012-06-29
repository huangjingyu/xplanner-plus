/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Aug 29, 2004
 * Time: 11:35:01 AM
 */
package com.technoetic.xplanner.acceptance.web.console;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.batik.ext.swing.GridBagConstants;

class SearchPanel extends JPanel {
    JTextArea content;
    private JTextField searchTextField;
    private JButton button;

    public SearchPanel(JTextArea content) {
        super(new GridBagLayout());
        this.content = content;
        initUI();

    }

    private void initUI() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstants.HORIZONTAL;
        c.weightx = 1;

        searchTextField = new JTextField();
        searchTextField.addKeyListener( new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                button.setEnabled(searchTextField.getText().length() > 0);
                if (button.isEnabled() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    search();
                }
            }
        });
        add(searchTextField, c);

        c.gridx = 1;
        c.fill = GridBagConstants.NONE;
        c.weightx = 0;
        button = new JButton("Search");
        button.setEnabled(false);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        add(button, c);
    }

    private void search() {
        String textToSearch = searchTextField.getText();
        String textToBeSearched = content.getText();
        int caretPosition = content.getSelectionEnd();
        int foundPos = textToBeSearched.indexOf(textToSearch, caretPosition);
        if (foundPos == -1) {
            content.setSelectionStart(0);
            content.setSelectionEnd(0);
        } else {
            content.getCaret().setSelectionVisible(true);
            content.select(foundPos, foundPos + textToSearch.length());
        }
    }
}