package com.ampaiva.wrobotserver.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ampaiva.wrobotserver.Proxy;

@WebListener
public class InitializeListener implements ServletContextListener {
    private static final Log LOG = LogFactory.getLog(InitializeListener.class);

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        LOG.info("Iniciando servidor...");
        Proxy.getInstance().start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        LOG.info("Finalizando servidor...");
        Proxy.getInstance().stop();
    }
}
