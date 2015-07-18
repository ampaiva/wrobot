package com.ampaiva.wrobot;

import java.awt.AWTException;
import java.awt.Button;
import java.awt.Image;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import com.ampaiva.wrobot.client.JNA;
import com.ampaiva.wrobot.client.Keyboard;
import com.ampaiva.wrobot.client.TCPClient;

/**
 *
 * @author Mohammad Faisal ermohammadfaisal.blogspot.com
 *         facebook.com/m.faisal6621
 *
 */

public class HideToSystemTray extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -9098665192558901652L;
    TrayIcon trayIcon;
    SystemTray tray;
    TextField nome = new TextField();
    TextArea window = new TextArea(10, 1);
    TextField keys = new TextField();

    final ActionListener alSendKeys = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = window.getText();
            String[] commands = text.split("\r\n");
            for (String string : commands) {
                String windowText = string.substring(0, string.indexOf(','));
                String keysText = string.substring(string.indexOf(',') + 1);
                typeWindow(windowText, keysText);
                pause(1000);
            }
        }

        private void pause(long millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        private void typeWindow(String windowTitle, String keysChars) {
            JNA.setForegroundWindow(windowTitle);
            try {
                Keyboard keyboard = new Keyboard();
                keyboard.type(keysChars);
            } catch (AWTException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    };

    HideToSystemTray() {
        super("SystemTray test");
        System.out.println("creating instance");
        try {
            System.out.println("setting look and feel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JPanel pane = new JPanel();
            SpringLayout layout = new SpringLayout();
            //Adjust constraints for the label so it's at (5,5).
            pane.setLayout(layout);
            add(pane);

            Label lblNome = new Label("Nome:");
            pane.add(lblNome);
            nome.setText("Alexandre");
            pane.add(nome);
            layout.putConstraint(SpringLayout.WEST, nome, 5, SpringLayout.EAST, lblNome);
            layout.putConstraint(SpringLayout.NORTH, nome, 5, SpringLayout.NORTH, pane);
            layout.putConstraint(SpringLayout.EAST, nome, -5, SpringLayout.EAST, pane);
            {
                Label lblWindow = new Label("Janela:");
                pane.add(lblWindow);
                pane.add(window);
                StringBuilder sb = new StringBuilder();
                sb.append("ampaiva - COBRANCA,%(c)c" + "\r\n");
                sb.append("Clientes,%(l)" + "\r\n");
                sb.append("Detalhes do Cliente,Alexandre Martins Paiva\\t\\t68665296620\\t\\tM4.443.522\\tm" + "\r\n");
                window.setText(sb.toString());

                layout.putConstraint(SpringLayout.BASELINE, lblWindow, 5, SpringLayout.SOUTH, nome);
                layout.putConstraint(SpringLayout.BASELINE, window, 5, SpringLayout.SOUTH, nome);
                layout.putConstraint(SpringLayout.WEST, window, 5, SpringLayout.EAST, lblWindow);
                layout.putConstraint(SpringLayout.EAST, window, 0, SpringLayout.EAST, nome);
            }
            {
                Label lblKeys = new Label("Teclas:");
                pane.add(lblKeys);
                pane.add(keys);
                keys.setText("TestArquivoX\n^(s)");
                layout.putConstraint(SpringLayout.BASELINE, lblKeys, 5, SpringLayout.SOUTH, window);
                layout.putConstraint(SpringLayout.BASELINE, keys, 5, SpringLayout.SOUTH, window);
                layout.putConstraint(SpringLayout.WEST, keys, 5, SpringLayout.EAST, lblKeys);
                layout.putConstraint(SpringLayout.EAST, keys, 0, SpringLayout.EAST, window);
            }

            Button button = new Button();
            button.setLabel("Enviar Teclas");
            button.addActionListener(alSendKeys);
            button.setSize(20, 40);
            layout.putConstraint(SpringLayout.BASELINE, button, 5, SpringLayout.SOUTH, keys);
            pane.add(button);
            keys.setSize(100, 40);
        } catch (Exception e) {
            System.out.println("Unable to set LookAndFeel");
        }
        if (SystemTray.isSupported()) {
            System.out.println("system tray supported");
            tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().getImage("/code/workspace/WRobot/src/cobrarr.png");
            ActionListener exitListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting....");
                    System.exit(0);
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            defaultItem = new MenuItem("Open");
            defaultItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(defaultItem);
            defaultItem = new MenuItem("Connect");
            defaultItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int corePoolSize = 10;
                    int maximumPoolSize = 20;
                    InetAddress remoteHostIP = InetAddress.getLoopbackAddress();
                    String description = "Example";
                    String name = nome.getText();
                    int remoteHostPort = 50000;
                    TCPClient tcpClient = new TCPClient(name, description, remoteHostIP, remoteHostPort, corePoolSize,
                            maximumPoolSize);
                    try {
                        tcpClient.Start();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });
            popup.add(defaultItem);
            defaultItem = new MenuItem("Send");
            defaultItem.addActionListener(alSendKeys);
            popup.add(defaultItem);
            trayIcon = new TrayIcon(image, "SystemTray Demo", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseMoved(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    // TODO Auto-generated method stub

                }
            });
            trayIcon.addMouseListener(new MouseListener() {

                @Override
                public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() >= 2) {
                        setVisible(true);
                        setExtendedState(JFrame.NORMAL);

                        //do something
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseClicked(MouseEvent e) {

                }
            });
        } else {
            System.out.println("system tray not supported");
        }
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == ICONIFIED) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                        System.out.println("added to SystemTray");
                    } catch (AWTException ex) {
                        System.out.println("unable to add to tray");
                    }
                }
                if (e.getNewState() == 7) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                        System.out.println("added to SystemTray");
                    } catch (AWTException ex) {
                        System.out.println("unable to add to system tray");
                    }
                }
                if (e.getNewState() == MAXIMIZED_BOTH) {
                    tray.remove(trayIcon);
                    setVisible(true);
                    System.out.println("Tray icon removed");
                }
                if (e.getNewState() == NORMAL) {
                    tray.remove(trayIcon);
                    setVisible(true);
                    System.out.println("Tray icon removed");
                }
            }
        });
        setIconImage(Toolkit.getDefaultToolkit().getImage("/code/workspace/WRobot/src/cobrarr.png"));

        setVisible(true);
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new HideToSystemTray();
    }
}