package com.ampaiva.wrobotserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quickserver.net.AppException;
import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.DataMode;
import org.quickserver.net.server.DataType;
import org.quickserver.net.server.QuickServer;

public class Proxy {
    private static final Log LOG = LogFactory.getLog(Proxy.class);
    private static final String[] ServerNames = { "Operador" };
    private static final String SERVER_PORTS_PROPERTIES = "/WRobotServer/conf/ServerPortsConfig.properties";
    private static final String keepAliveIntervalSecondsProp = "keepAliveIntervalSeconds";

    private static Proxy instance;

    private int keepAliveIntervalSeconds = 180;
    private final QuickServer[] servers = new QuickServer[ServerNames.length];
    private final int portBase = 50000;
    private final int[] ServerPorts = new int[ServerNames.length];

    public static Map<String, ClientHandler> operadores = new HashMap<String, ClientHandler>();
    public static Date startDate;

    private Proxy() {

    }

    private void init() throws IOException {

        for (int i = 0; i < ServerNames.length; i++) {
            ServerPorts[i] = portBase + i;
        }
        /* Load Server properties */

        File propertiesFile = new File(SERVER_PORTS_PROPERTIES);
        if (propertiesFile.exists()) {
            Properties serverProperties = loadserverProperties();
            for (int i = 0; i < ServerPorts.length; i++) {
                ServerPorts[i] = Integer.parseInt(serverProperties.getProperty(ServerNames[i] + "ServerPort"));
            }
            setKeepAliveIntervalSeconds(Integer.parseInt(serverProperties.getProperty(keepAliveIntervalSecondsProp)));
        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Could not find properties file " + propertiesFile);
            }
        }

        for (int i = 0; i < servers.length; i++) {
            QuickServer quickServer = new QuickServer();
            quickServer.setClientCommandHandler(String.format("%s.handler.%sHandler", Proxy.class.getPackage()
                    .getName(), ServerNames[i]));
            quickServer.setPort(ServerPorts[i]);
            quickServer.setClientData(String.format("%s.data.%sData", Proxy.class.getPackage().getName(),
                    ServerNames[i]));
            quickServer.setDefaultDataMode(DataMode.BYTE, DataType.IN);
            quickServer.setDefaultDataMode(DataMode.BYTE, DataType.OUT);
            if (i == 2) {
                quickServer.setTimeout(2 * getKeepAliveIntervalSeconds() * 1000);
            } else {
                quickServer.setTimeout(10 * 60 * 1000);
            }
            quickServer.setName(String.format("%s Server", ServerNames[i]));
            servers[i] = quickServer;
        }
    }

    public void start() {
        for (QuickServer quickServer : servers) {
            try {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Starting server " + quickServer.getName());
                }
                quickServer.startServer();
            } catch (AppException e) {
                if (LOG.isFatalEnabled()) {
                    LOG.fatal(e);
                }
            }
        }
        startDate = new Date();
    }

    public void stop() {
        startDate = null;
        for (QuickServer quickServer : servers) {
            try {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Stopping server " + quickServer.getName());
                }
                quickServer.stopServer();
            } catch (AppException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        Proxy.getInstance().start();
    }

    private static Properties loadserverProperties() {

        Properties properties = new Properties();
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(SERVER_PORTS_PROPERTIES));
        } catch (final FileNotFoundException e) {
            LOG.error("Cannot load config file: " + SERVER_PORTS_PROPERTIES, e);
        }
        try {
            properties.load(stream);
        } catch (final IOException e) {
            LOG.error("Cannot load config file: " + SERVER_PORTS_PROPERTIES, e);
        }

        return properties;
    }

    public static Proxy getInstance() {
        if (instance == null) {
            instance = new Proxy();

            try {
                instance.init();
            } catch (IOException e) {
                LOG.fatal(e);
            }
        }
        return instance;
    }

    public int getKeepAliveIntervalSeconds() {
        return keepAliveIntervalSeconds;
    }

    public void setKeepAliveIntervalSeconds(int keepAliveIntervalSeconds) {
        this.keepAliveIntervalSeconds = keepAliveIntervalSeconds;
    }

}
