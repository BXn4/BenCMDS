package bxn4.bencmds.GUI;

import bxn4.bencmds.Config;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SetupGUI {
    String token;
    public void makeSetupGUI() {
        JFrame frame = new JFrame("Set-up");
        JPanel panel = new JPanel();
        JLabel tokenLbl = new JLabel("Token:");
        JPasswordField tokenPwdFld = new JPasswordField("");
        JButton nextBtn = new JButton("Next");
        nextBtn.setEnabled(false);
        JButton cancelBtn = new JButton("Cancel");
        JButton getTokenBtn = new JButton("Get Token");
        tokenLbl.setBounds(15,10,50,25);
        tokenPwdFld.setBounds(60, 10, 280, 25);
        nextBtn.setBounds(270, 80, 100, 20);
        cancelBtn.setBounds(10, 80, 100, 20);
        getTokenBtn.setBounds(270, 50, 100, 20);
        frame.add(tokenLbl);
        frame.add(tokenPwdFld);
        frame.add(nextBtn);
        frame.add(cancelBtn);
        frame.add(getTokenBtn);
        frame.setSize(400, 150);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(panel);
        frame.setVisible(true);
        Config config = Config.getInstance();
        token = config.botToken;
        tokenPwdFld.setText(token);
        if (tokenPwdFld.getText().length() > 0) {
            nextBtn.setEnabled(true);
        }
        tokenPwdFld.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void check() {
                if (tokenPwdFld.getText().length() > 0 || !tokenPwdFld.getText().equals("")) {
                    nextBtn.setEnabled(true);
                } else {
                    nextBtn.setEnabled(false);
                }
            }
        });
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                token = tokenPwdFld.getText();
                Config config = Config.getInstance();
                config.botToken = token;
                BotSettingsGUI botSettingsGUI = new BotSettingsGUI();
                botSettingsGUI.makeGUI();
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                System.gc();
            }
        });
        getTokenBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URI uri = null;
                try {
                    uri= new URI("https://discord.com/developers/applications");
                } catch (URISyntaxException ex) {
                }
                try {
                    java.awt.Desktop.getDesktop().browse(uri);
                    uri = null;
                } catch (IOException ex) {
                }
            }
        });
    }
}
