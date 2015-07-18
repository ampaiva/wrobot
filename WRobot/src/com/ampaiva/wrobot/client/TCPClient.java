package com.ampaiva.wrobot.client;

import java.net.InetAddress;

import Extasys.DataFrame;
import Extasys.Network.TCP.Client.Connectors.TCPConnector;
import Extasys.Network.TCP.Client.Exceptions.ConnectorCannotSendPacketException;
import Extasys.Network.TCP.Client.Exceptions.ConnectorDisconnectedException;

/**
 *
 * @author Nikos Siatras
 */
public class TCPClient extends Extasys.Network.TCP.Client.ExtasysTCPClient {

    private AutoSendMessages fAutoSendMessagesThread;

    public TCPClient(String name, String description, InetAddress remoteHostIP, int remoteHostPort, int corePoolSize,
            int maximumPoolSize) {
        super(name, description, corePoolSize, maximumPoolSize);
        try {
            super.AddConnector(name, remoteHostIP, remoteHostPort, 10240, '\n');
        } catch (Exception ex) {
        }
    }

    @Override
    public void OnDataReceive(TCPConnector connector, DataFrame data) {

        String command = new String(data.getBytes());
        System.out.println("Data received: " + command);
        try {
            if (command.startsWith("[1]")) {
                connector.SendData(getName());
            }
        } catch (ConnectorDisconnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ConnectorCannotSendPacketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void OnConnect(TCPConnector connector) {
        System.out.println("Connected to server");
    }

    @Override
    public void OnDisconnect(TCPConnector connector) {
        System.out.println("Disconnected from server");
        StopSendingMessages();
    }

    public void StartSendingMessages() {
        StopSendingMessages();
        fAutoSendMessagesThread = new AutoSendMessages(this);
        fAutoSendMessagesThread.start();
    }

    public void StopSendingMessages() {
        if (fAutoSendMessagesThread != null) {
            fAutoSendMessagesThread.Dispose();
            fAutoSendMessagesThread.interrupt();
            fAutoSendMessagesThread = null;
        }
    }
}

class AutoSendMessages extends Thread {

    private TCPClient fMyClient;
    private boolean fActive = true;

    public AutoSendMessages(TCPClient client) {
        fMyClient = client;
    }

    @Override
    public void run() {
        int messageCount = 0;
        while (fActive) {
            try {
                messageCount++;
                fMyClient.SendData(String.valueOf(messageCount) + ((char) 2)); // Char 2 is the message splitter the server's message collector uses.
                Thread.sleep(10);
            } catch (ConnectorDisconnectedException ex) {
                System.err.println(ex.getConnectorInstance().getName() + " connector disconnected!");
                fActive = false;
                fMyClient.StopSendingMessages();
            } catch (ConnectorCannotSendPacketException ex) {
                System.err.println("Connector " + ex.getConnectorInstance().getName() + " cannot send packet"
                        + ex.getOutgoingPacket().toString());
                fActive = false;
                fMyClient.StopSendingMessages();
            } catch (Exception ex) {
                Dispose();
                fMyClient.StopSendingMessages();
            }
        }
    }

    public void Dispose() {
        fActive = false;
    }
}
