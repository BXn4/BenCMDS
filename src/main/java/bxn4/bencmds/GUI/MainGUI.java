package bxn4.bencmds.GUI;

import bxn4.bencmds.BenCMDS;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import com.jtattoo.plaf.aero.AeroLookAndFeel;
import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import com.jtattoo.plaf.bernstein.BernsteinLookAndFeel;
import com.jtattoo.plaf.fast.FastLookAndFeel;
import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import com.jtattoo.plaf.luna.LunaLookAndFeel;
import com.jtattoo.plaf.mcwin.McWinLookAndFeel;
import com.jtattoo.plaf.mint.MintLookAndFeel;
import com.jtattoo.plaf.noire.NoireLookAndFeel;
import com.jtattoo.plaf.smart.SmartLookAndFeel;
import com.jtattoo.plaf.texture.TextureLookAndFeel;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainGUI {
    public JLabel serversLbl = new JLabel("");
    public JTextArea logArea = new JTextArea();
    public void makeGUI(String skin) {
        try {
            switch (skin) {
                case "Metal" -> UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
                case "Acryl" -> UIManager.setLookAndFeel(AcrylLookAndFeel.class.getName());
                case "Aero" -> UIManager.setLookAndFeel(AeroLookAndFeel.class.getName());
                case "Aluminium" -> UIManager.setLookAndFeel(AluminiumLookAndFeel.class.getName());
                case "Bernstein" -> UIManager.setLookAndFeel(BernsteinLookAndFeel.class.getName());
                case "Fast" -> UIManager.setLookAndFeel(FastLookAndFeel.class.getName());
                case "Graphite" -> UIManager.setLookAndFeel(GraphiteLookAndFeel.class.getName());
                case "HiFi" -> UIManager.setLookAndFeel(HiFiLookAndFeel.class.getName());
                case "Luna" -> UIManager.setLookAndFeel(LunaLookAndFeel.class.getName());
                case "McWin" -> UIManager.setLookAndFeel(McWinLookAndFeel.class.getName());
                case "Mint" -> UIManager.setLookAndFeel(MintLookAndFeel.class.getName());
                case "Noire" -> UIManager.setLookAndFeel(NoireLookAndFeel.class.getName());
                case "Smart" -> UIManager.setLookAndFeel(SmartLookAndFeel.class.getName());
                case "Texture" -> UIManager.setLookAndFeel(TextureLookAndFeel.class.getName());
                default -> UIManager.setLookAndFeel(GraphiteLookAndFeel.class.getName());
            }
        }
        catch (Exception e) {
        }
        JMenuBar menuBar;
        JMenu optionsMenu, skinSubMenu;
        JMenu botMenu, startStop;
        menuBar = new JMenuBar();
        optionsMenu = new JMenu("Options");
        skinSubMenu = new JMenu("Change skin");
        botMenu = new JMenu("Bot");
        startStop = new JMenu("Start/Stop");
        JRadioButtonMenuItem skin1 = new JRadioButtonMenuItem("Metal");
        JRadioButtonMenuItem skin5 = new JRadioButtonMenuItem("Acryl");
        JRadioButtonMenuItem skin6 = new JRadioButtonMenuItem("Aero");
        JRadioButtonMenuItem skin7 = new JRadioButtonMenuItem("Aluminium");
        JRadioButtonMenuItem skin8 = new JRadioButtonMenuItem("Bernstein");
        JRadioButtonMenuItem skin9 = new JRadioButtonMenuItem("Fast");
        JRadioButtonMenuItem skin10 = new JRadioButtonMenuItem("Graphite");
        JRadioButtonMenuItem skin11 = new JRadioButtonMenuItem("HiFi");
        JRadioButtonMenuItem skin12 = new JRadioButtonMenuItem("Luna");
        JRadioButtonMenuItem skin13 = new JRadioButtonMenuItem("McWin");
        JRadioButtonMenuItem skin14 = new JRadioButtonMenuItem("Mint");
        JRadioButtonMenuItem skin15 = new JRadioButtonMenuItem("Noire");
        JRadioButtonMenuItem skin16 = new JRadioButtonMenuItem("Smart");
        JRadioButtonMenuItem skin17 = new JRadioButtonMenuItem("Texture");
        menuBar.add(optionsMenu);
        menuBar.add(botMenu);
        optionsMenu.add(skinSubMenu);
        botMenu.add(startStop);
        skinSubMenu.add(skin1);
        skinSubMenu.add(skin5);
        skinSubMenu.add(skin6);
        skinSubMenu.add(skin7);
        skinSubMenu.add(skin8);
        skinSubMenu.add(skin9);
        skinSubMenu.add(skin10);
        skinSubMenu.add(skin11);
        skinSubMenu.add(skin12);
        skinSubMenu.add(skin13);
        skinSubMenu.add(skin14);
        skinSubMenu.add(skin15);
        skinSubMenu.add(skin16);
        skinSubMenu.add(skin17);
        JFrame frame = new JFrame("BenCMDS");
        skin1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(AcrylLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(AeroLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(AluminiumLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(BernsteinLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(FastLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(GraphiteLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(HiFiLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(LunaLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin13.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(McWinLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin14.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(MintLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin15.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(NoireLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin16.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(SmartLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        skin17.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(TextureLookAndFeel.class.getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
        logArea.setEditable(false);
        JScrollPane sp = new JScrollPane(logArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        BenCMDS benCMDS = new BenCMDS(this);
        JPanel panel = new JPanel();
        JButton startStopBtn = new JButton("Start");
        JButton setUpBtn = new JButton("Set-up");
        JButton configBtn = new JButton("Config");
        JButton logBtn = new JButton("View Log");
        startStopBtn.setBounds(575,330,100,20);
        setUpBtn.setBounds(575,300,100,20);
        configBtn.setBounds(575,270,100,20);
        logBtn.setBounds(575,240,100,20);
        serversLbl.setBounds(5,340,100,20);
        logArea.setBounds(5,240,560,100);
        frame.add(startStopBtn);
        frame.add(setUpBtn);
        frame.add(configBtn);
        frame.add(logBtn);
        frame.add(serversLbl);
        frame.add(logArea);
        frame.getContentPane().add(sp);
        frame.setSize(700,420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(panel);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
        startStopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (startStopBtn.getText()) {
                    case "Start":
                        benCMDS.startBot();
                        startStopBtn.setText("Stop");
                        break;
                    case "Stop":
                        benCMDS.stopBot();
                        startStopBtn.setText("Start");
                }
            }
        });
        setUpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetupGUI setupGUI = new SetupGUI();
                setupGUI.makeSetupGUI();
            }
        });
    }

    public void appendLog(String text) {
        // NEED TO DO
        // logArea.append(text);
    }
}
