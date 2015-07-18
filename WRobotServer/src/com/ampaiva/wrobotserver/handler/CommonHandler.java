package com.ampaiva.wrobotserver.handler;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quickserver.net.server.ClientCommandHandler;
import org.quickserver.net.server.ClientEventHandler;
import org.quickserver.net.server.ClientHandler;

public abstract class CommonHandler implements ClientCommandHandler, ClientEventHandler {

    private static final Log LOG = LogFactory.getLog(CommonHandler.class);

    protected void sendClientMsgln(ClientHandler handler) throws IOException {
        sendClientMsg(handler, "");
    }

    protected void sendClientMsgln(ClientHandler handler, String msg) throws IOException {
        sendClientMsg(handler, msg + "\r\n");
    }

    protected void sendClientMsg(ClientHandler handler, String msg) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(handler.getName() + ":" + msg);
        }
        handler.sendClientBytes(msg);
    }

}
